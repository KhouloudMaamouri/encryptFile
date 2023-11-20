package com.khouloud.encryptfiles.features.encryptFile.domain.usesCases

import android.annotation.SuppressLint
import android.util.Log
import com.khouloud.encryptfiles.features.encryptFile.data.local.FileCacheEntity
import com.khouloud.encryptfiles.features.encryptFile.domain.repository.FileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFileUseCase @Inject constructor(
    private val repository: FileRepository,
) {

    private val TAG = GetFileUseCase::class.simpleName

    @SuppressLint("LogNotTimber")
    suspend fun invoke(): Flow<List<FileCacheEntity>> = flow {
        try {
           val result =  repository.get()
            emit(result)
            Log.d(TAG, "SUCCESS")
        } catch (e: Exception) {
            Log.e(TAG, "error : $e")
            emit(emptyList())
        } finally {
            Log.d(TAG,"Block finally executed GetFileUseCase")
        }
    }
}