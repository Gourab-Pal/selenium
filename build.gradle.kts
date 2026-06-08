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

val allureAdapterVersion = "2.35.2"
val allureCommandlineVersion = "2.42.0"

dependencies {
    testImplementation("org.testng:testng:7.10.2")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.44.0")
    testImplementation("io.qameta.allure:allure-testng:$allureAdapterVersion")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.slf4j:slf4j-simple:2.0.17")
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
        showStandardStreams = false
    }
}
