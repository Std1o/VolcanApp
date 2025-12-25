plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("library-convention")
}

android {
    namespace = "com.stdio.volcanapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.stdio.volcanapp"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    // Domain
    implementation(project(":domain"))
    // Data
    implementation(project(":data"))
    // Presentation
    implementation(project(":presentation:uploadImage"))
    // Koin
    implementation(libs.koin.android)
}