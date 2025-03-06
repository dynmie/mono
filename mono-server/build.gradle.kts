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

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.dynmie:jeorge:1.2.1")
    implementation("io.netty:netty-all:4.1.107.Final")
    implementation("org.jline:jline-terminal-jni:3.25.1")
    implementation("org.jline:jline-reader:3.25.1")
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
            attributes["Main-Class"] = "me.dynmie.mono.server.QSMain"
        }
        from(configurations.runtimeClasspath.map { it -> it.map { if (it.isDirectory) it else zipTree(it) } })
    }
}