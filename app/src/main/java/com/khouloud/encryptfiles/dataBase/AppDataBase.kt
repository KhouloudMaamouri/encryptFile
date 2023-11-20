package com.khouloud.encryptfiles.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khouloud.encryptfiles.features.encryptFile.data.local.FileCacheEntity
import com.khouloud.encryptfiles.features.encryptFile.data.local.FileDao

@Database(
    entities = [FileCacheEntity::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun fileDao(): FileDao

    companion object {
        const val DATABASE_NAME: String = "DBEncryptFile"
    }


}