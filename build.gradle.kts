java.sourceCompatibility = JavaVersion.VERSION_24

plugins {
    application
    kotlin("jvm") version "2.2.21"
}

val kotlinVersion = "2.2.21"

repositories {
    mavenLocal()
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Test> {
    useJUnitPlatform()
}
tasks {
    wrapper {
        gradleVersion = "8.14"
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

kotlin {
    jvmToolchain(24)
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testImplementation(kotlin("test"))
}