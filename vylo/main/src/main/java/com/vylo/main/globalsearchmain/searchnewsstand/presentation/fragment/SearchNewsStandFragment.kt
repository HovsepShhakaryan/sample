package com.vylo.main.globalsearchmain.searchnewsstand.presentation.fragment

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
import com.vylo.globalsearch.searchnewsstand.domain.SearchNewsstandItem
import com.vylo.main.globalsearchmain.searchnewsstand.presentation.adapter.NewsStandAdapter
import com.vylo.main.globalsearchmain.searchnewsstand.presentation.viewmodel.SearchNewsstandFragmentViewModel
import com.vylo.main.databinding.FragmentSearchNewsStandBinding

class SearchNewsStandFragment : BaseFragment<FragmentSearchNewsStandBinding>(),
    SearchMainFragment.Filtering,
    NewsStandAdapter.AdapterCallBack {

    override fun getViewBinding() = FragmentSearchNewsStandBinding.inflate(layoutInflater)
    private lateinit var adapter: NewsStandAdapter
    private var scrollBottomListener: ScrollBottomListener? = null
    private var searchText: String? = null
    private var isMakeSearch = true

    companion object {
        private lateinit var viewModel: SearchNewsstandFragmentViewModel
        private var graph: Int? = null

        fun newInstance(viewModel: SearchNewsstandFragmentViewModel, graph: Int): SearchNewsStandFragment {
            this.viewModel = viewModel
            this.graph = graph
            return SearchNewsStandFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = NewsStandAdapter(
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setActualData(adapter.getData(), graph)
    }


    private fun beginning() {
        createContentOfView()
    }

    private fun createContentOfView() {
        if (viewModel.getActualData(graph!!) != null) {
            val savedData = mutableListOf<SearchNewsstandItem>()
            savedData.addAll(viewModel.getActualData(graph!!)!!)
            adapter.clearData()
            adapter.setData(savedData)
            viewModel.setActualData(null, 0)
        } else showProgress()

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
        val linearLayoutManager = LinearLayoutManager(requireContext())
        viewBinder.newsStandList.layoutManager = linearLayoutManager
        viewBinder.newsStandList.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        scrollBottomListener = object : ScrollBottomListener(linearLayoutManager) {
            override fun onScrolledToBottom(): Boolean {
                isMakeSearch = false
                getSearchNewsstandData(isShowProgressBar = true, isMakeSearch = false)
                scrollBottomListener!!.isSilent(true)
                return false
            }
        }
    }

    private fun getSearchNewsstandData(isShowProgressBar: Boolean, isMakeSearch: Boolean) {
        if (isShowProgressBar) showProgress()
        viewModel.getSearchNewsstand(searchText, isMakeSearch)
    }

    override fun getFilterText(text: String) {
        viewModel.setBrakeCall(false)
        isMakeSearch = true
        searchText = text
        getSearchNewsstandData(isShowProgressBar = false, isMakeSearch = true)
    }

    override fun getNewsstandNews() {
        viewModel.insertRecent(searchText.toString())
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