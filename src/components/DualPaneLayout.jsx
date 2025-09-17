import React from 'react'
import LeftPane from './LeftPane'
import RightPane from './RightPane'
import './DualPaneLayout.css'

const DualPaneLayout = ({ activeTab }) => {
  return (
    <div className="dual-pane-layout">
      <div className="pane left-pane">
        <LeftPane tabId={activeTab} />
      </div>
      <div className="pane-divider"></div>
      <div className="pane right-pane">
        <RightPane tabId={activeTab} />
      </div>
    </div>
  )
}

export default DualPaneLayout