plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.0"
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.rootup"

    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.rootup"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
}