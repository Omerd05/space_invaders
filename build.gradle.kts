import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.googlecode.lanterna:lanterna:3.1.5")
}

kotlin {
    jvmToolchain(17)

    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }

    sourceSets {
        main {
            kotlin.srcDirs("src")
        }
        test {
            kotlin.srcDirs("test")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("MainKt")
}
