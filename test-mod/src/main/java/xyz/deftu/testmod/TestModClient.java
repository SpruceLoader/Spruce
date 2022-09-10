package xyz.deftu.testmod;

import net.minecraft.client.MinecraftClient;
import xyz.unifycraft.uniloader.api.ClientModEntrypoint;

public class TestModClient implements ClientModEntrypoint {
    public void initialize() {
        System.out.println("Hello from the client-side test mod!");
    }
}
