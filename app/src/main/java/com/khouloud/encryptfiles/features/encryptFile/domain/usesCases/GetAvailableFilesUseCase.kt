package com.khouloud.encryptfiles.features.encryptFile.domain.usesCases

import com.khouloud.encryptfiles.features.encryptFile.domain.manager.GetAvailableFilesManager
import com.khouloud.encryptfiles.features.encryptFile.domain.model.GetAvailableFilesResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAvailableFilesUseCase @Inject constructor(
    private val mGetAvailableFilesManager: GetAvailableFilesManager
) {

   suspend fun invoke(): Flow<GetAvailableFilesResult> = flow {
        try {
            emit(GetAvailableFilesResult.Loading())
            val result = mGetAvailableFilesManager.invoke()
            result.collect { data ->
                if (data.isFile) {
                    emit(GetAvailableFilesResult.Success(data))
                } else {
                    emit(GetAvailableFilesResult.Failure("File not found"))
                }
            }

        } catch (e: Exception) {
            emit(GetAvailableFilesResult.Failure("Error during Scan files: ${e.message}"))
        }
    }
}