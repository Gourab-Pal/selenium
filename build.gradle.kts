plugins {
    id("java")
    id("io.qameta.allure") version "4.0.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val seleniumVersion: String by project
val testngVersion: String by project
val allureAdapterVersion: String by project
val allureCommandlineVersion: String by project
val assertjVersion: String by project
val slf4jVersion: String by project

dependencies {
    testImplementation("org.testng:testng:$testngVersion")
    testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumVersion")
    testImplementation("io.qameta.allure:allure-testng:$allureAdapterVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("org.postgresql:postgresql:42.7.3")
}

allure {
    version.set(allureCommandlineVersion)
    report {
        reportDir.set(layout.buildDirectory.dir("reports/allure-report"))
    }
}

tasks.test {
    useTestNG {
        suites("src/test/resources/testng.xml")
    }

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

tasks.named("allureReport") {
    logging.captureStandardOutput(LogLevel.DEBUG)
}