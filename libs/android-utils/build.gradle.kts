plugins {
    id("android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.plexus.example.libs.androidutils"

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Android
    implementation("androidx.core:core-ktx:1.17.0") // FileProvider (ContextExtensions)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.3") // LocalLifecycleOwner (ComposeUtils)

    // Compose
    implementation(platform("androidx.compose:compose-bom:2025.08.01"))
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.ui:ui") // stringResource (UiText in ComposeUtils)

    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
}
