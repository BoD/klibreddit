plugins {
    kotlin("jvm")
    id("application")
}

repositories {
    mavenCentral()
}

dependencies {
    // Rx
    implementation(ReactiveX.rxJava2.rxKotlin)

    // Slf4j
    implementation("org.slf4j:slf4j-api:_")

    // Commons text
    implementation("org.apache.commons:commons-text:_")

    // Library
    implementation(project(":library"))
}

application {
    mainClassName = "org.jraf.klibreddit.sample.SampleKt"
}

// Use "./gradlew run" to run the sample
