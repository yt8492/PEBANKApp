package com.yt8492.pe_bankapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yt8492.pe_bankapp.model.datasource.GameDataSource
import com.yt8492.pe_bankapp.viewmodel.MapViewModel
import javax.inject.Inject

class MapViewModelFactory @Inject constructor(
    private val gameDataSource: GameDataSource
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        require(modelClass == MapViewModel::class.java) {
            "Unknown ViewModel Class"
        }
        return MapViewModel(gameDataSource) as T
    }
}