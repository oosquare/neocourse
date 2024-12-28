plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    idea
    id("com.ryandens.javaagent-test") version "0.5.1"
    id("com.vaadin") version "24.5.0"
}

group = "io.github.oosquare"
version = "0.1.0"

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
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.vaadin:vaadin-spring-boot-starter:24.5.0")
    implementation("org.xerial:sqlite-jdbc:3.47.1.0")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.6.3.Final")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testJavaagent("net.bytebuddy:byte-buddy-agent:1.14.15")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("com.vaadin:vaadin-bom:24.5.0")
    }
}

defaultTasks("clean", "vaadinBuildFrontend", "build")

java {
    toolchain {
	    languageVersion = JavaLanguageVersion.of(21)
    }
}

vaadin {
    optimizeBundle = false
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val integrationTest: SourceSet = sourceSets.create("integrationTest") {
    java {
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        srcDir("src/integrationTest/java")
    }
    resources.srcDir("src/integrationTest/resources")
}

configurations[integrationTest.implementationConfigurationName]
    .extendsFrom(configurations.testImplementation.get())
configurations[integrationTest.compileOnlyConfigurationName]
    .extendsFrom(configurations.compileOnly.get())
configurations[integrationTest.annotationProcessorConfigurationName]
    .extendsFrom(configurations.annotationProcessor.get())
configurations[integrationTest.runtimeOnlyConfigurationName]
    .extendsFrom(configurations.testRuntimeOnly.get())

val integrationTestTask = tasks.register<Test>("integrationTest") {
    group = "verification"

    useJUnitPlatform()

    testClassesDirs = integrationTest.output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    systemProperty("spring.profiles.active", "test")

    shouldRunAfter("test")

}

tasks.named("check") {
    dependsOn(integrationTestTask)
}