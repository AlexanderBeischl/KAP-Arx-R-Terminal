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
        
        //Label label2 = new Label(root, SWT.BORDER);
        Text dirtext = new Text(root, SWT.BORDER);
        if(OS.getR() != null)
        {
        	//label2.setText(OS.getR());
        	dirtext.setText(OS.getR());
        }
        dirtext.setLayoutData(RLayout.createFillHorizontallyGridData(true));
        //label2.setLayoutData(RLayout.createFillHorizontallyGridData(true));
    }
    
    private void showRVersion()
    {
    	String path = OS.getR();
    	String dirs[] = path.split(File.separator);
    	int pos = Arrays.asList(dirs).indexOf("Versions");
    	
    	Label version = new Label(root, SWT.NONE);
        version.setText("Version: ");
        
        Text vers = new Text(root, SWT.BORDER);
        if(pos >= 0)
        {
        	vers.setText(dirs[++pos]);
        }
        vers.setLayoutData(RLayout.createFillHorizontallyGridData(true));
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
				RTerminal.endR();
				//RTerminal.showNewR();
				RTerminal.startManuellRIntegration(pathToR);
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
                         
                         RTerminal.endR();
                         System.out.println("Done!");
                         RTerminal.startManuellRIntegration(directory);
                     }
                 }
             }
         });
    }
    /*
    public void manageBufferSize()
    {
    	Label buffSize = new Label(root, SWT.NONE);
    	buffSize.setText("Size of the Output-Buffer:");
        
   	 	final Text bufferSize = new Text(root, SWT.BORDER);
   	 	bufferSize.setText("");
   	 	bufferSize.setLayoutData(RLayout.createFillHorizontallyGridData(true));
        
        // Listen for enter key
   	 	bufferSize.addTraverseListener(new TraverseListener() {
            @Override
            public void keyTraversed(TraverseEvent event) {
                if (event.detail == SWT.TRAVERSE_RETURN) {
                	
                    if (bufferSize.getText() != null && !bufferSize.getText().isEmpty()) {
                    	int size = Integer.parseInt(bufferSize.getText());
                    	if(size < 0)
                    	{
                    		bufferSize.setText(RTerminal.buffer);
                    	}
                    	else
                    	{
                    		
                    	}
                        
                    }
                }
            }
        });
    }*/
    
}
