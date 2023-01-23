package com.vylo.main.globalsearchmain.searchstartscreen.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.BaseFragment
import com.vylo.common.entity.RespondData
import com.vylo.common.util.GRAPH_ID
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.databinding.FragmentSearchStartBinding
import com.vylo.main.globalsearchmain.searchstartscreen.presentation.adapter.RecentTrendingAdapter
import com.vylo.main.globalsearchmain.searchstartscreen.presentation.viewmodel.SearchStartFragmentViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchStartFragment : BaseFragment<FragmentSearchStartBinding>(),
    RecentTrendingAdapter.AdapterCallBack {

    override fun getViewBinding() = FragmentSearchStartBinding.inflate(layoutInflater)
    private val viewModel by viewModel<SearchStartFragmentViewModel>()
    private lateinit var adapter: RecentTrendingAdapter
    private lateinit var adapterTrending: RecentTrendingAdapter
    private var isFirstTimeOpen = true
    private var graph: Int? = null
    private lateinit var activity: MainFlawActivity

    companion object {
        const val SEARCH_TEXT_BUNDLE = "search_text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = RecentTrendingAdapter(
            this,
            requireContext()
        )

        adapterTrending = RecentTrendingAdapter(
            this,
            requireContext()
        )
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
        activity.showNavBar()
    }

    private fun beginning() {
        createContentOfView()
    }

    private fun createContentOfView() {
        activity = getActivity() as MainFlawActivity
        graph = arguments?.getInt(GRAPH_ID)
        showProgress()
        viewModel.getRecents()
        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            hideProgress()
            if (it != null) {
                if(it.isNotEmpty()) viewBinder.containerRecent.visibility = View.VISIBLE
                else viewBinder.containerRecent.visibility = View.GONE
                if (adapter.getData().isNotEmpty()) adapter.clearData()
                adapter.setData(it)
            } else viewBinder.containerRecent.visibility = View.GONE
        }

        viewBinder.labelClear.setOnClickListener {
            viewModel.deleteAllRecents()
            adapter.clearData()
            viewBinder.containerRecent.visibility = View.GONE
        }

        viewModel.responseTrendingSuccess.observe(viewLifecycleOwner) {
            hideProgress()
            if (it != null) {
                if(it.isNotEmpty()) viewBinder.containerTrending.visibility = View.VISIBLE
                else viewBinder.containerTrending.visibility = View.GONE
                if (adapterTrending.getData().isNotEmpty()) adapterTrending.clearData()
                adapterTrending.setData(it)
            } else viewBinder.containerTrending.visibility = View.GONE
        }

        viewModel.responseError.observe(viewLifecycleOwner) {
            hideProgress()
            showMessage(it)
        }

        val linearLayoutManager = LinearLayoutManager(requireContext())
        viewBinder.recentList.layoutManager = linearLayoutManager
        viewBinder.recentList.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        val linearLayoutManagerTrending = LinearLayoutManager(requireContext())
        viewBinder.trendingList.layoutManager = linearLayoutManagerTrending
        viewBinder.trendingList.adapter = adapterTrending
        adapterTrending.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        searchListener()
        viewModel.getTrending()
    }

    private fun searchListener() {
        var lastInput = ""
        var debounceJob: Job? = null
        val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        viewBinder.search.apply {
            setHint(resources.getString(R.string.label_search))
            showHideClearButton(View.VISIBLE)
            getClearButton().setOnClickListener { clearSearchViewByClearButton() }
            showHideCancelButton(View.GONE)
            showHideCancelBackButton(View.VISIBLE)
            val clickOnCancelBack = View.OnClickListener {
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
                                if (isFirstTimeOpen) {
                                    isFirstTimeOpen = false
                                    hideKeyboard()
                                    val searchBudle = Bundle()
                                    searchBudle.putString(SEARCH_TEXT_BUNDLE, text.toString())
                                    searchBudle.putInt(GRAPH_ID, graph!!)
                                    clearSearchView()
                                    navigateTo(
                                        R.id.action_searchStartFragment_to_searchMainFragment,
                                        searchBudle
                                    )
                                } else isFirstTimeOpen = true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity?.currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun getSearchedText(text: String) {
        val searchBudle = Bundle()
        searchBudle.putString(SEARCH_TEXT_BUNDLE, text)
        searchBudle.putInt(GRAPH_ID, graph!!)
        navigateTo(
            R.id.action_searchStartFragment_to_searchMainFragment,
            searchBudle
        )
    }

    override fun showProgress() {
        super.showProgress()
        viewBinder.progressBar.show()
    }

    override fun hideProgress() {
        super.hideProgress()
        viewBinder.progressBar.hide()
    }

    override fun openCreateResponseScreen(
        id: String?,
        responseType: Int,
        title: String?,
        categoryId: String?,
        categoryName: String?
    ) {
        super.openCreateResponseScreen(id, responseType, title, categoryId, categoryName)
        if (!id.isNullOrEmpty())
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    RESPOND_INFO to RespondData(
                        responseToGlobalId = id,
                        title!!,
                        categoryId!!,
                        categoryName!!
                    ),
                    RESPONSE_TYPE to responseType
                )
            )
        else
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    RESPONSE_TYPE to responseType
                )
            )
    }
}