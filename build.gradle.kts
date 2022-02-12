import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.6.10"

    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

    id("org.liquibase.gradle") version "2.1.1"
}

group = "fr.nicopico"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.h2database:h2")
    implementation("mysql:mysql-connector-java")
    implementation("org.liquibase:liquibase-core")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // We should specify all version explicitly for Liquibase
    // They don't have to be the same as the project dependencies
    liquibaseRuntime("org.liquibase:liquibase-core:4.5.0")
    liquibaseRuntime("mysql:mysql-connector-java:8.0.28")
    liquibaseRuntime("info.picocli:picocli:4.6.1")
    liquibaseRuntime("org.yaml:snakeyaml:1.17")
    // liquibase-hibernate5 version must be the same as liquibase-core
    liquibaseRuntime("org.liquibase.ext:liquibase-hibernate5:4.5.0")
    liquibaseRuntime("org.springframework:spring-beans:5.3.15")
    liquibaseRuntime("org.springframework.data:spring-data-jpa:2.6.1")
    liquibaseRuntime(sourceSets.main.map {it.output})
}

liquibase {
    activities {
        create("main") {
            arguments = mapOf(
                "logLevel" to "debug",
                "changeLogFile" to "src/main/resources/liquibase/master.yml",
                "url" to "jdbc:mysql://localhost:3307/blog",
                "username" to "root",
                "password" to "password",
                "referenceUrl" to "hibernate:spring:fr.nicopico.blogengine.domain.entities" +
                        "?dialect=org.hibernate.dialect.MySQLDialect",
                "referenceDriver" to "liquibase.ext.hibernate.database.connection.HibernateDriver",
            )
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
