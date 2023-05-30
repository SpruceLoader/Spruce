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

package xyz.spruceloader.trunk.api.transform;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Provides extra information to transformers about the current transformation.
 *
 * @since 0.0.1
 */
public interface ITransformationContext {
    /**
     * @return The fully qualified name of the class being transformed
     * (e.g. "java/lang/String", "com/example/Class$Inner").
     */
    @Contract(pure = true)
    @NotNull String getClassName();

    /**
     * This method provides the ClassLoader that is used to load the class
     * after transformation. This is useful for transformers that need to
     * load other classes or get resources.
     * <p>
     * The ClassLoader may sometimes be {@code null} if the transformer
     * is called from a context where a ClassLoader is not available, such as
     * a runtime entrypoint or if transforming from the bootstrap ClassLoader.
     * <p>
     * <b>Note</b>: you should <b>NOT</b> use this ClassLoader to load
     * {@link #getClassName() the class being transformed} since it may
     * cause a stack overflow due to recursive calls.
     *
     * @return The transformation executor.
     */
    @Contract(pure = true)
    @Nullable ClassLoader getClassLoader();

    /**
     * @return A {@link Predicate} that returns {@code true} if the given
     * {@link Capability} {@link Set} can be used for the current transformation.
     */
    @Contract(pure = true)
    Predicate<Set<Capability>> getCapabilitiesValidator();

    enum Capability {
        /**
         * The transformer is capable of "hotswap" transformations.
         */
        HOTSWAP,
    }
}
