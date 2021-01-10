package com.example.smsreadapp.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.smsreadapp.api.RestApiService
import com.example.smsreadapp.api.SmsInfo
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class Repository(private val msgDao:MsgDao) {
    val allMsgs: LiveData<List<Msg>> = msgDao.getLiveAll()
    val apiService = RestApiService()
    var scope = MainScope()
    suspend fun insert(vararg msg: Msg) {
        msgDao.insertAll(*msg)
    }

    suspend fun getAll(): List<Msg> {
        return msgDao.getAll()
    }

    suspend fun deleteAll(){
        msgDao.deleteAll()
    }

    suspend fun delete(msg:Msg){
        apiService.testConnection {
            Log.d("Network", "Connection avalaible $it")
            if (it == "200") {
                val smsInfo = SmsInfo(
                    from = "${msg.from}",
                    timestamp = "${msg.timestamp}",
                    message = "${msg.message}",
                    message_id = "${msg.timestamp}"
                )
                val msgObject = it
                apiService.deleteSms(smsInfo,onResult = {
                    scope.launch {msgDao.delete(msg)}
                    });
            }
            else{
                scope.launch {msgDao.delete(msg)}
            }
        }
    }

    suspend fun getBy(){

    }
}