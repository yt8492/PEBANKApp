package com.yt8492.pe_bankapp.model.datamodel

sealed class CellInfo(val statusCode: Int) {
    object Flight : CellInfo(1)

    object Crash : CellInfo(2)

    data class FetchingKey(val key: Key) : CellInfo(3)

    data class FetchingTreasure(val treasureKey: Key) : CellInfo(4)
}

