import React from 'react'
import './TabContentBase.css'

const HomeContent = ({ side }) => {
  return (
    <div className="tab-content-base">
      <div className="content-header">
        <h2>🏠 首页</h2>
        <p className="content-subtitle">欢迎使用Android平板应用</p>
      </div>
      
      <div className="content-body">
        <div className="feature-grid">
          <div className="feature-card">
            <div className="feature-icon">📱</div>
            <h3>双面板布局</h3>
            <p>左右两个面板可以独立显示内容，支持数据通信</p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">🔄</div>
            <h3>实时通信</h3>
            <p>左右面板之间可以实时发送和接收数据</p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">📊</div>
            <h3>数据展示</h3>
            <p>清晰展示接收到的数据，包括时间戳和来源</p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">⚡</div>
            <h3>响应式设计</h3>
            <p>完美适配Android平板横屏模式</p>
          </div>
        </div>
        
        <div className="info-section">
          <h3>当前面板信息</h3>
          <div className="info-card">
            <p><strong>面板位置：</strong>{side === 'left' ? '左侧' : '右侧'}</p>
            <p><strong>面板功能：</strong>数据发送和接收</p>
            <p><strong>支持操作：</strong>输入数据、发送消息、清空数据</p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default HomeContent