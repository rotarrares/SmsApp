package com.example.smsreadapp.api

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class SmsInfo (
    @SerializedName("from") val from: String?,
    @SerializedName("sent_timestamp") val timestamp: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("message_id") val message_id: String?
)


data class PhoneNumber(
    @SerializedName("anything")val response: JsonObject?
)