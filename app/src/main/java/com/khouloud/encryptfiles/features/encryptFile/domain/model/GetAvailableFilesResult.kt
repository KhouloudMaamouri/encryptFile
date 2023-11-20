package com.khouloud.encryptfiles.features.encryptFile.domain.model

import java.io.File

sealed class GetAvailableFilesResult {
    data class Loading(val loading:String? = null): GetAvailableFilesResult()
    data class Success(val file: File) : GetAvailableFilesResult()
    data class Failure(val errorMessage: String) : GetAvailableFilesResult()
}