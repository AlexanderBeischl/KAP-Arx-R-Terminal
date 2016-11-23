package org.deidentifier.arx.r;
import java.io.BufferedWriter;
import java.io.IOException;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


public class RFinder {
		Shell shell;
		BufferedWriter bw;
		
		//Browser to find Files or Scripts
		 private String openDirectoryBrowser(Shell shell)
		  {
			  //String srcDir = rDetection.getDirForWindow();
			  
			  DirectoryDialog dialog = new DirectoryDialog(shell);
			  //dialog.setFilterPath(srcDir); // Windows specific
			  String path = dialog.open();
			  //System.out.println("Dir: " + path);
			  return path;
		  }
	}
