package com.vylo.main.followmain.followingfragment.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vylo.common.BaseFragment
import com.vylo.common.entity.PublishersSubscription
import com.vylo.common.util.FOLLOWING_BACK_TYPE
import com.vylo.common.util.GoogleAnalytics
import com.vylo.common.util.ScrollBottomListener
import com.vylo.common.util.USER_PROFILE
import com.vylo.common.util.enums.FollowingScreenType
import com.vylo.common.util.enums.RecommendedScreenType
import com.vylo.main.R
import com.vylo.main.databinding.FragmentSearchBinding
import com.vylo.main.followmain.followingfragment.presentation.adapter.ProfileFollowAdapter
import com.vylo.main.followmain.followingfragment.presentation.viewmodel.FollowingViewModel
import com.vylo.main.profilefragment.common.ProfileData
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(),
    ProfileFollowAdapter.AdapterCallBack,
    ProfileFollowAdapter.NavigateToProfile {

    override fun getViewBinding() = FragmentSearchBinding.inflate(layoutInflater)
    private val viewModel by sharedViewModel<FollowingViewModel>()
    private lateinit var adapter: ProfileFollowAdapter
    private var scrollBottomListener: ScrollBottomListener? = null
    private var isMakeSearch = true
    private var isRefreshing = false
    private var isEnableCallForData = false
    private var removedProfiles = listOf<PublishersSubscription>()
    private var searchText: String? = null

    companion object {
        private lateinit var followingScreenType: FollowingScreenType
        private var isFromProfile: Boolean? = null

        fun newInstance(followingScreenType: FollowingScreenType, isFromProfile: Boolean?): SearchFragment {
            this.followingScreenType = followingScreenType
            this.isFromProfile = isFromProfile
            return SearchFragment()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ProfileFollowAdapter(
            this,
            this,
            requireContext()
        )
        viewModel.setSearchAdapter(adapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setScreenType(RecommendedScreenType.SEARCH)
    }

    override fun onPause() {
        super.onPause()
        isMakeSearch = false
        isEnableCallForData = false
        viewModel.setUsersSearchedData(adapter.getData())
    }

    private fun beginning() {
        createContentOfView()
    }

    private fun createContentOfView() {

        if (viewModel.getUsersSearchedData() != null) {
            isMakeSearch = false
            val savedData = mutableListOf<PublishersSubscription>()
            savedData.addAll(viewModel.getUsersSearchedData()!!)
            adapter.clearData()
            adapter.setData(savedData)
            viewModel.setUsersSearchedData(null)
        } else
            getSearchProfileData(isShowProgressBar = true, isMakeSearch = false, searchText = searchText)

        viewModel.setSearchBrakeCall(false)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        viewBinder.recommendedList.layoutManager = linearLayoutManager
        viewBinder.recommendedList.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        val data = mutableListOf<PublishersSubscription>()
        viewModel.responseSearchSuccess.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                data.addAll(it)
//                if (removedProfiles.isNotEmpty()) {
//                    for ((index, item1) in it.withIndex()) {
//                        for (item2 in removedProfiles)
//                            if (item1.id == item2.id) {
//                                data[index] = item1.copy(
//                                    isFollow = false
//                                )
//                                break
//                            }
//                    }
//                }
                if (isRefreshing) {
                    adapter.clearData()
                    isRefreshing = false
                }

                adapter.setData(data)
                data.clear()
            } else adapter.clearData()

            hideProgress()
            scrollBottomListener!!.isSilent(false)
            viewBinder.swipeToRefresh.isRefreshing = false
        }

        viewModel.responseSearchError.observe(viewLifecycleOwner) {
            hideProgress()
            viewBinder.swipeToRefresh.isRefreshing = false
            if (it != null) showMessage(it)
        }

        scrollBottomListener = object : ScrollBottomListener(linearLayoutManager) {
            override fun onScrolledToBottom(): Boolean {
                isMakeSearch = false
                getSearchProfileData(isShowProgressBar = true, isMakeSearch = false, null)
                scrollBottomListener!!.isSilent(true)
                return false
            }
        }
        viewBinder.recommendedList.addOnScrollListener(scrollBottomListener!!)

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            scrollBottomListener?.isSilent(false)
            viewBinder.swipeToRefresh.isRefreshing = true
            viewModel.setSearchBrakeCall(false)
            isMakeSearch = true
            isRefreshing = true
            getSearchProfileData(isShowProgressBar = true, isMakeSearch = false, searchText)
        }
        viewBinder.swipeToRefresh.setOnRefreshListener(refreshListener)

        viewModel.userAllPublishersSearchSuccess.observe(viewLifecycleOwner) {
            if (it != null)
                removedProfiles = it
        }

        var lastInput = ""
        var debounceJob: Job? = null
        val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        viewBinder.search.apply {
            setHint(resources.getString(R.string.label_search))
            showHideClearButton(View.VISIBLE)
            getClearButton().setOnClickListener {
                if (getInputText().isNotEmpty()) {
                    scrollBottomListener!!.isSilent(true)
                    clearSearchViewByClearButton()
                    adapter.clearData()
                }
            }
            showHideCancelButton(View.GONE)
            showHideCancelBackButton(View.VISIBLE)
            val clickOnCancelBack = View.OnClickListener {
                adapter.clearData()
                backToPrevious()
            }
            setOnClickCancelBack(clickOnCancelBack)
            initialize { text ->
                if (text != null) {
                    val newtInput = text.toString()
                    debounceJob?.cancel()
                    if (lastInput != newtInput) {
                        lastInput = newtInput
                        debounceJob = uiScope.launch {
                            delay(300)
                            if (lastInput == newtInput) {
                                scrollBottomListener!!.isSilent(true)
                                viewModel.setSearchBrakeCall(false)
                                searchText = text.toString()
                                if (newtInput.isNotEmpty())
                                    if (isMakeSearch) {
                                        adapter.clearData()
                                    } else isMakeSearch = true
                                else {
                                    searchText = null
                                    adapter.clearData()
                                    isMakeSearch = true
                                }
                                getSearchProfileData(isShowProgressBar = false, isMakeSearch = true, searchText = searchText)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getSearchProfileData(isShowProgressBar: Boolean, isMakeSearch: Boolean, searchText: String?) {
        if (isShowProgressBar) showProgress()
        when (followingScreenType) {
            FollowingScreenType.VYLO ->
                viewModel.getProfile(true, searchText, isMakeSearch, followingScreenType, true)
            FollowingScreenType.NEWSSTAND ->
                viewModel.getProfile(true, searchText, isMakeSearch, followingScreenType, true)
        }
    }

    override fun followCategory(id: Long, isFollowing: Boolean?) {
        if (isFollowing != null && isFollowing) {
            when (followingScreenType) {
                FollowingScreenType.VYLO ->
                    sendAnalyticEvent(GoogleAnalytics.FOLLOWING_VYLO, GoogleAnalytics.SEARCH)
                FollowingScreenType.NEWSSTAND ->
                    sendAnalyticEvent(GoogleAnalytics.FOLLOWING_NEWSSTAND, GoogleAnalytics.SEARCH)
            }
            viewModel.addPublisher(id)
        } else viewModel.deletePublisher(id)
    }

    override fun navigateToProfile(id: String?) {
        id?.let {
            navigateTo(
                when (getMainGraph()) {
                    false -> R.id.action_followingSearchFragment_to_profileFragment
                    true -> R.id.action_followingSearchFragment_to_mainProfileFragment
                },
                bundleOf(
                    USER_PROFILE to ProfileData(it),
                    FOLLOWING_BACK_TYPE to isFromProfile
                )
            )
        }
    }

    override fun showProgress() {
        super.showProgress()
        viewBinder.progressBar.show()
    }

    override fun hideProgress() {
        super.hideProgress()
        viewBinder.progressBar.hide()
    }

}