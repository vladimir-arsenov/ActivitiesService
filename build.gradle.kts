import com.github.spotbugs.snom.assign

plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
	jacoco
	id("com.github.spotbugs") version "6.0.26"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

spotbugs {
	showProgress = true
	effort = "MAX"
	reportLevel = "LOW"
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
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-aop")

	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.hibernate:hibernate-spatial:6.6.3.Final")


	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.wiremock:wiremock-standalone:3.9.2")

	implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")
}

tasks.test {
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	reports {
		xml.required = true
	}

	classDirectories.setFrom(
		fileTree( "${layout.buildDirectory.get().asFile}/classes/java/main") {
			exclude("**/model/**")
		}
	)
}

tasks.jacocoTestCoverageVerification {
	dependsOn("test")

	violationRules {
		rule {
			limit {
				minimum = "0.7".toBigDecimal()
			}
		}
	}
}
