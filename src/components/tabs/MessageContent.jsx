import React, { useState } from 'react'
import './TabContentBase.css'

const MessageContent = ({ side }) => {
  const [messages, setMessages] = useState([
    { id: 1, text: '欢迎使用消息功能！', time: '09:00', from: '系统' },
    { id: 2, text: '左右面板可以互相发送消息', time: '09:01', from: '系统' }
  ])

  return (
    <div className="tab-content-base">
      <div className="content-header">
        <h2>💬 消息</h2>
        <p className="content-subtitle">消息管理和通信记录</p>
      </div>
      
      <div className="content-body">
        <div className="message-list">
          <h3>消息列表</h3>
          {messages.map((message) => (
            <div key={message.id} className="message-item">
              <div className="message-header">
                <span className="message-from">{message.from}</span>
                <span className="message-time">{message.time}</span>
              </div>
              <div className="message-text">{message.text}</div>
            </div>
          ))}
        </div>
        
        <div className="message-stats">
          <h3>消息统计</h3>
          <div className="stats-grid">
            <div className="stat-item">
              <div className="stat-number">{messages.length}</div>
              <div className="stat-label">总消息数</div>
            </div>
            <div className="stat-item">
              <div className="stat-number">2</div>
              <div className="stat-label">系统消息</div>
            </div>
            <div className="stat-item">
              <div className="stat-number">0</div>
              <div className="stat-label">用户消息</div>
            </div>
          </div>
        </div>
        
        <div className="panel-info">
          <h3>面板信息</h3>
          <p>当前在{side === 'left' ? '左侧' : '右侧'}面板查看消息</p>
          <p>可以通过数据通信功能与另一侧面板交换消息</p>
        </div>
      </div>
    </div>
  )
}

export default MessageContent