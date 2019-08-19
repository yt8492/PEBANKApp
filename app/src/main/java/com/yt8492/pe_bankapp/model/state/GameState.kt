package com.yt8492.pe_bankapp.model.state

import com.yt8492.pe_bankapp.model.datamodel.Cell
import com.yt8492.pe_bankapp.model.datamodel.Key
import com.yt8492.pe_bankapp.model.datamodel.Treasure

sealed class Status {
    data class Flight(val cell: Cell) : Status()

    data class Crash(val cell: Cell) : Status()

    data class FetchedKey(val cell: Cell, val key: Key) : Status()

    data class KeyOverflowed(val cell: Cell, val fetchedKey: Key, val ownKeys: List<Key>) : Status()

    data class FetchingTreasureSuccess(val cell: Cell, val treasure: Treasure) : Status()

    data class FetchingTreasureFailure(val cell: Cell) : Status()

    data class GameFinished(val message: String) : Status()
}