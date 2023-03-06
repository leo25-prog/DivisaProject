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
        getPosts()

        // EJECUTA EL WORKMANAGER PERIODICAMENTE
        val workManager = WorkManager.getInstance(applicationContext)

        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<MyWorker>(
            15, // Intervalo mínimo de tiempo en minutos
            TimeUnit.MINUTES
            )
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)
    }

    private fun getPosts(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var api : ExchangerateAPI = retrofit.create(ExchangerateAPI::class.java)

        var call : Call<Posts> = api.posts

        call.enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                if(!response.isSuccessful) {
                    myJsonTxt.setText("Codigo: " + response.code())
                    return
                }

                var post = response.body()

                val applicationScope = CoroutineScope(SupervisorJob())
                var moneda = Moneda(
                    _id = 0,
                    code = "",
                    value = 0.0
                )
                post!!.conversion_ratesonversions.forEach { codes ->
                    moneda.code = codes.key
                    moneda.value = codes.value
                    myJsonTxt.append(moneda.code + "  " + moneda.value.toString() + "\n")
                    MonedaDatabase.getDatabase(applicationContext, applicationScope).MonedaDao().insert(moneda)
                }
            }

            override fun onFailure(call: Call<Posts>, t: Throwable) {
                myJsonTxt.setText(t.message)
            }
        })

    }

}