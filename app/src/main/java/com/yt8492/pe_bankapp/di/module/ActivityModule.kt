package com.yt8492.pe_bankapp.di.module

import com.yt8492.pe_bankapp.view.activity.MapActivity
import com.yt8492.pe_bankapp.di.scope.ActivityScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [MapActivityModule::class, MapModule::class])
    @ActivityScoped
    abstract fun mapActivity(): MapActivity
}