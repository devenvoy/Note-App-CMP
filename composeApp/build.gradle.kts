import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)


    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildConfig)

}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {

            implementation(libs.ktor.client.android)
            implementation(libs.sqldelight.android)

            // built in
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation("com.github.chuckerteam.chucker:library:4.1.0")
        }
        commonMain.dependencies {

            implementation(libs.sqldelight.coroutines)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.runtime)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)

            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenModel)

            implementation(libs.sdp.ssp.compose.multiplatform)

            // build in
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)


            implementation(libs.composeIcons.fontAwesome)
            implementation(libs.richeditor.compose)

            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenModel)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.tabNavigator)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.mvvm.core)

            // #1 - Basic settings
            implementation(libs.multiplatform.settings.no.arg)

            // #2 - For custom class serialization
            implementation(libs.kotlinx.serialization.json.v141)
            implementation(libs.multiplatform.settings.serialization)

            // #3 - For observing values as flows
            implementation(libs.multiplatform.settings.coroutines)

            implementation(libs.kotlinx.datetime)

            implementation(libs.sdp.ssp.compose.multiplatform)
            implementation(libs.sonner)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.retrofit)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.encoding)
            implementation(libs.ktor.client.serialization)

            implementation(libs.kermit)
            implementation(libs.kstore)

            implementation(libs.kmp.date.time.picker)

            implementation(libs.cmp.image.pick.n.crop)

            implementation(libs.connectivity.core)

            implementation("dev.chrisbanes.material3:material3-window-size-class-multiplatform:0.5.0")
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native)
        }

        desktopMain.dependencies {
            implementation(libs.sqldelight.jvm)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "com.devansh.noteapp"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.devansh.noteapp"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
            packageVersion = libs.versions.versionName.get()
        }
    }
}

sqldelight {
    databases {
        create("NoteDatabase") {
            packageName = "com.devansh.noteapp"
        }
    }
}

buildConfig {
    buildConfigField("APP_NAME", project.name)
    buildConfigField("APP_VERSION_CODE", project.version.toString())
    buildConfigField("APP_VERSION_NAME", project.version.toString())
    buildConfigField("BASE_URL", "https://notes-ktor-api.onrender.com")
}
