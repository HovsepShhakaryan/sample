package com.vylo.main.categorymain.category.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.main.R
import com.vylo.main.categorymain.category.domain.entity.Mapper
import com.vylo.main.categorymain.category.domain.usecase.impl.CategoryUseCaseImpl
import kotlinx.coroutines.launch

class CategoryFragmentViewModel(
    private val categoryUseCaseImpl: CategoryUseCaseImpl,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCaseImpl,
    private val mapper: Mapper,
    application: Application
) : AndroidViewModel(application) {

    val context = application
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<List<CommonCategoryItem>> by lazy { SingleLiveEvent() }

    fun getCategories() {
        viewModelScope.launch {
            when(val result = categoryUseCaseImpl.invokeCategories()) {
                is Resource.Success -> {
                    if (result.data != null) {
                        if (result.data!!.results != null) {
                            val categoryItems = mapper.fromCategoryToCategoryItem(result.data!!.results!!)
                            responseSuccess.postValue(categoryItems.sortedBy { it.name })
                        }
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(result.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(result.errorMessage)
            }
        }
    }
}