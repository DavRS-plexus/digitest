plugins {
    id("kotlin.library")
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
}
