package com.vylo.main.followmain.followingfragment.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.common.entity.ProfileItem
import com.vylo.common.entity.PublishersSubscription
import com.vylo.common.entity.toCommonCategoryItem
import com.vylo.common.util.enums.FollowingScreenType
import com.vylo.common.util.enums.RecommendedScreenType
import com.vylo.main.R
import com.vylo.main.component.adapter.UserAdapter
import com.vylo.main.followmain.followingfragment.domain.usecase.FollowingUseCase
import com.vylo.main.followmain.followingfragment.presentation.adapter.ProfileFollowAdapter
import com.vylo.main.globalsearchmain.searchprofiles.domain.usecase.SearchProfileMapperUseCase
import kotlinx.coroutines.launch

class FollowingViewModel(
    private val useCase: FollowingUseCase,
    private val mapperUseCase: GeneralErrorMapperUseCase,
    private val searchWithTextProfileMapperUseCase: SearchProfileMapperUseCase,
    private val searchWithTextProfileMapperUseCaseNewsstand: SearchProfileMapperUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    private var screenType: RecommendedScreenType? = null
    val profileError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val usersSuccess: SingleLiveEvent<List<PublishersSubscription>> by lazy { SingleLiveEvent() }
    val usersError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val publishersSuccess: SingleLiveEvent<List<PublishersSubscription>> by lazy { SingleLiveEvent() }
    val publishersError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val categoriesSuccess: SingleLiveEvent<List<CommonCategoryItem>> by lazy { SingleLiveEvent() }
    val categoriesError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val newsstandEmptyState = MediatorLiveData<Boolean>()

    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<List<PublishersSubscription>> by lazy { SingleLiveEvent() }

    val responseSearchError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSearchSuccess: SingleLiveEvent<List<PublishersSubscription>> by lazy { SingleLiveEvent() }

    val userAllPublishersSuccess: SingleLiveEvent<List<PublishersSubscription>> by lazy { SingleLiveEvent() }
    val userAllPublishersError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val userAllPublishersSearchSuccess: SingleLiveEvent<List<PublishersSubscription>> by lazy { SingleLiveEvent() }
    val userAllPublishersSearchError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    private var searchName: String? = null
    fun getSearchName() = searchName
    fun setSearchName(searchName: String?) {
        this.searchName = searchName
    }

    fun setScreenType(screenType: RecommendedScreenType) {
        this.screenType = screenType
    }

    private var users: List<PublishersSubscription>? = null
    fun getUsers() = users
    fun setUsers(users: List<PublishersSubscription>?) {
        this.users = users
    }

    private var usersAdapter: UserAdapter? = null
    fun setUsersAdapter(usersAdapter: UserAdapter) {
        this.usersAdapter = usersAdapter
    }

    fun getUsersAdapter() = usersAdapter

    private var usersData = mutableListOf<PublishersSubscription>()
    fun getUsersData() = usersData
    fun setUsersData(usersData: List<PublishersSubscription>?) {
        this.usersData.addAll(usersData!!)
    }

    private var usersSearchedData: List<PublishersSubscription>? = null
    fun getUsersSearchedData() = usersSearchedData
    fun setUsersSearchedData(usersSearchedData: List<PublishersSubscription>?) {
        this.usersSearchedData = usersSearchedData
    }

    private var recommendedAdapter: ProfileFollowAdapter? = null
    fun setRecommendedAdapter(recommendedAdapter: ProfileFollowAdapter) {
        this.recommendedAdapter = recommendedAdapter
    }

    fun getRecommendedAdapter() = recommendedAdapter

    private var searchAdapter: ProfileFollowAdapter? = null
    fun setSearchAdapter(searchAdapter: ProfileFollowAdapter) {
        this.searchAdapter = searchAdapter
    }

    fun getSearchAdapter() = searchAdapter

    fun setBrakeCall(isBrake: Boolean) {
        searchWithTextProfileMapperUseCase.setBrakeCall(isBrake)
    }

    fun setSearchBrakeCall(isBrake: Boolean) {
        searchWithTextProfileMapperUseCaseNewsstand.setBrakeCall(isBrake)
    }

    private var isCategoryEmpty = true
    private var isPublisherEmpty = true
    fun setCategoryIsEmpty(isCategoryEmpty: Boolean) {
        this.isCategoryEmpty = isCategoryEmpty
        newsstandEmptyState.postValue(this.isCategoryEmpty && this.isPublisherEmpty)
    }

    fun setPublisherIsEmpty(isPublisherEmpty: Boolean) {
        this.isPublisherEmpty = isPublisherEmpty
        newsstandEmptyState.postValue(this.isCategoryEmpty && this.isPublisherEmpty)
    }

    init {
//        newsstandEmptyState.addSource(publishersError) {
//            if (it.equals("") && categoriesError.value?.equals("") == true) {
//                newsstandEmptyState.postValue(true)
//            }
//        }
//        newsstandEmptyState.addSource(categoriesError) {
//            if (it.equals("") && publishersError.value?.equals("") == true) {
//                newsstandEmptyState.postValue(true)
//            }
//        }

        newsstandEmptyState.addSource(publishersSuccess) { newsstandEmptyState.postValue(false) }
        newsstandEmptyState.addSource(categoriesSuccess) { newsstandEmptyState.postValue(false) }
    }

    private fun getProfilesUsers(
        searchText: String?,
        isMakeSearch: Boolean,
        users: List<PublishersSubscription>?,
        isSearchScreen: Boolean
    ) {
        if (!searchWithTextProfileMapperUseCase.isBrakeCall())
            viewModelScope.launch {
                when (val searchProfileResponse = useCase.searchProfileUsersCall(
                    if (!isMakeSearch) searchWithTextProfileMapperUseCase.getPagingToken() else null,
                    searchText
                )) {
                    is Resource.Success -> {
                        if (searchProfileResponse.data != null) {
                            val usersData = mutableListOf<PublishersSubscription>()
                            val feedData =
                                searchWithTextProfileMapperUseCase.fromSearchProfileDataToPublishersSubscriptionList(
                                    searchProfileResponse.data!!
                                )
                            usersData.addAll(feedData!!)
                            if (!users.isNullOrEmpty())
                                usersData.removeAll(users)
                            when (isSearchScreen) {
                                true -> {
                                    val data = mutableListOf<PublishersSubscription>()
                                    if (users!!.isNotEmpty())
                                        for (item1 in feedData) {
                                            for (item2 in users)
                                                if (item1.id == item2.id) {
                                                    data.add(
                                                        item1.copy(
                                                            isFollow = false
                                                        )
                                                    )
                                                    break
                                                } else {
                                                    data.add(item1)
                                                }
                                        }
                                    else data.addAll(feedData)
                                    responseSearchSuccess.postValue(data)
                                }
                                false -> responseSuccess.postValue(usersData)
                            }
                        } else {
                            when (isSearchScreen) {
                                true -> responseSearchError.postValue(null)
                                false -> responseError.postValue(null)
                            }
                        }
                    }
                    is Resource.ApiError -> {
                        val apiErrorMessage =
                            mapperUseCase.getApiErrorMessage(searchProfileResponse.errorData)
                        apiErrorMessage?.detail.let {
                            when (isSearchScreen) {
                                true -> responseSearchError.postValue(apiErrorMessage!!.detail)
                                false -> responseError.postValue(apiErrorMessage!!.detail)
                            }
                        }
                    }
                    is Resource.Error -> {
                        when (isSearchScreen) {
                            true -> responseSearchError.postValue(searchProfileResponse.errorMessage)
                            false -> responseError.postValue(searchProfileResponse.errorMessage)
                        }
                    }
                }
            }
        else when (isSearchScreen) {
            true -> responseSearchError.postValue(null)
            false -> responseError.postValue(null)
        }

    }

    private fun getProfilesPublishers(
        searchText: String?,
        isMakeSearch: Boolean,
        publishers: List<PublishersSubscription>?,
        isSearchScreen: Boolean
    ) {
        if (!searchWithTextProfileMapperUseCaseNewsstand.isBrakeCall())
            viewModelScope.launch {
                when (val searchProfileResponse = useCase.searchProfilePublishersCall(
                    if (!isMakeSearch) searchWithTextProfileMapperUseCaseNewsstand.getPagingToken() else null,
                    searchText
                )) {
                    is Resource.Success -> {
                        if (searchProfileResponse.data != null) {
                            val publishersData = mutableListOf<PublishersSubscription>()
                            val feedData =
                                searchWithTextProfileMapperUseCaseNewsstand.fromSearchProfileDataToPublishersSubscriptionList(
                                    searchProfileResponse.data!!
                                )
                            publishersData.addAll(feedData!!)
                            if (!publishers.isNullOrEmpty())
                                publishersData.removeAll(publishers)
                            when (isSearchScreen) {
                                true -> {
                                    val data = mutableListOf<PublishersSubscription>()
                                    if (publishers!!.isNotEmpty())
                                        for (item1 in feedData) {
                                            for (item2 in publishers)
                                                if (item1.id == item2.id) {
                                                    data.add(
                                                        item1.copy(
                                                            isFollow = false
                                                        )
                                                    )
                                                    break
                                                } else {
                                                    data.add(item1)
                                                }
                                        }
                                    else data.addAll(feedData)
                                    responseSearchSuccess.postValue(data)
                                }
                                false -> responseSuccess.postValue(publishersData)
                            }
                        } else {
                            when (isSearchScreen) {
                                true -> responseSearchError.postValue(null)
                                false -> responseError.postValue(null)
                            }
                        }
                    }
                    is Resource.ApiError -> {
                        val apiErrorMessage =
                            mapperUseCase.getApiErrorMessage(searchProfileResponse.errorData)
                        apiErrorMessage?.detail.let {
                            when (isSearchScreen) {
                                true -> responseSearchError.postValue(apiErrorMessage!!.detail)
                                false -> responseError.postValue(apiErrorMessage!!.detail)
                            }
                        }
                    }
                    is Resource.Error -> {
                        when (isSearchScreen) {
                            true -> responseSearchError.postValue(searchProfileResponse.errorMessage)
                            false -> responseError.postValue(searchProfileResponse.errorMessage)
                        }
                    }
                }
            }
        else when (isSearchScreen) {
            true -> responseSearchError.postValue(null)
            false -> responseError.postValue(null)
        }
    }

    fun getProfile(
        isGetFollowing: Boolean,
        searchText: String?,
        isMakeSearch: Boolean,
        followingScreenType: FollowingScreenType?,
        isSearchScreen: Boolean
    ) {
        viewModelScope.launch {
            when (val response = useCase.getProfile()) {
                is Resource.Success -> {
                    response.data?.let {

                        if (!isGetFollowing) usersSuccess.postValue(fromProfileToUsers(it))
                        else {
                            when (followingScreenType) {
                                FollowingScreenType.VYLO -> getProfilesUsers(
                                    searchText,
                                    isMakeSearch,
                                    fromProfileToUsers(it),
                                    isSearchScreen
                                )
                                FollowingScreenType.NEWSSTAND -> getProfilesPublishers(
                                    searchText,
                                    isMakeSearch,
                                    fromProfileToPublishers(it),
                                    isSearchScreen
                                )
                                else -> {
                                }
                            }
                        }
                        // publishers
                        fromProfileToPublishers(it)?.let { publishers ->
                            if (publishers.isNotEmpty()) {
                                publishersSuccess.postValue(publishers)
                            } else publishersError.postValue("")
                        } ?: run {
                            publishersError.postValue("")
                        }

                        fromProfileToGetPublishers(it)?.let { publishers ->
                            if (publishers.isNotEmpty()) {
                                when (screenType) {
//                                    RecommendedScreenType.RECOMMENDED -> userAllPublishersSuccess.postValue(
//                                        publishers
//                                    )
//                                    else -> userAllPublishersSearchSuccess.postValue(publishers)
                                }
                            } else
                                when (screenType) {
//                                    RecommendedScreenType.RECOMMENDED -> userAllPublishersError.postValue(
//                                        ""
//                                    )
//                                    else -> userAllPublishersSearchError.postValue("")
                                }
                        } ?: run {
                            when (screenType) {
//                                RecommendedScreenType.RECOMMENDED -> userAllPublishersError.postValue(
//                                    ""
//                                )
//                                else -> userAllPublishersSearchError.postValue("")
                            }
                        }

                        // categories
                        fromProfileToCategories(it)?.let { categories ->
                            if (categories.isNotEmpty()) {
                                val categoryItem = mutableListOf<CommonCategoryItem>()
                                categoryItem.addAll(categories.map { it.toCommonCategoryItem() })
                                categoryItem.map { it.copy(isFollow = true) }.let {
                                    categoriesSuccess.postValue(it.distinct().sortedBy { it.name })
                                }
                            } else categoriesError.postValue("")
                        } ?: run {
                            categoriesError.postValue("")
                        }
                    } ?: run {
                        profileError.postValue("")
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) profileError.postValue(apiErrorMessage.detail)
                    else profileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    profileError.postValue(response.errorMessage)
                }
            }
        }
    }

    private fun fromProfileToUsers(profile: ProfileItem) =
        profile.publishersSubscription?.filter { it.isSource == false }

    private fun fromProfileToPublishers(profile: ProfileItem) =
        profile.publishersSubscription?.filter { it.isSource == true }

    private fun fromProfileToGetPublishers(profile: ProfileItem) =
        profile.publishersSubscription

    private fun fromProfileToCategories(profile: ProfileItem) =
        profile.categoriesSubscription

    fun addPublisher(id: Long) {
        viewModelScope.launch {
            when (val response = useCase.addPublisher(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        // users
                        fromProfileToUsers(it)?.let { users ->
//                            usersSuccess.postValue(users)
                            Log.d("FollowingViewModel", "addPublisher: $users")
                        } ?: run {
//                            usersError.postValue(context.resources.getString(R.string.no_users_data))
                        }
                        // publishers
                        fromProfileToPublishers(it)?.let { publishers ->
//                            publishersSuccess.postValue(publishers)
                            Log.d("FollowingViewModel", "addPublisher: $publishers")
                        } ?: run {
//                            publishersError.postValue(context.resources.getString(R.string.no_publishers_data))
                        }

//                        getProfile()

                    } ?: run {
                        profileError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) profileError.postValue(apiErrorMessage.detail)
                    else profileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> profileError.postValue(response.errorMessage)
            }
        }
    }

    fun deletePublisher(id: Long) {
        viewModelScope.launch {
            when (val response = useCase.deletePublisher(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        // users
                        fromProfileToUsers(it)?.let { users ->
//                            usersSuccess.postValue(users)
                            Log.d("FollowingViewModel", "addPublisher: $users")
                        } ?: run {
//                            usersError.postValue(context.resources.getString(R.string.no_users_data))
                        }
                        // publishers
                        fromProfileToPublishers(it)?.let { publishers ->
//                            publishersSuccess.postValue(publishers)
                            Log.d("FollowingViewModel", "addPublisher: $publishers")
                        } ?: run {
//                            publishersError.postValue(context.resources.getString(R.string.no_publishers_data))
                        }

//                        getProfile()

                    } ?: run {
                        profileError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) profileError.postValue(apiErrorMessage.detail)
                    else profileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> profileError.postValue(response.errorMessage)
            }
        }
    }

    fun addCategory(id: Long) {
        viewModelScope.launch {
            when (val response = useCase.addCategory(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        // categories
                        fromProfileToCategories(it)?.let { categories ->
//                            categoriesSuccess.postValue(categories)
                            Log.d("FollowingViewModel", "addPublisher: $categories")
                        } ?: run {
//                            categoriesError.postValue(context.resources.getString(R.string.no_categories_data))
                        }
                    } ?: run {
                        profileError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) profileError.postValue(apiErrorMessage.detail)
                    else profileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> profileError.postValue(response.errorMessage)
            }
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            when (val response = useCase.deleteCategory(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        // categories
                        fromProfileToCategories(it)?.let { categories ->
//                            categoriesSuccess.postValue(categories)
                            Log.d("FollowingViewModel", "addPublisher: $categories")
                        } ?: run {
//                            categoriesError.postValue(context.resources.getString(R.string.no_categories_data))
                        }
                    } ?: run {
                        profileError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) profileError.postValue(apiErrorMessage.detail)
                    else profileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> profileError.postValue(response.errorMessage)
            }
        }
    }
}