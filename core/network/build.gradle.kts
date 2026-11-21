plugins {
    id("kotlin.data")
    alias(libs.plugins.koin.compiler)
}

dependencies {
    // OkHttp
    implementation(libs.okhttp)
}
