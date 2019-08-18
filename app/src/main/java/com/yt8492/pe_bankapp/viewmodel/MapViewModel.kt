package com.yt8492.pe_bankapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yt8492.pe_bankapp.model.datamodel.Cell
import com.yt8492.pe_bankapp.model.datamodel.CellInfo
import com.yt8492.pe_bankapp.model.datamodel.Key
import com.yt8492.pe_bankapp.model.datasource.GameDataSource
import com.yt8492.pe_bankapp.model.state.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapViewModel(private val gameDataSource: GameDataSource) : ViewModel() {

    private val _state = MutableLiveData<Status>()
    val state: LiveData<Status>
        get() = _state

    private val keys = mutableListOf<Key>()

    var userWaiting = false

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun registerRoute(cells: List<Cell>) {
        startSearch(cells)
    }

    fun startSearch(cells: List<Cell>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (cell in cells) {
                val cellInfo = gameDataSource.getCellInfo(cell)
                when (cellInfo) {
                    is CellInfo.Flight -> _state.postValue(Status.Flight(cell))
                    is CellInfo.Crash -> {
                        _state.postValue(Status.Crash(cell))
                        keys.clear()
                        _state.postValue(Status.GameFinished("墜落しました。"))
                        return@launch
                    }
                    is CellInfo.FetchingKey -> {
                        keys.add(cellInfo.key)
                        if (keys.size < 4) {
                            gameDataSource.postGotKeyInfo(cellInfo.key)
                            _state.postValue(Status.FetchedKey(cell, cellInfo.key))
                        } else {
                            userWaiting = true
                            _state.postValue(Status.KeyOverflowed(cell, cellInfo.key, keys))
                            while (userWaiting) {
                                delay(500)
                            }
                        }
                    }
                    is CellInfo.FetchingTreasure -> {
                        val treasureKey = cellInfo.treasureKey
                        if (treasureKey in keys) {
                            val treasure = gameDataSource.openTreasure(treasureKey)
                            userWaiting = true
                            _state.postValue(Status.FetchingTreasureSuccess(cell, treasure))
                        } else {
                            _state.postValue(Status.FetchingTreasureFailure(cell))
                        }
                    }
                }
                delay(5000)
            }
            _state.postValue(Status.GameFinished("探索が終了しました。"))
        }
    }

    fun deleteKey(key: Key) {
        viewModelScope.launch(Dispatchers.IO) {
            val latestKey = keys.last()
            val removedKey = keys.first { it.id == key.id }
            keys.remove(removedKey)
            gameDataSource.postGotKeyInfo(latestKey, removedKey)
            userWaiting = false
        }
    }

    fun finishGame() {
        keys.clear()
    }
}