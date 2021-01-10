package com.example.smsreadapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.widget.Toast
import com.example.smsreadapp.api.RestApiService
import com.example.smsreadapp.api.SmsInfo
import com.example.smsreadapp.repo.AppDatabase
import com.example.smsreadapp.repo.Msg
import com.example.smsreadapp.repo.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class SMSReceiver : BroadcastReceiver() {

    private fun sendMessage(context: Context,timeRcv:Long,phoneNumber:String?,msgText:String?,msgId:Long){
        val apiService = RestApiService()
        val smsInfo = SmsInfo(
            from = "$phoneNumber",
            timestamp = "$timeRcv",
            message = "$msgText",
            message_id = "$msgId" )

        val repository: Repository

        val msgDao = AppDatabase.getDatabase(context).msgDao()
        repository = Repository(msgDao)
        val from = "$phoneNumber"
        val timestamp = "$timeRcv"
        val message = "$msgText"
        val message_id = "$msgId"

        GlobalScope.launch {
            repository.insert(Msg(message_id,from,timestamp,message))
        }
    }

    override fun onReceive(context: Context, intent: Intent) {

        val extras = intent.extras
        if(extras != null){
            val sms = extras.get("pdus") as Array<Any>
            for(i in sms.indices){
                val format = extras.getString("format")
                var smsMessage = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    SmsMessage.createFromPdu(sms[i] as ByteArray, format)
                }else{
                    SmsMessage.createFromPdu(sms[i] as ByteArray)
                }

                val formatter = SimpleDateFormat("hh:mm:ss")
                val timeReceived = formatter.format(smsMessage.timestampMillis)
                val phoneNumber = smsMessage.originatingAddress
                val messageText = smsMessage.messageBody.toString()
                Toast.makeText(
                    context,
                "phoneNumber: (private)\n" + "messageText: $messageText",
                Toast.LENGTH_SHORT
                ).show()
                sendMessage(context,smsMessage.timestampMillis,phoneNumber,messageText,smsMessage.timestampMillis)

            }
        }
            // Will do stuff with message here

    }
}