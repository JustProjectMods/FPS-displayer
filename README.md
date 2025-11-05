# JustProject FPS Display Mod

![Minecraft](https://img.shields.io/badge/Minecraft-1.16.5-green)
![Forge](https://img.shields.io/badge/Forge-36.2.42-orange)
![License](https://img.shields.io/badge/License-MIT-blue)

📥 **Download**: [Latest Release](https://github.com/JustProjectMods/fpsdisplayer/releases/latest)

Advanced FPS display mod for Minecraft 1.16.5 with useful game statistics.

## ✨ Features
- **FPS Counter** with color indicators
- **Coordinates Display** (X, Y, Z)  
- **CPS Counter** (clicks per second)
- **Armor Display** with durability percentages
- **Entity Info** when looking at mobs
- **Key Bind System** with GUI
- **Customizable** positions and toggles

## 🎮 Commands
- `.bind <key> <command>` - Create key bind
- `.binds` - List active binds
- `.unbind <key>` - Remove bind  
- `/binds` - GUI bind settings

## ⚙️ Installation
1. Download from [Releases](../../releases)
2. Place in `mods` folder
3. Requires **Forge 36.2.42+** for **Minecraft 1.16.5**

## 🔧 Configuration
- In-game: `Options` → `JustProject Settings`
- Manual: Edit `config/fpsdisplayer.toml`

## 🛠️ Building
```bash
git clone https://github.com/JustProjectMods/fpsdisplayer.git
cd fpsdisplayer
./gradlew build