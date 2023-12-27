import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.21"
    id("org.jetbrains.compose") version "1.5.11"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    val osName = System.getProperty("os.name")
    val targetOs = when {
        osName == "Mac OS X" -> "macos"
        osName.startsWith("Win") -> "windows"
        osName.startsWith("Linux") -> "linux"
        else -> error("Unsupported OS: $osName")
    }

    val targetArch = when (val osArch = System.getProperty("os.arch")) {
        "x86_64", "amd64" -> "x64"
        "aarch64" -> "arm64"
        else -> error("Unsupported arch: $osArch")
    }

    val version = "0.7.70" // or any more recent version
    val target = "${targetOs}-${targetArch}"

    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
                implementation("media.kamel:kamel-image:0.9.1")
                implementation("io.ktor:ktor-client-core:2.3.6")
                implementation("io.ktor:ktor-client-cio:2.3.6")
                implementation("org.jetbrains.skiko:skiko-awt-runtime-$target:$version")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "MultiplatformApp"
            packageVersion = "1.0.0"
            macOS {
                packageVersion = "1.0.0"

                dmgPackageVersion = "1.0.0"

                packageBuildVersion = "1.0.0"

                dmgPackageBuildVersion = "1.0.0"
            }
            windows {
                packageVersion = "1.0.0"

                exePackageVersion = "1.0.0"
            }
            linux {
                packageVersion = "1.0.0"

                debPackageVersion = "1.0.0"
            }
        }
    }
}
