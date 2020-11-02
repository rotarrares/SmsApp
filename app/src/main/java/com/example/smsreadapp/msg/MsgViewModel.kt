package com.example.smsreadapp.msg

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.smsreadapp.repo.Repository
import com.example.smsreadapp.repo.AppDatabase
import com.example.smsreadapp.repo.Msg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class  MsgViewModel ( application: Application): AndroidViewModel(application) {

    private val repository: Repository

    val allMsgs: LiveData<List<Msg>>

    init {
        val msgDao = AppDatabase.getDatabase(application).msgDao()
        repository = Repository(msgDao)
        allMsgs = repository.allMsgs
    }


    fun insert(msg: Msg) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(msg)
    }
}