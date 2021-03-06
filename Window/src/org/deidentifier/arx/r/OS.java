package org.deidentifier.arx.r;
import java.io.File;


/**
 * OS-specific functions for finding the R executable
 * 
 * @author Fabian Prasser
 * @author Alexander Beischl
 * @author Thuy Tran
 */
public class OS {

    /**
     * Enum for the OS type
     * 
     * @author Fabian Prasser
     */
    public static enum OSType {
        WINDOWS,
        UNIX,
        MAC
    }
    
	/** Locations*/
	private static final String[] locationsMac = {"/usr/local/bin/"};
	/** Locations*/
	private static final String[] locationsUnix = {"/usr/lib/R/bin",
	                                               "/usr/bin/",
	                                               "/usr/share/R/share"};

	/** Locations*/
	private static final String[] locationsWindows = {"C:\\Program Files\\R\\R-3.3.2\\bin",
	                                                  "C:\\Program Files\\R\\R-2.1.5.1\\bin"};
	/** Executables*/
	private static final String[] executablesMac = {"R"};
	/** Executables*/
	private static final String[] executablesUnix = {"R","exec"};
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
        
        // For each location
        for (String location : locations) {
            if (!location.endsWith(File.separator)) {
                location += File.separator;
            }
            
            // For each name of the executable
            for (String executable : executables) {
                try {
                    
                    // Check whether the file exists
                    File file = new File(location + executable);
                   
                    if (file.exists()) 
                    {
           
                        // Check if we have the permissions to run the file
                        ProcessBuilder builder = new ProcessBuilder(file.getCanonicalPath(), "--vanilla");
                        builder.start().destroy();
                        
                        // Return
                        return file.getCanonicalPath();
                    }
                } catch (Exception e) {
                    // Ignore: try the next location
                }
            }
        }
        
        // We haven't found anything
        return null;
    }

    /**
     * Returns the parameters for the R process
     * @param path 
     * @return
     */
    public static String[] getParameters(String path) {
        switch (getOS()) {
        case MAC:
        	return new String[]{path, "--vanilla", "--quiet", "--interactive"};
        case UNIX:
            return new String[]{path, "--vanilla", "--quiet", "--interactive"};
        case WINDOWS:
            return new String[]{path, "--vanilla", "--quiet", "--ess"};
        default:
            throw new IllegalStateException("Unknown operating system");
        }
    }
    
    public static String printOS()
    {
    	switch (getOS()) {
        case MAC:
            return "macOS";
        case UNIX:
            return "Unix";
        case WINDOWS:
            return "Windows";
        default:
            throw new IllegalStateException("Unknown operating system");
        }
    }
    
    /**
     * Check if the chosen File is a R-Exec by comparing it to the OS-specific 
     * executables
     * @param path
     * @return 
     */
    public static boolean isR_Exec(String path)
    {
    	String[] exec;
    	
    	switch (getOS()) {
        case MAC:
            exec = executablesMac;
            break;
        case UNIX:
        	exec = executablesUnix;
            break;
        case WINDOWS:
        	exec = executablesWindows;
            break;
        default:
            throw new IllegalStateException("Unknown operating system");
        }
    	
    	for(int i = 0; i < exec.length; i++)
    	{
    		if(path.endsWith(exec[i]))
    		{
    			return true;
    		}
    	}
    	return false;
    }
}
