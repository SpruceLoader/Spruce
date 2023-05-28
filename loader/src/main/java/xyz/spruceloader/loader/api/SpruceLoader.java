package xyz.spruceloader.loader.api;

import xyz.spruceloader.loader.impl.SpruceLoaderImpl;

/**
 * @since 0.0.1
 */
public interface SpruceLoader {
    static SpruceLoader getInstance() {
        return Companion.getInstance();
    }
}

class Companion {
    private static SpruceLoader instance;

    static SpruceLoader getInstance() {
        if (instance == null) {
            instance = new SpruceLoaderImpl();
        }
        return instance;
    }
}
