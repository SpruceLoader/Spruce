plugins {
    java
    kotlin("jvm")
}

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net/")
    maven("https://repo.spongepowered.org/maven/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    implementation("net.minecraft:launchwrapper:1.12")
    api("net.fabricmc:access-widener:2.1.0")

    val asmVersion = "9.3"
    compileOnly(api("org.ow2.asm:asm:$asmVersion")!!)
    compileOnly(api("org.ow2.asm:asm-analysis:$asmVersion")!!)
    compileOnly(api("org.ow2.asm:asm-commons:$asmVersion")!!)
    compileOnly(api("org.ow2.asm:asm-tree:$asmVersion")!!)
    compileOnly(api("org.ow2.asm:asm-util:$asmVersion")!!)
}
