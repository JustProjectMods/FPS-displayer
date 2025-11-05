# JustProject FPS Display Mod

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.16.5-green" alt="Minecraft">
  <img src="https://img.shields.io/badge/Forge-36.2.42-orange" alt="Forge">
  <img src="https://img.shields.io/badge/License-MIT-blue" alt="License">
  <img src="https://img.shields.io/github/v/release/JustProjectMods/FPS-displayer" alt="Release">
</p>

Advanced FPS display mod for Minecraft 1.16.5 with comprehensive game statistics and customization options.

## 📥 Download

**Latest Version**: [v1.0.0](https://github.com/JustProjectMods/FPS-displayer/releases/tag/v1.0.0)

## ✨ Features

### 📊 Statistics Display
- **FPS Counter** with color-coded performance indicators
- **Player Coordinates** (X, Y, Z) in real-time
- **CPS Counter** (clicks per second) for both mouse buttons
- **Armor Information** with durability percentages
- **Entity Info** when looking at mobs

### 🎮 Controls
- **Key Binding System** with command support
- **GUI Interface** for settings management
- **Customizable Positions** of screen elements
- **Toggle On/Off** individual features

## 🚀 Installation

1. Download the latest version from [Releases](https://github.com/JustProjectMods/FPS-displayer/releases)
2. Place the `.jar` file in your Minecraft client's `mods` folder
3. Launch the game with **Forge 36.2.42** or newer for **Minecraft 1.16.5**

## ⚙️ Usage

### 🔧 Commands
- `.bind <key> <command>` - Create a key binding
- `.binds` - List active bindings
- `.unbind <key>` - Remove a binding
- `/binds` - Open GUI bind settings

### 🎛️ Configuration
- **In-game**: `Options` → `JustProject Settings`
- **Manual**: Edit `config/fpsdisplayer.toml` file

## 🛠️ Building from Source

```bash
# Clone repository
git clone https://github.com/JustProjectMods/FPS-displayer.git
cd FPS-displayer

# Build the mod
./gradlew build

# Built mod file will be in ./build/libs/
