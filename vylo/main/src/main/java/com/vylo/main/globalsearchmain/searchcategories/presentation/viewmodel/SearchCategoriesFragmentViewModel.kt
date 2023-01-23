package com.vylo.main.globalsearchmain.searchcategories.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.RecentUseCaseImpl
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.common.entity.toCommonCategoryItem
import com.vylo.globalsearch.searchcategories.domain.usecase.impl.SearchCategoryMapperUseCaseImpl
import com.vylo.main.R
import com.vylo.main.globalsearchmain.searchcategories.domain.entity.response.toCommonCategory
import com.vylo.main.globalsearchmain.searchcategories.domain.usecase.impl.SearchCategoryUseCaseImpl
import com.vylo.main.globalsearchmain.searchcategories.presentation.adapter.CategoryAdapter
import kotlinx.coroutines.launch

class SearchCategoriesFragmentViewModel(
    private val searchCategoryUseCaseImpl: SearchCategoryUseCaseImpl,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCaseImpl,
    private val searchCategoryMapperUseCaseImpl: SearchCategoryMapperUseCaseImpl,
    private val recentUseCaseImpl: RecentUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    private var actualData: List<CommonCategoryItem>? = null
    private var isLoadData = true
    private var categoryAdapter: CategoryAdapter? = null
    private var adapter: CategoryAdapter? = null
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<List<CommonCategoryItem>> by lazy { SingleLiveEvent() }
    val responseSuccessActualData: SingleLiveEvent<List<CommonCategoryItem>> by lazy { SingleLiveEvent() }
    val profileError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun setActualData(actualData: List<CommonCategoryItem>?) {
        this.actualData = actualData
    }

    fun getActualData() = actualData

    fun getIsLoadData() = isLoadData
    fun setIsLoadData(isLoadData: Boolean) {
        this.isLoadData = isLoadData
    }

    fun setAdapter(categoryAdapter: CategoryAdapter) {
        this.categoryAdapter = categoryAdapter
    }

    fun getAdapter() = categoryAdapter

    fun setMainAdapter(adapter: CategoryAdapter) {
        this.adapter = adapter
    }

    fun getMainAdapter() = adapter

    fun getProfile(searchText: String?, isCategoryManipulate: Boolean) {
        viewModelScope.launch {
            when (val response = searchCategoryUseCaseImpl.getProfile()) {
                is Resource.Success -> {
                    val categories = mutableListOf<CommonCategoryItem>()
                    response.data?.categoriesSubscription?.map { it.toCommonCategoryItem() }?.let {
                        categories.addAll(it)
                    }
//                    response.data?.categoriesSubscription?.map {
//                        it.subcategories?.map { it.toCommonCategory() }?.let {
//                            categories.addAll(it)
//                        }
//                    }
                    getSearchCategory(searchText, categories, isCategoryManipulate)
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) profileError.postValue(apiErrorMessage.detail)
                    else profileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    profileError.postValue(response.errorMessage)
                }
            }
        }
    }

    private fun getSearchCategory(
        searchText: String?,
        categoriesSubscription: List<CommonCategoryItem>?,
        isCategoryManipulate: Boolean
    ) {
        viewModelScope.launch {
            when (val searchCategoryResponse = searchCategoryUseCaseImpl.invokeSearchCategory(
                null,
                searchText
            )) {
                is Resource.Success -> {
                    if (searchCategoryResponse.data != null) {
                        val searchCategoryItems = mutableListOf<CommonCategoryItem>()
                        val feedData =
                            searchCategoryMapperUseCaseImpl.fromSearchCategoryDataToSearchCategoryItemList(
                                searchCategoryResponse.data!!
                            )
                        val categoriesData = mutableListOf<CommonCategoryItem>()
                        feedData?.map { it.toCommonCategory() }?.let {
                            categoriesData.addAll(it)
                        }
                        feedData?.map {
                            it.subcategories?.map { it.toCommonCategory() }?.let {
                                categoriesData.addAll(it)
                            }
                        }
                        if (categoriesSubscription != null && categoriesSubscription.isNotEmpty())
                            categoriesData.forEach { profileItem ->
                                categoriesSubscription.forEach start@{
                                    if (profileItem.id == it.id) {
                                        profileItem.isFollow = true
                                        return@start
                                    }
                                }
                                searchCategoryItems.add(profileItem)
                            }
                        else categoriesData.forEach { item ->
                            searchCategoryItems.add(item)
                        }
                        if (!isCategoryManipulate)
                            responseSuccess.postValue(
                                searchCategoryItems.distinct().sortedBy { it.name })
                        else
                            responseSuccessActualData.postValue(
                                searchCategoryItems.distinct().sortedBy { it.name })
                    } else responseError.postValue(context.resources.getString(R.string.label_no_vylo))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(searchCategoryResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(searchCategoryResponse.errorMessage)
            }
        }
    }

    fun addCategory(categoryId: Long) {
        viewModelScope.launch {
            when (val addCategoryResponse = searchCategoryUseCaseImpl.addCategory(categoryId)) {
                is Resource.Success -> {
                    if (addCategoryResponse.data != null) {
                        getProfile(null, true)
                    } else responseError.postValue(context.resources.getString(R.string.label_no_vylo))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(addCategoryResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(addCategoryResponse.errorMessage)
            }
        }
    }

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            when (val addCategoryResponse = searchCategoryUseCaseImpl.deleteCategory(categoryId)) {
                is Resource.Success -> {
                    if (addCategoryResponse.data != null) {
                        getProfile(null, true)
                    } else responseError.postValue(context.resources.getString(R.string.label_no_vylo))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(addCategoryResponse.errorData)
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
}