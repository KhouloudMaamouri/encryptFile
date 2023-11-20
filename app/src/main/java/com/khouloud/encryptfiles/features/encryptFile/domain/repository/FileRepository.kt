package com.khouloud.encryptfiles.features.encryptFile.domain.repository

import com.khouloud.encryptfiles.features.encryptFile.data.local.FileCacheEntity

interface FileRepository {
    suspend fun save(mFileCacheEntity: FileCacheEntity): Long
    suspend fun get():List<FileCacheEntity>
}