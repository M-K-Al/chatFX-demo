package utils;

public class SystemUtils {

    private static OS os = null;

    static {
        final String sys = System.getProperty("os.name").toLowerCase();
        if (sys.contains("win")) os = OS.WINDOWS;
        else if (sys.contains("nix") || sys.contains("nux") || sys.contains("aix")) os = OS.LINUX;
        else if (sys.contains("mac")) os = OS.MAC;
        else if (sys.contains("sunos")) os = OS.SOLARIS;
    }

    public static void showFile(String uri) {
        try {
            switch (os) {
                case WINDOWS -> Runtime.getRuntime().exec("explorer /select, " + uri);
                case MAC -> Runtime.getRuntime().exec("open -R " + uri);
                case LINUX -> Runtime.getRuntime().exec("xdg-open " + uri);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public enum OS {

        WINDOWS,

        LINUX,

        MAC,

        SOLARIS
    }
}
