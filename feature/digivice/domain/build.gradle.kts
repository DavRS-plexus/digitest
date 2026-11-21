plugins {
    id("kotlin.domain")
}

dependencies {
    implementation(project(":core:error:domain"))

    implementation(project(":libs:kotlin-utils"))
}
