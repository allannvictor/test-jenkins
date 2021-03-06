import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
}

group = "com.nadir"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.flywaydb:flyway-core:8.1.0")
	implementation("org.flywaydb:flyway-core")
	implementation("mysql:mysql-connector-java:8.0.29")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")

	//Gson
	implementation("com.google.code.gson:gson:2.9.0")

	//Feign
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

	//Redis
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	// https://mvnrepository.com/artifact/org.mockito/mockito-core
	testImplementation("org.mockito:mockito-core:4.4.0")

	// https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter
	testImplementation("org.mockito:mockito-junit-jupiter:4.4.0")

	testImplementation("io.mockk:mockk:1.12.0")

	runtimeOnly("org.postgresql:postgresql")

//	runtimeOnly("com.h2database:h2")
//
//	testRuntimeOnly("com.h2database:h2")

	// https://mvnrepository.com/artifact/org.testcontainers/testcontainers
	testImplementation("org.testcontainers:testcontainers:1.17.3")
	// https://mvnrepository.com/artifact/org.testcontainers/junit-jupiter
	testImplementation("org.testcontainers:junit-jupiter:1.17.3")
// https://mvnrepository.com/artifact/org.testcontainers/postgresql
	testImplementation("org.testcontainers:postgresql:1.17.3")

	// https://mvnrepository.com/artifact/org.testcontainers/mysql
	testImplementation("org.testcontainers:mysql:1.17.3")




}

extra["springCloudVersion"] = "2021.0.3"
dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.bootJar{
	archiveFileName.set("application.jar")
	manifest{
	   attributes["mainClass"] = mainClass
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
