package com.vylo.main.categorymain.categorytrendingfragment.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.common.entity.toCommonCategoryItem
import com.vylo.main.R
import com.vylo.main.categorymain.categorytrendingfragment.domain.entity.response.toCommonCategoryItem
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.CategoryUseCase
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.impl.CategoryMapperUseCaseImpl
import com.vylo.main.globalsearchmain.searchcategories.domain.entity.response.toCommonCategory
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val useCase: CategoryUseCase,
    private val mapperUseCase: GeneralErrorMapperUseCase,
    private val categoryMapperUseCaseImpl: CategoryMapperUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application

    var isCategoryFirstTime = true

    val categorySuccess: SingleLiveEvent<List<CommonCategoryItem>> by lazy { SingleLiveEvent() }
    val categoryError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val myCategorySuccess: SingleLiveEvent<List<CommonCategoryItem>> by lazy { SingleLiveEvent() }
    val myCategoryError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun getCategory(search: String?) {
        isCategoryFirstTime = false
        viewModelScope.launch {
            if (!categoryMapperUseCaseImpl.isBrakeCall()) {
                when (val response = useCase.getCategory(
                    search,
                    categoryMapperUseCaseImpl.getPagingToken()
                )) {
                    is Resource.Success -> {
                        response.data?.let {
                            val categoryData =
                                categoryMapperUseCaseImpl.fromSearchCategoryDataToCategoryItemList(
                                    response.data!!
                                )
                            val categories = mutableListOf<CommonCategoryItem>()
                            categoryData?.map { it.toCommonCategoryItem() }?.let {
                                categories.addAll(it)
                            }
                            categoryData?.map {
                                it.subcategories?.map { it.toCommonCategory() }?.let {
                                    categories.addAll(it)
                                }
                            }
                            categorySuccess.postValue(categories.distinct().sortedBy { it.name })
                        } ?: run {
                            categoryError.postValue(context.resources.getString(R.string.no_category_data))
                        }
                    }
                    is Resource.ApiError -> {
                        val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                        if (apiErrorMessage?.detail != null) categoryError.postValue(apiErrorMessage.detail)
                        else categoryError.postValue(context.resources.getString(R.string.label_something_wrong))
                    }
                    is Resource.Error -> {
                        categoryError.postValue(response.errorMessage)
                    }
                }
            } else {
                categoryError.postValue(context.resources.getString(R.string.no_category_data))
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            when (val response = useCase.getProfile()) {
                is Resource.Success -> {
                    response.data?.let {
                        val categories = mutableListOf<CommonCategoryItem>()
                        it.categoriesSubscription?.map { it.toCommonCategoryItem() }?.let {
                            categories.addAll(it)
                        }
//                        it.categoriesSubscription?.map {
//                            it.subcategories?.map { it.toCommonCategory() }?.let {
//                                categories.addAll(it)
//                            }
//                        }
                        myCategorySuccess.postValue(categories.distinct().sortedBy { it.name })
                    } ?: run {
                        myCategoryError.postValue(context.resources.getString(R.string.no_profile_data))
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) myCategoryError.postValue(apiErrorMessage.detail)
                    else myCategoryError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> myCategoryError.postValue(response.errorMessage)
            }
        }
    }

    fun addCategory(id: Long) {
        viewModelScope.launch {
            when (val response = useCase.addCategory(id)) {
                is Resource.Success -> {
                    response.data?.let {

                    } ?: run {
                        categoryError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) categoryError.postValue(apiErrorMessage.detail)
                    else categoryError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> categoryError.postValue(response.errorMessage)
            }
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            when (val response = useCase.deleteCategory(id)) {
                is Resource.Success -> {
                    response.data?.let {

                    } ?: run {
                        categoryError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) categoryError.postValue(apiErrorMessage.detail)
                    else categoryError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> categoryError.postValue(response.errorMessage)
            }
        }
    }

    fun setCategoryBrakeCall(isBrakeCall: Boolean) {
        categoryMapperUseCaseImpl.setBrakeCall(isBrakeCall)
    }
}