package com.yt8492.pe_bankapp.di.module

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class MapModule {

    @Provides
    @Named("Columns")
    fun provideColumns() = 6

    @Provides
    @Named("Rows")
    fun provideRows() = 12
}