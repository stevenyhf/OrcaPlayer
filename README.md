# OrcaPlayer — Android 本地媒体播放器

简洁优雅的 Android 本地音频/视频播放器，支持记忆播放、字幕同步。

## 特性

- **多格式支持**：MP3/FLAC/WAV/AAC/M4A（音频），MP4/AVI/WebM/MKV/MOV（视频）
- **记忆播放**：自动保存断点，退出后重新打开可续播
- **字幕同步**：支持 .srt/.vtt 外挂字幕解析
- **现代 UI**：Jetpack Compose + Material 3，深色模式原生支持
- **后台播放**：锁屏通知控制 + MiniPlayer 常驻底部栏

## 技术栈

| 层 | 选择 |
|---|---|
| 语言 | Kotlin 1.9+ |
| UI | Jetpack Compose + Material 3 |
| 播放引擎 | Media3 ExoPlayer 1.5 |
| 存储 | Room (播放记录) |
| 架构 | MVI (ViewModel + StateFlow + Intent) |
| DI | Hilt |
| Min SDK | API 26 (Android 8.0) |

## 构建

需要 Android Studio Hedgehog+ 或 Android Command-line Tools。

```bash
cd orcplayer
./gradlew assembleDebug   # 生成 debug APK
./gradlew assembleRelease # 签名 release
```

## 项目结构

```
app/src/main/java/com/chaos/orcplayer/
├── OrcPlayerApp.kt          # Application + @HiltAndroidApp
├── MainActivity.kt          # 入口 + NavHost
├── di/                      # Hilt: DatabaseModule, PlayerModule
├── data/
│   ├── local/               # Room: PlaybackEntity, PlaybackDao, PlayerDatabase
│   └── repository/          # PlaybackRepository
├── domain/
│   ├── model/               # MediaItem, SubtitleTrack, PlaybackState
│   └── usecase/             # ScanMediaUseCase, ResumedPlayUseCase, SavePositionUseCase
├── ui/
│   ├── theme/               # Color, Theme, Typography
│   ├── home/                # HomeScreen, HomeViewModel, MediaCard, FilterChipRow
│   ├── player/              # PlayerScreen, MiniPlayer
│   └── settings/            # SettingsScreen
└── service/                 # ForegroundPlaybackService, SubtitleParser
```

## 许可证

MIT
