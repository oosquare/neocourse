plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    idea
    id("com.ryandens.javaagent-test") version "0.5.1"
    id("com.vaadin") version "24.5.0"
}

group = "io.github.oosquare"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
	    languageVersion = JavaLanguageVersion.of(21)
    }
}

vaadin {
    optimizeBundle = false
}

configurations {
    compileOnly {
	    extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.vaadin.com/vaadin-addons")
    }
}

dependencies {
    implementation("com.google.guava:guava:33.3.1-jre")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.vaadin:vaadin-spring-boot-starter:24.5.0")
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

dependencyManagement {
    imports {
        mavenBom("com.vaadin:vaadin-bom:24.5.0")
    }
}

defaultTasks("clean", "vaadinBuildFrontend", "build")