package com.adamkapus.hikingapp.data.disk.route

import androidx.room.Dao
import androidx.room.Insert
import com.adamkapus.hikingapp.data.model.CoordinateItem

@Dao
interface CoordinateDao {

    @Insert
    suspend fun insertCoordinateItem(coordinateItem: CoordinateItem)

}