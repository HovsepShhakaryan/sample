package com.vylo.main.globalsearchmain.searchnewsstand.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.RecentUseCaseImpl
import com.vylo.common.util.enums.GraphType
import com.vylo.main.R
import com.vylo.globalsearch.searchnewsstand.domain.SearchNewsstandItem
import com.vylo.globalsearch.searchnewsstand.domain.usecase.impl.SearchNewsstandMapperUseCaseImpl
import com.vylo.globalsearch.searchnewsstand.domain.usecase.impl.SearchNewsstandUseCaseImpl
import com.vylo.main.globalsearchmain.searchnewsstand.presentation.adapter.NewsStandAdapter
import kotlinx.coroutines.launch

class SearchNewsstandFragmentViewModel(
    private val searchNewsstandUseCaseImpl: SearchNewsstandUseCaseImpl,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCaseImpl,
    private val searchNewsstandMapperUseCaseImpl: SearchNewsstandMapperUseCaseImpl,
    private val recentUseCaseImpl: RecentUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    private var adapter: NewsStandAdapter? = null
    private var actualDataVylo: List<SearchNewsstandItem>? = null
    private var actualDataNewsstand: List<SearchNewsstandItem>? = null
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<List<SearchNewsstandItem>> by lazy { SingleLiveEvent() }

    fun setActualData(actualData: List<SearchNewsstandItem>?, graph: Int?) {
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

    fun setAdapter(adapter: NewsStandAdapter) { this.adapter = adapter }
    fun getAdapter() = adapter

    fun getSearchNewsstand(searchText: String?, isMakeSearch: Boolean) {
        if (!searchText.isNullOrEmpty()) {
            if (!searchNewsstandMapperUseCaseImpl.isBrakeCall())
                viewModelScope.launch {
                    when (val searchVyloResponse = searchNewsstandUseCaseImpl.invokeSearchNewsstand(
                        if (!isMakeSearch) searchNewsstandMapperUseCaseImpl.getPagingToken() else null,
                        searchText
                    )) {
                        is Resource.Success -> {
                            if (searchVyloResponse.data != null) {
                                val feedData = searchNewsstandMapperUseCaseImpl.fromSearchNewsstandDataToSearchNewsstandItemList(searchVyloResponse.data!!)
                                responseSuccess.postValue(feedData)
                            } else responseError.postValue(context.resources.getString(R.string.label_no_vylo))
                        }
                        is Resource.ApiError -> {
                            val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(searchVyloResponse.errorData)
                            if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                            else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                        }
                        is Resource.Error -> responseError.postValue(searchVyloResponse.errorMessage)
                    }
                }
            else responseError.postValue(null)
        } else responseSuccess.postValue(null)
    }

    fun insertRecent(recent: String) {
        viewModelScope.launch {
            recentUseCaseImpl.insertRecent(recent)
        }
    }

    fun setBrakeCall(isBrake: Boolean) {
        searchNewsstandMapperUseCaseImpl.setBrakeCall(isBrake)
    }
}