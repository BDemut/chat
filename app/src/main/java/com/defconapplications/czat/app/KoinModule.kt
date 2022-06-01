package com.defconapplications.czat.app

import com.defconapplications.czat.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {
    viewModel {
        MainViewModel()
    }
}