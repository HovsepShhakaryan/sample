package com.vylo.main.globalsearchmain.searchstartscreen.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.RecentUseCaseImpl
import com.vylo.main.R
import com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase.SearchStartMapperUseCase
import com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase.SearchStartUseCase
import kotlinx.coroutines.launch

class SearchStartFragmentViewModel(
    private val recentUseCaseImpl: RecentUseCaseImpl,
    private val generalErrorMapperUseCase: GeneralErrorMapperUseCase,
    private val searchStartMapperUseCase: SearchStartMapperUseCase,
    private val searchStartUseCase: SearchStartUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    private var actualData: List<String>? = null
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<List<String>> by lazy { SingleLiveEvent() }
    val responseTrendingSuccess: SingleLiveEvent<List<String>> by lazy { SingleLiveEvent() }

    fun setActualData(actualData: List<String>) {
        this.actualData = actualData
    }

    fun getActualData() = this.actualData

    fun getRecents() {
        viewModelScope.launch {
            val recents = recentUseCaseImpl.getRecent()
            responseSuccess.postValue(recents)
        }
    }

    fun deleteAllRecents() {
        viewModelScope.launch {
            recentUseCaseImpl.deleteAllRecents()
        }
    }

    fun getTrending() {
        viewModelScope.launch {
            when (val searchStartResponse = searchStartUseCase.invokeTrending()) {
                is Resource.Success -> {
                    if (searchStartResponse.data != null) {
                        val trendingData = searchStartMapperUseCase.fromTrendingListToStringList(searchStartResponse.data!!)
                        responseTrendingSuccess.postValue(trendingData)
                    } else responseError.postValue(context.resources.getString(R.string.label_no_vylo))
                }
                is Resource.ApiError -> {
                    if (searchStartResponse.errorData != null) {
                        val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(searchStartResponse.errorData)
                        apiErrorMessage?.detail?.let { responseError.postValue(apiErrorMessage.detail) }
                    } else responseError.postValue(context.resources.getString(R.string.label_no_vylo))

                }
                is Resource.Error -> responseError.postValue(searchStartResponse.errorMessage)
            }
        }
    }
}