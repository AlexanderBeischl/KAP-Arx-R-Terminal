package org.deidentifier.arx.gui;
/*
import java.io.File;
import java.util.Arrays;
*/
import org.deidentifier.arx.r.OS;
import org.deidentifier.arx.r.RBrowserWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

/**
 * Setup tab
 * 
 * @author Fabian Prasser
 * @author Alexander Beischl
 * @author Thuy Tran
 */
public class RSetupTab {
    
    /** Widget*/
    private Composite root;
    
    private Button manuellSearch;
    
    public Text dirtext;
    
    public Text vers;
    
    /** Listener */
    
    /**
     * Creates a new instance
     * @param folder
     */
    public RSetupTab(TabFolder folder) 
    {
        // Root
        root = new Composite(folder, SWT.NONE);
        root.setLayout(RLayout.createGridLayout(1));
        //Create the GUI-Elements of the SetupTab
        showOS();
        showRLocation();
        showRVersion();
       
        createManuellSearchWindow();
        createDirSearchLine();        
    }

    /**
     * Returns the control
     * @return
     */
    public Control getControl() {
        return this.root;
    }
    
    
    /**
     * Creates a Label and Textbox showing the OS
     */
    private void showOS()
    {
    	 Label os = new Label(root, SWT.NONE);
         os.setText("Operating System:");
         Text osText = new Text(root, SWT.BORDER);
         osText.setText(OS.printOS());
         osText.setLayoutData(RLayout.createFillHorizontallyGridData(true));
         osText.setEditable(false);
    }
    
    /**
     * Creates a Label and Textbox for the Location of the R-Version
     * It checks if R was found
     * If not it prints the Text seen above
     * If R was found it will be added by the method update(), which is triggered by 
     * starting the R-process
     */
    private void showRLocation()
    {
    	Label location = new Label(root, SWT.NONE);
        location.setText("Location: ");
        
        dirtext = new Text(root, SWT.BORDER);
        
        if(OS.getR() == null)
        {
    		dirtext.setText("No falid R-exec found!");
    	}		
        dirtext.setLayoutData(RLayout.createFillHorizontallyGridData(true));
        dirtext.setEditable(false);
    }
    
    /**
     * Creates a Label and Textbox showing the Version
     * After the R-Process started, the Text vers is updated by update()
     */
    private void showRVersion()
    {
    	Label version = new Label(root, SWT.NONE);
        version.setText("Version: ");
        
        vers = new Text(root, SWT.BORDER);
        if(OS.getR() == null)
        {
        	vers.setText("No falid R-Version selected!");
        }
        vers.setLayoutData(RLayout.createFillHorizontallyGridData(true));
        vers.setEditable(false);
    }
   
    /**
     * Creates a Button for the manuell search of R via a Window
     */
    private void createManuellSearchWindow()
    {
        Label searchLabel = new Label(root, SWT.NONE);
        searchLabel.setText("Select the R-Exec manuelly: ");
        manuellSearch = new Button(root,SWT.PUSH);
        manuellSearch.setText("Select R-File");
        manuellSearch.setLayoutData(RLayout.createFillHorizontallyGridData(true));
        
        //Listener for the manuellSearch
        manuellSearch.addMouseListener(new MouseAdapter()
		{
			public void mouseUp(MouseEvent e)
			{
				String pathToR = RBrowserWindow.openBrowser(new Shell());
				
				updateSetup(pathToR);
				
			}
		});
    }
    
    /**
     * Creates a textline for the manuell search of R via typing in the path to the executive
     */
    private void createDirSearchLine()
    {
    	 Label manuellDir = new Label(root, SWT.NONE);
    	 manuellDir.setText("Manuelly select the R-exec by typing in its location:");
         
    	 final Text manuellDirText = new Text(root, SWT.BORDER);
         manuellDirText.setText("");
         manuellDirText.setLayoutData(RLayout.createFillHorizontallyGridData(true));
         
      // Listen for enter key
         manuellDirText.addTraverseListener(new TraverseListener() {
             @Override
             public void keyTraversed(TraverseEvent event) {
                 if (event.detail == SWT.TRAVERSE_RETURN) {
                     if (manuellDirText.getText() != null && !manuellDirText.getText().isEmpty()) {
                         String directory = manuellDirText.getText();
                         manuellDirText.setText("");
                         
                         updateSetup(directory);
                     }
                 }
             }
         });
    }
     
    /**
     * This method is called by the two manuell-searches 
     * It takes the chosen path to the R-exec, calls the 
     * startManuellRIntegration(path) Method in RTerminal
     * and checks if it was successful.
     * If it was, the Std-Tab is updated by update()
     * otherwise updateSetup(path) shows the attempt failed
     * and updates the StdTab
     * @param path
     */
    public void updateSetup(String path)
    {
    	if(!RTerminal.startManuellRIntegration(path))
    	{
    		dirtext.setText("No valid R-exec found!");
    		vers.setText("No valid R-Version selected!");
    	}
    }
    
    /**
     * This method is is triggered by the RListener in RTerminal.
     * It's called after each successful start of R in RIntegration
     * to update the shown Version and Directory in the Std-Tab
     */
    public void update()
    {
    	this.dirtext.setText(RTerminal.rPath);
    	this.vers.setText(RTerminal.r.getVersion());
    }
    
}
