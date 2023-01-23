package com.vylo.globalsearch.searchvylo.domain.usecase

import com.vylo.globalsearch.searchvylo.domain.entity.response.SearchVyloItem
import com.vylo.globalsearch.searchvylo.domain.entity.response.SearchVyloData

interface SearchVyloMapperUseCase {
    fun fromSearchVyloDataToSearchVyloItemList(searchVyloData: SearchVyloData): List<SearchVyloItem>?
    fun getPagingToken(): String?
}