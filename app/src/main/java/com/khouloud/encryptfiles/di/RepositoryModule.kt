package com.khouloud.encryptfiles.di

import android.content.Context
import androidx.room.Room
import com.khouloud.encryptfiles.dataBase.AppDataBase
import com.khouloud.encryptfiles.features.encryptFile.data.local.FileDao
import com.khouloud.encryptfiles.features.encryptFile.data.local.repository.FileRepositoryImpl
import com.khouloud.encryptfiles.features.encryptFile.domain.repository.FileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFileRepository(
        mFileDao: FileDao
    ): FileRepository {
        return FileRepositoryImpl(mFileDao)
    }

}