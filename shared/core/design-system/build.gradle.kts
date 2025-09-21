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

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:common"))
            implementation(libs.bundles.jetbrains.compose)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)

            implementation(libs.material3.adaptive)
            implementation(libs.material3.expressive)
            implementation(libs.material3.adaptive.layout)
            implementation(libs.material3.adaptive.navigation)
            implementation(libs.material3.adaptive.navigation.suite)

            implementation(libs.coil.compose)
            implementation(libs.sonner)
            implementation(libs.color.materialKolor)
            implementation(libs.richeditor.compose)
            implementation(libs.compose.colorpicker)
            implementation(libs.sdp.ssp.compose.multiplatform)
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