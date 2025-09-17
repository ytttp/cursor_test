# Android平板模式应用

一个专为Android平板设计的React应用，具有5个底部标签页和双面板布局，支持左右面板间的数据通信。

## 功能特性

### 🏠 主要功能
- **5个底部标签页**：首页、消息、设置、工具、关于
- **双面板布局**：每个标签页支持左右两个面板同时显示
- **数据通信**：左右面板可以互相发送和接收数据
- **响应式设计**：完美适配Android平板横屏模式
- **现代化UI**：美观的用户界面和流畅的交互体验

### 📱 技术特性
- **React 18**：使用最新的React特性
- **Context API**：实现跨组件数据通信
- **CSS3**：响应式设计和动画效果
- **模块化架构**：清晰的组件结构
- **TypeScript支持**：类型安全的开发体验

## 项目结构

```
src/
├── components/
│   ├── TabletLayout.jsx          # 主布局组件
│   ├── TabNavigation.jsx         # 底部标签导航
│   ├── DualPaneLayout.jsx        # 双面板布局
│   ├── LeftPane.jsx             # 左面板组件
│   ├── RightPane.jsx            # 右面板组件
│   ├── TabContent.jsx           # 标签内容容器
│   └── tabs/                    # 各标签页内容
│       ├── HomeContent.jsx      # 首页内容
│       ├── MessageContent.jsx   # 消息内容
│       ├── SettingsContent.jsx  # 设置内容
│       ├── ToolsContent.jsx     # 工具内容
│       └── AboutContent.jsx     # 关于内容
├── context/
│   └── DataContext.jsx          # 数据通信上下文
├── App.jsx                      # 主应用组件
├── App.css                      # 全局样式
└── main.jsx                     # 应用入口
```

## 数据通信机制

### 发送数据
```javascript
const { sendData } = useDataContext()

// 从左面板发送数据到右面板
sendData('left', 'right', {
  message: 'Hello from left panel!',
  timestamp: new Date().toLocaleTimeString(),
  from: '左面板'
})
```

### 接收数据
```javascript
const { subscribe } = useDataContext()

// 监听来自右面板的数据
useEffect(() => {
  const unsubscribe = subscribe('right', 'left', (data) => {
    console.log('Received data:', data)
  })
  return unsubscribe
}, [subscribe])
```

### 获取数据
```javascript
const { getData } = useDataContext()

// 获取特定方向的数据
const data = getData('left', 'right')
```

## 安装和运行

### 安装依赖
```bash
npm install
```

### 开发模式
```bash
npm run dev
```

### 构建生产版本
```bash
npm run build
```

### 预览生产版本
```bash
npm run preview
```

## 使用说明

### 基本操作
1. **切换标签页**：点击底部导航栏的标签页按钮
2. **发送数据**：在任意面板的输入框中输入数据，点击"发送"按钮
3. **接收数据**：数据会自动显示在对应面板的接收区域
4. **清空数据**：点击"清空数据"按钮清除当前面板的接收数据

### 各标签页功能
- **首页**：展示应用功能概览和面板信息
- **消息**：消息管理和通信记录
- **设置**：应用设置和偏好配置
- **工具**：内置计算器等实用工具
- **关于**：应用信息和版本详情

## Android平板适配

### 响应式断点
- **小屏幕**：< 768px
- **平板横屏**：768px - 1023px
- **大屏平板**：≥ 1024px

### 适配特性
- 字体大小根据屏幕尺寸自动调整
- 按钮和输入框大小适配触摸操作
- 双面板布局在横屏模式下优化显示
- 底部导航栏高度适配不同屏幕

## 浏览器兼容性

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## 开发说明

### 添加新的标签页
1. 在 `src/components/tabs/` 目录下创建新的内容组件
2. 在 `TabContent.jsx` 中注册新组件
3. 在 `TabletLayout.jsx` 中添加标签配置

### 自定义数据通信
可以通过修改 `DataContext.jsx` 来扩展数据通信功能，支持更多数据类型和通信模式。

## 许可证

MIT License