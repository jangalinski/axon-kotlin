plugins {
    id("nebula.maven-publish")
    id("nebula.info")
    id("nebula.source-jar")
//    id("nebula.javadoc-jar")
//    id("nebula.nebula-bintray")
    id("nebula.nebula-bintray-publishing") version("3.5.5")
    id("nebula.release")
    id("org.ajoberstar.github-pages")
}

apply(from = "$rootDir/gradle/dokka.gradle")
apply(from = "$rootDir/gradle/publishing.gradle")
apply(from = "$rootDir/gradle/site.gradle")

dependencies {
    testImplementation("org.springframework:spring-beans:4.3.11.RELEASE")
    testImplementation("javax.inject:javax.inject:1")
    implementation("org.axonframework:axon-core")
    implementation("org.axonframework:axon-test")
    implementation("org.hamcrest:hamcrest-all:1.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.0.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.0.3")
    testCompile("org.hamcrest:hamcrest-all:1.3")
    testCompile("com.nhaarman:mockito-kotlin:1.5.0")

    testCompile("org.jetbrains.kotlin:kotlin-test:1.2.41")
    testImplementation("org.axonframework:axon-core")
    testImplementation("org.axonframework:axon-test")
}