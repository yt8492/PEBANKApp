package com.yt8492.pe_bankapp.model.state

import com.yt8492.pe_bankapp.model.datamodel.Cell
import com.yt8492.pe_bankapp.model.datamodel.Key
import com.yt8492.pe_bankapp.model.datamodel.Treasure

sealed class Status(open val cell: Cell) {
    data class Flight(override val cell: Cell) : Status(cell)

    data class Crash(override val cell: Cell) : Status(cell)

    data class FetchedKey(override val cell: Cell, val key: Key) : Status(cell)

    data class KeyOverflowed(override val cell: Cell, val fetchedKey: Key, val ownKeys: List<Key>) : Status(cell)

    data class FetchingTreasureSuccess(override val cell: Cell, val treasure: Treasure) : Status(cell)

    data class FetchingTreasureFailure(override val cell: Cell) : Status(cell)
}