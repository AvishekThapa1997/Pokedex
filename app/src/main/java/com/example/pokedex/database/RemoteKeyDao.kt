package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: RemoteKey)

    @Query("SELECT * FROM remote_keys ORDER BY id DESC LIMIT 1")
    suspend fun getLastKey(): RemoteKey

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllKeys()

}