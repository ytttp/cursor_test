package com.example.tabletapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * 基础Fragment类，提供消息通信功能
 */
abstract class BaseFragment : Fragment(), MessageListener {
    
    protected var messageListener: MessageListener? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupMessageListener()
    }
    
    /**
     * 设置消息监听器
     */
    fun setMessageListener(listener: MessageListener) {
        this.messageListener = listener
    }
    
    /**
     * 发送消息到另一个面板
     */
    protected fun sendMessage(message: String) {
        messageListener?.onMessageReceived(message, getFragmentName())
    }
    
    /**
     * 获取布局资源ID
     */
    abstract fun getLayoutRes(): Int
    
    /**
     * 初始化视图
     */
    abstract fun initViews(view: View)
    
    /**
     * 设置消息监听
     */
    abstract fun setupMessageListener()
    
    /**
     * 获取Fragment名称
     */
    abstract fun getFragmentName(): String
}