package com.example.smsreadapp.api


import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RestApi {

    @Headers("Content-Type: application/json")
    @POST("post-test")
    fun addSms(@Body smsData: SmsInfo): Call<SmsInfo>
    @POST("delete-test")
    fun deleteSms(@Body smsData: SmsInfo): Call<SmsInfo>
    @GET("healthcheck")
    fun testConnection(): Call<SmsInfo>
    @GET( "phones")
    fun getPhones():Call<RequestBody>
}