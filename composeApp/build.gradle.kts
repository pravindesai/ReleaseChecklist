import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    kotlin("plugin.serialization") version "1.9.0"
}

kotlin {
    androidTarget().compilations.all {
            kotlinOptions {
                jvmTarget = "17"
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
    
    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.android.driver)
//            implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
            implementation("io.ktor:ktor-client-okhttp:2.3.10")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")


        }

        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:2.3.10")
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.compose.ui.tooling.preview)
            implementation(compose.material3)
            implementation("com.arkivanov.decompose:decompose:2.2.3")
            implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.2.3")
            implementation("io.insert-koin:koin-core:3.2.0")
            implementation("cafe.adriel.voyager:voyager-navigator:1.1.0-alpha04")
            implementation("cafe.adriel.voyager:voyager-screenmodel:1.1.0-alpha04")
            implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:1.1.0-alpha04")
            implementation("cafe.adriel.voyager:voyager-tab-navigator:1.1.0-alpha04")
            implementation("cafe.adriel.voyager:voyager-transitions:1.1.0-alpha04")
            implementation("cafe.adriel.voyager:voyager-koin:1.1.0-alpha04")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")


            implementation("io.ktor:ktor-client-core:2.3.10")
            implementation("io.ktor:ktor-client-logging:2.3.10")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.10")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.10")
            implementation("io.ktor:ktor-client-encoding:2.3.10")
            implementation("io.ktor:ktor-client-resources:2.3.10")

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
            implementation("io.coil-kt.coil3:coil-compose:3.0.0-alpha06")
            implementation("io.coil-kt.coil3:coil-network-ktor:3.0.0-alpha06")


        }
    }

    sourceSets.nativeMain.dependencies {
        implementation(libs.native.driver)
    }

    sourceSets.jvmMain.dependencies {
        implementation(libs.sqlite.driver)
    }

}

android {
    namespace = "app.releasechecklist.code"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "app.releasechecklist.code"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
}

tasks.register("testClasses"){}

