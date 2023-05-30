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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.spruceloader.trunk.Trunk;
import xyz.spruceloader.trunk.api.transform.ITransformationService;

/**
 * Trunk entrypoint based on jar classpath.
 *
 * @since 0.0.1
 */
public final class TrunkMain {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            ITransformationService transformationService =
                    new ClasspathTransformationService();
            TransformingClassLoader classLoader =
                    new TransformingClassLoader(transformationService);
            Thread.currentThread().setContextClassLoader(classLoader);
            new Trunk(transformationService).run(args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
