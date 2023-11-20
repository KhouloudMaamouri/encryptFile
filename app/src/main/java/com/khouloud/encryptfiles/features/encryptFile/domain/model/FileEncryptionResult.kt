package com.khouloud.encryptfiles.features.encryptFile.domain.model

import java.io.File

sealed class FileEncryptionResult {
    data class Success(val encryptedFile: File) : FileEncryptionResult()
    data class Failure(val errorMessage: String) : FileEncryptionResult()
}