package xyz.spruceloader.trunk.api.transform;

import org.jetbrains.annotations.Nullable;

/**
 * @since 0.0.1
 */
@FunctionalInterface
public interface ITransformer {
    /**
     * Performs byte transformations on a given class buffer.
     *
     * @param fqcn            The fully qualified class name of the resource being transformed
     *                        (e.g. "java/lang/String", "com/example/Class$Inner").
     * @param classfileBuffer The raw bytes of the class being transformed.
     * @return The transformed bytes, or {@code null} if no transformations were performed.
     */
    byte @Nullable [] transform(String fqcn, byte[] classfileBuffer);

    /**
     * @return The priority of this transformer; higher priority transformers are run first.
     */
    default int priority() {
        return 0;
    }
}
