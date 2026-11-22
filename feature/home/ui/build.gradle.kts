plugins {
    id("android.dependencies")
    id("compose.dependencies")
}

android {
    namespace = "com.plexus.example.features.home.ui"
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":core:resources"))
    implementation(project(":core:theme"))
}
