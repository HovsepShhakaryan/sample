package com.vylo.main.component.sharedviewmodel.module

import com.vylo.main.component.sharedviewmodel.NavigationSharedViewModel
import com.vylo.main.settingsprivacy.presentation.viewmodel.SettingsSharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sharedViewModule = module {

    viewModel {
        NavigationSharedViewModel()
    }
}

val settingsSharedViewModel = module {
    viewModel {
        SettingsSharedViewModel()
    }
}