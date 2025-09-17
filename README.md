# Android 平板应用

这是一个专为Android平板设计的应用，实现了您要求的所有功能。

## 功能特性

### 1. 5个Tab页面
- 首页
- 工作
- 媒体
- 设置
- 个人

### 2. 左右分屏显示
- 左边：主屏
- 右边：副屏
- 每个Tab都支持显示左右两个不同的页面

### 3. 消息通信功能
- 左边的页面可以接收右边页面的消息
- 左边的页面可以给右边的页面发送消息
- 右边的页面可以接收左边页面的消息
- 右边的页面可以给左边的页面发送消息
- 消息显示包含时间戳和发送者信息

### 4. 平板适配
- 强制横屏显示
- 针对平板屏幕尺寸优化
- 支持不同屏幕密度的适配

## 项目结构

```
app/
├── src/main/java/com/example/tabletapp/
│   ├── MainActivity.kt              # 主Activity
│   ├── MessageListener.kt           # 消息通信接口
│   ├── BaseFragment.kt              # 基础Fragment类
│   ├── HomeLeftFragment.kt          # 首页-主屏
│   ├── HomeRightFragment.kt         # 首页-副屏
│   ├── WorkLeftFragment.kt          # 工作-主屏
│   ├── WorkRightFragment.kt         # 工作-副屏
│   ├── MediaLeftFragment.kt         # 媒体-主屏
│   ├── MediaRightFragment.kt        # 媒体-副屏
│   ├── SettingsLeftFragment.kt      # 设置-主屏
│   ├── SettingsRightFragment.kt     # 设置-副屏
│   ├── ProfileLeftFragment.kt       # 个人-主屏
│   └── ProfileRightFragment.kt      # 个人-副屏
├── src/main/res/
│   ├── layout/                      # 布局文件
│   ├── values/                      # 资源文件
│   ├── drawable/                    # 图标和背景
│   └── xml/                         # 配置文件
└── AndroidManifest.xml              # 应用清单
```

## 使用方法

1. 编译并安装应用到Android平板设备
2. 应用会自动以横屏模式启动
3. 点击顶部Tab切换不同页面
4. 在任意一侧输入消息并点击"发送消息"按钮
5. 消息会实时显示在另一侧面板中

## 技术实现

### 消息通信机制
- 使用`MessageListener`接口实现Fragment间通信
- 通过`MainActivity`设置消息监听器
- 支持双向消息传递

### 布局设计
- 使用`LinearLayout`实现左右分屏
- 使用`TabLayout`实现Tab切换
- 使用`FrameLayout`作为Fragment容器

### 平板适配
- 在`AndroidManifest.xml`中设置强制横屏
- 创建了`values-land`和`values-sw600dp`资源文件夹
- 针对不同屏幕尺寸提供适配

## 构建要求

- Android Studio Arctic Fox或更高版本
- Android SDK API 24+
- Kotlin 1.9.10+
- Gradle 8.1.4+

## 安装说明

1. 使用Android Studio打开项目
2. 连接Android平板设备或启动模拟器
3. 点击"Run"按钮编译并安装应用
4. 确保设备支持横屏显示

## 注意事项

- 应用专为平板设计，建议在平板设备上运行
- 消息通信是实时的，支持跨Tab通信
- 每个Tab都有独立的左右面板，互不干扰