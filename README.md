# Android 录音器应用

一个使用Kotlin开发的Android录音应用，支持录音、播放和分享功能。

## 功能特性

- ✅ 录音输入和输出
- ✅ 录音文件保存到系统存储
- ✅ 支持播放录音文件
- ✅ 支持分享到QQ、微信等应用
- ✅ 现代化的Material Design界面
- ✅ 权限管理
- ✅ 文件管理

## 技术栈

- **语言**: Kotlin
- **UI框架**: Android View Binding
- **录音**: MediaRecorder API
- **播放**: MediaPlayer API
- **分享**: Intent + FileProvider
- **权限**: ActivityResultContracts

## 权限要求

- `RECORD_AUDIO`: 录音权限
- `WRITE_EXTERNAL_STORAGE`: 写入外部存储权限
- `READ_EXTERNAL_STORAGE`: 读取外部存储权限
- `READ_MEDIA_AUDIO`: Android 13+ 媒体权限
- `INTERNET`: 网络权限（用于分享）

## 项目结构

```
app/
├── src/main/
│   ├── java/com/example/recorder/
│   │   ├── MainActivity.kt          # 主Activity
│   │   ├── RecordingService.kt      # 录音服务
│   │   ├── PlaybackService.kt       # 播放服务
│   │   └── ShareService.kt          # 分享服务
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml    # 主界面布局
│   │   ├── values/
│   │   │   ├── strings.xml          # 字符串资源
│   │   │   ├── colors.xml           # 颜色资源
│   │   │   └── themes.xml           # 主题资源
│   │   └── xml/
│   │       ├── file_paths.xml       # FileProvider路径配置
│   │       ├── backup_rules.xml     # 备份规则
│   │       └── data_extraction_rules.xml # 数据提取规则
│   └── AndroidManifest.xml          # 应用清单
├── build.gradle                     # 应用级构建配置
└── proguard-rules.pro              # ProGuard规则
```

## 使用方法

1. **录音**: 点击"开始录音"按钮开始录音，再次点击停止录音
2. **播放**: 录音完成后，点击"播放录音"按钮播放录音
3. **分享**: 使用底部的分享按钮将录音文件分享到其他应用

## 录音文件存储位置

- **Android 10+**: `/Android/data/com.example.recorder/files/Music/Recordings/`
- **Android 9及以下**: `/Music/Recordings/`

## 构建和安装

1. 使用Android Studio打开项目
2. 连接Android设备或启动模拟器
3. 点击"Run"按钮构建并安装应用

## 注意事项

- 首次使用需要授予录音权限
- 录音文件以M4A格式保存
- 支持分享到任何支持音频文件的应用
- 应用会自动处理权限请求和错误处理

## 兼容性

- **最低Android版本**: API 21 (Android 5.0)
- **目标Android版本**: API 34 (Android 14)
- **推荐Android版本**: API 23+ (Android 6.0+)

## 许可证

此项目仅供学习和参考使用。