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

package xyz.spruceloader.loader.api;

import java.util.ServiceLoader;

/**
 * @since 0.0.1
 */
public interface SpruceLoader {
    static SpruceLoader getInstance() {
        return Companion.getInstance();
    }
}

class Companion {
    private static final ServiceLoader<SpruceLoader> serviceLoader =
            ServiceLoader.load(SpruceLoader.class);
    private static SpruceLoader instance;

    static SpruceLoader getInstance() {
        if (instance == null) {
            serviceLoader.reload();
            instance = serviceLoader.iterator().next();
        }
        if (instance == null) {
            throw new IllegalStateException("No SpruceLoader implementation found.");
        }
        return instance;
    }
}
