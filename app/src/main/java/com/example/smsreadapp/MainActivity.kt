package com.example.smsreadapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smsreadapp.api.RestApiService
import com.example.smsreadapp.msg.MsgListAdapter
import com.example.smsreadapp.msg.MsgViewModel
import com.example.smsreadapp.repo.Msg
import com.example.smsreadapp.service.SendingService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlinx.android.synthetic.main.error_toast.*;
class MainActivity : AppCompatActivity() {

    private val requestReceiveSms = 2
    private lateinit var msgViewModel: MsgViewModel
    private val newMsgActivityRequestCode = 1

    companion object {
        private var instance: MainActivity? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.RECEIVE_SMS),
            requestReceiveSms)
        }
        val sendingThread = SendingService()
        sendingThread.startUpdates(applicationContext)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = MsgListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        msgViewModel = ViewModelProvider(this).get(MsgViewModel::class.java)
        msgViewModel.allMsgs.observe(this, Observer { msgs ->
            // Update the cached copy of the msgs in the adapter.
            msgs?.let { adapter.setmsgs(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewMsgActivity::class.java)
            startActivityForResult(intent, newMsgActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newMsgActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewMsgActivity.EXTRA_REPLY)?.let {
                val time = System.currentTimeMillis()
                val msg = Msg(UUID.randomUUID().toString(),"+40744641786","$time",it)
                msgViewModel.insert(msg)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

}