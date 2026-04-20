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
    implementation(kotlin("stdlib")) // biblioteca que contém as ferramentas essenciais para o Kotlin trabalhar. Apesar de a partir da versão 1.4 do Kotlin, saída a 2020, esta biblioteca já vem incorporada nos projetos Kotlin através do plug-in "kotlin("jvm")". Sendo assim , é mantida a mesma linha de código para reforçar que esta bilbioteca já exisitia por default no projeto mas assim temos a noção do seu uso e para que serve
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}