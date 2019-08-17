package com.yt8492.pe_bankapp.model.datasource

import com.yt8492.pe_bankapp.model.datamodel.Cell
import com.yt8492.pe_bankapp.model.datamodel.CellInfo
import com.yt8492.pe_bankapp.model.datamodel.Key
import com.yt8492.pe_bankapp.model.datamodel.Treasure

interface GameDataSource {
    suspend fun getCellInfo(cell: Cell): CellInfo
    suspend fun postGotKeyInfo(key: Key)
    suspend fun postGotKeyInfo(gotKey: Key, releasedKey: Key)
    suspend fun openTreasure(key: Key): Treasure

}