package xyz.spruceloader.trunk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.spruceloader.trunk.api.transform.ITransformationService;

/**
 * @since 0.0.1
 */
public final class Trunk {
    private static Trunk instance;
    private final Logger logger = LogManager.getLogger(Trunk.class);

//    private final TrunkClassLoader trunkClassLoader;
    private final ITransformationService transformationService;

    public Trunk(ITransformationService transformationService/*, TrunkClassLoader classLoader*/) {
        if (instance != null) {
            throw new UnsupportedOperationException("Trunk has already been initialized!");
        }
        this.transformationService = transformationService;
//        this.trunkClassLoader = classLoader;

        instance = this;
    }

    private void run(String[] args) {
        logger.info("Trunk has been initialized!");
    }

    public static @NotNull Trunk getInstance() {
        if (instance == null) {
            throw new UnsupportedOperationException("Trunk has not been initialized!");
        }
        return instance;
    }
}
