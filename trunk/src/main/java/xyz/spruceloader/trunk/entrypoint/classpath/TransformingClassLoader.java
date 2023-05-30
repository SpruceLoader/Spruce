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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.spruceloader.trunk.api.transform.ITransformationService;
import xyz.spruceloader.trunk.util.SecureJarHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @since 0.0.1
 */
class TransformingClassLoader extends ClassLoader {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Set<String> invalidCache =
            Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final ITransformationService transformationService;

    public TransformingClassLoader(ITransformationService transformationService) {
        super(null);
        this.transformationService = transformationService;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (invalidCache.contains(name)) {
            throw new ClassNotFoundException(name);
        }
        LOGGER.error("Loading class {}", name);

        try {
            Pair<Pair<Manifest, URL>, byte[]> classData = findClassData(name);
            Pair<Manifest, URL> classMeta = classData.getKey();

            Manifest manifest = classMeta.getKey();
            URL baseURL = classMeta.getValue();
            byte[] postBuffer = classData.getValue();

            if (postBuffer == null || postBuffer.length == 0) {
                throw new IOException("Class data not found.");
            }

            int i = name.lastIndexOf('.');
            String packageName = i > 0 ? name.substring(0, i) : "";
            if (getPackage(packageName) == null) {
                definePackage(packageName, manifest);
            }

            String path = name.replace('.', '/')
                    .concat(".class");
            CodeSource codeSource = SecureJarHandler.createCodeSource(
                    path, baseURL, postBuffer, manifest);
            return defineClass(name, postBuffer, 0, postBuffer.length,
                    SecureJarHandler.createProtectionDomain(codeSource, this));
        } catch (IOException e) {
            LOGGER.error("Failed to find class data for {}", name, e);
            invalidCache.add(name);
            throw new ClassNotFoundException(name);
        }
    }

    private @NotNull Pair<@NotNull Pair<@Nullable Manifest, @Nullable URL>, byte @Nullable []> findClassData(
            String className
    ) throws IOException {
        Manifest manifest = null;
        URL baseURL = null;
        byte[] classData = null;

        String fqcn = className.replace('.', '/');
        URL jarUrl = getResource(fqcn + ".class");
        if (jarUrl != null) {
            LOGGER.error("url {}", jarUrl);
            URLConnection connection = jarUrl.openConnection();
            if (connection instanceof JarURLConnection) {
                LOGGER.error("fdwfghuiowehgje");
                manifest = ((JarURLConnection) connection).getManifest();
                baseURL = ((JarURLConnection) connection).getJarFileURL();
            }
            try (InputStream inputStream = connection.getInputStream()) {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteStream.write(buffer, 0, bytesRead);
                }
                classData = byteStream.toByteArray();
            }
        }

        byte[] postBuffer = transformationService.applyChanges(
                new ClasspathTransformationContext(fqcn, this),
                classData
        );
        if (postBuffer == null) {
            postBuffer = classData;
        }
        return pair(pair(manifest, baseURL), postBuffer);
    }

    @Contract("_, null -> null")
    private @Nullable Package definePackage(
            @NotNull String name,
            @Nullable Manifest manifest
    ) {
        if (manifest != null) {
            Attributes mainAttributes = manifest.getMainAttributes();
            Attributes pkgAttributes = manifest.getAttributes(
                    name.replace('.', '/')
            );

            String title = getAttributeValue(pkgAttributes, mainAttributes,
                    Attributes.Name.IMPLEMENTATION_TITLE);
            String version = getAttributeValue(pkgAttributes, mainAttributes,
                    Attributes.Name.IMPLEMENTATION_VERSION);
            String vendor = getAttributeValue(pkgAttributes, mainAttributes,
                    Attributes.Name.IMPLEMENTATION_VENDOR);

            String specTitle = getAttributeValue(pkgAttributes, mainAttributes,
                    Attributes.Name.SPECIFICATION_TITLE);
            String specVersion = getAttributeValue(pkgAttributes, mainAttributes,
                    Attributes.Name.SPECIFICATION_VERSION);
            String specVendor = getAttributeValue(pkgAttributes, mainAttributes,
                    Attributes.Name.SPECIFICATION_VENDOR);

            String sealed = getAttributeValue(pkgAttributes, mainAttributes,
                    Attributes.Name.SEALED);
            boolean isSealed = "true".equalsIgnoreCase(sealed);

            if (title != null || version != null || vendor != null ||
                    specTitle != null || specVersion != null ||
                    specVendor != null) {
                return definePackage(name, specTitle, specVersion, specVendor,
                        title, version, vendor, null);
            }
        }

        return null;
    }

    private @Nullable String getAttributeValue(
            Attributes packageAttributes,
            Attributes mainAttributes,
            Attributes.Name attributeName
    ) {
        String value = null;
        if (packageAttributes != null) {
            value = packageAttributes.getValue(attributeName);
        }
        if (value == null && mainAttributes != null) {
            value = mainAttributes.getValue(attributeName);
        }

        return value;
    }

    @Override
    public @Nullable URL getResource(String name) {
        return TransformingClassLoader.class.getClassLoader().getResource(name);
    }

    @Override
    public @NotNull Enumeration<URL> getResources(String name) throws IOException {
        return TransformingClassLoader.class.getClassLoader().getResources(name);
    }

    static {
        ClassLoader.registerAsParallelCapable();
    }

    private <K, V> Pair<K, V> pair(K key, V value) {
        return new Pair<>(key, value);
    }

    private static class Pair<K, V> extends AbstractMap.SimpleEntry<K, V> {
        public Pair(K key, V value) {
            super(key, value);
        }
    }
}
