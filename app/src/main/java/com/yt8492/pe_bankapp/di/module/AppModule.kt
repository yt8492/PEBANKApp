package com.yt8492.pe_bankapp.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.yt8492.pe_bankapp.model.datasource.GameDataSource
import com.yt8492.pe_bankapp.model.infra.repository.MockGameRepository
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Named

@Module
class AppModule {

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Named("GUID")
    fun provideGuid(sharedPreferences: SharedPreferences): String {
        return sharedPreferences.getString(KEY_GUID, null) ?: run {
            UUID.randomUUID().toString().also { uuid ->
                sharedPreferences.edit {
                    putString(KEY_GUID, uuid)
                }
            }
        }
    }

    @Provides
    fun provideGameDataSource(): GameDataSource = MockGameRepository

    companion object {
        private const val PREF_NAME = "PEBANK_APP"
        private const val KEY_GUID = "GUID"
    }
}