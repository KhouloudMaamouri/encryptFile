package com.khouloud.encryptfiles.di

import android.content.Context
import androidx.room.Room
import com.khouloud.encryptfiles.dataBase.AppDataBase
import com.khouloud.encryptfiles.features.encryptFile.data.local.FileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDataBase {
        val builder = Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java, AppDataBase.DATABASE_NAME
        )
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideFileDao(database: AppDataBase): FileDao {
        return database.fileDao()
    }


}