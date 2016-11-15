//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.MouseAdapter;
//import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.layout.FillLayout;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.layout.RowLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.DirectoryDialog;
//import org.eclipse.swt.widgets.Group;
//import org.eclipse.swt.widgets.List;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.TabFolder;
//
//class SetupTab extends Tab{
//	
//	ArxRTerminal terminal;
//	
//	private FillLayout fillingLayout;
//	private TabFolder tabFolder; //Reference to the TabFolder from ArxTerminal
//	private Shell shell;
//	private List list;
//	static 	Button button;
//	private OS rDetection;
//	private RIntegration rExecutor;
//	private InputBox inputBox;
//	
//	static boolean foundR;
//	static String pathToR; //The directory of the R-Application is stored here
//	//String execRPath;
//	
//	//These Arrays contain the different locations R is usually installed
//	private String[] locationsOfRMac = {"/Applications/R.app"};
//	private String[] locationsOfRLinux = {"/usr/lib/R/bin/exec"};
//	
//	/*We are using another format here, because the path-structure of R in Windows depends on the Verison of R
//	 Therefore we use two fields per path:
//	 1. Path until the directory of the version
//	 2. Childpath of the directory
//	*/
//	private String[] locationsOfRWindows = {"C:\\Program Files\\R\\*\\bin\\x64\\Rgui.exe"};
//	
//	//The constructor already starts find(..), this method checks if R is installed
//	SetupTab(ArxRTerminal terminal, String tabName, TabFolder tabFolder, Shell shell)
//	{
//		super(terminal, tabName);
//		this.terminal = terminal;
//		this.tabFolder = tabFolder;
//		this.shell = shell;
//		rDetection = new OS();
//	}
//	
//	void createLayout() 
//	{
//		fillingLayout = new FillLayout();
//		layoutComposite.setLayout(fillingLayout);
//	}
//
//	/**
//	* Gets the text for the tab folder item.
//	*/
//	String getTabText() 
//	{
//		return this.tabName;
//	}
//	
//	void createLayoutGroup()
//	{
//	  	layoutGroup = new Group(sash, SWT.None); 
//	  	GridLayout output = new GridLayout();
//	    output.numColumns = 1;
//	    layoutGroup.setLayout(output);
//	    createLayoutComposite();
//	    createTabContent();
//	}
//	
//	void createTabContent()
//	{
//		list = new List(layoutComposite, SWT.V_SCROLL);
//		button = new Button(layoutComposite, SWT.PUSH);
//		button.setText("Search the directory of R manuelly");
//		button.addMouseListener(new MouseAdapter()
//		{
//			public void mouseUp(MouseEvent e)
//			{
//				showMaunellSearch(shell);
//			}
//		});
//		
//		if(this.rDetection.getRStatus())
//		{
//			rExecutor = new RIntegration(rDetection, terminal);
//		}
//		
//		printRStatus();
//	}
//	
//
//	private void printRStatus()
//	{
//		list.removeAll();
//		if(rDetection.getRStatus()){
//			list.add("R has been found on your System!");
//			list.add("The Operating System is " + rDetection.detectOS());
//			list.add("R was found in: "+ rDetection.getPathToR());
//			//TODO mehr Infos
//		} else {
//			list.add("R counldn't be found on your System!");
//			list.add("If you have R installed, please browse the location in the window above.");
//			list.add("If R is not installed on your system, please download it from:");
//			list.add("https://www.r-project.org");
//			
//		}
//		tabFolder.setSelection(1); //Shows the third tap to display the user if R is installed
//	  }
//	
//	 public boolean showMaunellSearch(Shell shell)
//	 {
//		 String manuellPath = openDirectoryBrowser(shell);
//		 if(manuellPath == null)
//		 {
//			 return false;
//		 }
//		
//
//		 boolean success = rDetection.manuellfindR(manuellPath);
//		 if(success)
//		 {
//			 rExecutor = new RIntegration(rDetection, terminal);
//		 }
//		 printRStatus();
//		 return success;
//		
//	  }
//	  
//	 
//	 /*@Fabian: 
//	  * soll die Methode lieber auch in die rDetection oder 
//	  * passt es in dieser Klasse, ist ja Teil der GUI?
//	  */
//	  private String openDirectoryBrowser(Shell shell)
//	  {
//		  String srcDir = rDetection.getDirForWindow();
//		  
//		  DirectoryDialog dialog = new DirectoryDialog(shell);
//		  dialog.setFilterPath(srcDir); // Windows specific
//		  String path = dialog.open();
//		  //System.out.println("Dir: " + path);
//		  return path;
//	  }
//	  
//}