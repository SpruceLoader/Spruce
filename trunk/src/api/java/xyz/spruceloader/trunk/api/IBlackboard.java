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

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * @since 0.0.1
 */
public interface IBlackboard {
    <T> Optional<T> get(Key<T> key);

    <T> void computeIfAbsent(Key<T> key, Function<? super Key<T>, ? extends T> valueSupplier);

    /**
     * @since 0.0.1
     */
    interface Key<V> {
        Key<Path> GAME_DIR = of("gameDir", Path.class);

        String getName();

        Class<V> getType();

        static <V> Key<V> of(String name, Class<V> type) {
            return new KeyImpl<>(name, type);
        }
    }
}

/**
 * @since 0.0.1
 * @param <V> The type of the value.
 */
class KeyImpl<V> implements IBlackboard.Key<V> {
    private final String name;
    private final Class<V> type;

    public KeyImpl(String name, Class<V> type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class<V> getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyImpl)) return false;
        KeyImpl<?> key = (KeyImpl<?>) o;
        return name.equals(key.name) && type.equals(key.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
