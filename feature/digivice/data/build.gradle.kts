plugins {
    id("kotlin.library")
}

dependencies {
    implementation(project(":feature:digivice:domain"))
    implementation(project(":libs:kotlin-utils"))

    // Retrofit
    implementation(libs.retrofit)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(libs.kotlinx.coroutines.core)

}
