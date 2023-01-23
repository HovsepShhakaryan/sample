package com.vylo.globalsearch.searchnewsstand.domain.usecase

import com.vylo.globalsearch.searchnewsstand.domain.SearchNewsstandData
import com.vylo.globalsearch.searchnewsstand.domain.SearchNewsstandItem

interface SearchNewsstandMapperUseCase {
    fun fromSearchNewsstandDataToSearchNewsstandItemList(searchNewsstandData: SearchNewsstandData): List<SearchNewsstandItem>?
    fun getPagingToken(): String?
}