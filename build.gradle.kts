plugins {
	kotlin("jvm") version "1.5.31"
}

repositories {
	mavenCentral()
	repositories {
		maven {
			url = uri("https://maven.pkg.github.com/dimitree54/chnn-library")
			credentials {
				username = "dimitree54"
				password = "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"
			}
		}
	}
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation("we.rashchenko:chnn-library:v0.1.0")
	testImplementation(kotlin("test-junit5"))
	testImplementation("org.openjdk.jol:jol-core:0.16")
}

// we need to specify following sourceSets because we store main and test not in default
//  location (which is module_path/src/main and module_path/src/test)
sourceSets.main {
	java.srcDirs("src/main")
}

sourceSets.test {
	java.srcDirs("src/test")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
	useJUnitPlatform()
	maxParallelForks = 8
}