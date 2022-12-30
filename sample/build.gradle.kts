plugins {
    kotlin("jvm")
    id("application")
}

repositories {
    mavenCentral()
}

dependencies {
    // Rx
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")

    // Slf4j
    implementation("org.slf4j:slf4j-api:1.7.26")

    // Commons text
    implementation("org.apache.commons:commons-text:1.6")

    // Library
    implementation(project(":library"))
}

application {
    mainClassName = "org.jraf.klibreddit.sample.SampleKt"
}

// Use "./gradlew run" to run the sample
