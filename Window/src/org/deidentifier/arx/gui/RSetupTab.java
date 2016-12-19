package org.deidentifier.arx.gui;

import java.io.File;
import java.util.Arrays;

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
    
    private Text dirtext;
    
    private Text vers;
    
    /**
     * Creates a new instance
     * @param folder
     */
    public RSetupTab(TabFolder folder) {

    	
        // Root
        root = new Composite(folder, SWT.NONE);
        root.setLayout(RLayout.createGridLayout(1));
        
        showOS();
        showRLocation();
        showRVersion();
        
        //TODO add manuell Search via directory
        createManuellSearch();
        createDirSearchLine();
        
    }

    /**
     * Returns the control
     * @return
     */
    public Control getControl() {
        return this.root;
    }
    
    private void showOS()
    {
    	 Label os = new Label(root, SWT.NONE);
         os.setText("Operating System:");
         Text osText = new Text(root, SWT.BORDER);
         osText.setText(OS.printOS());
         osText.setLayoutData(RLayout.createFillHorizontallyGridData(true));
         osText.setEditable(false);
    }
    
    private void showRLocation()
    {
    	Label location = new Label(root, SWT.NONE);
        location.setText("Location: ");
        
        dirtext = new Text(root, SWT.BORDER);
        
        if(OS.getR() != null)
        {
    		dirtext.setText(OS.getR());
        }
    	else
    	{
    		dirtext.setText("No falid R-exec found!");
    	}		
        
        dirtext.setLayoutData(RLayout.createFillHorizontallyGridData(true));
    }
    
    private void showRVersion()
    {
    	Label version = new Label(root, SWT.NONE);
        version.setText("Version: ");
        
        vers = new Text(root, SWT.BORDER);
        
        getRVersion(OS.getR());
        
        vers.setLayoutData(RLayout.createFillHorizontallyGridData(true));
    }
    
    private void getRVersion(String path)
    {
    	String dirs[] = path.split(File.separator);
    	int pos = Arrays.asList(dirs).indexOf("Versions");
    	
    	if(pos >= 0)
        {
        	vers.setText(dirs[++pos]);
        }
    	else
    	{
    		vers.setText("No falid R-Version selected!");
    	}
    		
    }
    
    private void createManuellSearch()
    {
    	//To open the manuell Search...
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
    
    private void updateSetup(String path)
    {
    	boolean success = RTerminal.startManuellRIntegration(path);
    	
    	if(success)
    	{
    		dirtext.setText(path);
    		getRVersion(path);
    	}
    	else
    	{
    		dirtext.setText("No falid R-exec found!");
    		vers.setText("No falid R-Version selected!");
    	}
    }
}
