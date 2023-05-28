package xyz.spruceloader.trunk.api;

import org.jetbrains.annotations.NotNull;
import xyz.spruceloader.trunk.Trunk;

/**
 * @since 0.0.1
 */
public interface ITrunkService {
    /**
     * Initialize the Trunk Service.
     *
     * @param trunk The {@link Trunk} instance.
     */
    void initialize(@NotNull Trunk trunk);
}
