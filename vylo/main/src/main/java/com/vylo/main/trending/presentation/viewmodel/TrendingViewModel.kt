package com.vylo.main.trending.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.MyProfileUseCase
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.common.ext.toText
import com.vylo.main.R
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.CategoryUseCase
import com.vylo.main.component.kebab.domain.entity.request.ReportRequest
import com.vylo.main.component.kebab.domain.entity.response.EventCounterData
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.homefragment.domain.usecase.impl.FeedMapperUseCaseImpl
import com.vylo.main.trending.domain.usecase.TrendingUseCase
import kotlinx.coroutines.launch

class TrendingViewModel(
    private val useCase: TrendingUseCase,
    private val kebabUseCase: KebabUseCase,
    private val myProfileUseCase: MyProfileUseCase,
    private val mapperUseCase: GeneralErrorMapperUseCase,
    private val feedMapperUseCaseImpl: FeedMapperUseCaseImpl,
    private val useCaseCategory: CategoryUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application

    private var actualData: List<FeedItem>? = null
    var isTrendingFirstTime = true
    private var categoryItem: CommonCategoryItem? = null

    val trendingSuccess: SingleLiveEvent<List<FeedItem>> by lazy { SingleLiveEvent() }
    val trendingError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val eventError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val eventSuccess: SingleLiveEvent<EventCounterData> by lazy { SingleLiveEvent() }
    val deleteResponseSuccess: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val categoryError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    private var categoryId: Long? = null
    private var isActive: Boolean? = null

    fun setCategoryId(categoryId: Long?) {
        this.categoryId = categoryId
    }
    fun getCategoryId() = categoryId

    fun setIsActive(isActive: Boolean) {
        this.isActive = isActive
    }
    fun getIsActive() = isActive

    val myGlobalId: String?
        get() = myProfileUseCase.myGlobalId

    val isEnableDataCall: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent() }

    fun setActualData(actualData: List<FeedItem>?) {
        this.actualData = actualData
    }

    fun getActualData() = this.actualData

    fun setCategoryItem(categoryItem: CommonCategoryItem?) {
        this.categoryItem = categoryItem
    }

    fun getCategoryItem() = this.categoryItem

    fun getTrending(categoryId: String?) {
        isTrendingFirstTime = false
        viewModelScope.launch {
            if (!feedMapperUseCaseImpl.isBrakeCall()) {
                when (val response =
                    useCase.getTrending(feedMapperUseCaseImpl.getPagingToken(), categoryId)) {
                    is Resource.Success -> {
                        response.data?.let {
                            val feedData =
                                feedMapperUseCaseImpl.fromFeedDataToFeedItemList(response.data!!)
                            trendingSuccess.postValue(feedData)
                            isEnableDataCall.postValue(!feedMapperUseCaseImpl.isBrakeCall())
                        } ?: run {
                            trendingError.postValue(context.resources.getString(R.string.no_trending_data))
                        }
                    }
                    is Resource.ApiError -> {
                        val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                        if (apiErrorMessage?.detail != null) trendingError.postValue(apiErrorMessage.detail)
                        else trendingError.postValue(context.resources.getString(R.string.label_something_wrong))
                    }
                    is Resource.Error -> {
                        trendingError.postValue(response.errorMessage)
                    }
                }
            }
        }
    }

    fun getMyProfile() {
        viewModelScope.launch {
            when (val signInResponse = useCase.getMyProfile()) {
                is Resource.Success -> {
                    signInResponse.data?.globalId?.let { id ->
                        useCase.saveMyGlobalId(id)
                    } ?: run {
                        trendingError.postValue(context.resources.getString(R.string.no_profile_data))
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage =
                        mapperUseCase.getApiErrorMessage(signInResponse.errorData)
                    if (apiErrorMessage?.detail != null) trendingError.postValue(apiErrorMessage.detail)
                    else trendingError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> trendingError.postValue(signInResponse.errorMessage)
            }
        }
    }

    fun setTrendingBrakeCall(isBrakeCall: Boolean) {
        feedMapperUseCaseImpl.setBrakeCall(isBrakeCall)
    }

    fun getEventCounter(id: String) {
        viewModelScope.launch {
            when (val response = kebabUseCase.getEventCounter(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        eventSuccess.postValue(
                            EventCounterData(
                                globalId = id,
                                eventCounterItem = it
                            )
                        )
                    } ?: run {
                        eventError.postValue(context.resources.getString(R.string.no_trending_data))
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun addLike(data: EventCounterData) {
        viewModelScope.launch {
            when (val response = kebabUseCase.addLike(data.globalId)) {
                is Resource.Success -> {
                    getEventCounter(data.globalId)
                }
                is Resource.ApiError -> {
                    val errorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun deleteLike(data: EventCounterData) {
        viewModelScope.launch {
            when (val response = kebabUseCase.deleteLike(data.globalId)) {
                is Resource.Success -> {
                    getEventCounter(data.globalId)
                }
                is Resource.ApiError -> {
                    val errorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun sendReport(status: Int, globalId: String) {
        viewModelScope.launch {
            val request = ReportRequest(status, globalId)
            when (val response = kebabUseCase.sendReport(request)) {
                is Resource.Success -> {

                }
                is Resource.ApiError -> {
                    val errorMessage = mapperUseCase.getApiReportErrorMessage(response.errorData)
                    if (errorMessage?.newsGlobalId != null) trendingError.postValue(errorMessage.newsGlobalId!!.toText())
                    else trendingError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    trendingError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun shareReport(id: String) {
        viewModelScope.launch {
            when (val response = kebabUseCase.shareReport(id)) {
                is Resource.Success -> {
//                    getEventCounter(data.globalId)
                }
                is Resource.ApiError -> {
                    val errorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun viewReport(id: String) {
        viewModelScope.launch {
            when (val response = kebabUseCase.viewReport(id)) {
                is Resource.Success -> {
//                    getEventCounter(data.globalId)
                }
                is Resource.ApiError -> {
                    val errorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun deleteNews(id: String) {
        viewModelScope.launch {
            when (val response = useCase.deleteNews(id)) {
                is Resource.Success -> {
                    deleteResponseSuccess.postValue(id)
                }
                is Resource.ApiError -> {
                    val errorMessage =
                        mapperUseCase.getApiUpdateNewsErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) trendingError.postValue(errorMessage.detail)
                    else trendingError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> trendingError.postValue(response.errorMessage)
            }
        }
    }

    fun addCategory(id: Long) {
        viewModelScope.launch {
            when (val response = useCaseCategory.addCategory(id)) {
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
            when (val response = useCaseCategory.deleteCategory(id)) {
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

}