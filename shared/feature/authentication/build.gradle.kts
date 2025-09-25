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
            implementation(projects.shared.core.designSystem)

            implementation(projects.shared.data.models)
            implementation(projects.shared.data.repository)

            implementation(libs.bundles.jetbrains.compose)
            implementation(libs.bundles.jetbrains.lifecycle)
            implementation(libs.bundles.jetbrains.material3)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.supabase.compose.auth)
            implementation(libs.supabase.compose.auth.ui)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
        }
    }
}

android {
    namespace = "com.devansh.noteapp.feature.authentication"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}