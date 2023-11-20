package com.khouloud.encryptfiles.features.encryptFile.domain.usesCases

import android.annotation.SuppressLint
import android.util.Log
import com.khouloud.encryptfiles.core.Constants
import com.khouloud.encryptfiles.features.encryptFile.data.local.FileCacheEntity
import com.khouloud.encryptfiles.features.encryptFile.domain.repository.FileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertFileUseCase @Inject constructor(
    private val repository: FileRepository,
) {

    private val TAG = InsertFileUseCase::class.simpleName

    @SuppressLint("LogNotTimber")
    suspend fun invoke(mFileCacheEntity: FileCacheEntity): Flow<Long> = flow {
        try {
            repository.save(mFileCacheEntity)
            emit(Constants.SUCCESS)
            Log.d(TAG, "SUCCESS")
        } catch (e: Exception) {
            Log.e(TAG, "error : $e")
            emit(Constants.ERROR)
        } finally {
            Log.d(TAG,"Block finally executed InsertFile")
        }
    }
}