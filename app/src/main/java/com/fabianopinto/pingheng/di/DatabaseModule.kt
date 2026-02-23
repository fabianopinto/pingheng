package com.fabianopinto.pingheng.di

import android.content.Context
import androidx.room.Room
import com.fabianopinto.pingheng.data.local.AppDatabase
import com.fabianopinto.pingheng.data.local.AssetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pingheng_db"
        ).build()
    }

    @Provides
    fun provideAssetDao(database: AppDatabase): AssetDao {
        return database.assetDao()
    }
}
