plugins {
    id("com.android.application")
    id("config.android")
    id("config.compose")
}

android {
    namespace = "com.plexus.example"

    defaultConfig {
        applicationId = "com.plexus.example.digitest"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            buildConfigField("String", "digiapiUrl", "\"https://digi-api.com/\"")
        }
        create("pro") {
            dimension = "environment"
            buildConfigField("String", "digiapiUrl", "\"https://digi-api.com/\"")
        }
    }
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
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:resources"))
    implementation(project(":core:storage:data"))
    implementation(project(":core:theme"))

    // Features
    implementation(project(":feature:digivice:data"))
    implementation(project(":feature:digivice:domain"))
    implementation(project(":feature:digivice:ui"))

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
}