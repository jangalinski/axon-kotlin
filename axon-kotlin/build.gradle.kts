import nebula.plugin.contacts.Contact
import nebula.plugin.contacts.ContactsExtension
import nebula.plugin.contacts.ContactsPlugin

plugins {
    id("nebula.maven-publish")
    id("nebula.info")
    id("nebula.source-jar")
    id("nebula.javadoc-jar")
    id("nebula.nebula-bintray-publishing")
    id("nebula.release")
    id("org.ajoberstar.github-pages")
    id("nebula.contacts-pom") version("2.2.2")
}
//apply(plugin = "nebula.contacts-base")
//apply(plugin = "contacts-pom")
//apply(plugin = "nebula-maven-publish")

//ContactsPlugin().
//contacts(Contact::class) {
//    "tyler.b.thrailkill@gmail.com" {
//        moniker = "Tyler Thrailkill"
//        role = "owner"
//    }
//}
//ContactsExtension(LinkedHashMap<String, Contact>()).methodMissing("contacts") //{
//    "tyler.b.thrailkill@gmail.com" {
//        moniker = "Tyler Thrailkill"
//        role = "owner"
//    }
//}

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
