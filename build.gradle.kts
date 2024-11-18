plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx-base:21.0.5:mac")
    implementation("org.openjfx:javafx-controls:21.0.5:mac")
    implementation("org.openjfx:javafx-graphics:21.0.5:mac")
    implementation("org.openjfx:javafx-fxml:21.0.5:mac")
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
    implementation("org.postgresql:postgresql:42.7.4")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}