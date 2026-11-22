plugins {
    id("kotlin.data")
}

dependencies {
    implementation(project(":feature:digivice:domain"))

    implementation(project(":core:error:data"))
    implementation(project(":core:error:domain"))
    implementation(project(":core:storage:data"))

    implementation(project(":libs:kotlin-utils"))
}