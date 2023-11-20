package com.khouloud.encryptfiles.features.encryptFile.domain.usesCases

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.khouloud.encryptfiles.core.Constants
import com.khouloud.encryptfiles.features.encryptFile.domain.manager.EncryptionManager
import com.khouloud.encryptfiles.features.encryptFile.domain.model.FileEncryptionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.io.*

import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

import java.io.IOException;
import java.nio.file.*
import java.util.EnumSet.of
import java.util.Set.of
import kotlin.io.path.Path

class EncryptFileUseCase @Inject constructor(private val encryptionManager: EncryptionManager) {

    private val TAG = EncryptFileUseCase::class.simpleName

    @RequiresApi(Build.VERSION_CODES.M)
    fun encryptFile(context: Context, inputFile: File): Flow<FileEncryptionResult> = flow {
        try {
            val encryptionResult = encryptionManager.encrypt(context, inputFile)
            emit(FileEncryptionResult.Success(encryptionResult))
        } catch (e: Exception) {
            emit(FileEncryptionResult.Failure("Error during encryption: ${e.message}"))
        }
    }


}