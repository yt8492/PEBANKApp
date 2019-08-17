package com.yt8492.pe_bankapp.di.module

import com.yt8492.pe_bankapp.view.activity.MainActivity
import com.yt8492.pe_bankapp.di.scope.ActivityScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    @ActivityScoped
    abstract fun mainActivity(): MainActivity
}