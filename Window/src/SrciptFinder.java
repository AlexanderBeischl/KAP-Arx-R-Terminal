import java.io.BufferedWriter;
import java.io.IOException;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class SrciptFinder 
{
	Shell shell;
	BufferedWriter bw;
	
	SrciptFinder(Shell shell, BufferedWriter bw)
	{
		this.shell = shell;
		this.bw = bw;
		openScriptBrowser(shell);
		
	}
	
	//Browser to find Files or Scripts
	public void openScriptBrowser(Shell shell)
	{
		FileDialog dialogFile = new FileDialog(shell);		 
		String path = dialogFile.open();
		if(path == null)
		{
			return;
		}
		
		try {
			bw.write("source(\""+path+"\")");
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			System.out.println("Problem with sending the path to the BufferedWriter.");
			e.printStackTrace();
		}
	}

	public void setBufferedWriter(BufferedWriter buffWrit)
	 {
		bw = buffWrit;
	 }
}
