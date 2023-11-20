package com.khouloud.encryptfiles.features.encryptFile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file")
class FileCacheEntity (
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name:String,
    var extension:String
)