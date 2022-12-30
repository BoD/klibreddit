plugins {
    kotlin("jvm")
    id("java-library")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    // Rx
    api(ReactiveX.rxJava2)
    implementation(ReactiveX.rxJava2.rxKotlin)

    // Json
    implementation("org.json:json:_")

    // Retrofit
    implementation(Square.retrofit2)
    implementation(Square.retrofit2.adapter.rxJava2)
    implementation(Square.retrofit2.converter.gson)

    // OkHttp
    implementation(Square.okHttp3.loggingInterceptor)

    // Gson
    implementation("com.google.code.gson:gson:_")

    // Slf4j
    implementation("org.slf4j:slf4j-api:_")

}

// Use "./gradlew publishToMavenLocal" to deploy the artifacts to your local maven repository
