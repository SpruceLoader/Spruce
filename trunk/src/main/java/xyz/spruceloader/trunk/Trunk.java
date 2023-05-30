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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.spruceloader.trunk.api.transform.ITransformationService;

/**
 * @since 0.0.1
 */
public final class Trunk {
    private static Trunk instance;
    private final Logger logger = LogManager.getLogger(Trunk.class);

//    private final TrunkClassLoader trunkClassLoader;
    private final ITransformationService transformationService;

    public Trunk(@NotNull ITransformationService transformationService/*, TrunkClassLoader classLoader*/) {
        if (instance != null) {
            throw new UnsupportedOperationException("Trunk has already been initialized!");
        }
        this.transformationService = transformationService;
//        this.trunkClassLoader = classLoader;

        instance = this;

        String caller = new Throwable().getStackTrace()[1].getClassName();
        logger.trace(LogMarkers.ENTRYPOINT, "Initializing Trunk from entrypoint \"{}\"", caller);
    }

    public void run(String[] args) {
        System.setProperty("log4j2.formatMsgNoLookups", "true");

        logger.info("Initialized Trunk successfully, delegating launch...");
    }

    public static @NotNull Trunk getInstance() {
        if (instance == null) {
            throw new UnsupportedOperationException("Trunk has not been initialized!");
        }
        return instance;
    }
}
