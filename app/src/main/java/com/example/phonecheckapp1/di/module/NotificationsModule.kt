package com.example.phonecheckapp1.di.module

import com.example.phonecheckapp1.ui.notifications.NotificationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val notificationsModule = module {
    viewModel {
        NotificationsViewModel()
    }
}