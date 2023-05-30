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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.spruceloader.trunk.LogMarkers;
import xyz.spruceloader.trunk.api.transform.ITransformationContext;
import xyz.spruceloader.trunk.api.transform.ITransformationService;
import xyz.spruceloader.trunk.api.transform.ITransformer;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A transformation service for classpath based entrypoints.
 *
 * @since 0.0.1
 */
final class ClasspathTransformationService implements ITransformationService {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<ITransformer, Set<ITransformationContext.Capability>> transformers = new HashMap<>();

    @Override
    public void registerTransformer(@NotNull ITransformer transformer, @NotNull ITransformationContext.Capability @NotNull ... transformerCapabilities) {
        this.transformers.put(transformer, Arrays.stream(transformerCapabilities).collect(Collectors.toSet()));
    }

    @Override
    public void unregisterTransformer(@NotNull ITransformer transformer) {
        this.transformers.remove(transformer);
    }

    @Override
    public byte @Nullable [] applyChanges(@NotNull ITransformationContext context, byte @Nullable [] classData) {
        List<ITransformer> candidates = new ArrayList<>(transformers.size());
        Predicate<Set<ITransformationContext.Capability>> validator = context.getCapabilitiesValidator();
        String fqcn = context.getClassName();

        for (Map.Entry<ITransformer, Set<ITransformationContext.Capability>> entry : transformers.entrySet()) {
            if (validator.test(entry.getValue())) {
                ITransformer transformer = entry.getKey();
                if (transformer.getTransformationFilter().test(fqcn)) {
                    candidates.add(transformer);
                }
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }
        candidates.sort(Comparator.comparingInt(ITransformer::getPriority));

        boolean transformed = false;
        byte[] transformedData = classData;
        for (ITransformer transformer : candidates) {
            long start = System.currentTimeMillis();
            LOGGER.trace(LogMarkers.TRANSFORM, "Applying transformer {} to {}", transformer.getClass().getName(), fqcn);
            byte[] processedBuffer = transformer.transform(context, classData);
            LOGGER.trace(LogMarkers.TRANSFORM, "Tramsformation took {}ms", System.currentTimeMillis() - start);
            if (processedBuffer != null) {
                transformed = true;
                transformedData = processedBuffer;
            }
        }

        if (!transformed) {
            return null;
        }
        return transformedData;
    }
}
