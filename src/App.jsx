import React from 'react'
import { BrowserRouter as Router } from 'react-router-dom'
import TabletLayout from './components/TabletLayout'
import './App.css'

function App() {
  return (
    <Router>
      <div className="app">
        <TabletLayout />
      </div>
    </Router>
  )
}

export default App