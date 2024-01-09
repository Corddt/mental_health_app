[English](README.md) | 简体中文
# 心理健康助手应用功能介绍

## 目录
1. [主界面 (MainActivity)](#主界面-mainactivity)
2. [计划活动 (PlanActivity)](#计划活动-planactivity)
3. [瓶子活动 (BottleActivity)](#瓶子活动-bottleactivity)
4. [日记活动 (DiaryActivity)](#日记活动-diaryactivity)
5. [奖励活动 (RewardActivity)](#奖励活动-rewardactivity)
6. [联系界面 (ContactActivity)](#联系界面-contactactivity)
7. [哭泣女孩界面 (CryingGirlActivity)](#哭泣女孩界面-cryinggirlactivity)
8. [启动界面 (SplashActivity)](#启动界面-splashactivity)

### 主界面 (MainActivity)
- 包含四个按钮，每个按钮链接到不同的功能模块。
- 提供访问日记、计划、奖励和联系信息的途径。

### 计划活动 (PlanActivity)
- 允许用户添加、编辑和查看每日计划。
- 支持通过滑动操作标记计划的完成状态。
- 完成计划后显示鼓励动画。

### 瓶子活动 (BottleActivity)
- 显示当前日期的计划总数和已完成计划数。
- 根据完成计划的数量播放不同的动画效果。

### 日记活动 (DiaryActivity)
- 用户可以记录和查看他们的日记。
- 支持添加和编辑日记条目。
- 显示所有日记条目的列表。

### 奖励活动 (RewardActivity)
- 包括一个日历视图，允许用户选择特定日期。
- 显示所选日期的日记和计划记录。
- 根据日记和计划完成情况提供激励反馈。

### 联系界面 (ContactActivity)
- 提供联系信息和其他重要细节。
- 用于寻求帮助或提供反馈。

### 哭泣女孩界面 (CryingGirlActivity)
- 显示反映用户活动状态的动画女孩。
- 如果用户几天都没有完成任何任务，会显示哭泣的女孩动画。
- 鼓励按钮鼓励用户完成任务并改变女孩的状态。

### 启动界面 (SplashActivity)
- 应用启动时显示。
- 根据当前主题（日间/夜间模式）显示不同的启动屏幕。
- 在转换到主界面之前包括一个简短的动画。

## 技术架构
1. **前端界面 (UI/UX)**
   - 使用安卓的标准UI组件和布局。
   - 采用Lottie动画库增强用户交互体验。
   - 使用`RecyclerView`展示动态列表，如计划和日记条目。

2. **后端逻辑**
   - 主要编程语言为Java。
   - 活动 (`Activity`) 管理不同的用户界面和交互。
   - 使用手势检测器 (`GestureDetector`) 处理复杂的用户输入，如双击和滑动操作。

3. **数据存储**
   - 使用SQLite数据库进行本地数据存储。
   - 数据库操作包括表的创建、插入、更新、查询和删除。

4. **数据模型**
   - 定义数据类 (`Plan`, `DiaryEntry`) 来表示应用程序内的核心数据结构。

## 数据库设计
1. **计划表**
   -

`id`: 主键，自动递增。
- `plan`: 文本，代表计划内容。
- `completed`: 整数，表示计划是否完成（0代表未完成，1代表完成）。
- `timestamp`: 文本，记录计划的日期。

2. **日记条目表**
   - `id`: 主键，自动递增。
   - `entry`: 文本，代表日记内容。
   - `timestamp`: 文本，记录日记条目的日期。

## 关键技术点
- 数据库操作使用安卓的`SQLiteDatabase`类进行。
- `ContentValues`类用于封装要插入或更新的数据。
- 查询结果使用`Cursor`对象遍历和处理。
- 数据持久化确保即使应用关闭后也能保留用户数据。
- 适配器模式（`Adapter`）用于连接数据模型与`RecyclerView`。