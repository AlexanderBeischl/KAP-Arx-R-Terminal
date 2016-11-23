package org.deidentifier.arx.gui;

import org.deidentifier.arx.r.OS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;

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
        root.setLayout(RLayout.createGridLayout(2));
        
        //To open the manuell Search...
        manuellSearch = new Button(root,SWT.PUSH);
        manuellSearch.setText("Select R-File");
        manuellSearch.setLayoutData(RLayout.createFillHorizontallyGridData(true));
        
        //Listener for the manuellSearch
        
        Label label1 = new Label(root, SWT.NONE);
        label1.setText("Location: ");
        
        Label label2 = new Label(root, SWT.BORDER);
        if(OS.getR() != null)
        {
        	label2.setText(OS.getR());
        }
        label2.setLayoutData(RLayout.createFillHorizontallyGridData(true));
    }

    /**
     * Returns the control
     * @return
     */
    public Control getControl() {
        return this.root;
    }
}
