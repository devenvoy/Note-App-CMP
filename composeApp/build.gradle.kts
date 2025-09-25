
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.hot.reload)
    alias(libs.plugins.buildConfig)

}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts.add("-lsqlite3")
        }
    }

    jvm()

    js {
        outputModuleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
        useEsModules()
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.kotlinx.coroutines.android)

            implementation(libs.koin.android)

            implementation(libs.ktor.client.android)

            implementation(libs.chucker.library)
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:34.2.0"))
            implementation("com.google.firebase:firebase-analytics")
            implementation("com.google.firebase:firebase-config")
        }

        commonMain.dependencies {

            implementation(projects.shared.core.common)
            implementation(projects.shared.core.network)
            implementation(projects.shared.core.database)
            implementation(projects.shared.core.designSystem)

            implementation(projects.shared.data.models)
            implementation(projects.shared.data.repository)

            implementation(projects.shared.feature.authentication)
            implementation(projects.shared.feature.notes)
            implementation(projects.shared.feature.settings)

            implementation(libs.bundles.ktor.common)

            implementation(libs.bundles.jetbrains.lifecycle)
            implementation(libs.bundles.jetbrains.material3)
            implementation(libs.bundles.jetbrains.compose)

//            implementation("org.jetbrains.compose:compose-full:1.10.0-alpha01")
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)


            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.sonner)
            implementation(libs.color.materialKolor)

            implementation(libs.multiplatform.settings.no.arg)

        }

        iosMain.dependencies {
            implementation(libs.touchlab.stately.isolate)
            implementation(libs.touchlab.stately.iso.collections)
        }

        jvmMain.dependencies {
            implementation(libs.slf4j.simple)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
        jsMain.dependencies {
            implementation(compose.html.core)
        }
    }
}

android {
    namespace = "com.devansh.noteapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/composeResources")

    defaultConfig {
        applicationId = "com.devansh.noteapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        /* named("debug") {
             storeFile = file("assemble/android/debug.keystore")
             keyAlias = "androiddebugkey"
             storePassword = "android"
             keyPassword = "android"
         }*/
    }
    buildTypes {
        named("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
        }
        named("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "assemble/proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility(libs.versions.android.jvmTarget.get())
        targetCompatibility(libs.versions.android.jvmTarget.get())
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.devansh.noteapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.devansh.noteapp"
            packageVersion = "1.0.0"
            description = "Compose Multiplatform App"
            copyright = "© 2024 My Name. All rights reserved."
            windows {
                shortcut = true
                dirChooser = true
            }
            modules(
                "java.sql",
                "java.prefs",
                "java.net.http",
                "java.management",
                "jdk.unsupported",
                "java.instrument",
                "jdk.security.auth",
            )
        }
        buildTypes.release.proguard {
            obfuscate.set(false)
            configurationFiles.from(project.file("assemble/proguard-rules.pro"))
        }
    }
}

val localProperties = Properties().apply {
    load(
        project.file("${project.rootDir}/local.properties").inputStream()
    )
}

buildConfig {
    packageName = "com.devansh.noteapp"
    buildConfigField("APP_NAME", project.displayName)
    buildConfigField("APP_VERSION", provider { "${project.version}" })
    buildConfigField(
        "BASE_URL",
        provider { localProperties.getProperty("base_url") ?: "https://default.com" })
}
