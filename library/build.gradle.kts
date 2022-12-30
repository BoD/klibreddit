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
    api("io.reactivex.rxjava2:rxjava:2.2.7")
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")

    // Json
    implementation("org.json:json:20180130")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.5.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.5.0")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")

    // OkHttp
    implementation("com.squareup.okhttp3:logging-interceptor:3.14.0")

    // Gson
    implementation("com.google.code.gson:gson:2.8.5")

    // Slf4j
    implementation("org.slf4j:slf4j-api:1.7.26")

}

// Use "./gradlew publishToMavenLocal" to deploy the artifacts to your local maven repository
