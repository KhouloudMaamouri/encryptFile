package com.khouloud.encryptfiles.di

import com.khouloud.encryptfiles.features.encryptFile.domain.manager.GetAvailableFilesManager
import com.khouloud.encryptfiles.features.encryptFile.domain.usesCases.GetAvailableFilesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Singleton
    @Provides
    fun provideFileScannerUseCase(mGetAvailableFilesManager: GetAvailableFilesManager) =
        GetAvailableFilesUseCase(mGetAvailableFilesManager)
}