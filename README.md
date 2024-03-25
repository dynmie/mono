[![Mono](https://socialify.git.ci/dynmie/mono/image?description=1&forks=1&issues=1&language=1&name=1&owner=1&pulls=1&stargazers=1&theme=Light)](https://github.com/dynmie/AOCBot)
<div align="center"><img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/dynmie/mono?style=for-the-badge"> <img alt="GitHub Workflow Status" src="https://img.shields.io/github/actions/workflow/status/dynmie/mono/gradle.yml?branch=master&logo=github&style=for-the-badge"></div>

### Notable features
- Colored video output
- Full pixel video support
- YouTube video and playlist support
- Default playlist queue system 
- Custom video queue system 
- Floyd-Steinberg text dithering 
- Auto resizing terminal window

### What doesn't work
- Instantaneous config editing

### Gallery
![ascii enabled dithering](https://github.com/dynmie/mono/assets/41315732/51b9a510-a97f-4c0a-b344-5c4f75b9cce2)

Hollow Hunger - Ironmouse @ 1:05 with dithering enabled

![dithering off ascii](https://github.com/dynmie/mono/assets/41315732/783d6f8e-5ec0-4f87-93a5-20054b42cae6)

Hollow Hunger - Ironmouse @ 1:05 with dithering enabled

![Dithering enabled](https://github.com/dynmie/mono/assets/41315732/d76726b8-bb91-4900-bc21-e0a41003a3fb)

Hollow Hunger - Ironmouse @ 2:50 with dithering enabled

![Dithering disabled](https://github.com/dynmie/mono/assets/41315732/6f0c1e5f-deff-4812-a7c7-9d25dbd5dda8)

Hollow Hunger - Ironmouse @ 2:50 with dithering disabled

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
- [Java 17 JDK](https://adoptium.net/temurin/releases/?version=17)
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