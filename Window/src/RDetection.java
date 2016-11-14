import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class RDetection {

	private boolean foundR = false;
	private String pathToR; //The directory of the R-Application is stored here
	private String oS;
	
	//These Arrays contain the different locations R is usually installed
	private String[] locationsOfRMac = {"/Applications/R.app"};
	private String[] locationsOfRLinux = {"/usr/lib/R/bin/exec"};
	
	/*We are using another format here, because the path-structure of R in Windows depends on the Verison of R
	 Therefore we use two fields per path:
	 1. Path until the directory of the version
	 2. Childpath of the directory
	*/
	private String[] locationsOfRWindows = {"C:\\Program Files\\R\\*\\bin\\x64\\Rgui.exe"};
	
	RDetection()
	{
		oS = detectOS();
		foundR = autoDetectionR();
	}
	
	private boolean autoDetectionR()
	{	
		boolean findR = false;
		
		if(oS.startsWith("Windows"))
		{
			//we get the current Version of R by using getRVersionOnWindows(String)
			String[] versions = getRVersionOnWindows(locationsOfRWindows[0].substring(0, locationsOfRWindows[0].indexOf('*'))); 
			//Then we search in all possibly folders for the R-App
			for(int i = 0; i<versions.length; i++)
			{	//replace * with the R-Version
				pathToR = locationsOfRWindows[0].replace("*", versions[i]);
				//was R.exe found on this path
				if((findR =searchForFile(pathToR)))
				{
					break;
				}
			}
		}
		else
		{
			//contains the paths for the right OS
			String [] paths = locationsOfR(oS);
			
			//Search on all paths until the first R-App is found
			for(int i = 0; i < paths.length;i++)
			{
				pathToR = paths[i];
				if((findR = searchForFile(pathToR)))
				{
					break;
				}
			}
		}
		//outputRStatus();
		/*if(foundR)
		{
			ArxRTerminal.startR(pathToR, );
		}*/
		return findR;
	}
	
	public boolean manuellfindR(String path)
	{
		// Unter Mac + Linux -> which r
		//( Oder in echo $PATH suchen
		
		if(oS.startsWith("Windows"))
		{
			path = path + "\\Rgui.exe"; // Should be R.exe
		}
		if(oS.startsWith("Mac"))
		{
			path = path + "/R.app";
		}
		if(oS.startsWith("Linux"))
		{
			path = path + "/exec";
		}
		
		return searchForFile(path);
		//outputRStatus();
	}
	
	public String detectOS()
	{
	    Properties prop = System.getProperties();
	    String os = prop.getProperty("os.name");
	    return os;
	}
	
	//Returns the correct Array of Paths for OS X and Linux
	private String[] locationsOfR(String os)
	{
		if(os.startsWith("Mac OS")){
			return locationsOfRMac;
		}
		    
		if(os.startsWith("Linux")){
			return locationsOfRLinux;
		}
		return new String[] {""};
	  }
		
	//Returns the Folder/Folders of the R-Versions installed
	private String[] getRVersionOnWindows(String dir)
	{
		File[] files = new File(dir).listFiles();
		String[] versions = new String [files.length];
		for(int i=0; i<files.length; i++)
		{
			versions[i] = files[i].getName();
		}
		return versions;
	}
		
	/*Converts the String-Path to the Path-Format and searches for it.
	 * Success: Set foundR true, store the Path and activate the InputBox (which is by default deactivated) 
	 * 			Return true
	 * Failure: Return false
	 */
	private boolean searchForFile(String path)
	{
		Path files = FileSystems.getDefault().getPath(path);
		
		if(Files.exists(files))
		{
			pathToR = path;
			foundR = true;
			return foundR;
		}
		else{
			return false;
		}
	}

	boolean setPath(String path)
	{
		if(searchForFile(path))
		{
			//createTabContent();
		}
		return true;
	}
	
	public boolean getRStatus()
	{
		return foundR;
	}
	
	//The directory the R-Search-Window starts in
	public String getDirForWindow()
	 {
		if(oS.startsWith("Mac OS")){
			return "/Applications";
		}
		
		if(oS.startsWith("Windows"))
		{
			return "c:\\";
		}
		    
		if(oS.startsWith("Linux")){
			return " ";
		}
		return "";
	 }
	
	public String getPathToR()
	{
		return pathToR;
	}
}
