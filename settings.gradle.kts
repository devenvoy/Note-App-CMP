rootProject.name = "NoteApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

include(":composeApp")
// Core modules
include(":shared:core:common")
include(":shared:core:network")
include(":shared:core:database")
include(":shared:core:design-system")

// Data modules
include(":shared:data:models")
include(":shared:data:repository")

// Feature modules
include(":shared:feature:notes")
include(":shared:feature:settings")
include(":shared:feature:authentication")