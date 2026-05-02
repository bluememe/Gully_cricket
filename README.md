# Gully Cricket Score Manager 🏏

A premium, feature-rich Android application designed to manage and track gully cricket matches with professional precision. Built using modern Android development practices, this app provides a seamless scoring experience for casual and competitive local cricket.

## ✨ Features

### 🏁 Match Management
- **Custom Setup**: Configure team names, number of overs, and player lists.
- **Match Persistence**: All match data is saved locally using Room database, allowing you to resume or review matches anytime.

### 📊 Real-time Scoring Dashboard
- **Comprehensive Run Tracking**: Support for all standard runs (0, 1, 2, 3, 4, 6).
- **Advanced Extras**:
    - **Wide Balls**: Track extra runs scored on wide deliveries.
    - **No Balls**: Support for runs off the bat and specialized "Run Out" logic for no-balls.
- **Detailed Wickets**: Log wickets by type: Bowled, Caught, Run Out, Stumped, or LBW.
- **Dynamic Player Selection**: Easy selection of striker, non-striker, and bowlers throughout the match.
- **Undo Functionality**: Correct mistakes instantly with a reliable ball-by-ball undo feature.

### 🏆 Advanced Mechanics
- **Chase Logic**: Automatically calculates targets and displays "runs required" and "balls remaining" during the second innings.
- **Innings Transition**: Smooth transition between innings with automated team swaps.
- **Ball-by-Ball History**: Visual representation of the current over's progress with color-coded events.

### 📈 Stats & Sharing
- **Leaderboard**: Track player performances across multiple matches.
- **Match History**: Access a complete archive of previous matches and their results.
- **Scorecard Export**: Generate professional match summary images to share with your friends and local cricket community.

## 🛠️ Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Persistence**: [Room Database](https://developer.android.com/training/data-storage/room)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Asynchronous Flow**: Kotlin Coroutines & StateFlow
- **Exporting**: Custom Canvas API for image generation

## 📸 Screenshots

*(Add your screenshots here to showcase the premium UI)*

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- Android SDK 34+
- JDK 17

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/Gully_cricket.git
   ```
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Run the app on an emulator or physical device.

## 🤝 Contributing

Contributions are welcome! Whether it's fixing bugs, improving UI, or adding new cricket rules (like Mankading or specific gully rules), feel free to open a Pull Request.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Made with ❤️ for the Cricket community.*
