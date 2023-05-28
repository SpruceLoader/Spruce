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

package xyz.spruceloader.trunk;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * A classloader for Spruce related classes.
 * <p>
 * This includes:
 *   <ul>
 *     <li>Spurce's libraries</li>
 *     <li>Spruce Loader</li>
 *     <li>Spruce mods</li>
 *   </ul>
 * <p>
 * <b>Note</b>: This classloader does <b>not</b> serve as a transformation platform, see
 * {@link xyz.spruceloader.trunk.api.transform.ITransformationService ITransformationService} for more information.
 *
 * @since 0.0.1
 */
public class TrunkClassLoader extends URLClassLoader {
    public TrunkClassLoader() {
        super(new URL[0], TrunkClassLoader.class.getClassLoader());
    }
}
