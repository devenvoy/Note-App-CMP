    plugins {
        alias(libs.plugins.kotlinMultiplatform)
        alias(libs.plugins.androidLibrary)
        alias(libs.plugins.sqldelight)
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

                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.kotlinx.coroutines.core)
    //            implementation(libs.sqlite.bundled)
                implementation(libs.koin.core)
            }

            androidMain.dependencies {
                implementation(libs.sqldelight.android.driver)
            }

            iosMain.dependencies {
                implementation(libs.sqldelight.native.driver)
            }

            jvmMain.dependencies {
                implementation(libs.sqldelight.sqlite.driver)
            }

            jsMain.dependencies {
                implementation(libs.sqldelight.web.worker.driver)

                implementation (npm("sql.js", "1.12.0"))
                implementation (devNpm("copy-webpack-plugin", "9.1.0"))
                implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.1.0"))
            }

        }
    }

    android {
        namespace = "com.devansh.noteapp.core.database"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        defaultConfig {
            minSdk = libs.versions.android.minSdk.get().toInt()
        }
    }

    sqldelight {
        databases {
            create("NoteAppDatabase") {
                packageName = "com.devansh.noteapp.core.database"
                generateAsync.set(true)
            }
        }
    }