package com.example.pokedex.pojo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "pokemon")
@Parcelize
data class Pokemon(
    @PrimaryKey
    val pokemonId: Long,
    val pokemonName: String,
    val imageUrl: String,
    val createdAt : Long = System.currentTimeMillis()
) : Parcelable