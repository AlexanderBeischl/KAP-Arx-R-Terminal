package org.deidentifier.arx.gui;

import java.nio.Buffer;

import org.deidentifier.arx.r.RBrowserWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

/**
 * Terminal tab
 * 
 * @author Fabian Prasser
 * @author Alexander Beischl
 * @author Thuy Tran
 */
public class RTerminalTab {

    /** Widget */
    private final Combo       input;
    /** Widget */
    private final StyledText output;
    /** Widget */
    private final Composite  root;
    /** Listener */
    private RCommandListener listener;
    /** Button for searching Scripts*/
    private final Button scriptButton;

    /**
     * Creates a new instance
     * @param folder
     */
    public RTerminalTab(TabFolder folder) {

        // Root
        root = new Composite(folder, SWT.NONE);
        root.setLayout(RLayout.createGridLayout(1));
        
        Composite topline = new Composite(root, SWT.NONE);
        topline.setLayout(RLayout.createGridLayout(2));
        topline.setLayoutData(RLayout.createFillHorizontallyGridData(true));
        // User input
        input = new Combo(topline, SWT.DROP_DOWN);
        input.setLayoutData(RLayout.createFillHorizontallyGridData(false));
        
        //Dropdown
//        final Integer itemPos = 9;
        // final String[] items = {"Das", "ist", "ein", "Test", "5", "6", "7", "8", "9", "10"};
        final String[] items = {};
        input.setItems(items);
        input.setVisibleItemCount(10);
        
        // Listen for enter key
        input.addTraverseListener(new TraverseListener() {
            @Override
            public void keyTraversed(TraverseEvent event) {
            	
                if (event.detail == SWT.TRAVERSE_RETURN) {
                    if (input.getText() != null && !input.getText().isEmpty()) {
                        String command = input.getText();
                        input.setText("");
                        String[] tmp = input.getItems();
                        int newLength = tmp.length >= 10 ? 10 : tmp.length + 1;
                        String[] commArray = new String[newLength];
                        commArray[0] = command;
                        if(newLength > 1)
                        	System.arraycopy(tmp, 0, commArray, 1, newLength-1);
                        //input.add(command);
                        input.setItems(commArray);
                        if (listener != null) {
                            listener.command(command);  
                        }
                    }
                }
            }
        });
        
        scriptButton = new Button(topline, SWT.PUSH);
        scriptButton.setText("Select Script");
        
        //Listener for the mouseButton, opens a File-Browser
        scriptButton.addMouseListener(new MouseAdapter()
		{
			//Writes the text 
			public void mouseUp(MouseEvent e)
			{
				String path = RBrowserWindow.openBrowser(new Shell());
				if (path != null && listener != null) {
					listener.command("source(\""+path+"\")");
                }
			}
		});
        
        // User output
        output = new StyledText(root, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        output.setLayoutData(RLayout.createFillGridData());
        output.setEditable(false);
       
        //Disables the usage of the tab, is enabled after R was found
        disableTab();
    }

    /**
     * Returns the control
     * @return
     */
    public Control getControl() {
        return this.root;
    }

    /**
     * Sets the content of the buffer
     * @param text
     */
    public void setOutput(String text) {
        this.root.setRedraw(false);
        this.output.setText(text);
        this.output.setSelection(text.length());
        this.root.setRedraw(true);
    }
    
    /**
     * Sets the listener
     * @param listener
     */
    public void setCommandListener(RCommandListener listener) {
        this.listener = listener;
    }
    
    public void disableTab()
    {
    	root.setEnabled(false);
    }
    
    public void enableTab()
    {
    	root.setEnabled(true);
    }
}
