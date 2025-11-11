Paginated Launcher (Android)
============================

概述
----
此示例项目实现了一个由 `ViewPager2` + `RecyclerView` 组合构建的多页桌面应用图标面板。每页展示 16 个应用图标，支持跨页拖拽排序，并在底部提供页码指示器。项目基于 Kotlin 和 AndroidX，最低支持到 Android 7.0（API 24）。

主要特性
--------
- 双层容器：`ViewPager2` 负责翻页，单页内通过 `RecyclerView` + `GridLayoutManager` 渲染 4x4 图标网格。
- 跨页拖拽：使用 `ItemTouchHelper`，结合边缘感知逻辑，实现从当前页拖动到相邻页并自动切换。
- 动态页码指示器：显示当前停留的页面并随翻页或拖拽后的跳转更新。
- 示例数据：内置 48 个“应用”条目，方便直接预览三页效果。

快速开始
--------
1. **准备环境**
   - 安装 Android Studio Ladybug (或更高版本)。
   - 安装 Android SDK 34、Android Gradle Plugin 8.5+、JDK 17。
   - 可在项目根目录执行 `gradle wrapper` 生成本地 Gradle Wrapper（本仓库未包含二进制 Wrapper）。
2. **导入项目**
   - 打开 Android Studio，选择 “Open an Existing Project”，指向本仓库根目录。
3. **同步与运行**
   - 首次打开会提示同步 Gradle，按提示完成依赖下载。
   - 连接模拟器或真机（Android 7.0+），点击 Run 即可体验分页桌面。

项目结构
--------
- `app/src/main/java/com/example/launcher/MainActivity.kt`：应用入口，负责 ViewPager2、指示器和跨页拖拽协调。
- `LauncherPagerAdapter.kt`：ViewPager2 适配器，为每一页创建 `LauncherPageFragment`。
- `LauncherPageFragment.kt`：单页容器，内部包含 `RecyclerView` 和拖拽逻辑。
- `IconGridAdapter.kt`：单页图标网格适配器。
- `LauncherViewModel.kt`：共享数据源，负责图标列表与跨页移动的统一管理。
- `AppIconRepository.kt`：提供模拟应用数据。
- `GridSpacingItemDecoration.kt`：网格边距装饰器。
- `res/layout/`：包含 `activity_main.xml`、`fragment_launcher_page.xml`、`item_app_icon.xml` 等布局文件。
- `res/drawable/indicator_dot.xml`：分页指示点样式。

测试建议
--------
- 在设备上尝试拖动第一页最右/最左的应用以验证跨页排序。
- 在不同分辨率与方向（竖屏/横屏）下观察分页与指示器。
- 根据需要将 `AppIconRepository` 接入真实应用列表或数据库数据。

许可证
------
示例代码仅用于学习和演示，可自由修改与分发。