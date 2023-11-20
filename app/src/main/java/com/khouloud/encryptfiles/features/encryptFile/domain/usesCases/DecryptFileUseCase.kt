package com.khouloud.encryptfiles.features.encryptFile.domain.usesCases

import android.content.Context
import android.util.Log
import com.khouloud.encryptfiles.core.Constants
import com.khouloud.encryptfiles.features.encryptFile.data.local.FileCacheEntity
import com.khouloud.encryptfiles.features.encryptFile.domain.manager.EncryptionManager
import com.khouloud.encryptfiles.features.encryptFile.domain.model.FileDecryptionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.*
import java.util.*
import javax.inject.Inject

class DecryptFileUseCase @Inject constructor(private val encryptionManager: EncryptionManager) {

    private val TAG = DecryptFileUseCase::class.simpleName

    fun decryptFile(
        mContext: Context,
        mFileCacheEntity: FileCacheEntity
    ): Flow<FileDecryptionResult> = flow {
        try {
            encryptionManager.decrypt(mContext, mFileCacheEntity)
            emit(FileDecryptionResult.Success(Constants.SUCCESS.toString()))
        } catch (e: Exception) {
            emit(FileDecryptionResult.Failure("Error during decryption: ${e.message}"))
        }
    }

}