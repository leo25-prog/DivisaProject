package com.example.proyectodivisa

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.proyectodivisa.Database.Moneda
import com.example.proyectodivisa.Database.MonedaDao
import com.example.proyectodivisa.Database.MonedaDatabase
import com.example.proyectodivisa.Interface.ExchangerateAPI
import com.example.proyectodivisa.Model.Posts
import com.example.proyectodivisa.WorkManager.MyWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var myJsonTxt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myJsonTxt = findViewById(R.id.jsonText)

        // EJECUTA EL WORKMANAGER PERIODICAMENTE
        val workManager = WorkManager.getInstance(applicationContext)

        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<MyWorker>(
            24, // Intervalo mínimo de tiempo en minutos
            TimeUnit.HOURS
            )
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)

        val applicationScope = CoroutineScope(SupervisorJob())
        val lista = MonedaDatabase.getDatabase(applicationContext, applicationScope).MonedaDao().getAllLista()

        for(list in lista){
            myJsonTxt.append(list.code + " -> " + list.value + "\n")
        }
    }
}