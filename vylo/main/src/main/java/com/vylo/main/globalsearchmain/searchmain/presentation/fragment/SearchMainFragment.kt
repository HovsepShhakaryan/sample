package com.vylo.main.globalsearchmain.searchmain.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.FragmentAdapter
import com.vylo.common.entity.RespondData
import com.vylo.common.entity.VideoData
import com.vylo.common.entity.WebData
import com.vylo.common.util.*
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.databinding.FragmentSearchMainBinding
import com.vylo.main.globalsearchmain.searchcategories.presentation.fragment.SearchCategoriesFragment
import com.vylo.main.globalsearchmain.searchcategories.presentation.viewmodel.SearchCategoriesFragmentViewModel
import com.vylo.main.globalsearchmain.searchmain.presentation.viewmodel.SearchMainFragmentViewModel
import com.vylo.main.globalsearchmain.searchnewsstand.presentation.adapter.NewsStandAdapter
import com.vylo.main.globalsearchmain.searchnewsstand.presentation.fragment.SearchNewsStandFragment
import com.vylo.main.globalsearchmain.searchnewsstand.presentation.viewmodel.SearchNewsstandFragmentViewModel
import com.vylo.main.globalsearchmain.searchprofiles.presentation.adapter.ProfileAdapter
import com.vylo.main.globalsearchmain.searchprofiles.presentation.fragment.SearchProfilesFragment
import com.vylo.main.globalsearchmain.searchprofiles.presentation.viewmodel.SearchProfileFragmentViewModel
import com.vylo.main.globalsearchmain.searchvylo.presentation.adapter.VyloAdapter
import com.vylo.main.globalsearchmain.searchvylo.presentation.fragment.SearchVyloFragment
import com.vylo.main.globalsearchmain.searchvylo.presentation.viewmodel.SearchVyloFragmentViewModel
import com.vylo.main.profilefragment.common.ProfileData
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchMainFragment : BaseFragment<FragmentSearchMainBinding>(),
    VyloAdapter.SearchResultCallBack,
    NewsStandAdapter.SearchResultCallBack,
    ProfileAdapter.NavigateToProfile {

    interface Filtering {
        fun getFilterText(text: String)
    }

    companion object {
        const val SEARCH_TEXT_BUNDLE = "search_text"
    }

    private var filteringVylo: Filtering? = null
    private var filteringNewsStand: Filtering? = null
    private var filteringCategories: Filtering? = null
    private var filteringProfiles: Filtering? = null
    private lateinit var activity: MainFlawActivity
    override fun getViewBinding() = FragmentSearchMainBinding.inflate(layoutInflater)
    private val viewModel by viewModel<SearchMainFragmentViewModel>()
    private val viewModelVylo by viewModel<SearchVyloFragmentViewModel>()
    private val viewModelNewsstand by viewModel<SearchNewsstandFragmentViewModel>()
    private val viewModelCategory by viewModel<SearchCategoriesFragmentViewModel>()
    private val viewModelProfile by viewModel<SearchProfileFragmentViewModel>()

    private var graph: Int? = null

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
        viewModel.setSearchName(viewBinder.search.getInputText().toString())
    }

    override fun onResume() {
        super.onResume()
        activity.showNavBar()
    }

    private fun beginning() {
        createToolBar()
        createContentOfView()
    }

    private fun createToolBar() {
        activity = getActivity() as MainFlawActivity
        viewBinder.toolbar.apply {
            val typeface = ResourcesCompat.getFont(requireContext(), com.vylo.common.R.font.suisse_lntl_regular)
            setTitleFontFamily(typeface)
            setButtonBackText(resources.getString(R.string.label_search_results))
            setStyleButtonBackText(com.vylo.common.R.style.MainText_H2_1)
        }
    }

    private fun createContentOfView() {
        showNavBar()
        val searchText = arguments?.getString(SEARCH_TEXT_BUNDLE)
        graph = arguments?.getInt(GRAPH_ID)!!
        viewModel.setValue(graph!!)
        VyloAdapter.setSearchResultCallBackListener(this)
        NewsStandAdapter.setSearchResultCallBackListener(this)
        ProfileAdapter.setListenerToNavigation(this)

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
                                if (lastInput != viewModel.getSearchName()) {
                                    makeSearch(text.toString())
                                    viewModel.setSearchName(null)
                                }
                            }
                        }
                    }
                }
            }
        }

        val searchVyloFragment = SearchVyloFragment.newInstance(viewModelVylo, graph!!)
        val searchNewsStandFragment = SearchNewsStandFragment.newInstance(viewModelNewsstand, graph!!)
        val searchCategoriesFragment = SearchCategoriesFragment.newInstance(viewModelCategory)
        val searchProfilesFragment = SearchProfilesFragment.newInstance(viewModelProfile, graph!!)

        val fragmentAdapter = FragmentAdapter(this)
        fragmentAdapter.addFragment(searchVyloFragment, resources.getString(R.string.label_vylo))
        fragmentAdapter.addFragment(searchNewsStandFragment, resources.getString(R.string.label_news_stand))
        fragmentAdapter.addFragment(searchCategoriesFragment, resources.getString(R.string.label_categories))
        fragmentAdapter.addFragment(searchProfilesFragment, resources.getString(R.string.label_profiles))
        viewBinder.pager.adapter = fragmentAdapter
        TabLayoutMediator(viewBinder.tabLayout, viewBinder.pager) { tab, position ->
            tab.text = fragmentAdapter.getPageTitle(position)
        }.attach()
        viewBinder.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        setFilterForVylo(searchVyloFragment)
        setFilterForNewsstand(searchNewsStandFragment)
        setFilterForCategories(searchCategoriesFragment)
        setFilterForProfiles(searchProfilesFragment)

        viewBinder.search.setInputText(searchText!!)
    }

    private fun setFilterForVylo(filtering: Filtering) {
        this.filteringVylo = filtering
    }

    private fun setFilterForNewsstand(filtering: Filtering) {
        this.filteringNewsStand = filtering
    }

    private fun setFilterForCategories(filtering: Filtering) {
        this.filteringCategories = filtering
    }

    private fun setFilterForProfiles(filtering: Filtering) {
        this.filteringProfiles = filtering
    }

    private fun makeSearch(searchText: String) {
        filteringVylo?.getFilterText(searchText)
        filteringNewsStand?.getFilterText(searchText)
        filteringCategories?.getFilterText(searchText)
        filteringProfiles?.getFilterText(searchText)
    }

    override fun navigateToProfile(id: String?) {
        id?.let {
            findNavController().navigate(
                R.id.action_searchMainFragment_to_profileFragment,
                bundleOf(USER_PROFILE to ProfileData(it))
            )
        }
    }

    override fun navigateToNews(data: WebData) {
        findNavController().navigate(
            R.id.action_searchMainFragment_to_webFragment,
            bundleOf(WEB_DATA to data)
        )
    }

    override fun navigateToResponse(data: VideoData) {
        findNavController().navigate(
            R.id.action_searchMainFragment_to_videoFragment,
            bundleOf(VIDEO_DATA to data)
        )
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