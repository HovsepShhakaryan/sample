package com.vylo.main.insightfulfragment.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.PagingUseCase
import com.vylo.main.R
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.insightfulfragment.domain.entity.response.InsightfulItem
import com.vylo.main.insightfulfragment.domain.usecase.InsightfulUseCase
import com.vylo.main.insightfulfragment.domain.usecase.impl.InsightfulMapperUseCaseImpl
import kotlinx.coroutines.launch

class InsightfulViewModel(
    private val useCase: InsightfulUseCase,
    private val kebabUseCase: KebabUseCase,
    private val mapperUseCase: GeneralErrorMapperUseCase,
    private val insightfulMapperUseCaseImpl: InsightfulMapperUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application

    val insightfulSuccess: SingleLiveEvent<List<InsightfulItem>> by lazy { SingleLiveEvent() }
    val insightfulError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val eventError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun getInsightful() {
        viewModelScope.launch {
            if (!insightfulMapperUseCaseImpl.isBrakeCall()) {
                when (val response = useCase.getInsightful(
                    insightfulMapperUseCaseImpl.getPagingToken()
                )) {
                    is Resource.Success -> {
                        response.data?.let {
                            val insightfulData =
                                insightfulMapperUseCaseImpl.fromInsightfulDataToInsightfulItemList(response.data!!)
                            insightfulSuccess.postValue(insightfulData)
                        } ?: run {
                            insightfulError.postValue(context.resources.getString(R.string.no_insightful_data))
                        }
                    }
                    is Resource.ApiError -> {
                        mapperUseCase.getApiErrorMessage(response.errorData)?.let {
                            insightfulError.postValue(it.detail)
                        }
                    }
                    is Resource.Error -> {
                        insightfulError.postValue(response.errorMessage)
                    }
                }
            } else {
                insightfulError.postValue(context.resources.getString(R.string.no_insightful_data))
            }
        }
    }

    fun setInsightfulBrakeCall(isBrakeCall: Boolean) {
        insightfulMapperUseCaseImpl.setBrakeCall(isBrakeCall)
    }

    fun viewReport(id: String) {
        viewModelScope.launch {
            when (val response = kebabUseCase.viewReport(id)) {
                is Resource.Success -> {
//                    getEventCounter(data.globalId)
                }
                is Resource.ApiError -> {
                    val errorMessage =
                        mapperUseCase.getApiErrorMessage(response.errorData)
                    errorMessage?.detail.let {
                        eventError.postValue(it)
                    }
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }
}