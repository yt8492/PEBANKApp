package com.yt8492.pe_bankapp.di.component

import android.content.Context
import com.yt8492.pe_bankapp.App
import com.yt8492.pe_bankapp.di.module.ActivityModule
import com.yt8492.pe_bankapp.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(app: App)
}