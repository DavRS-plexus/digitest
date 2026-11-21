plugins {
    id("com.android.application")
    id("config.android")
    id("config.compose")
}

android {
    namespace = "com.plexus.example"

    defaultConfig {
        applicationId = "com.plexus.example.poketest"
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

//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }

//    buildFeatures {
//        compose = true
//    }
}

dependencies {
    // Koin DI
    implementation(libs.koin.android)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)

    // Core
    implementation(project(":core:network"))
    implementation(project(":core:navigation"))
    implementation(project(":core:resources"))
    implementation(project(":core:theme"))

    // Features
    implementation(project(":feature:digivice:data"))
    implementation(project(":feature:digivice:domain"))
    implementation(project(":feature:digivice:ui"))

//    implementation(libs.androidx.activity.compose)
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}