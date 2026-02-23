package com.fabianopinto.pingheng.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AssetEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun assetDao(): AssetDao
}
