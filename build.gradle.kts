import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.13")
    }
}
plugins {
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("com.google.protobuf") version "0.8.13"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.10"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

group = "com.kotlingrpc"
version = "0.0.1-SNAPSHOT"

var grpcVersion = "1.35.0"
var grpcKotlinVersion = "1.0.0" // CURRENT_GRPC_KOTLIN_VERSION
var protobufVersion = "3.14.0"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        google()
    }

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}


dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    api("com.google.protobuf:protobuf-java-util:${protobufVersion}")
    api("io.grpc:grpc-kotlin-stub:${grpcKotlinVersion}")

    runtimeOnly("io.grpc:grpc-netty:${grpcVersion}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }
    generatedFilesBaseDir = "$projectDir/src/main/kotlin/generated"
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}