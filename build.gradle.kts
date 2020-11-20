plugins {
    java
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.apache.logging.log4j", "log4j-api", "2.14.0")
    implementation("org.apache.logging.log4j", "log4j-core", "2.14.0")
    implementation("org.slf4j", "slf4j-simple", "2.0.0-alpha1")

    implementation("io.projectreactor", "reactor-core", "3.4.0")
    implementation("io.projectreactor.netty", "reactor-netty", "1.0.1")

    implementation("org.springframework", "spring-context", "5.3.1")
    implementation("org.springframework", "spring-webflux", "5.3.1")
    implementation("org.thymeleaf", "thymeleaf", "3.0.11.RELEASE")
    implementation("org.thymeleaf", "thymeleaf-spring5", "3.0.11.RELEASE")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

configure<ApplicationPluginConvention> {
    mainClassName = "com.example.Main"
}
