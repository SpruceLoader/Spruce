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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * @since 0.0.1
 */
@FunctionalInterface
public interface ITransformer {
    /**
     * Performs byte transformations on a given class buffer.
     *
     * @param context   The immutable transformation context, containing extra
     *                  information about the transformation; unchanged for a
     *                  singular transformation.
     * @param classData The classfile byte buffer, may be {@code null}.
     *                  If {@code null}, the transformer may attempt to load the
     *                  class data itself or try to get the resources via
     *                  {@link ITransformationContext#getClassLoader()} and
     *                  {@link ClassLoader#getResource(String)}.
     * @return The transformed bytes, or {@code null} if no transformations were performed.
     */
    byte @Nullable [] transform(@NotNull ITransformationContext context, byte @Nullable [] classData);

    /**
     * @return The priority of this transformer; higher priority transformers are run first.
     */
    default int getPriority() {
        return 0;
    }

    /**
     * @return A filter for which classes this transformer should be applied to.
     */
    default Predicate<String> getTransformationFilter() {
        return s -> true;
    }
}
