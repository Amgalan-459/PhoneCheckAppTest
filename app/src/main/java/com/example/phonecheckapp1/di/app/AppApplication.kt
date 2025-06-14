package com.example.phonecheckapp1.di.app

import android.app.Application
import com.example.phonecheckapp1.di.module.homeModule
import com.example.phonecheckapp1.di.module.notificationsModule
import com.example.phonecheckapp1.di.module.phoneNumModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@AppApplication)
            modules(homeModule, notificationsModule, phoneNumModule)
        }
    }
}