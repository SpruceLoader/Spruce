package xyz.deftu.testmod;

import xyz.spruceloader.api.ClientModEntrypoint;

public class TestModClient implements ClientModEntrypoint {
    public void initialize() {
        System.out.println("Hello from the client-side test mod!");
    }
}
