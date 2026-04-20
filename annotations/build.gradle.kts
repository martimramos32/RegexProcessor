plugins {
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib")) // biblioteca que contém as ferramentas essenciais para o Kotlin trabalhar
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}