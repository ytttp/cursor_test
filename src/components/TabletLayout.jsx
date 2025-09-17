import React, { useState } from 'react'
import { DataProvider } from '../context/DataContext'
import TabNavigation from './TabNavigation'
import DualPaneLayout from './DualPaneLayout'
import './TabletLayout.css'

const TabletLayout = () => {
  const [activeTab, setActiveTab] = useState(0)

  const tabs = [
    { id: 0, name: '首页', icon: '🏠' },
    { id: 1, name: '消息', icon: '💬' },
    { id: 2, name: '设置', icon: '⚙️' },
    { id: 3, name: '工具', icon: '🔧' },
    { id: 4, name: '关于', icon: 'ℹ️' }
  ]

  return (
    <DataProvider>
      <div className="tablet-layout">
        <div className="content-area">
          <DualPaneLayout activeTab={activeTab} />
        </div>
        <TabNavigation 
          tabs={tabs} 
          activeTab={activeTab} 
          onTabChange={setActiveTab} 
        />
      </div>
    </DataProvider>
  )
}

export default TabletLayout