package xyz.spruceloader.loader.api.mod.metadata;

import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Map;

/**
 * @since 0.0.1
 */
public interface ModPerson {
    String getId();

    default String getDisplayName() {
        return getId();
    }

    default @Unmodifiable Map<String, Object> getOther() {
        return Collections.emptyMap();
    }
}