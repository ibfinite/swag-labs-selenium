import java.util.Properties

plugins {
    id("java")
    id("io.qameta.allure") version "4.3.0"
}

group = "com.swaglabs"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(26)
    }
}

repositories {
    mavenCentral()
}

val allureVersion = "3.0.0"

dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:4.51.0")
    // @Step annotations used by the page objects in src/main
    implementation("io.qameta.allure:allure-java-commons:$allureVersion")

    testImplementation("org.testng:testng:7.12.0")
}

allure {
    adapter {
        // Adds allure-testng to the test classpath, registers its listener
        // and attaches the AspectJ weaver so @Step annotations are recorded.
        allureJavaVersion = allureVersion
        aspectjWeaver = true
        frameworks {
            testng {
                enabled = true
            }
        }
    }
}

tasks.test {
    useTestNG()

    // Tests run in a separate JVM, so pass -D overrides from the Gradle command line through to it.
    // Every key in config.properties can be overridden, so new keys need no change here.
    val configKeys = Properties().apply {
        file("src/main/resources/config.properties").reader().use { load(it) }
    }.stringPropertyNames()
    configKeys.forEach { key ->
        providers.systemProperty(key).orNull?.let { systemProperty(key, it) }
    }
}