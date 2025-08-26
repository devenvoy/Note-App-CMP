
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

android {
    namespace = "com.maxkeppeler.sheets.color"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint {
        checkGeneratedSources = false
        checkReleaseBuilds = false
        abortOnError = false
    }
}

kotlin {
    androidTarget ()
    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    macosX64()
    macosArm64()

    js(IR) {
        browser()
        binaries.executable()
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        androidMain.dependencies {
//            implementation(libs.androidx.ui.test.junit4.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)

//            implementation(libs.serialization)

            api(project(":sheetCore"))
        }
    }
}
