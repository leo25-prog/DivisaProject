package com.example.proyectodivisa.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Moneda::class], version = 1, exportSchema = false)
abstract class MonedaDatabase  : RoomDatabase(){
    abstract fun MonedaDao(): MonedaDao

    companion object {
        private var INSTANCE: MonedaDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): MonedaDatabase {

            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context,MonedaDatabase::class.java, "moneda")
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}