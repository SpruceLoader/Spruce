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
import xyz.spruceloader.trunk.api.transform.ITransformer;

/**
 * A transformation service for classpath based entrypoints.
 */
final class ClasspathTransformationService implements ITransformationService {
    @Override
    public void registerTransformer(ITransformer transformer, Capability... transformerCapability) {

    }
}
