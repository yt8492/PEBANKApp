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
            when(Random.nextInt(20)) {
                in 0..5 -> CellInfo.Flight
                in 6..12 -> CellInfo.FetchingKey(listOf("1" to "Δ", "2" to "Φ", "3" to "Ω").map { Key(it.first, it.second) }.random())
                in 13..18 -> CellInfo.FetchingTreasure(listOf("1" to "Δ", "2" to "Φ", "3" to "Ω").map { Key(it.first, it.second) }.random())
                19 -> CellInfo.Crash
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