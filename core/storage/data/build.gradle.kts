plugins {
    id("kotlin.library")
    alias(libs.plugins.koin.compiler)
}

dependencies {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
}
