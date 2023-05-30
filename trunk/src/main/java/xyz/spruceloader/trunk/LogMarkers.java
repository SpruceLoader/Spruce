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

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * @since 0.0.1
 */
public class LogMarkers {
    public static final Marker CORE = MarkerManager.getMarker("CORE");
    public static final Marker ENTRYPOINT = MarkerManager.getMarker("ENTRYPOINT").addParents(CORE);
    public static final Marker SERVICELOOKUP = MarkerManager.getMarker("SERVICELOOKUP").addParents(CORE);
    public static final Marker TRANSFORM = MarkerManager.getMarker("TRANSFORM").addParents(CORE);
    public static final Marker LAUNCH = MarkerManager.getMarker("LAUNCH").addParents(CORE);
}
