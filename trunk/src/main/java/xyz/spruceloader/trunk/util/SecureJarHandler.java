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

package xyz.spruceloader.trunk.util;

import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;
import sun.security.util.ManifestEntryVerifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * This file is part of <a href="https://github.com/McModLauncher/modlauncher/blob/main-8.1.x/src/main/java/cpw/mods/modlauncher/SecureJarHandler.java">McModLauncher/modlauncher</a>.
 * <p>
 * It is licensed under the GNU Lesser General Public License v3.0 (LGPLv3) as per the modlauncher license's requirements.
 * <p>
 * Modified by SpruceLoader.
 *
 * @since 0.0.1
 */
public class SecureJarHandler {
    private static final Class<?> JVCLASS;
    private static final Method BEGIN_ENTRY;
    private static final Method UPDATE;
    private static final Field JV;
    private static final Function<Manifest, ManifestEntryVerifier> MEV_FACTORY;

    static {
        Class<?> jvclass;
        Method beginEntry;
        Method update;
        try {
            jvclass = Class.forName("java.util.jar.JarVerifier");
            beginEntry = jvclass.getMethod("beginEntry", JarEntry.class, ManifestEntryVerifier.class);
            update = jvclass.getMethod("update", int.class, byte[].class, int.class, int.class, ManifestEntryVerifier.class);
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("LEGACY JDK DETECTED, SECURED JAR HANDLING DISABLED");
            jvclass = null;
            beginEntry = null;
            update = null;
        }
        JVCLASS = jvclass;
        BEGIN_ENTRY = beginEntry;
        UPDATE = update;

        Field jv;
        try {
            jv = Manifest.class.getDeclaredField("jv");
            jv.setAccessible(true);
            BEGIN_ENTRY.setAccessible(true);
            UPDATE.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("LEGACY JDK DETECTED, SECURED JAR HANDLING DISABLED");
            jv = null;
        }

        Function<Manifest, ManifestEntryVerifier> mevFactory;
        try {
            Constructor<ManifestEntryVerifier> mevConstructor = ManifestEntryVerifier.class.getConstructor(Manifest.class, String.class);
            mevFactory = (manifest) -> {
                try {
                    return mevConstructor.newInstance(manifest, JarFile.MANIFEST_NAME);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (NoSuchMethodException e) {
            try {
                Constructor<ManifestEntryVerifier> mevConstructor = ManifestEntryVerifier.class.getConstructor(Manifest.class);
                mevFactory = (manifest) -> {
                    try {
                        return mevConstructor.newInstance(manifest);
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                };
            } catch (NoSuchMethodException e2) {
                mevFactory = null;
                jv = null;
            }
        }

        JV = jv;
        MEV_FACTORY = mevFactory;
    }


    @SuppressWarnings("ConstantConditions")
    // Manifest is required to originate from an ensureInitialized JarFile. Otherwise it will not work
    public static CodeSource createCodeSource(final String name, @Nullable final URL url, final byte[] bytes, @Nullable final Manifest manifest) {
        if (JV == null) return null;
        if (manifest == null) return null;
        if (url == null) return null;
        JarEntry je = new JarEntry(name);
        ManifestEntryVerifier mev = MEV_FACTORY.apply(manifest);
        Object obj = null;
        try {
            obj = JV.get(manifest);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (obj == null) {
            // we don't have a fully fledged manifest with security info, for some reason (likely loaded by default JAR code, rather than our stuff)
            return null;
        }
        try {
            // begin Entry on JarVerifier
            BEGIN_ENTRY.invoke(obj, je, mev);
            // Feed the bytes to the underlying MEV
            UPDATE.invoke(obj, bytes.length, bytes, 0, bytes.length, mev);
            // Generate the cert check - signers will be loaded into the dummy jar entry
            UPDATE.invoke(obj, -1, bytes, 0, bytes.length, mev);
        } catch (SecurityException se) {
            // SKIP security exception - we didn't validate the signature for some reason
            LogManager.getLogger().info("Validation problem during class loading of {}", name, se);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        CodeSigner[] signers = je.getCodeSigners();
        return new CodeSource(url, signers);
    }

    private static final Map<CodeSource, ProtectionDomain> pdCache = new HashMap<>();

    public static ProtectionDomain createProtectionDomain(CodeSource codeSource, ClassLoader cl) {
        synchronized (pdCache) {
            return pdCache.computeIfAbsent(codeSource, cs -> {
                Permissions perms = new Permissions();
                perms.add(new AllPermission());
                return new ProtectionDomain(codeSource, perms, cl, null);
            });
        }
    }

    public static boolean canHandleSecuredJars() {
        return JV != null;
    }
}