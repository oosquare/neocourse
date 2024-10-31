plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    idea
    id("com.ryandens.javaagent-test") version "0.5.1"
}

group = "io.github.oosquare"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
	    languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
	    extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.3.1-jre")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testJavaagent("net.bytebuddy:byte-buddy-agent:1.14.15")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
