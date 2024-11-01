plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("maven-publish")
}

group = "com.garodriguezlp"
version = "0.0.2-SNAPSHOT"

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

tasks.test {
    exclude("**/AWS*.class")
}

tasks.register<Test>("testAws") {
    description = "Runs tests that interact with AWS services"
    group = "verification"

    include("**/AWS*.class")

    // Copy properties from the default test task
    val defaultTestTask = tasks.getByName("test") as Test
    classpath = defaultTestTask.classpath
    testClassesDirs = defaultTestTask.testClassesDirs

    // Copy other relevant properties
    maxParallelForks = defaultTestTask.maxParallelForks
    forkEvery = defaultTestTask.forkEvery
    ignoreFailures = defaultTestTask.ignoreFailures
    reports.html.required.set(defaultTestTask.reports.html.required)
    reports.junitXml.required.set(defaultTestTask.reports.junitXml.required)
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
    repositories {
        maven {
            name = "stagingLocal"
            url = uri(layout.buildDirectory.dir("staging-deploy").get().asFile)
        }
    }
}
