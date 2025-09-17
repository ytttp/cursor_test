import React from 'react'
import './TabContentBase.css'

const AboutContent = ({ side }) => {
  return (
    <div className="tab-content-base">
      <div className="content-header">
        <h2>ℹ️ 关于</h2>
        <p className="content-subtitle">应用信息和版本详情</p>
      </div>
      
      <div className="content-body">
        <div className="about-section">
          <h3>应用信息</h3>
          <div className="info-card">
            <p><strong>应用名称：</strong>Android平板应用</p>
            <p><strong>版本号：</strong>1.0.0</p>
            <p><strong>构建日期：</strong>2024年1月</p>
            <p><strong>开发者：</strong>AI Assistant</p>
          </div>
        </div>
        
        <div className="features-section">
          <h3>主要功能</h3>
          <ul className="features-list">
            <li>✅ 5个底部导航标签</li>
            <li>✅ 左右双面板布局</li>
            <li>✅ 面板间数据通信</li>
            <li>✅ 响应式设计</li>
            <li>✅ Android平板适配</li>
            <li>✅ 实时数据同步</li>
            <li>✅ 现代化UI设计</li>
            <li>✅ 跨面板消息传递</li>
          </ul>
        </div>
        
        <div className="technical-section">
          <h3>技术栈</h3>
          <div className="tech-grid">
            <div className="tech-item">
              <div className="tech-icon">⚛️</div>
              <div className="tech-name">React</div>
            </div>
            <div className="tech-item">
              <div className="tech-icon">🎨</div>
              <div className="tech-name">CSS3</div>
            </div>
            <div className="tech-item">
              <div className="tech-icon">📱</div>
              <div className="tech-name">响应式</div>
            </div>
            <div className="tech-item">
              <div className="tech-icon">🔄</div>
              <div className="tech-name">Context API</div>
            </div>
          </div>
        </div>
        
        <div className="panel-info">
          <h3>面板信息</h3>
          <div className="info-card">
            <p><strong>当前面板：</strong>{side === 'left' ? '左侧' : '右侧'}</p>
            <p><strong>面板功能：</strong>信息展示和数据通信</p>
            <p><strong>支持操作：</strong>查看应用信息、发送接收数据</p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AboutContent