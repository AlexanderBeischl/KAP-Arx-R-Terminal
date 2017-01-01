package org.deidentifier.arx.gui;
import org.deidentifier.arx.r.OS;
import org.deidentifier.arx.r.RBuffer;
import org.deidentifier.arx.r.RIntegration;
import org.deidentifier.arx.r.RListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * R terminal
 * 
 * @author Fabian Prasser
 * @author Alexander Beischl
 */
public class RTerminal {

    /** Buffer size*/
    private static final int BUFFER_SIZE = 10000;
    /** Event delay*/
    private static final int EVENT_DELAY = 10;
    
    static RTerminalTab tabTerminal;
    static RSetupTab tabSetup;
    static RBuffer buffer;
    static RListener listener;
    /** store the currently used path of the R-executive*/
    static String rPath;
    
    static RIntegration r;
    
    /**
     * Creates a new shell within the given control
     * @param shell
     */
    public RTerminal(Composite parent) {

        // Folder
        TabFolder folder = new TabFolder(parent, SWT.NULL);

        // Tabs
        tabTerminal = new RTerminalTab(folder);
        tabSetup = new RSetupTab(folder);
        
        // Item 1
        TabItem item1 = new TabItem(folder, SWT.NULL);
        item1.setText("Terminal");
        item1.setControl(tabTerminal.getControl());

        // Item 2
        TabItem item2 = new TabItem(folder, SWT.NULL);
        item2.setText("Setup");
        item2.setControl(tabSetup.getControl());
        
        // R integration
        buffer = new RBuffer(BUFFER_SIZE);
        listener = new RListener(EVENT_DELAY) {

            @Override
            public void bufferUpdated() {
                tabTerminal.setOutput(buffer.toString());
            }

            @Override
            public void closed() {
                // TODO: Handle
            }
            
            //Update the Std-Tab after a new R was started
            @Override
            public void setupUpdate() {
                tabSetup.update();
            }
        };
        
        startRIntegration(OS.getR());
    }
    
    /**
     * This method is used for starting an R-Exec from the 
     * default locations
     */
    public static void startRIntegration(final String path)
    {
        if(path != null)
        {
        	r = new RIntegration(path, buffer, listener);
        	// Redirect user input
        	tabTerminal.setCommandListener(new RCommandListener() {
        		@Override
        		public void command(String command) {
        			r.execute(command);
        		}
        	});
        	//Store the location of the currently used R-Exec
        	rPath = path;
        	tabTerminal.enableTab();
        }
    }
    
    /**
     * Before starting a new R-Exec the old one is shut down and 
     * a Dialog in the RTerminalTab is shown to give the user feedback.
     * Until a new R is running the Tab is disabled to prohibit any input.
     */
    public static void endR()
    {
    	if(r != null)
    	{
    		r.shutdown();
    		showEndDialog();
    	}
    	tabTerminal.disableTab();
    }
    
    /**
     * When the new R-Exec is started, a Dialog is printed in the RTerminalTab 
     * to feedback the user he can use R now. 
     */
    public static void showNewR()
    {
    	r.appendNewLines(2);
    	char[] text = "New R was started!".toCharArray();
    	buffer.append(text);
    	r.appendNewLines(3);	
    }
    
    /**
     * Prints the endDialog in the RTabTerminal after R was shutdown.
     */
    public static void showEndDialog()
    {
    	r.appendNewLines(2);
    	char[] text = "R was shut down!".toCharArray();
    	buffer.append(text);
    }
    
    /**
     * This method manages starting a R-Exec, which is chosen manuelly by the user.
     * Therefore, it shuts down the current R-Process at first.
     * Then it checks if the chosen file is a R-Exec
     * On success, it creates a new RIntegration to start R, enables the RTerminalTab,
     * print the startup dialog (showNewR()) and stores the current path.
     * @param path
     * @return Status of the R-process: true -> running, false -> no valid R-exec
     */
    public static boolean startManuellRIntegration(final String path)
    {      	
        if(path == null)
        {
        	return true;
        }
        		
        if(OS.isR_Exec(path))
        {
        	endR();
        	
        	r = new RIntegration(path, buffer, listener);
        	// Redirect user input
        	tabTerminal.setCommandListener(new RCommandListener() {
        		@Override
        		public void command(String command) {
        			r.execute(command);
        		}
        	});
        	
        	if(r.isAlive())
        	{
        		rPath = path;
        		showNewR();
        		tabTerminal.enableTab();
        	}
        	return r.isAlive();
        }
        return false;
    }
   
}