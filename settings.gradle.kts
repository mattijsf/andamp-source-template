// SPDX-License-Identifier: Apache-2.0

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // An SDK published with `./gradlew publishToMavenLocal` from a checkout
        // of the player is found here before Maven Central, for trying an
        // unreleased SDK change. Only the SDK's group is looked up here.
        mavenLocal {
            content { includeGroup("nl.mattix.andamp") }
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "andamp-source-template"
include(":app")
