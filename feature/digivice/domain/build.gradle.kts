plugins {
    id("kotlin.library")
}

dependencies {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(libs.kotlinx.coroutines.core)

    implementation(project(":libs:kotlin-utils"))
}
