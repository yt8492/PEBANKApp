package com.yt8492.pe_bankapp

import android.app.Activity
import android.app.Application
import com.yt8492.pe_bankapp.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    internal lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.factory()
            .create(applicationContext)
            .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}