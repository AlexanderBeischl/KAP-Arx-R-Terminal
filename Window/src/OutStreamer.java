import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class OutStreamer extends Thread{
	
	OutputStream os;
	private Scanner sc;
	InputBox inputBox;
	Tab[] tabs;
	OutputStreamWriter osw;
	public BufferedWriter bw;
	String line;
	
	OutStreamer(OutputStream os, InputBox inputBox, Tab[] tabs){
		this.os = os;
		this.inputBox = inputBox;
		this.tabs = tabs;
	}
	  
	public void run() {
		try {
			sc = new Scanner(System.in);
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
	        line = null;
	        inputBox.setBufferedWriter(bw);
	        
	        while(inputBox.newInput) {
 				line = sc.nextLine();
 				bw.write(line);
 				bw.flush();
 				
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();  
		}
	}
	
	public BufferedWriter getBufferedWriter()
	{
		return this.bw;
	}
}
