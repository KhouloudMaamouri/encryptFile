package com.khouloud.encryptfiles.features.encryptFile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mFileCacheEntity: FileCacheEntity): Long

    @Query("SELECT * FROM file")
    suspend fun get(): List<FileCacheEntity>
}