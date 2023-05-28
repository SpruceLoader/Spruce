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
