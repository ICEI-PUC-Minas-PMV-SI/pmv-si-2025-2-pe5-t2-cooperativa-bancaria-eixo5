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


tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Builds a fat JAR including resources for Flyway"

    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.main.get().output)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    from("src/main/resources") {
        include("db/migration/**")
    }

    manifest {
        attributes["Main-Class"] = "com.br.pucBank.ApplicationKt"
    }
}