# 实用工具集合 🛠️

**本项目由 TRAE 生成**

一个集成多种实用小工具的Android应用，旨在为用户提供便捷的日常工具服务。

## 📱 应用信息

- **应用名称**: 实用工具集合
- **版本**: 1.0
- **平台**: Android
- **最低支持版本**: Android 7.0 (API 24)
- **目标版本**: Android 14 (API 36)

## ✨ 功能特色

### 🌙 入眠助手

帮助您更好地入睡的专业工具，通过阅读枯燥内容来促进睡眠。

**主要功能：**
- 📚 多种内容类型：课程、小说、手册、报告
- 🎯 智能内容推荐：根据类型随机推送枯燥内容
- 📖 自动阅读模式：自动滚动和内容切换
- ➕ 自定义内容：用户可添加个人枯燥内容
- 🎨 护眼界面：深色渐变背景，减少眼部疲劳

**内容类型说明：**
- **课程类**: 会计学、统计学、经济学等学术内容
- **小说类**: 平凡日常生活描述，如等车、购物等
- **手册类**: 设备操作、安全规程、维护保养等技术文档
- **报告类**: 工作总结、财务分析、市场调研等商务报告

## 🚀 技术栈

- **开发语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构**: MVVM + Compose
- **数据存储**: SharedPreferences + Gson
- **依赖管理**: Gradle Version Catalogs

## 📦 项目结构

```
app/
├── src/main/
│   ├── java/com/ivor/tools/
│   │   ├── MainActivity.kt              # 主界面，工具目录
│   │   ├── SleepHelperActivity.kt       # 入眠助手主界面
│   │   ├── AddContentActivity.kt        # 添加自定义内容界面
│   │   ├── BoringContentData.kt         # 枯燥内容数据管理
│   │   ├── UserContentManager.kt       # 用户内容管理
│   │   └── ui/theme/                    # UI主题配置
│   ├── res/
│   │   ├── drawable/                    # 图标资源
│   │   ├── mipmap-*/                    # 应用图标
│   │   ├── values/                      # 字符串、颜色等资源
│   │   └── xml/                         # 配置文件
│   └── AndroidManifest.xml              # 应用清单
└── build.gradle                         # 模块构建配置
```

## 🎨 设计特色

### 应用图标
- 采用现代化渐变设计
- 包含扳手、齿轮、螺丝刀、锤子等工具元素
- 蓝紫色渐变背景，体现专业性

### 界面设计
- Material Design 3 设计规范
- 深色主题，护眼舒适
- 渐变背景，视觉层次丰富
- 圆角卡片，现代简洁

## 🔧 开发环境

### 系统要求
- Android Studio Hedgehog | 2023.1.1 或更高版本
- JDK 11 或更高版本
- Android SDK API 36
- Gradle 8.13

### 依赖库
```kotlin
// 核心库
implementation "androidx.core:core-ktx"
implementation "androidx.lifecycle:lifecycle-runtime-ktx"
implementation "androidx.activity:activity-compose"

// Compose UI
implementation platform("androidx.compose:compose-bom")
implementation "androidx.compose.ui:ui"
implementation "androidx.compose.ui:ui-graphics"
implementation "androidx.compose.ui:ui-tooling-preview"
implementation "androidx.compose.material3:material3"

// JSON处理
implementation "com.google.code.gson:gson:2.10.1"
```

## 📱 安装使用

### 构建应用
1. 克隆项目到本地
2. 使用Android Studio打开项目
3. 等待Gradle同步完成
4. 连接Android设备或启动模拟器
5. 点击运行按钮构建并安装应用

### 使用说明
1. **启动应用**: 打开"实用工具集合"应用
2. **选择工具**: 在主界面选择"入眠助手"
3. **选择内容类型**: 点击顶部标签选择内容类型
4. **开始阅读**: 点击播放按钮开始自动阅读
5. **添加内容**: 点击"+"按钮添加自定义枯燥内容
6. **切换内容**: 点击刷新按钮手动切换内容

## 🔮 未来规划

- 📊 **数据统计工具**: 简单的数据分析和图表生成
- 🔐 **密码生成器**: 安全密码生成和管理
- 📏 **单位转换器**: 常用单位换算工具
- 🎨 **颜色工具**: 颜色选择器和调色板
- 📱 **二维码工具**: 二维码生成和扫描
- ⏰ **时间工具**: 倒计时、秒表等时间管理工具

## 🤝 贡献指南

欢迎提交Issue和Pull Request来帮助改进这个项目！

### 贡献方式
1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 📧 邮箱: [ivor_aif@163.com](mailto:ivor_aif@163.com)
- 🐛 问题反馈: Issues

---

**感谢使用实用工具集合！** 🎉

> 让工具更简单，让生活更便捷。