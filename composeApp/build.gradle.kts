import org.jetbrains.compose.desktop.application.dsl.TargetFormat

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

    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            implementation(libs.ktor.client.android)

            implementation(libs.sqldelight.android.driver)

            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation("com.github.chuckerteam.chucker:library:4.2.0")
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:34.2.0"))
            implementation("com.google.firebase:firebase-analytics")
            implementation("com.google.firebase:firebase-config")
        }

        commonMain.dependencies {

            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.navigation.compose)

            implementation(libs.bundles.ktor.common)

            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.animation)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.material3.adaptive)
            implementation(libs.material3.expressive)
            implementation(libs.material3.adaptive.layout)
            implementation(libs.material3.adaptive.navigation)
            implementation(libs.material3.adaptive.navigation.suite)

            implementation(libs.kotlin.logging)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // #1 - Basic settings
            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.multiplatform.settings.coroutines)

            implementation(libs.kotlinx.datetime)

            implementation(libs.sdp.ssp.compose.multiplatform)

            implementation(libs.sqlite.bundled)

            implementation(libs.coil.compose)

            implementation(libs.sonner)
            implementation(libs.color.materialKolor)

            implementation(libs.richeditor.compose)
            implementation(libs.composeSettings.ui)
            implementation(libs.composeSettings.ui.extended)

            implementation("io.github.jan-tennert.supabase:compose-auth:3.2.2")
            implementation("io.github.jan-tennert.supabase:compose-auth-ui:3.2.2")

            implementation(projects.sheetCore)
            implementation(projects.color)

        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native.driver)
            implementation(libs.touchlab.stately.isolate)
            implementation(libs.touchlab.stately.iso.collections)
        }

        jvmMain.dependencies {
            implementation(libs.slf4j.simple)
            implementation(libs.ktor.client.java)
            implementation(compose.desktop.currentOs)
            implementation(libs.sqldelight.sqlite.driver)
            implementation(libs.kotlinx.coroutines.swing)
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
//            applicationIdSuffix = ".debug"
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

sqldelight {
    databases {
        create("NoteAppDatabase") {
            packageName = "com.devansh.noteapp"
            generateAsync.set(true)
        }
    }
}

buildConfig {
    packageName = "com.jignesh.society"
    buildConfigField("APP_NAME", project.name)
    buildConfigField("APP_VERSION", provider { "${project.version}" })
    buildConfigField("APP_SECRET", "Z3JhZGxlLWphdmEtYnVpbGRjb25maWctcGx1Z2lu")
    buildConfigField("BASE_URL", "https://m0s0wkg40gsws8g00c4cs8ww.65.109.173.240.sslip.io")
    buildConfigField<String>("OPTIONAL", null)
}
