import org.gradle.initialization.BuildRequestMetaData
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Date
import java.text.SimpleDateFormat

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
    implementation("org.springdoc:springdoc-openapi-ui:1.6.6")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.6")

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
    liquibaseRuntime("org.jetbrains.kotlin:kotlin-reflect")
    liquibaseRuntime(sourceSets.main.map {it.output})
}

// Retrieve environment variables
val dotEnv = project.getDotenv()

liquibase {
    activities {
        register("main") {
            arguments = mapOf(
                "logLevel" to "info",
                "changeLogFile" to "src/main/resources/liquibase/master.yml",
                "url" to dotEnv["DATABASE_URL"],
                "username" to dotEnv["DATABASE_USERNAME"],
                "password" to dotEnv["DATABASE_PASSWORD"],
            )
        }
        register("diffMain") {
            val startTime = Date(project.gradle.serviceOf<BuildRequestMetaData>().startTime)
            val df = SimpleDateFormat("YYYYMMdd")

            arguments = mapOf(
                "logLevel" to "info",
                "changeLogFile" to "src/main/resources/liquibase/changelog/${df.format(startTime)}-diff.yml",
                "url" to dotEnv["DATABASE_URL"],
                "username" to dotEnv["DATABASE_USERNAME"],
                "password" to dotEnv["DATABASE_PASSWORD"],
                "referenceUrl" to "hibernate:spring:fr.nicopico.blogengine.domain.entities" +
                        "?dialect=org.hibernate.dialect.MySQLDialect" +
                        "&amp;hibernate.physical_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy" +
                        "&amp;hibernate.implicit_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy",
                "referenceDriver" to "liquibase.ext.hibernate.database.connection.HibernateDriver",
            )
        }
    }
    // Running diff commands must set the activity to diffMain
    // like `./gradlew diffChangeLog -PrunList=diffMain`
    this.runList = project.ext.let {
        if (it.has("runList")) it["runList"]
        else "main"
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
