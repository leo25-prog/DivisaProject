package com.example.proyectodivisa.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Moneda (
    @PrimaryKey(autoGenerate = true)
    var _id : Int,
    var base_code: String,
    var update: String,
    var code : String,
    var value : Double
)