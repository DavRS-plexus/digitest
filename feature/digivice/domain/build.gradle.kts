plugins {
    id("kotlin.domain")
    alias(libs.plugins.koin.compiler)
}

dependencies {
    implementation(project(":core:error:domain"))

    implementation(project(":libs:kotlin-utils"))
}
