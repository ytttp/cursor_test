package com.example.tabletapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MediaLeftFragment : BaseFragment() {
    
    private lateinit var tvReceivedMessage: TextView
    private lateinit var etMessage: EditText
    private lateinit var btnSendMessage: Button
    
    override fun getLayoutRes(): Int = R.layout.fragment_media_left
    
    override fun initViews(view: View) {
        tvReceivedMessage = view.findViewById(R.id.tvReceivedMessage)
        etMessage = view.findViewById(R.id.etMessage)
        btnSendMessage = view.findViewById(R.id.btnSendMessage)
        
        btnSendMessage.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                etMessage.text.clear()
            }
        }
    }
    
    override fun setupMessageListener() {
        // 消息监听器由MainActivity设置
    }
    
    override fun getFragmentName(): String = "媒体-主屏"
    
    override fun onMessageReceived(message: String, from: String) {
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val displayMessage = "[$currentTime] 来自 $from: $message"
        
        activity?.runOnUiThread {
            tvReceivedMessage.text = displayMessage
        }
    }
}