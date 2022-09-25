package xyz.deftu.testmod;

import xyz.unifycraft.uniloader.api.ServerModEntrypoint;

public class TestModServer implements ServerModEntrypoint {
    public void initialize() { System.out.println("Hello from Server!"); }
}
