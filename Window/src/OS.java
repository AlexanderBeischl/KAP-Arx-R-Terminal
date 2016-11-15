import java.io.File;
import java.io.IOException;

public class OS {

    /**
     * Enum for the OS type
     * @author Fabian Prasser
     *
     */
    public static enum OSType {
        WINDOWS,
        UNIX,
        MAC
    }
    
	/** Locations*/
	private static final String[] locationsMac = {"/Applications/R.app/Contents/MacOS/R"};
	/** Locations*/
	private static final String[] locationsUnix = {"/usr/lib/R/bin"};

	/** Locations*/
	private static final String[] locationsWindows = {"C:\\Program Files\\R\\R-3.3.2\\bin",
	                                                  "C:\\Program Files\\R\\R-2.1.5.1\\bin"};
	/** Executables*/
	private static final String[] executablesMac = {"R.app"};
	/** Executables*/
	private static final String[] executablesUnix = {"exec"};
    /** Executables*/
	private static final String[] executablesWindows = {"R.exe"};
	
	/**
     * Returns the OS
     * @return
     */
    public static OSType getOS() {

        String os = System.getProperty("os.name").toLowerCase();

        if (os.indexOf("win") >= 0) {
            return OSType.WINDOWS;
        } else if (os.indexOf("mac") >= 0) {
            return OSType.MAC;
        } else if ((os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 )) {
            return OSType.UNIX;
        } else {
            throw new IllegalStateException("Unsupported operating system");
        }
    }

    /**
	 * Returns a path to the R executable or null if R cannot be found
	 * @return
	 */
	public static String getR() {
	    switch (getOS()) {
	    case MAC:
	        return getPath(locationsMac, executablesMac);
	    case UNIX:
	        return getPath(locationsUnix, executablesUnix);
	    case WINDOWS:
	        return getPath(locationsWindows, executablesWindows);
	    default:
	        throw new IllegalStateException("Unknown operating system");
	    }
	}

    /**
     * Returns a path to the R executable or null if R cannot be found
     * @param folder The folder to look in
     * @return
     */
    public static String getR(String folder) {
        switch (getOS()) {
        case MAC:
            return getPath(new String[]{folder}, executablesMac);
        case UNIX:
            return getPath(new String[]{folder}, executablesUnix);
        case WINDOWS:
            return getPath(new String[]{folder}, executablesWindows);
        default:
            throw new IllegalStateException("Unknown operating system");
        }
    }
    
    /**
     * Returns the path of the R executable or null if R cannot be found
     * @return
     */
    private static String getPath(String[] locations, String[] executables) {
        for (String location : locations) {
            if (!location.endsWith(File.separator)) {
                location += File.separator;
            }
            for (String executable : executables) {
                try {
                    File file = new File(location + executable);
                    if (file.exists()) {
                        return file.getCanonicalPath();
                    }
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return null;
    }
}
