plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("io.freefair.lombok") version "8.6"
}

group = "nl.daanplugge.seyoxwheel"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.mattstudios.me/artifactory/public/")
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.zaxxer:HikariCP:5.1.0")
    compileOnly("org.mariadb.jdbc:mariadb-java-client:3.3.3")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("dev.triumphteam:triumph-gui:3.1.5")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveFileName.set(rootProject.name + ".jar")

        fun reloc(pkg: String, des: String) = relocate(pkg, "nl.daanplugge.$des")
        reloc("co.aikar.commands", "acf");
        reloc("co.aikar.locales", "locales");
        reloc("dev.triumphteam.gui", "gui")
    }
}