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
        };
        
        startRIntegration(OS.getR());
    }
    
    public static void startRIntegration(final String path)
    {
    	//TODO
        // Start integration
       
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
        	
        	tabTerminal.enableTab();
        }
    }
    
    public static void endR()
    {
    	r.shutdown();
    	showEndDialog();
    	tabTerminal.disableTab();
    }
    
    public static void showNewR()
    {
    	r.appendNewLines(2);
    	char[] text = "New R was started!".toCharArray();
    	buffer.append(text);
    	r.appendNewLines(3);
    }
    
    public static void showEndDialog()
    {
    	r.appendNewLines(2);
    	char[] text = "R was shut down!".toCharArray();
    	buffer.append(text);
    }
    
    public static boolean startManuellRIntegration(final String path)
    {  
    	endR();
    	
        if(path != null && OS.isR_Exec(path))
        {
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
        		showNewR();
        		tabTerminal.enableTab();
        	}
        	
        	return r.isAlive();
        }
        return false;
    }
   
}