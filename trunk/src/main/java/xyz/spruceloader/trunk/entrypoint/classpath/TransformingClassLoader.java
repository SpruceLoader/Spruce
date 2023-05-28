package xyz.spruceloader.trunk.entrypoint.classpath;

import xyz.spruceloader.trunk.api.transform.ITransformationService;

import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @since 0.0.1
 */
class TransformingClassLoader extends ClassLoader {
    private final Set<String> invalidCache = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final ITransformationService transformationService;

    public TransformingClassLoader(ITransformationService transformationService) {
        super(null);
        this.transformationService = transformationService;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (invalidCache.contains(name)) {
            throw new ClassNotFoundException(name);
        }

        URL resource = getResource(name.replace('.', '/') + ".class");
        if (resource != null) {
            byte[] fileBytes =
        }
        invalidCache.add(name);
        throw new ClassNotFoundException(name);
    }

    static {
        ClassLoader.registerAsParallelCapable();
    }
}
