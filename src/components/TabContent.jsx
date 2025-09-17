import React from 'react'
import HomeContent from './tabs/HomeContent'
import MessageContent from './tabs/MessageContent'
import SettingsContent from './tabs/SettingsContent'
import ToolsContent from './tabs/ToolsContent'
import AboutContent from './tabs/AboutContent'
import './TabContent.css'

const TabContent = ({ tabId, side }) => {
  const contentComponents = [
    HomeContent,
    MessageContent,
    SettingsContent,
    ToolsContent,
    AboutContent
  ]

  const ContentComponent = contentComponents[tabId] || HomeContent

  return (
    <div className="tab-content">
      <ContentComponent side={side} />
    </div>
  )
}

export default TabContent