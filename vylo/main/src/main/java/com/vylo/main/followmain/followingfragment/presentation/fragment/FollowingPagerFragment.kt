package com.vylo.main.followmain.followingfragment.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.entity.PublishersSubscription
import com.vylo.common.ext.setTextOrGone
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toInvisible
import com.vylo.common.ext.toVisible
import com.vylo.common.util.FOLLOWING_BACK_TYPE
import com.vylo.common.util.FOLLOWING_TYPE
import com.vylo.common.util.USER_PROFILE
import com.vylo.main.R
import com.vylo.main.component.adapter.CategoryAdapter
import com.vylo.main.component.adapter.UserAdapter
import com.vylo.main.databinding.FragmentFollowingPagerBinding
import com.vylo.main.followmain.followingfragment.common.FollowingType
import com.vylo.main.followmain.followingfragment.presentation.adapter.PublisherAdapter
import com.vylo.main.followmain.followingfragment.presentation.viewmodel.FollowingViewModel
import com.vylo.main.profilefragment.common.ProfileData
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FollowingPagerFragment : BaseFragment<FragmentFollowingPagerBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by sharedViewModel<FollowingViewModel>()

    override fun getViewBinding() = FragmentFollowingPagerBinding.inflate(layoutInflater)

    private var isFromProfile = false

    private val userAdapter by lazy {
        UserAdapter(::onPublisherFollowClick, ::onUserClick)
    }
    private val publisherAdapter by lazy {
        PublisherAdapter(::onPublisherFollowClick, ::onUserClick)
    }
    private val categoryAdapter by lazy {
        CategoryAdapter(::onCategoryFollowClick)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setSearchName(viewBinder.search.getInputText().toString())
    }

    private fun beginning() {
        setViewSettings()
        setFollowingData()
        clearSavedDateFormFollowing()
    }

    private fun clearSavedDateFormFollowing() {
        viewModel.getUsersData().clear()
        viewModel.setUsersSearchedData(null)
    }

    private fun setViewSettings() {

        if (!viewModel.getUsers().isNullOrEmpty()) {
            val savedData = mutableListOf<PublishersSubscription>()
            savedData.addAll(viewModel.getUsers()!!)
            userAdapter.clearData()
            userAdapter.submitList(savedData)
            viewModel.setUsers(null)
        } else {
            showProgress()
            viewModel.getProfile(false, null, false, null, false)
        }

        viewBinder.apply {
            followingList.addItemDecoration(
                MarginItemDecoration(
                    bottom = resources.getDimensionPixelSize(
                        R.dimen.margin_padding_size_medium_small_small_mid
                    )
                )
            )

            categoryList.addItemDecoration(
                MarginItemDecoration(
                    bottom = resources.getDimensionPixelSize(
                        R.dimen.margin_padding_size_small_middle
                    )
                )
            )

            swipeToRefresh.setOnRefreshListener(this@FollowingPagerFragment)
        }

        arguments?.getBoolean(FOLLOWING_BACK_TYPE)?.let {
            isFromProfile = it
        }
    }

    private fun setFollowingData() {
        viewModel.setUsersAdapter(userAdapter)
        viewModel.profileError.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                arguments?.let {
//                    viewBinder.topEmptyState.toVisible()
                    if (it.getInt(FOLLOWING_TYPE) == FollowingType.VYLO.ordinal) {
                        viewBinder.topEmptyState.createFollowingVyloEmptyState(
                            onFindUsersToFollowClick = {
                                navigateToFindingScreen(FollowingType.VYLO)
                            }
                        )
                        userGone()
                    } else {
                        viewBinder.topEmptyState.createFollowingNewsstandEmptyState(
                            onFindPublishersToFollowClick = {
                                navigateToFindingScreen(FollowingType.NEWSSTAND)
                            },
                            onFindCategoriesToFollowClick = {
                                navigateToFindingCategoryScreen(FollowingType.NEWSSTAND)
                            }
                        )
                        publisherGone()
                        categoryGone()
                    }
                }
            } else {
                showMessage(it)
            }
        }

        viewBinder.apply {
            arguments?.let {
                if (it.getInt(FOLLOWING_TYPE) == FollowingType.VYLO.ordinal) {
                    search.initialize { text ->
                        if (viewModel.getSearchName() != null) {
                            userAdapter.filter.filter(viewModel.getSearchName())
                            viewModel.setSearchName(null)
                        }
                        else if (text!!.isEmpty())
                            viewModel.getProfile(false, null, false, null, false)
                        else
                            userAdapter.filter.filter(text)
                    }
                    setVyloData()
                } else {
                    setNewsstandData()
                }
            }
        }
    }

    private fun navigateToFindingScreen(followType: FollowingType) {
        navigateTo(
            R.id.action_followingFragment_to_followingSearchFragment,
            bundleOf(
                FOLLOWING_TYPE to followType,
                FOLLOWING_BACK_TYPE to isFromProfile
            )
        )
    }

    private fun navigateToFindingCategoryScreen(followType: FollowingType) {
        navigateTo(
            R.id.action_followingFragment_to_followingSearchCategoryFragment,
            bundleOf(
                FOLLOWING_TYPE to followType,
                FOLLOWING_BACK_TYPE to isFromProfile
            )
        )
    }

    private fun setVyloData() {
        viewBinder.apply {
            firstTitle.text = resources.getString(R.string.label_find_new_users)
            search.toVisible()
            secondTitle.setTextOrGone(null)
            followingList.adapter = userAdapter
            firstTitle.setOnClickListener {
                navigateToFindingScreen(FollowingType.VYLO)
            }

            secondTitle.setOnClickListener {
                navigateToFindingCategoryScreen(FollowingType.NEWSSTAND)
            }
        }

        viewModel.usersSuccess.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                viewBinder.topEmptyState.toVisible()
                viewBinder.topEmptyState.createFollowingVyloEmptyState(
                    onFindUsersToFollowClick = {
                        navigateToFindingScreen(FollowingType.VYLO)
                    }
                )
                userGone()
            } else {
                userAdapter.submitList(it)
                userVisible()
            }
            successResult()
        }

        viewModel.usersError.observe(viewLifecycleOwner) {
            errorResult()

            if (it.isEmpty()) {
                viewBinder.bottomEmptyState.toVisible()
                viewBinder.bottomEmptyState.createFollowingVyloEmptyState(
                    onFindUsersToFollowClick = {
                        navigateToFindingScreen(FollowingType.VYLO)
                    }
                )
                userGone()
            } else {
                showMessage(it)
            }
        }
    }

    private fun setNewsstandData() {
        viewBinder.apply {
            firstTitle.text = resources.getString(R.string.label_find_new_publishers)
            search.toGone()
            secondTitle.setTextOrGone(resources.getString(R.string.label_find_new_categories))
            followingList.adapter = publisherAdapter
            categoryList.adapter = categoryAdapter
            firstTitle.setOnClickListener {
                navigateToFindingScreen(FollowingType.NEWSSTAND)
            }

            secondTitle.setOnClickListener {
                navigateToFindingCategoryScreen(FollowingType.NEWSSTAND)
            }
        }

        viewModel.publishersSuccess.observe(viewLifecycleOwner) {
            viewModel.setPublisherIsEmpty(false)
            viewBinder.bottomEmptyState.toGone()
            publisherAdapter.submitList(it)
            successResult()
            publisherVisible()
        }

        viewModel.categoriesSuccess.observe(viewLifecycleOwner) {
            viewModel.setCategoryIsEmpty(false)
            viewBinder.bottomEmptyStateCategory.toGone()
            categoryAdapter.submitList(it)
            successResult()
            categoryVisible()
        }

        viewModel.publishersError.observe(viewLifecycleOwner) {
            viewModel.setPublisherIsEmpty(true)
            errorResult()

            if (it.isEmpty()) {
                viewBinder.bottomEmptyState.toVisible()
                viewBinder.bottomEmptyState.createFollowingNewsstandPublishersEmptyState(
                    onFindPublishersToFollowClick = {
                        navigateToFindingScreen(FollowingType.NEWSSTAND)
                    }
                )
                publisherGone()
            } else {
                showMessage(it)
            }
        }

        viewModel.categoriesError.observe(viewLifecycleOwner) {
            viewModel.setCategoryIsEmpty(true)
            errorResult()

            if (it.isEmpty()) {
                viewBinder.bottomEmptyStateCategory.toVisible()
                viewBinder.bottomEmptyStateCategory.createFollowingNewsstandCategoryEmptyState(
                    onFindCategoriesToFollowClick = {
                        navigateToFindingCategoryScreen(FollowingType.NEWSSTAND)
                    }
                )
                categoryGone()
            } else {
                showMessage(it)
            }
        }

        viewModel.newsstandEmptyState.observe(viewLifecycleOwner) {
            if (it) {
                errorResult()

                publisherGone()
                categoryGone()
                viewBinder.apply {
                    bottomEmptyState.toGone()
                    bottomEmptyStateCategory.toGone()

                    topEmptyState.toVisible()
                    topEmptyState.createFollowingNewsstandEmptyState(
                        onFindPublishersToFollowClick = {
                            navigateToFindingScreen(FollowingType.NEWSSTAND)
                        },
                        onFindCategoriesToFollowClick = {
                            navigateToFindingCategoryScreen(FollowingType.NEWSSTAND)
                        }
                    )
                }
            } else viewBinder.topEmptyState.toGone()
        }
    }

    private fun successResult() {
        viewBinder.swipeToRefresh.isRefreshing = false
        hideProgress()
    }

    private fun errorResult() {
        viewBinder.swipeToRefresh.isRefreshing = false
        hideProgress()
    }

    private fun refreshData() {
        userAdapter.refreshData()
        publisherAdapter.refreshData()
        categoryAdapter.refreshData()
        viewBinder.apply {
            search.clearSearchView()
            swipeToRefresh.isRefreshing = true
        }
        showProgress()
        viewModel.getProfile(false, null, false, null, false)
    }

    private fun onPublisherFollowClick(id: Long, isFollowing: Boolean) {
        userAdapter.setFollowItem(id, isFollowing)
        publisherAdapter.setFollowItem(id, isFollowing)

        if (isFollowing) {
            viewModel.addPublisher(id)
        } else {
            viewModel.deletePublisher(id)
        }
    }

    private fun onCategoryFollowClick(id: Long, isFollowing: Boolean) {
        categoryAdapter.setFollowItem(id, isFollowing)
        categoryAdapter.updateFollowStatus(id, isFollowing)
        if (isFollowing) {
            viewModel.addCategory(id)
            categoryAdapter.getSubCategoriesIds(id)?.map { subId ->
                subId?.let {
                    viewModel.addCategory(it)
                }
            }
        } else {
            viewModel.deleteCategory(id)
            categoryAdapter.getSubCategoriesIds(id)?.map { subId ->
                subId?.let {
                    viewModel.deleteCategory(it)
                }
            }
        }
    }

    private fun onUserClick(id: String?) {
        id?.let {
            navigateTo(R.id.action_followingFragment_to_profileFragment, bundleOf(
                USER_PROFILE to ProfileData(it),
                FOLLOWING_BACK_TYPE to isFromProfile
            ))
        }
    }

    override fun onRefresh() {
        refreshData()
        setFollowingData()
    }

    private fun userGone() {
        viewBinder.apply {
            firstTitle.toGone()
            search.toGone()
            followingList.toGone()
        }
    }

    private fun publisherGone() {
        viewBinder.apply {
            firstTitle.toGone()
            followingList.toGone()
        }
    }

    private fun categoryGone() {
        viewBinder.apply {
            secondTitle.toGone()
            categoryList.toGone()
        }
    }

    private fun userVisible() {
        viewBinder.apply {
            firstTitle.toVisible()
            search.toVisible()
            followingList.toVisible()
            topEmptyState.toGone()
        }
    }

    private fun publisherVisible() {
        viewBinder.apply {
            firstTitle.toVisible()
            followingList.toVisible()
        }
    }

    private fun categoryVisible() {
        viewBinder.apply {
            secondTitle.toVisible()
            categoryList.toVisible()
        }
    }

    override fun showProgress() {
        super.showProgress()
        viewBinder.container.toInvisible()
        viewBinder.progressBar.show()
    }

    override fun hideProgress() {
        super.hideProgress()
        viewBinder.container.toVisible()
        viewBinder.progressBar.hide()
    }
}