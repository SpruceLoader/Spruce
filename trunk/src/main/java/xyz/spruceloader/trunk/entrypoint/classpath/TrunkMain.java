/*
 * This file is part of SpruceLoader.
 * Copyright (C) 2023  SpruceLoader & Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
