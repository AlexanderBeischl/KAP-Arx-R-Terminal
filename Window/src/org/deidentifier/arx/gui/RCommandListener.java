package org.deidentifier.arx.gui;

/**
 * A simple listener
 * 
 * @author Fabian Prasser
 */
public interface RCommandListener {
    
    /** 
     * Implement this to listen for commands
     * 
     * @param command
     */
    public abstract void command(String command);
}
