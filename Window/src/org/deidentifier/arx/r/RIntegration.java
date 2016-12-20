package org.deidentifier.arx.r;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

/**
 * Java integration with R. 
 * 
 * @author Fabian Prasser
 * @author Alexander Beischl
 * @author Thuy Tran
 */
public class RIntegration {

    /** Debug flag */
    private static final boolean DEBUG   = false;
    /** Newline */
    private static final char[]  NEWLINE = System.getProperty("line.separator").toCharArray();

    /** Process */
    private Process              process;
    /** Listener */
    private RListener            listener;
    /** Buffer */
    private final RBuffer        buffer;
    
    private String 				 version;

	/**
	 * Creates a new instance
	 * @param path
	 * @param buffer
	 */
	public RIntegration(final String path, 
	                         final RBuffer buffer,
	                         final RListener listener) {
	    
	    // Check args
	    if (path == null || buffer == null || listener == null) {
	        throw new NullPointerException("Argument must not be null");
	    }
	    
	    // Store
	    this.listener = listener;
	    this.buffer = buffer;
	    
	    // Create process
	    ProcessBuilder builder = new ProcessBuilder(OS.getParameters(path))
	                                 .redirectErrorStream(true); // Redirect stderr to stdout
	    
	    // Try
        try {
            
            // Start
            this.process = builder.start();
            
            // Attach process to buffer
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Reader reader = new InputStreamReader(RIntegration.this.process.getInputStream());
                        int character;
                        //Reads out the current version whenever a new R is started
                        getVersion(reader);
                        
                        while ((character = reader.read()) != -1) {
                            buffer.append((char) character);
                            listener.fireBufferUpdatedEvent();
                        }
                        shutdown();
                    } catch (IOException e) {
                        debug(e);
                        shutdown();
                    }
                }
            });
            t.setDaemon(true);
            t.start();
            
        } catch (IOException e) {
            debug(e);
            shutdown();
        }
	}

	/**
	 * Executes a command
	 * 
	 * @param command
	 */
	public void execute(String command) {
	    if (this.process == null) {
	        return;
	    }

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));
            writer.write(command);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            debug(e);
            shutdown();
        }
	}

	/**
     * Returns whether R is alive
     * @return
     */
    public boolean isAlive() {
        return this.process != null;
    }

    /**
	 * Closes R
	 */
    public void shutdown() {
        if (this.process != null) {
            RIntegration.this.process.destroy();
            RIntegration.this.process = null;
            listener.fireClosedEvent();
        }
    }

    /**
	 * Debug helper
	 * @param exception
	 */
	private void debug(Exception exception) {
        if (DEBUG) {
            exception.printStackTrace();
        }
    }
	
	public void appendNewLines(int n)
	{
		for(int i=0; i<n;i++)
		this.buffer.append(NEWLINE);
	}
	
	/**
	 * Executes the "version" command to fetch the version output of R
	 * and store the current version+nickname in the variable version
	 * @param reader
	 */
	private void getVersion(Reader reader) 
	{
		 execute("version");
		 int character;
		 RBuffer versBuffer = new RBuffer(1000);
		 
         try {
        	 while ((character = reader.read()) != '>') {
			   //Verwirft das erst '>'
			 }
        	 
        	//Store the whole output of command "version"
			while ((character = reader.read()) != '>') {
			     
				 versBuffer.append((char) character);  
			 }
			String output = versBuffer.toString();
			
			//Extract version and nickname from the output
			String searchString = "R version ";
	    	int startVersion = output.indexOf(searchString);
	    	String part = output.substring(startVersion + searchString.length());
	    	String[] parts = part.split(" ");
	    	
	    	int startNickname = output.indexOf("nickname ");
	    	String nickname = output.substring(startNickname + "nickname ".length());
	    	nickname = nickname.trim();
	    	
	    	this.version = parts[0] + " (Nickname: " + nickname +")";
			
	    	//Send update to the listener to notify it
			listener.fireSetupUpdatedEvent();
			//add missing '>'
			buffer.append('>');
			
		} catch (IOException e) {
		}	
         
	}

	public String getVersion()
	{
		return version;	
	}
	
}