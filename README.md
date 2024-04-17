[![Mono](https://socialify.git.ci/dynmie/mono/image?description=1&forks=1&issues=1&language=1&name=1&owner=1&pulls=1&stargazers=1&theme=Light)](https://github.com/dynmie/mono)
<div align="center"><img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/dynmie/mono?style=for-the-badge"> <img alt="GitHub Workflow Status" src="https://img.shields.io/github/actions/workflow/status/dynmie/mono/gradle.yml?branch=master&logo=github&style=for-the-badge"></div>

### Notable features
- Colored video output
- Full pixel video support
- YouTube video and playlist support
- Default playlist queue system 
- Custom video queue system 
- Floyd-Steinberg text dithering 
- Auto resizing playback window

### What doesn't work
- Instantaneous config editing

# Gallery
![Hollow hunger with dithering enabled](https://github.com/dynmie/monolizer-java/assets/41315732/9e8d1453-c681-4b1e-9a21-8b78b76adb49)

Hollow Hunger - Ironmouse @ 1:05 with dithering enabled

![Hollow hunger with dithering disabled](https://github.com/dynmie/monolizer-java/assets/41315732/e3c24557-5e17-46d7-8cf7-b9a9348cf80c)

Hollow Hunger - Ironmouse @ 1:05 with dithering disabled

![Dithering enabled](https://github.com/dynmie/monolizer-java/assets/41315732/016ee90d-72f4-485b-8213-3624020f743b)

Hollow Hunger - Ironmouse @ 2:50 with dithering enabled

![full pixel](https://github.com/dynmie/monolizer-java/assets/41315732/213d43ac-94cf-4e7b-ab7e-0c0b365b3f0b)

Telecaster b boy (long ver.) - Kanata Amane @ 0:26 with full pixel and color enabled

# Getting started
### Prerequisites
- [Java 21 JRE](https://adoptium.net/temurin/releases/?version=21)
- [MongoDB](https://www.mongodb.com/try/download/community)

### Setup
1. Run the server once to generate the configuration files. Make sure your current Java version is at least 21.
2. Open the server configuration file at `./mono-data/server/config.json` and set the default playlist.

### Playing videos
1. Run the server:
```bash
java -jar server.jar
```

2. Run the client:
```bash
java -jar client.jar
```
3. You're done!

## Building
### Prerequisites
- [Java 21 JDK](https://adoptium.net/temurin/releases/?version=21)
- [Git](https://git-scm.com/downloads)

### Cloning the GitHub repository
```bash
git clone https://github.com/dynmie/mono.git
```
### Compiling
Windows:
```cmd
.\gradlew jar
```

GNU Linux:
```bash
chmod +x ./gradlew
./gradlew jar
```

You can find the output jar at `(module name)/build/libs`.
