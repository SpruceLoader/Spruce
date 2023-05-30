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

package xyz.spruceloader.loader.api.env;

/**
 * @since 0.0.1
 */
public enum Environment {
    /**
     * The client/local game environment.
     */
    CLIENT,

    /**
     * The server environment.
     */
    SERVER,

    /**
     * Both the client and server environment.
     * <p>
     * Note that this is not to be used in {@link EnvTarget} as it would make no
     * practical sense.
     */
    BOTH,
}
