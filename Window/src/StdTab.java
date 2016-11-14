import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

class StdTab extends Tab {	
  FillLayout fillingLayout;
  List list;
  OutputStream stream;
 
  
  StdTab(ArxRTerminal instance, String tabName) {
    super(instance, tabName);
  }
 
  void createLayout() {
    fillingLayout = new FillLayout();
    layoutComposite.setLayout(fillingLayout);
  }


  String getTabText() {
    return this.tabName;
  }
  
  void createLayoutGroup() {
	    layoutGroup = new Group(sash, SWT.None);
	    layoutGroup.setText("Ausgabe");
	    GridLayout output = new GridLayout();
	    output.numColumns = 1;
	    layoutGroup.setLayout(output);
	    createLayoutComposite();
	    createTabContent();
	    redirectSystemStreams();
  }
  
/*  public void getTabContent(Thread stream){
	  String x = (OutStreamer) stream.start();
	  
  }
  */
  public void createTabContent()
  {
	  // Create a List with a vertical scroll bar
	   list = new List(layoutComposite, SWT.V_SCROLL);
  } 
  
  public void updateTab(final String text){
	  Display.getDefault().asyncExec(new Runnable(){
		  public void run() {
			  list.add(text);
			 
		  }
	  });
  }
  
  private void redirectSystemStreams() {
	  	
	     OutputStream out = new OutputStream() {
	      @Override
	      public void write(int b) throws IOException {
	        updateTab(String.valueOf((char) b));
	      }
	 
	      @Override
	      public void write(byte[] b, int off, int len) throws IOException {
	        updateTab(new String(b, off, len));
	      }
	 
	      @Override
	      public void write(byte[] b) throws IOException {
	        write(b, 0, b.length);
	      }
	    };
	 
	    System.setOut(new PrintStream(out, true));
	    System.setErr(new PrintStream(out, true));
	  }
}