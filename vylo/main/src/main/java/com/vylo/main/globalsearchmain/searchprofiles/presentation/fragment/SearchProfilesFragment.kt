package com.vylo.main.globalsearchmain.searchprofiles.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.BaseFragment
import com.vylo.common.util.ScrollBottomListener
import com.vylo.main.globalsearchmain.searchmain.presentation.fragment.SearchMainFragment
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileItem
import com.vylo.main.globalsearchmain.searchprofiles.presentation.adapter.ProfileAdapter
import com.vylo.main.globalsearchmain.searchprofiles.presentation.viewmodel.SearchProfileFragmentViewModel
import com.vylo.main.databinding.FragmentSearchProfilesBinding

class SearchProfilesFragment : BaseFragment<FragmentSearchProfilesBinding>(),
    SearchMainFragment.Filtering, ProfileAdapter.AdapterCallBack {

    override fun getViewBinding() = FragmentSearchProfilesBinding.inflate(layoutInflater)
    private lateinit var adapter: ProfileAdapter
    private var scrollBottomListener: ScrollBottomListener? = null
    private var isMakeSearch = true

    companion object {
        private lateinit var viewModel: SearchProfileFragmentViewModel
        private var graph: Int? = null

        fun newInstance(viewModel: SearchProfileFragmentViewModel, graph: Int): SearchProfilesFragment {
            this.viewModel = viewModel
            this.graph = graph
            return SearchProfilesFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ProfileAdapter(
            this,
            requireContext()
        )
        viewModel.setAdapter(adapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.setActualData(adapter.getData(), graph)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    private fun beginning() {
        createContentOfView()
    }

    private fun createContentOfView() {

        if (viewModel.getActualData(graph!!) != null) {
            val savedData = mutableListOf<ProfileItem>()
            savedData.addAll(viewModel.getActualData(graph!!)!!)
            adapter.clearData()
            adapter.setData(savedData)
            viewModel.setActualData(null, 0)
        } else showProgress()

        val linearLayoutManager = LinearLayoutManager(requireContext())
        viewBinder.profilesList.layoutManager = linearLayoutManager
        viewBinder.profilesList.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                if (viewBinder.emptyStateMessage.isVisible)
                    viewBinder.emptyStateMessage.visibility = View.GONE
                if (isMakeSearch)
                    adapter.clearData()
                adapter.setData(it)
            } else {
                adapter.clearData()
                if (!viewBinder.emptyStateMessage.isVisible)
                    viewBinder.emptyStateMessage.visibility = View.VISIBLE
            }
            hideProgress()
            scrollBottomListener!!.isSilent(false)
        }

        viewModel.responseError.observe(viewLifecycleOwner) {
            hideProgress()
            if (it != null) showMessage(it)
        }

        viewModel.profileError.observe(viewLifecycleOwner) {
            if (it != null) showMessage(it)
        }

        scrollBottomListener = object : ScrollBottomListener(linearLayoutManager) {
            override fun onScrolledToBottom(): Boolean {
                isMakeSearch = false
                getSearchProfileData(isShowProgressBar = true, isMakeSearch = false)
                scrollBottomListener!!.isSilent(true)
                return false
            }
        }
        viewBinder.profilesList.addOnScrollListener(scrollBottomListener!!)
    }

    private fun getSearchProfileData(isShowProgressBar: Boolean, isMakeSearch: Boolean) {
        if (isShowProgressBar) showProgress()
        viewModel.getProfile(viewModel.getSearchText(), isMakeSearch)
    }

    override fun getFilterText(text: String) {
        viewModel.setBrakeCall(false)
        isMakeSearch = true
        viewModel.setSearchText(text)
        getSearchProfileData(isShowProgressBar = false, isMakeSearch = true)
    }

    override fun followCategory(id: Long, isFollowing: Boolean?) {
        if (!isFollowing!!) viewModel.deleteSubscribedPublisher(id)
        else viewModel.subscribePublisher(id)
    }

    override fun getProfile() {
        viewModel.insertRecent(viewModel.getSearchText().toString())
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