package com.yt8492.pe_bankapp.model.infra.repository

import com.yt8492.pe_bankapp.model.datamodel.Cell
import com.yt8492.pe_bankapp.model.datamodel.CellInfo
import com.yt8492.pe_bankapp.model.datamodel.Key
import com.yt8492.pe_bankapp.model.datamodel.Treasure
import com.yt8492.pe_bankapp.model.datasource.GameDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

object MockGameRepository : GameDataSource {
    override suspend fun getCellInfo(cell: Cell): CellInfo {
        return withContext(Dispatchers.IO) {
            when(Random.nextInt(10)) {
                in 0..3 -> CellInfo.Flight
                in 4..6 -> CellInfo.FetchingKey(listOf("1", "2").map { Key(it) }.random())
                7, 8 -> CellInfo.FetchingTreasure(listOf("1", "2", "3").map { Key(it) }.random())
                9 -> CellInfo.Crash
                else -> throw IllegalArgumentException()
            }
        }
    }

    override suspend fun postGotKeyInfo(key: Key) {

    }

    override suspend fun postGotKeyInfo(gotKey: Key, releasedKey: Key) {

    }

    override suspend fun openTreasure(key: Key): Treasure {
        return Treasure(key.id, "おたから", "https://1.bp.blogspot.com/-abtG2HYMsA8/UU--5kLFD0I/AAAAAAAAO_w/ta20nlofB6Y/s1600/kaizoku_takara.png")
    }
}