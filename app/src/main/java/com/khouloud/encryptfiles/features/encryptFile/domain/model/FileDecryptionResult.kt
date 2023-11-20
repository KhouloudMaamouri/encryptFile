package com.khouloud.encryptfiles.features.encryptFile.domain.model

import java.io.File

sealed class FileDecryptionResult {
    data class Success(val success:String) : FileDecryptionResult()
    data class Failure(val errorMessage: String) : FileDecryptionResult()
}