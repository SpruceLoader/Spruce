package xyz.spruceloader.trunk.api;

/**
 * @since 0.0.1
 */
public interface ILauncherService {
    /**
     * Get the launch target.
     *
     * @return The launch target.
     */
    LauncherRunnable launchTarget(ClassLoader classLoader, String[] args);

    @FunctionalInterface
    interface LauncherRunnable {
        void launch() throws Throwable;
    }
}
