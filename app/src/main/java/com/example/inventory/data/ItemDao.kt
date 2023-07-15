package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object is used to separate the persistence layer with the rest of the application )
 * DAO hides all the complexities involved in performing the database operations in the underlying persistence
 * DAO is an interface that accesses the database
 * Room will generate the implementation of this class at compile time
 */
@Dao
interface ItemDao {

    @Query("SELECT * FROM item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * FROM item ORDER BY name")
    fun getItems(): Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

}