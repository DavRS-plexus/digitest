plugins {
    id("kotlin.data")
}

dependencies {
    implementation(project(":core:error:data"))
    implementation(project(":core:error:domain"))
    implementation(project(":feature:digivice:domain"))
    implementation(project(":libs:kotlin-utils"))


    testImplementation(libs.junit)
    testImplementation(libs.gson)
}
