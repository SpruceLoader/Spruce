package xyz.spruceloader.loader.impl.launch;

import xyz.spruceloader.trunk.api.ILauncherService;

/**
 * @since 0.0.1
 */
public class SpruceLauncherService implements ILauncherService {
    @Override
    public LauncherRunnable launchTarget(ClassLoader classLoader, String[] args) {
        return () -> {
            throw new UnsupportedOperationException("Unimplemented");
        };
    }
}
