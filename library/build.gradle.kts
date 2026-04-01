plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("java-library")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(KotlinX.coroutines.jdk9)

    // Json
    implementation("org.json:json:_")

    // Retrofit
    implementation(Square.retrofit2)
    implementation(Square.retrofit2.converter.gson)

    // Ktor
    implementation(Ktor.client.core)
    implementation(Ktor.client.contentNegotiation)
    implementation(Ktor.client.auth)
    implementation(Ktor.client.logging)
    implementation(Ktor.plugins.serialization.kotlinx.json)
    implementation(Ktor.client.okHttp)

    implementation(KotlinX.serialization.json)

    // OkHttp
    implementation(Square.okHttp3.loggingInterceptor)

    // Gson
    implementation("com.google.code.gson:gson:_")

    // Slf4j
    implementation("org.slf4j:slf4j-api:_")

}

// Use "./gradlew publishToMavenLocal" to deploy the artifacts to your local maven repository
