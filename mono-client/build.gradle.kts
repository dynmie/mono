plugins {
    id("java")
}

group = "me.dynmie.mono"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("org.bytedeco:javacv:1.5.10")
    implementation("org.bytedeco:ffmpeg-platform:6.1.1-1.5.10")
    implementation("org.jline:jline-terminal-jni:3.25.1")
    implementation("com.github.dynmie:jeorge:1.2.1")
    implementation("io.netty:netty-all:4.1.107.Final")
    implementation(project(":mono-shared"))
    implementation("com.github.sealedtx:java-youtube-downloader:3.2.8")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "me.dynmie.mono.client.QCMain"
        }
        from(configurations.runtimeClasspath.map { it -> it.map { if (it.isDirectory) it else zipTree(it) } })
    }
}