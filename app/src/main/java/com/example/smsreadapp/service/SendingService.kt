package com.example.smsreadapp.service

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.example.smsreadapp.MainActivity
import com.example.smsreadapp.R
import com.example.smsreadapp.api.RestApiService
import com.example.smsreadapp.api.SmsInfo
import com.example.smsreadapp.repo.AppDatabase
import com.example.smsreadapp.repo.Msg
import com.example.smsreadapp.repo.Repository
import kotlinx.coroutines.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class SendingService() {

    var scope = MainScope()
    @RequiresApi(Build.VERSION_CODES.M)
    fun startUpdates(context: Context) {
        scope.launch {

            while(true) {
                val repository: Repository
                val msgDao = AppDatabase.getDatabase(context).msgDao()
                repository = Repository(msgDao)
                val msgs = repository.getAll()
                val apiService = RestApiService()

                if(msgs.isNotEmpty()) {
                    Log.d("Major Tom", "Required permission for launch")
                    apiService.testConnection {
                        Log.d("Houston", "We are having a clear $it")
                        if (it=="200") {
                            msgs.forEach {
                                val smsInfo = SmsInfo(
                                    from = "${it.from}",
                                    timestamp = "${it.timestamp}",
                                    message = "${it.message}",
                                    message_id = "${it.timestamp}"
                                )
                                val msgObject = it
                                apiService.addSms(smsInfo,onResult = {
                                    val response = it
                                    if (response == 200) {
                                        scope.launch {repository.delete(msgObject)}
                                    } else {
                                        if(response == 400){
                                            val errorCode = "Error 400: ".plus(msgObject.message)
                                            Toast.makeText(
                                                context,
                                                errorCode,
                                                Toast.LENGTH_LONG).show()
                                        }
                                        else {
                                            Toast.makeText(
                                                context,
                                                R.string.connect_error,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                })
                            }
                        }
                        else{

                            Toast.makeText(
                                context,
                                R.string.network_error,
                                Toast.LENGTH_LONG
                            ).show()



                        }
                    }
                }
                delay(10000)
            }
        }
    }

    fun stopUpdates() {
        scope.cancel()
        scope = MainScope()
    }

}

