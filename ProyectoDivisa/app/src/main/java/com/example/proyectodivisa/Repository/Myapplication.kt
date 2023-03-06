package com.example.proyectodivisa.Repository

import android.app.Application
import com.example.proyectodivisa.Database.MonedaDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class Myapplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { MonedaDatabase.getDatabase(this, applicationScope) }
    val repositoryMoneda by lazy {  MonedaRepository (database.MonedaDao()) }

    override fun onCreate() {
        super.onCreate()

    }
}