package onethreeseven.common.util;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A simple wrapper around resource access.
 * Simply use the static helpers to start in a resource folder
 * or specify your own resource folder using the constructor.
 * Once the resource folder has been specified a stream or url
 * to a particular file in that folder can be established.
 * @author Luke Bermingham
 */
public final class Res {

    private String current;

    public Res(@Nonnull String resourceFolder) {
        this.current = resourceFolder + "/";
    }

    public Res() {
        this.current = "";
    }

    private String append(String file) {
        current += file;
        return current;
    }

    /**
     * Resolve a filename into a resource URL which can be used for loading
     *
     * @param file the thing to load, i.e style.css
     * @return A url to the resource
     */
    public URL getUrl(@Nonnull String file) {
        String resPath = append(file);
        return Thread.currentThread().getContextClassLoader().getResource(resPath);
    }

    public File getFile(@Nonnull String file) {
        try {
            return new File(getUrl(file).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("The file passed in as a resource could not be found: " + file);
        }
    }

    public InputStream getStream(@Nonnull String file) {
        String resPath = append(file);
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resPath);
    }

}
