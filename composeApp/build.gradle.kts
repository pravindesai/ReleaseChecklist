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

            implementation ("com.google.firebase:firebase-core:21.1.1")
            implementation ("com.google.firebase:firebase-firestore:25.0.0")
            implementation ("com.google.firebase:firebase-auth:23.0.0")
            implementation ("com.google.firebase:firebase-analytics:22.0.0")
            implementation ("com.google.firebase:firebase-analytics:22.0.0")

//            implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
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

//            implementation("com.google.firebase:firebase-common-ktx:21.0.0")

//            implementation ("co.touchlab:firestore:0.2.5")

//            implementation ("com.google.firebase:firebase-firestore-ktx:25.0.0")
//            implementation ("com.google.firebase:firebase-auth-ktx:23.0.0")
//            implementation ("com.google.firebase:firebase-analytics-ktx:22.0.0")
//            implementation ("com.google.firebase:firebase-analytics-ktx:22.0.0")
//
//            implementation("dev.gitlive:firebase-auth:1.12.0")
//            implementation("dev.gitlive:firebase-database:1.12.0")
//            implementation("dev.gitlive:firebase-firestore:1.12.0")
//            implementation("dev.gitlive:firebase-functions:1.12.0")
//            implementation("dev.gitlive:firebase-messaging:1.12.0")
//            implementation("dev.gitlive:firebase-storage:1.12.0")
//            implementation("dev.gitlive:firebase-crashlytics:1.12.0")

            implementation("io.ktor:ktor-client-core:2.3.6")
            implementation("io.ktor:ktor-client-darwin:2.3.6")
            implementation("io.ktor:ktor-client-okhttp:2.3.6")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.6")




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
