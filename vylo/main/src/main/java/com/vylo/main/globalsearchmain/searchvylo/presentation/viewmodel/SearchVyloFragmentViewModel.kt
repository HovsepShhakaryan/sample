package com.vylo.main.globalsearchmain.searchvylo.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.RecentUseCaseImpl
import com.vylo.common.util.enums.GraphType
import com.vylo.main.R
import com.vylo.globalsearch.searchvylo.domain.entity.response.SearchVyloItem
import com.vylo.main.globalsearchmain.searchvylo.domain.usecase.impl.SearchVyloMapperUseCaseImpl
import com.vylo.globalsearch.searchvylo.domain.usecase.impl.SearchVyloUseCaseImpl
import com.vylo.main.globalsearchmain.searchvylo.presentation.adapter.VyloAdapter
import kotlinx.coroutines.launch

class SearchVyloFragmentViewModel(
    private val searchVyloUseCaseImpl: SearchVyloUseCaseImpl,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCaseImpl,
    private val searchVyloMapperUseCaseImpl: SearchVyloMapperUseCaseImpl,
    private val recentUseCaseImpl: RecentUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    private var actualDataVylo: List<SearchVyloItem>? = null
    private var actualDataNewsstand: List<SearchVyloItem>? = null
    private var adapter: VyloAdapter? = null
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<List<SearchVyloItem>> by lazy { SingleLiveEvent() }

    fun setActualData(actualData: List<SearchVyloItem>?, graph: Int?) {
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

    fun setAdapter(adapter: VyloAdapter) {
        this.adapter = adapter
    }

    fun getAdapter() = adapter

    fun getSearchVylo(searchText: String?, isMakeSearch: Boolean) {
        if (!searchText.isNullOrEmpty()) {
            if (!searchVyloMapperUseCaseImpl.isBrakeCall())
                viewModelScope.launch {
                    when (val searchVyloResponse = searchVyloUseCaseImpl.invokeSearchVylo(
                        if (!isMakeSearch) searchVyloMapperUseCaseImpl.getPagingToken() else null,
                        searchText
                    )) {
                        is Resource.Success -> {
                            if (searchVyloResponse.data != null) {
                                val feedData = searchVyloMapperUseCaseImpl.fromSearchVyloDataToSearchVyloItemList(searchVyloResponse.data!!)
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
        searchVyloMapperUseCaseImpl.setBrakeCall(isBrake)
    }
}