plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()
    js(IR) {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.common)
            implementation(projects.shared.data.models)
            implementation(libs.bundles.jetbrains.compose)
            implementation(libs.bundles.jetbrains.lifecycle)
            implementation(libs.bundles.jetbrains.material3)

            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)

            implementation(libs.kotlinx.datetime)

            implementation(libs.coil.compose)
            implementation(libs.sonner)
            implementation(libs.color.materialKolor)
            implementation(libs.richeditor.compose)
            implementation(libs.compose.colorpicker)
            implementation(libs.sdp.ssp.compose.multiplatform)

            // Koin dependencies for dependency injection
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.calf.ui)
            implementation(libs.compose.colorpicker)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "com.devansh.noteapp.core.designsystem"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}