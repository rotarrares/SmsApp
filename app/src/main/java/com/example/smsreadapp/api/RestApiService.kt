package com.example.smsreadapp.api

import android.util.Log
import com.google.gson.JsonObject
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {
    fun addSms(smsData: SmsInfo, onResult: (Int?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addSms(smsData).enqueue(
            object : Callback<SmsInfo> {
                override fun onFailure(call: Call<SmsInfo>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<SmsInfo>, response: Response<SmsInfo>) {

                    onResult(response.code())
                }
            }
        )
    }

    fun deleteSms(smsData: SmsInfo, onResult: (Int?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteSms(smsData).enqueue(
            object : Callback<SmsInfo> {
                override fun onFailure(call: Call<SmsInfo>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<SmsInfo>, response: Response<SmsInfo>) {

                    onResult(response.code())
                }
            }
        )
    }

    fun testConnection( onResult: (String?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.testConnection().enqueue(
            object : Callback<SmsInfo> {
                override fun onFailure(call: Call<SmsInfo>, t: Throwable) {
                    onResult(t.toString())
                }
                override fun onResponse(call: Call<SmsInfo>, response: Response<SmsInfo>) {
                    val result = response.code()
                    onResult("$result")
                }
            }
        )
    }

    fun getPhones( onResult: (String?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getPhones().enqueue(
            object : Callback<RequestBody> {
                override fun onFailure(call: Call<RequestBody>, t: Throwable) {
                    onResult(call.toString())
                }
                override fun onResponse(call: Call<RequestBody>, response: Response<RequestBody>) {
                    onResult(call.toString())
                }
            }
        )
    }
}