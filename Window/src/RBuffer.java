/**
 * A simple ring buffer for characters
 * 
 * @author Fabian Prasser
 */
public class RBuffer {

    /** The buffer*/
    private final char[] buffer;
    
    /** Start*/
    private int offset = 0;
    
    /** Length*/
    private int length = 0;
    
    /**
     * Creates a buffer of the given size
     * @param size
     */
    public RBuffer(int size) {
        this.buffer = new char[size];
    }
    
    /**
     * Appends the char
     * @param c
     */
    public void append(char c) {
        buffer[offset] = c;
        length = (length == buffer.length) ? length : length + 1;
        offset = (offset == buffer.length - 1) ? 0 : offset + 1;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (length < buffer.length) {
            builder.append(buffer, 0, length);
        } else {
            builder.append(buffer, offset, buffer.length - offset);
            builder.append(buffer, 0, offset);
        }
        return builder.toString();
    }
}
