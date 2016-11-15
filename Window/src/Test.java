import java.util.Scanner;


public class Test {

    public static void main(String[] args) {
        
        // Test 1: environment
        System.out.println(OS.getR());
        
        // Test 2: buffer
        RBuffer buffer1 = new RBuffer(4);
        System.out.println("'"+buffer1.toString()+"'");
        buffer1.append('0');
        System.out.println("'"+buffer1.toString()+"'");
        buffer1.append('1');
        System.out.println("'"+buffer1.toString()+"'");
        buffer1.append('2');
        System.out.println("'"+buffer1.toString()+"'");
        buffer1.append('3');
        System.out.println("'"+buffer1.toString()+"'");
        buffer1.append('4');
        System.out.println("'"+buffer1.toString()+"'");
        buffer1.append('5');
        System.out.println("'"+buffer1.toString()+"'");
        buffer1.append('6');
        System.out.println("'"+buffer1.toString()+"'");
        buffer1.append('7');
        System.out.println("'"+buffer1.toString()+"'");
        buffer1.append('8');
        System.out.println("'"+buffer1.toString()+"'");
        buffer1.append('9');
        System.out.println("'"+buffer1.toString()+"'");
        
        // Test 3: R integration
        final RBuffer buffer = new RBuffer(100);
        final RListener listener = new RListener(10) {

            @Override public void bufferUpdated() {
                System.out.println("Buffer:");
                System.out.println(buffer.toString());
            }

            @Override public void closed() {
                System.out.println("R has terminated");
            }
        };
        final RIntegration r = new RIntegration(OS.getR(),
                                                buffer,
                                                listener);
        
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        while (line != null) {
            System.out.println("Execute: " + line);
            System.out.println("Alive: " + r.isAlive());
            r.execute(line);
            line = scanner.nextLine();
        }
        scanner.close();
    }
}
