import java.io.IOException;

public class RExecution 
{
	private RDetection rDetection;
	private ArxRTerminal terminal;
	private String pathToR;
	public Process rProc;
	private OutStreamer outputStream;
	
	RExecution(RDetection rDetection, ArxRTerminal terminal)
	{
		this.rDetection = rDetection;
		this.terminal = terminal;
		pathToR = addExecutionDir(rDetection.getPathToR());
		startR(pathToR);
		terminal.inputBox.setBufferedWriter(outputStream.getBufferedWriter());
		//ArxRTerminal.startR(manuellPath, terminal);
	}
	
	private String addExecutionDir(String path)
	{
		String os = rDetection.detectOS();

		if(os.startsWith("Mac OS")){
			return path +"/R.app/Contents/MacOS/R";
		}
		
		if(os.startsWith("Windows"))
		{
			return path;
		}
		   
		//ToDo
		if(os.startsWith("Linux")){
			return " ";
		}
		//execRPath = path;
		return path;	
	}
	
	private void startR(String path)
	{	
		//StdTab errTab = (StdTab) tabs[0];
		StdTab outTab = (StdTab) terminal.tabs[0];
		SetupTab setupTab = (SetupTab) terminal.tabs[1];
		
		
		
		ProcessBuilder processR = new ProcessBuilder("/usr/local/bin/r", "--vanilla");
		try {
			processR.redirectErrorStream(true);
			rProc = processR.start();
			Streamer errorStream = new Streamer(rProc.getErrorStream());
			Streamer inputStream = new Streamer(rProc.getInputStream());
			outputStream = new OutStreamer(rProc.getOutputStream(), terminal.inputBox, terminal.tabs);
			//  PrintStream outputPrint = new PrintStream(OutputStream);
			   
			errorStream.start();
		    outputStream.start();
		    inputStream.start();
			/*try {
			rProc.waitFor();
			} catch (InterruptedException e) {
				System.out.println("InterruptedException of Processbuilder.waitFor");
				e.printStackTrace();
			}
			*/
		} catch (IOException e) {
			System.out.println("IOException of ProcessBuilder.start()");
			e.printStackTrace();
		}
	  }
	
	public OutStreamer getOutStreamer()
	{
		return outputStream;
	}
}


