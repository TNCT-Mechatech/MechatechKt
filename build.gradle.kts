plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.t7e"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("dev.kord:kord-core:0.10.0")
    implementation("com.charleskorn.kaml:kaml:0.54.0")
}

kotlin {
    jvmToolchain(8)
}

tasks {
    shadowJar {
        manifest {
            attributes("Main-Class" to "dev.t7e.mechatechkt.MainKt")
        }
    }
}