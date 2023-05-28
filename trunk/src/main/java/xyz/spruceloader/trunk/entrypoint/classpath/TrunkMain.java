package xyz.spruceloader.trunk.entrypoint.classpath;

import xyz.spruceloader.trunk.api.transform.ITransformationService;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Trunk entrypoint based on jar classpath.
 *
 * @since 0.0.1
 */
public final class TrunkMain {
    public static void main(String[] args) {
        try {
            ITransformationService transformationService =
                    new ClasspathTransformationService();
            TransformingClassLoader classLoader =
                    new TransformingClassLoader(transformationService);
            Thread.currentThread().setContextClassLoader(classLoader);

            Class<?> trunkClass =
                    classLoader.loadClass("xyz.spruceloader.trunk.Trunk");
            MethodHandle launchMethod = MethodHandles.publicLookup()
                    .findConstructor(trunkClass, MethodType.methodType(
                            void.class, ITransformationService.class
                    ));
            Object trunkInstance = launchMethod.invokeExact(transformationService);

            MethodHandle runMethod = MethodHandles.publicLookup()
                    .findVirtual(trunkClass, "run", MethodType.methodType(
                            void.class, String[].class
                    ));
            runMethod.invokeExact(trunkInstance, args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
