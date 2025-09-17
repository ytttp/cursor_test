import React, { createContext, useContext, useState, useCallback } from 'react'

const DataContext = createContext()

export const useDataContext = () => {
  const context = useContext(DataContext)
  if (!context) {
    throw new Error('useDataContext must be used within a DataProvider')
  }
  return context
}

export const DataProvider = ({ children }) => {
  const [data, setData] = useState({})
  const [listeners, setListeners] = useState({})

  // 发送数据到指定页面
  const sendData = useCallback((fromPage, toPage, data) => {
    const key = `${fromPage}-${toPage}`
    setData(prev => ({
      ...prev,
      [key]: {
        data,
        timestamp: Date.now(),
        from: fromPage,
        to: toPage
      }
    }))
    
    // 触发监听器
    if (listeners[key]) {
      listeners[key].forEach(callback => callback(data))
    }
  }, [listeners])

  // 监听数据变化
  const subscribe = useCallback((fromPage, toPage, callback) => {
    const key = `${fromPage}-${toPage}`
    setListeners(prev => ({
      ...prev,
      [key]: [...(prev[key] || []), callback]
    }))
    
    return () => {
      setListeners(prev => ({
        ...prev,
        [key]: prev[key]?.filter(cb => cb !== callback) || []
      }))
    }
  }, [])

  // 获取数据
  const getData = useCallback((fromPage, toPage) => {
    const key = `${fromPage}-${toPage}`
    return data[key] || null
  }, [data])

  const value = {
    sendData,
    subscribe,
    getData,
    data
  }

  return (
    <DataContext.Provider value={value}>
      {children}
    </DataContext.Provider>
  )
}