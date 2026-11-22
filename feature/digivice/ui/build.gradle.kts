plugins {
    id("android.dependencies")
    id("compose.dependencies")
}

android {
    namespace = "com.plexus.example.features.digivice.ui"
}

dependencies {
    implementation(project(":feature:digivice:domain"))

    implementation(project(":core:error:domain"))
    implementation(project(":core:navigation"))
    implementation(project(":core:resources"))
    implementation(project(":core:theme"))

    implementation(project(":libs:android-utils"))
    implementation(project(":libs:kotlin-utils"))

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
