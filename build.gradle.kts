plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("maven-publish")
}

group = "com.github.garodriguezlp"
version = "0.0.3-SNAPSHOT"

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
    implementation("org.apache.commons:commons-csv:1.12.0")
    implementation("info.picocli:picocli-spring-boot-starter:4.7.6")

    implementation(platform("software.amazon.awssdk:bom:2.28.20"))
    implementation("software.amazon.awssdk:sso")
    implementation("software.amazon.awssdk:ssooidc")
    implementation("software.amazon.awssdk:textract")

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

tasks.register<Copy>("processJReleaserResources") {
    from("src/jreleaser/config.properties")
    into(layout.buildDirectory.dir("processedResources/jreleaser"))
    expand(project.properties)
}

tasks.named("processResources") {
    dependsOn("processJReleaserResources")
}
