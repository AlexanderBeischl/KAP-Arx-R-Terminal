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
 */
public class RIntegration {

    /** Debug flag*/
    public static final boolean DEBUG = false;
    
	/** Process*/
	private Process process;
	/** Listener*/
    private RListener listener;
	
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
	    
	    // Create process
	    ProcessBuilder builder = new ProcessBuilder(path, "--vanilla", "--quiet", "--interactive")
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
}