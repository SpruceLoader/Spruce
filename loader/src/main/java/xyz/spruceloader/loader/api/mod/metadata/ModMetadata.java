package xyz.spruceloader.loader.api.mod.metadata;

import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

/**
 * @since 0.0.1
 */
public interface ModMetadata {
    String getId();
    String getDisplayName();
    String getDescription();
    String getVersion();

    default @Unmodifiable List<ModPerson> getModAuthors() {
        return Collections.emptyList();
    }
}
