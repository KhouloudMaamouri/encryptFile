package com.khouloud.encryptfiles.features.encryptFile.data.local.repository

import com.khouloud.encryptfiles.features.encryptFile.data.local.FileCacheEntity
import com.khouloud.encryptfiles.features.encryptFile.data.local.FileDao
import com.khouloud.encryptfiles.features.encryptFile.domain.repository.FileRepository
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileDao: FileDao
) : FileRepository {

    override suspend fun save(mFileCacheEntity: FileCacheEntity) : Long{
       return fileDao.insert(mFileCacheEntity)
    }

    override suspend fun get(): List<FileCacheEntity> {
        return fileDao.get()
    }

}