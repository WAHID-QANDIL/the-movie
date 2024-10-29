plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
    alias(libs.plugins.dagger.hilt.android.plugin)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.codescape.themovie"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.codescape.themovie"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_URL", "\"https://api.themoviedb.org/\"")
            buildConfigField("String", "IMAGE_URL", "\"https://image.tmdb.org/t/p/w500/\"")
            buildConfigField(
                "String",
                "API_KEY",
                "\"REPLACE WITH API KEY FROM https://www.themoviedb.org/\""
            )
        }
        debug {
            buildConfigField("String", "API_URL", "\"https://api.themoviedb.org/\"")
            buildConfigField("String", "IMAGE_URL", "\"https://image.tmdb.org/t/p/w500/\"")
            buildConfigField(
                "String",
                "API_KEY",
                "\"REPLACE WITH API KEY FROM https://www.themoviedb.org/\""
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)

    // Splash Screen
    implementation(libs.splashscreen)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.compose.material.material)
    implementation(libs.compose.material.icons)
    implementation(libs.androidx.material3)

    // Constraint Layout
    implementation(libs.constraintlayout)

    // KotlinX DateTime
    implementation(libs.kotlinx.datetime)

    // KotlinX Serialization
    implementation(libs.serialization)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)

    // Navigation
    implementation(libs.navigation.compose)

    // Hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
    ksp(libs.hilt.compiler)

    // Compose Hilt Navigation
    implementation(libs.hilt.navigation.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    // Retrofit
    implementation(libs.retrofit.retrofit)
    implementation(libs.retrofit.converter.kotinx.serialization)
    implementation(libs.retrofit.interceptor)

    // Paging 3
    implementation(libs.paging.runtime.ktx)
    implementation(libs.paging.compose)

    // Coil
    implementation(libs.coil.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    // Unit tests
    testImplementation(libs.junit)

    // Instrumented tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
