import React, { useState } from 'react'
import './TabContentBase.css'

const ToolsContent = ({ side }) => {
  const [calculator, setCalculator] = useState({
    display: '0',
    operation: null,
    previousValue: null
  })

  const handleNumber = (num) => {
    setCalculator(prev => ({
      ...prev,
      display: prev.display === '0' ? num : prev.display + num
    }))
  }

  const handleOperation = (op) => {
    setCalculator(prev => ({
      ...prev,
      operation: op,
      previousValue: parseFloat(prev.display),
      display: '0'
    }))
  }

  const handleEquals = () => {
    const { operation, previousValue, display } = calculator
    if (operation && previousValue !== null) {
      const currentValue = parseFloat(display)
      let result = 0
      
      switch (operation) {
        case '+':
          result = previousValue + currentValue
          break
        case '-':
          result = previousValue - currentValue
          break
        case '*':
          result = previousValue * currentValue
          break
        case '/':
          result = currentValue !== 0 ? previousValue / currentValue : 0
          break
        default:
          return
      }
      
      setCalculator({
        display: result.toString(),
        operation: null,
        previousValue: null
      })
    }
  }

  const handleClear = () => {
    setCalculator({
      display: '0',
      operation: null,
      previousValue: null
    })
  }

  return (
    <div className="tab-content-base">
      <div className="content-header">
        <h2>🔧 工具</h2>
        <p className="content-subtitle">实用工具和计算器</p>
      </div>
      
      <div className="content-body">
        <div className="calculator-section">
          <h3>计算器</h3>
          <div className="calculator">
            <div className="calculator-display">
              {calculator.display}
            </div>
            <div className="calculator-buttons">
              <button onClick={handleClear} className="calc-btn clear">C</button>
              <button onClick={() => handleOperation('/')} className="calc-btn operator">/</button>
              <button onClick={() => handleOperation('*')} className="calc-btn operator">×</button>
              <button onClick={() => handleOperation('-')} className="calc-btn operator">-</button>
              
              <button onClick={() => handleNumber('7')} className="calc-btn number">7</button>
              <button onClick={() => handleNumber('8')} className="calc-btn number">8</button>
              <button onClick={() => handleNumber('9')} className="calc-btn number">9</button>
              <button onClick={() => handleOperation('+')} className="calc-btn operator">+</button>
              
              <button onClick={() => handleNumber('4')} className="calc-btn number">4</button>
              <button onClick={() => handleNumber('5')} className="calc-btn number">5</button>
              <button onClick={() => handleNumber('6')} className="calc-btn number">6</button>
              <button onClick={handleEquals} className="calc-btn equals">=</button>
              
              <button onClick={() => handleNumber('1')} className="calc-btn number">1</button>
              <button onClick={() => handleNumber('2')} className="calc-btn number">2</button>
              <button onClick={() => handleNumber('3')} className="calc-btn number">3</button>
              <button onClick={() => handleNumber('0')} className="calc-btn number zero">0</button>
            </div>
          </div>
        </div>
        
        <div className="tools-info">
          <h3>工具信息</h3>
          <div className="info-card">
            <p><strong>当前面板：</strong>{side === 'left' ? '左侧' : '右侧'}</p>
            <p><strong>计算器状态：</strong>就绪</p>
            <p><strong>支持操作：</strong>基本四则运算</p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ToolsContent