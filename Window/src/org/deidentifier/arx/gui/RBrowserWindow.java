package org.deidentifier.arx.gui;

/*
 * @author Alexander Beischl
 */

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


public class RBrowserWindow {
		Shell shell;
		
		//Browser to find Files or Scripts
		public static String openBrowser(Shell shell)
		{
			FileDialog dialogFile = new FileDialog(shell);		 
			String path = dialogFile.open();
			return path;
		}
	}
