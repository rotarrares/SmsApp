package com.example.smsreadapp.msg

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smsreadapp.R
import com.example.smsreadapp.repo.AppDatabase
import com.example.smsreadapp.repo.Msg
import com.example.smsreadapp.repo.Repository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MsgListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<MsgListAdapter.MsgViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var msgs = emptyList<Msg>() // Cached copy of msgs

    inner class MsgViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msgItemView: TextView = itemView.findViewById(R.id.textView)
        val phoneItemView: TextView = itemView.findViewById(R.id.subtextView)
        val imageButtonView: ImageButton = itemView.findViewById(R.id.imageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return MsgViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MsgViewHolder, position: Int) {
        val current = msgs[position]
        holder.msgItemView.text =  current.message
        holder.phoneItemView.text =   current.from
        holder.imageButtonView.setOnClickListener {
            val repository = Repository(AppDatabase.getDatabase(it.context).msgDao())
            MainScope().launch { repository.delete(current) }
        }
    }

    internal fun setmsgs(msgs: List<Msg>) {
        this.msgs = msgs
        notifyDataSetChanged()
    }

    override fun getItemCount() = msgs.size
}