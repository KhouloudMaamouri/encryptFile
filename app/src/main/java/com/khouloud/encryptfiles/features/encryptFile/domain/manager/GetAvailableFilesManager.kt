package com.khouloud.encryptfiles.features.encryptFile.domain.manager

import android.annotation.SuppressLint
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class GetAvailableFilesManager @Inject constructor(
) {
    private val TAG = GetAvailableFilesManager::class.simpleName

    suspend fun invoke(): Flow<File> = withContext(Dispatchers.IO) {
        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        return@withContext getFile(externalStorageDirectory)
    }

    @SuppressLint("LogNotTimber")
    private fun getFile(directory: File): Flow<File> = flow {
        Log.d(TAG, "Scanning directory: ${directory.absolutePath}")
        val files = directory.listFiles()
        if (files != null) {
             for (file in files) {
                if (file.isDirectory) {
                    // Recursively scan files in subdirectories
                    getFile(file).collect { directory -> emit(directory) }
                } else if (file.isFile) {
                    if (isDesiredFileType(file)) {
                        Log.e(TAG, "Found file: ${file.absolutePath}")
                        emit(file)
                        break
                    }
                }
            }

    } }

    private fun isDesiredFileType(file: File): Boolean {
        val allowedExtensions = listOf("jpg", "jpeg", "png", "gif","mp3", "mp4", "mkv","txt", "pdf", "csv", "doc", "docx","ogg")

        val fileName = file.name
        val extension = fileName.substringAfterLast('.', "")

        return allowedExtensions.contains(extension.toLowerCase())
    }
}