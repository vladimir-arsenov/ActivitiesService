plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
	jacoco
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
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
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
