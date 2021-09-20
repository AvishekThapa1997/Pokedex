package com.example.pokedex.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    val nextOffset: Int?,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
