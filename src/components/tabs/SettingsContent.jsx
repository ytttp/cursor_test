import React, { useState } from 'react'
import './TabContentBase.css'

const SettingsContent = ({ side }) => {
  const [settings, setSettings] = useState({
    theme: 'light',
    fontSize: 'medium',
    notifications: true,
    autoSync: false
  })

  const handleSettingChange = (key, value) => {
    setSettings(prev => ({
      ...prev,
      [key]: value
    }))
  }

  return (
    <div className="tab-content-base">
      <div className="content-header">
        <h2>⚙️ 设置</h2>
        <p className="content-subtitle">应用设置和偏好配置</p>
      </div>
      
      <div className="content-body">
        <div className="settings-section">
          <h3>外观设置</h3>
          <div className="setting-item">
            <label>主题模式</label>
            <select 
              value={settings.theme} 
              onChange={(e) => handleSettingChange('theme', e.target.value)}
              className="setting-input"
            >
              <option value="light">浅色模式</option>
              <option value="dark">深色模式</option>
              <option value="auto">跟随系统</option>
            </select>
          </div>
          
          <div className="setting-item">
            <label>字体大小</label>
            <select 
              value={settings.fontSize} 
              onChange={(e) => handleSettingChange('fontSize', e.target.value)}
              className="setting-input"
            >
              <option value="small">小</option>
              <option value="medium">中</option>
              <option value="large">大</option>
            </select>
          </div>
        </div>
        
        <div className="settings-section">
          <h3>功能设置</h3>
          <div className="setting-item">
            <label className="checkbox-label">
              <input
                type="checkbox"
                checked={settings.notifications}
                onChange={(e) => handleSettingChange('notifications', e.target.checked)}
                className="setting-checkbox"
              />
              <span>启用通知</span>
            </label>
          </div>
          
          <div className="setting-item">
            <label className="checkbox-label">
              <input
                type="checkbox"
                checked={settings.autoSync}
                onChange={(e) => handleSettingChange('autoSync', e.target.checked)}
                className="setting-checkbox"
              />
              <span>自动同步数据</span>
            </label>
          </div>
        </div>
        
        <div className="settings-section">
          <h3>面板信息</h3>
          <div className="info-card">
            <p><strong>当前面板：</strong>{side === 'left' ? '左侧' : '右侧'}</p>
            <p><strong>设置状态：</strong>已保存</p>
            <p><strong>同步状态：</strong>{settings.autoSync ? '已启用' : '已禁用'}</p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default SettingsContent