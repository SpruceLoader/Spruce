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

package xyz.spruceloader.trunk.api;

/**
 * @since 0.0.1
 */
public interface ILauncherService {
    /**
     * Get the launch target.
     *
     * @return The launch target.
     */
    LauncherRunnable launchTarget(ClassLoader classLoader, String[] args);

    @FunctionalInterface
    interface LauncherRunnable {
        void launch() throws Throwable;
    }
}
