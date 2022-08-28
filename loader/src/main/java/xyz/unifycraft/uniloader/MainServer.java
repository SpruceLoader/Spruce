package xyz.unifycraft.uniloader;

import xyz.unifycraft.uniloader.loader.Launch;
import xyz.unifycraft.uniloader.loader.api.Environment;

public class MainServer {
    public static void main(String[] args) {
        Launch.start(args, Environment.SERVER);
    }
}
