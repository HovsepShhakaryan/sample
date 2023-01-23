package com.vylo.main.globalsearchmain.searchprofiles.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.RecentUseCaseImpl
import com.vylo.common.entity.PublishersSubscription
import com.vylo.common.util.enums.GraphType
import com.vylo.main.R
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileItem
import com.vylo.main.globalsearchmain.searchprofiles.domain.usecase.SearchProfileMapperUseCase
import com.vylo.main.globalsearchmain.searchprofiles.domain.usecase.SearchProfileUseCase
import com.vylo.main.globalsearchmain.searchprofiles.presentation.adapter.ProfileAdapter
import kotlinx.coroutines.launch

class SearchProfileFragmentViewModel(
    private val searchProfileUseCase: SearchProfileUseCase,
    private val generalErrorMapperUseCase: GeneralErrorMapperUseCase,
    private val searchProfileMapperUseCase: SearchProfileMapperUseCase,
    private val recentUseCaseImpl: RecentUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    private var adapter: ProfileAdapter? = null
    private var actualDataVylo: List<ProfileItem>? = null
    private var actualDataNewsstand: List<ProfileItem>? = null
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<List<ProfileItem>> by lazy { SingleLiveEvent() }
    val profileError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun setActualData(actualData: List<ProfileItem>?, graph: Int?) {
        when (graph) {
            GraphType.VYLO_GRAP.type -> actualDataVylo = actualData
            GraphType.NEWSTAND_GRAPH.type -> actualDataNewsstand = actualData
            else -> {}
        }
    }

    fun getActualData(grap: Int) =
        when (grap) {
            1 -> actualDataVylo
            2 -> actualDataNewsstand
            else -> mutableListOf()
        }

    private var searchText: String? = null
    fun setSearchText(text: String) {
        searchText = text
    }

    fun getSearchText() = searchText

    fun setAdapter(adapter: ProfileAdapter) {
        this.adapter = adapter
    }

    fun getAdapter() = adapter

    fun getProfile(searchText: String?, isMakeSearch: Boolean) {
        viewModelScope.launch {
            when (val response = searchProfileUseCase.getProfile()) {
                is Resource.Success -> {
                    getProfiles(searchText, isMakeSearch, response.data?.publishersSubscription)
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) profileError.postValue(apiErrorMessage.detail)
                    else profileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    profileError.postValue(response.errorMessage)
                }
            }
        }
    }

    private fun getProfiles(searchText: String?, isMakeSearch: Boolean, publishersSubscription: List<PublishersSubscription>?) {
        if (!searchText.isNullOrEmpty()) {
            if (!searchProfileMapperUseCase.isBrakeCall())
                viewModelScope.launch {
                    when (val searchProfileResponse = searchProfileUseCase.searchProfileCall(
                        if (!isMakeSearch) searchProfileMapperUseCase.getPagingToken() else null,
                        searchText
                    )) {
                        is Resource.Success -> {
                            if (searchProfileResponse.data != null) {
                                val profileItems = mutableListOf<ProfileItem>()
                                val feedData = searchProfileMapperUseCase.fromSearchProfileDataToSearchProfileItemList(searchProfileResponse.data!!)
                                if (publishersSubscription != null)
                                    feedData?.forEach { profileItem ->
                                        publishersSubscription.forEach start@ {
                                            if (profileItem.id == it.id) {
                                                profileItem.isFollow = true
                                                return@start
                                            }
                                        }
                                        profileItems.add(profileItem)
                                    }
                                responseSuccess.postValue(profileItems)
                            } else responseError.postValue(context.resources.getString(R.string.label_no_vylo))
                        }
                        is Resource.ApiError -> {
                            val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(searchProfileResponse.errorData)
                            if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                            else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                        }
                        is Resource.Error -> responseError.postValue(searchProfileResponse.errorMessage)
                    }
                }
            else responseError.postValue(null)
        } else responseSuccess.postValue(null)
    }

    fun subscribePublisher(categoryId: Long) {
        viewModelScope.launch {
            when (val addCategoryResponse = searchProfileUseCase.subscribePublisher(categoryId)) {
                is Resource.Success -> {
                    if (addCategoryResponse.data != null) {
                        //TODO something
                    } else responseError.postValue(context.resources.getString(R.string.label_no_vylo))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(addCategoryResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(addCategoryResponse.errorMessage)
            }
        }
    }

    fun deleteSubscribedPublisher(categoryId: Long) {
        viewModelScope.launch {
            when (val addCategoryResponse = searchProfileUseCase.deleteSubscribedPublisher(categoryId)) {
                is Resource.Success -> {
                    if (addCategoryResponse.data != null) {
                        //TODO something
                    } else responseError.postValue(context.resources.getString(R.string.label_no_vylo))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(addCategoryResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(addCategoryResponse.errorMessage)
            }
        }
    }

    fun insertRecent(recent: String) {
        viewModelScope.launch {
            recentUseCaseImpl.insertRecent(recent)
        }
    }

    fun setBrakeCall(isBrake: Boolean) {
        searchProfileMapperUseCase.setBrakeCall(isBrake)
    }
}