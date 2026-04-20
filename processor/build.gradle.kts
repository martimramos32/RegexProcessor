plugins {
    kotlin("jvm")
    kotlin("kapt") // plug-in que permite ao kotlin usar as ferramentas de processamento de anotações
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
    // AutoService to register the processor automatically
    implementation("com.google.auto.service:auto-service:1.1.1")
    // Required for registering the processor
    kapt("com.google.auto.service:auto-service:1.1.1")
    // KotlinPoet for generating Kotlin code
    implementation("com.squareup:kotlinpoet:1.14.2")
    // Include the annotations module
    implementation(project(":annotations"))
}

kapt {
    correctErrorTypes = true
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}