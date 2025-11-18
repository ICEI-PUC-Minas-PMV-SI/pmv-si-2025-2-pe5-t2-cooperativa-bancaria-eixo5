plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.br.pucBank"
version = "0.1.7"

application {
    mainClass = "com.br.pucBank.ApplicationKt"
}

dependencies {
    implementation(libs.koin.ktor)

    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.auth.jwt)

    implementation(libs.logback.classic)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jbdc)

    implementation(libs.sqlite.jdbc)
    implementation(libs.mysql.connector)
    implementation(libs.flyway.core)
    implementation(libs.flyway.mysql)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}


tasks.register<Jar>("buildFatJar") {
    group = "build"
    description = "Builds a fat JAR including all dependencies and resources"

    archiveBaseName.set("pucBank")
    archiveClassifier.set("all")
    archiveVersion.set(project.version.toString())

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.main.get().output)

    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })

    from("src/main/resources") {
        include("**/*")
    }

    manifest {
        attributes(
            "Main-Class" to "com.br.pucBank.ApplicationKt"
        )
    }
}