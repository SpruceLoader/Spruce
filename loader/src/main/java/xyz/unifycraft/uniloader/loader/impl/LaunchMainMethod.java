package xyz.unifycraft.uniloader.loader.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class LaunchMainMethod {
    public static MethodHandle getMainMethod(Class<?> clz) throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(
                clz,
                "main",
                MethodType.methodType(
                        void.class,
                        String[].class
                )
        );
    }
}
