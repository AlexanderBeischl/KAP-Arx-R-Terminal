package org.deidentifier.arx.r;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


public class RBrowserWindow {
		Shell shell;
		
		//Browser to find Files or Scripts
		public static String openBrowser(Shell shell)
		{
			FileDialog dialogFile = new FileDialog(shell);		 
			String path = dialogFile.open();
			System.out.print(path);
			return path;
		}
	}
