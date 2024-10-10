plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("maven-publish")
}

group = "com.garodriguezlp"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.apache.commons:commons-imaging:1.0.0-alpha5")
    implementation("net.sourceforge.tess4j:tess4j:5.13.0")
    implementation("org.apache.commons:commons-csv:1.12.0")
    implementation("info.picocli:picocli-spring-boot-starter:4.7.6")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<ProcessResources>("processResources") {
    filesMatching("**/application.yml") {
        expand(project.properties)
    }
}

publishing {
    publications {
        create<MavenPublication>("bootJava") {
            artifact(tasks.named("bootJar"))
        }
    }
}
