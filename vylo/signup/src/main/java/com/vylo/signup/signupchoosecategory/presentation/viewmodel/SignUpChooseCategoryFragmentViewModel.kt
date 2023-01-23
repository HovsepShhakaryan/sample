package com.vylo.signup.signupchoosecategory.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.signup.R
import com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryItem
import com.vylo.signup.signupchoosecategory.domain.usecase.CategoryMapperUseCase
import com.vylo.signup.signupchoosecategory.domain.usecase.CategoryUseCase
import kotlinx.coroutines.launch

class SignUpChooseCategoryFragmentViewModel(
    private val categoryUseCase: CategoryUseCase,
    private val generalErrorMapperUseCase: GeneralErrorMapperUseCase,
    private val categoryMapperUseCase: CategoryMapperUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<List<CategoryItem>> by lazy { SingleLiveEvent() }

    fun getCategory(searchText: String?) {
        viewModelScope.launch {
            when (val searchCategoryResponse = categoryUseCase.invokeSearchCategory(
                null,
                searchText
            )) {
                is Resource.Success -> {
                    if (searchCategoryResponse.data != null) {
                        val feedData = categoryMapperUseCase.fromSearchCategoryDataToSearchCategoryItemList(searchCategoryResponse.data!!)
                        responseSuccess.postValue(feedData)
                    } else responseError.postValue(context.resources.getString(R.string.label_no_data_available))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(searchCategoryResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(searchCategoryResponse.errorMessage)
            }
        }
    }

    fun addCategory(categoryId: Long) {
        viewModelScope.launch {
            when (val addCategoryResponse = categoryUseCase.addCategory(categoryId)) {
                is Resource.Success -> {
                    if (addCategoryResponse.data != null) {
                        //TODO something
                    } else responseError.postValue(context.resources.getString(R.string.label_no_data_available))
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

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            when (val addCategoryResponse = categoryUseCase.deleteCategory(categoryId)) {
                is Resource.Success -> {
                    if (addCategoryResponse.data != null) {
                        //TODO something
                    } else responseError.postValue(context.resources.getString(R.string.label_no_data_available))
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
}