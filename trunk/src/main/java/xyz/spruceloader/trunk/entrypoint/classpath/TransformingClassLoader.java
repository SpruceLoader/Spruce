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
