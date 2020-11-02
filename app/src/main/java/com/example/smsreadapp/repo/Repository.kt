package com.example.smsreadapp.repo

import androidx.lifecycle.LiveData
import com.example.smsreadapp.repo.Msg
import com.example.smsreadapp.repo.MsgDao

class Repository(private val msgDao:MsgDao) {
    val allMsgs: LiveData<List<Msg>> = msgDao.getLiveAll()

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
        msgDao.delete(msg)
    }

    suspend fun getBy(){

    }
}