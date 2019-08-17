package com.yt8492.pe_bankapp.di.module

import com.yt8492.pe_bankapp.view.fragment.MapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MapActivityModule {

    @ContributesAndroidInjector
    abstract fun mapFragment(): MapFragment
}