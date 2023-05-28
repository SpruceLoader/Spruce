package xyz.spruceloader.loader.api.mod;

import xyz.spruceloader.loader.api.mod.metadata.ModMetadata;

import java.net.URI;

/**
 * @since 0.0.1
 */
public interface ModContainer {
    Object getModInstance();

    URI getSource();

    ModMetadata getMetadata();
}
