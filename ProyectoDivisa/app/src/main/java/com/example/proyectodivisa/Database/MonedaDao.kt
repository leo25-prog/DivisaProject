package com.example.proyectodivisa.Database

import android.database.Cursor
import android.provider.ContactsContract
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MonedaDao {
    @Insert
    fun insert(moneda: Moneda)

    @Update
    fun update(moneda : Moneda)

    @Query("SELECT * FROM Moneda WHERE code = :code")
    fun getByCode(code: String) : Moneda

    @Query("select * from Moneda")
    fun getAllCursor(): Cursor

    @Query("select * from Moneda")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<Moneda>>

    @Query ("delete from Moneda")
    fun deleteAll()
}