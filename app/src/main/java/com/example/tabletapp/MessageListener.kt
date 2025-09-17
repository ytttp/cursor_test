package com.example.tabletapp

/**
 * 消息监听接口，用于Fragment间通信
 */
interface MessageListener {
    fun onMessageReceived(message: String, from: String)
}