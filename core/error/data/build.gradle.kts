plugins {
    id("kotlin.data")
}

dependencies {
    implementation(project(":core:error:domain"))

    // Gson
    implementation(libs.gson)
}
