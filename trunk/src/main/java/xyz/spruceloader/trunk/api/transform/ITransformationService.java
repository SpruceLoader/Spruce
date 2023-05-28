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

/**
 * @since 0.0.1
 */
public interface ITransformationService {
    /**
     * Register a transformer to this service.
     *
     * @param transformer           The transformer to register.
     * @param transformerCapability The capabilities of the transformer.
     */
    void registerTransformer(ITransformer transformer, Capability... transformerCapability);

    /**
     * Removes a transformer.
     *
     * @param transformer The transformer to remove.
     */
    void unregisterTransformer(ITransformer transformer);

    enum Capability {
        /**
         * The transformer is capable of env "hotswap" transformations.
         */
        HOTSWAP,
    }
}
