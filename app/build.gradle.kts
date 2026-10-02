// SPDX-License-Identifier: Apache-2.0

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.detekt)
}

android {
    namespace = "nl.mattix.andamp.pack.template"

    defaultConfig {
        // change this for your own source; README.md lists what else to rename
        applicationId = "nl.mattix.andamp.pack.template"
        targetSdk = 36
        versionName = "0.1.0" // x-release-please-version
    }

    buildFeatures {
        // BuildConfig.VERSION_NAME is the version the source reports to the player
        buildConfig = true
    }
}

dependencies {
    // the contract: the AIDL and the types that cross it
    implementation(libs.andamp.source.api)
    // the source side of the contract: the service base, the audio pipe, the
    // launcher entry and stream playback
    implementation(libs.andamp.source.common)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
