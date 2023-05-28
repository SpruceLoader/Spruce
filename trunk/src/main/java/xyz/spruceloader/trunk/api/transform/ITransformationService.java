package xyz.spruceloader.trunk.api.transform;

/**
 * @since 0.0.1
 */
public interface ITransformationService {
    /**
     * Register a transformer to this service.
     *
     * @param transformer           The transformer to register.
     * @param transformerCapability The capabilities of the transformer.
     */
    void registerTransformer(ITransformer transformer, Capability... transformerCapability);

    /**
     * Removes a transformer.
     *
     * @param transformer The transformer to remove.
     */
    void unregisterTransformer(ITransformer transformer);

    enum Capability {
        /**
         * The transformer is capable of env "hotswap" transformations.
         */
        HOTSWAP,
    }
}
