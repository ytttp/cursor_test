package com.example.tabletapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tabletapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), MessageListener {
    
    private lateinit var binding: ActivityMainBinding
    private var currentLeftFragment: BaseFragment? = null
    private var currentRightFragment: BaseFragment? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupTabs()
        setupInitialFragments()
    }
    
    private fun setupTabs() {
        // 添加5个Tab
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("首页"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("工作"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("媒体"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("设置"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("个人"))
        
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    switchToTab(it.position)
                }
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun setupInitialFragments() {
        // 默认显示第一个Tab的内容
        switchToTab(0)
    }
    
    private fun switchToTab(tabPosition: Int) {
        val leftFragment = createLeftFragment(tabPosition)
        val rightFragment = createRightFragment(tabPosition)
        
        // 设置消息监听器，实现左右面板互相通信
        leftFragment.setMessageListener(rightFragment)
        rightFragment.setMessageListener(leftFragment)
        
        // 替换Fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.leftPanel, leftFragment)
            .replace(R.id.rightPanel, rightFragment)
            .commit()
        
        currentLeftFragment = leftFragment
        currentRightFragment = rightFragment
    }
    
    private fun createLeftFragment(tabPosition: Int): BaseFragment {
        return when (tabPosition) {
            0 -> HomeLeftFragment()
            1 -> WorkLeftFragment()
            2 -> MediaLeftFragment()
            3 -> SettingsLeftFragment()
            4 -> ProfileLeftFragment()
            else -> HomeLeftFragment()
        }
    }
    
    private fun createRightFragment(tabPosition: Int): BaseFragment {
        return when (tabPosition) {
            0 -> HomeRightFragment()
            1 -> WorkRightFragment()
            2 -> MediaRightFragment()
            3 -> SettingsRightFragment()
            4 -> ProfileRightFragment()
            else -> HomeRightFragment()
        }
    }
    
    override fun onMessageReceived(message: String, from: String) {
        // 主Activity也可以接收消息，这里可以添加全局消息处理逻辑
        // 目前由Fragment之间直接通信处理
    }
}