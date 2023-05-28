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
