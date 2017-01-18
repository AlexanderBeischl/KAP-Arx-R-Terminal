package ExampleWithoutGUI;
import java.util.Scanner;
import org.deidentifier.arx.r.OS;
import org.deidentifier.arx.r.RBuffer;
import org.deidentifier.arx.r.RIntegration;
import org.deidentifier.arx.r.RListener;


public class Example {

	    public static void main(String[] args) {
	        
	        
        RBuffer inputBuffer = new RBuffer(100);
        inputBuffer.append("1+2+3+4+5".toCharArray());
        System.out.println("'"+inputBuffer.toString()+"'");
        
        final RBuffer outputBuffer = new RBuffer(500);
        final RListener listener = new RListener(10) {

            @Override public void bufferUpdated() {
            	//Prints the output in the Console
                System.out.println("OutputBuffer:");
                System.out.println(outputBuffer.toString());
            }

            @Override public void closed() {
                System.out.println("R was shut down!");
            }

			@Override
			public void setupUpdate() {
				 System.out.println("Version of R has been updated");
					
				}
	        };
	        final RIntegration rProcess = new RIntegration(OS.getR(), outputBuffer, listener);
	         
	        //Input can be typed in by using the Console
	        Scanner scanner = new Scanner(System.in);
	        String line = scanner.nextLine();
	        while (line != null) {
	            rProcess.execute(line);
	            line = scanner.nextLine();
	        }
	        scanner.close();
	    }
}
