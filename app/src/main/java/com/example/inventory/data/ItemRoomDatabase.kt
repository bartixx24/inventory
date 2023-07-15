package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// exportSchema - do not keep schema version history backups
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase: RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        // all write and read requests will be done to and from the main memory and will never be cached
        @Volatile
        private var INSTANCE: ItemRoomDatabase ? = null

        fun getDatabase(context: Context): ItemRoomDatabase {
            // synchronized - only one thread of execution at a time can enter this block of code
            // database only gets initialized once
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context, ItemRoomDatabase::class.java, "item_database")
                        // migration strategy when the schema changes
                        // it defines how to take all rows with the old schema and convert them to rows in the new schema
                        // fallbackToDestructiveMigration() destroys and rebuilds the database
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}