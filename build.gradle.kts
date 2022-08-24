plugins {
    id("xyz.unifycraft.gradle.multiversion-root")
}

preprocess {
    val fabric11902 = createNode("1.19.2-fabric", 11902, "yarn")
    val fabric11802 = createNode("1.18.2-fabric", 11802, "yarn")

    fabric11902.link(fabric11802)
}
