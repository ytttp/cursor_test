import React, { useState, useEffect } from 'react'
import { useDataContext } from '../context/DataContext'
import TabContent from './TabContent'
import './Pane.css'

const RightPane = ({ tabId }) => {
  const { sendData, subscribe, getData } = useDataContext()
  const [receivedData, setReceivedData] = useState(null)
  const [inputValue, setInputValue] = useState('')

  // 监听来自左面板的数据
  useEffect(() => {
    const unsubscribe = subscribe('left', 'right', (data) => {
      setReceivedData(data)
    })
    return unsubscribe
  }, [subscribe])

  // 获取初始数据
  useEffect(() => {
    const data = getData('left', 'right')
    if (data) {
      setReceivedData(data.data)
    }
  }, [getData])

  const handleSendData = () => {
    if (inputValue.trim()) {
      sendData('right', 'left', {
        message: inputValue,
        timestamp: new Date().toLocaleTimeString(),
        from: '右面板'
      })
      setInputValue('')
    }
  }

  const handleClearData = () => {
    setReceivedData(null)
  }

  return (
    <div className="pane-container">
      <div className="pane-header">
        <h3>右面板 - Tab {tabId + 1}</h3>
        <div className="pane-controls">
          <button onClick={handleClearData} className="clear-btn">
            清空数据
          </button>
        </div>
      </div>
      
      <div className="pane-content">
        <TabContent tabId={tabId} side="right" />
        
        <div className="data-section">
          <h4>数据通信</h4>
          
          <div className="send-section">
            <h5>发送数据到左面板：</h5>
            <div className="input-group">
              <input
                type="text"
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                placeholder="输入要发送的数据..."
                className="data-input"
              />
              <button onClick={handleSendData} className="send-btn">
                发送
              </button>
            </div>
          </div>
          
          <div className="receive-section">
            <h5>接收来自左面板的数据：</h5>
            <div className="data-display">
              {receivedData ? (
                <div className="data-item">
                  <p><strong>消息：</strong>{receivedData.message}</p>
                  <p><strong>时间：</strong>{receivedData.timestamp}</p>
                  <p><strong>来源：</strong>{receivedData.from}</p>
                </div>
              ) : (
                <p className="no-data">暂无数据</p>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default RightPane