package sh.miles.aurorameals.system;

import org.bson.codecs.pojo.annotations.BsonId;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Represents an operating system
 */
public enum OperatingSystem {

    WINDOWS(Paths.get(System.getProperty("user.home"), "AppData")),
    MAC(Paths.get(System.getProperty("user.home"), "Library", "Preferences")),
    LINUX(Paths.get(System.getProperty("user.home"), ".local", "share")),
    UNKNOWN(null);

    private static OperatingSystem current;

    private final Path appdata;

    OperatingSystem(final Path appdata) {
        this.appdata = appdata;
    }

    public Path getAppData() {
        return this.appdata;
    }

    public Path getAppData(@NotNull final String extra) {
        return this.appdata.resolve(extra);
    }

    @BsonId
    public static OperatingSystem getCurrent() {
        if (current == null) {
            String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if (os.contains("win")) {
                current = WINDOWS;
            } else if (os.contains("nux")) {
                current = LINUX;
            } else if (os.contains("mac")) {
                current = MAC;
            } else {
                current = UNKNOWN;
            }
        }
        return current;
    }

}
