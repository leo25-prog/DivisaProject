package com.example.proyectodivisa.Interface

import com.example.proyectodivisa.Model.Posts
import retrofit2.Call
import retrofit2.http.GET

interface ExchangerateAPI {
    @get:GET("v6/064e537b97fd03303fa8e8ae/latest/USD")
    val posts : Call<Posts>
}