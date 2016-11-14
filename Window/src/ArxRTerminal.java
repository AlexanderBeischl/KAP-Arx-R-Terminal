import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;


public class ArxRTerminal {
  	
  private static TabFolder tabFolder;
  public static Tab[] tabs;
  public static boolean manuellRSearch = true;
  public static Process rProc;
  public static InputBox inputBox;
  public static OutStreamer outputStream;
  
  //Erzeugt drei Tabs
  public ArxRTerminal(Composite parent, InputBox box) 
  {
	  //this.parent = (Shell)parent;
	  tabFolder = new TabFolder(parent, SWT.NULL);
	  createSpaceFillingWindow(tabFolder);
	
	  tabs = new Tab[] { //new StdTab(this, "StdErr"),
			  new StdTab(this, "StdOut"), new SetupTab(this, "Setup", tabFolder, (Shell)parent)};
    
	  for (int i = 0; i < tabs.length; i++) {
		TabItem item = new TabItem(tabFolder, SWT.NULL);
    	item.setText(tabs[i].getTabText());
    	item.setControl(tabs[i].createTabFolderPage(tabFolder));
    }	

  }
  
 
  public static void main(String[] args) {
	  final Display display = new Display();
	  final Shell shell = createShell(display); 
	  
	  inputBox = new InputBox(shell);
	  new ArxRTerminal(shell, inputBox);
	  
	  shell.setText("ARX-R-Terminal");
	  shell.addShellListener(new ShellAdapter() {
		  public void shellClosed(ShellEvent e) {
			  Shell[] shells = display.getShells();
			  for (int i = 0; i < shells.length; i++) {
				  if (shells[i] != shell)
					  shells[i].close();
			  }
		  	}
	  	});
	  
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }  
    display.dispose();
  }
  
  private static Shell createShell(Display display){
	    Shell shell = new Shell(display);
	    GridLayout gridLayout = new GridLayout();
	    gridLayout.numColumns = 1;
	    shell.setLayout(gridLayout);
	    return shell;
  }
  
  private void createSpaceFillingWindow(Composite window){
	  GridData gridData = new GridData();
	  gridData.horizontalAlignment = GridData.FILL;
	  gridData.verticalAlignment = GridData.FILL;
	  gridData.grabExcessHorizontalSpace = true;
	  gridData.grabExcessVerticalSpace = true;
	  window.setLayoutData(gridData);
  }
  
  public void dispose() {
	  tabFolder = null;
  }
}