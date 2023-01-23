package com.vylo.main.globalsearchmain.searchmain.presentation.viewmodel

import androidx.lifecycle.ViewModel

class SearchMainFragmentViewModel : ViewModel() {

    private var searchName: String? = null
    private var value = 0

    fun getSearchName() = searchName

    fun setSearchName(searchName: String?) {
        this.searchName = searchName
    }

    fun setValue(value: Int) { this.value = value }
    fun getValue() = value

}