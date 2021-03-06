package org.deidentifier.arx.r;
import org.eclipse.swt.widgets.Display;

/**
 * A listener for the R process. If a display is present, all notification 
 * events will come from the SWT event dispatch thread.
 * 
 * @author Fabian Prasser
 */
public abstract class RListener {

    /** Delay in milliseconds */
    private final int delay;

    /** Should an event be fired */
    private boolean   fire = false;
    private boolean   update = false;

    /** Timestamp of the last event */
    private long      time = 0;

    /**
     * Creates a new instance
     * 
     * @param ticksPerSecond Maximal number of events per second
     */
    public RListener(int ticksPerSecond) {
        
        // Calculate delay
        this.delay = (int)Math.round(1000d / (double)ticksPerSecond);
        
        // Create repeating task
        repeat(delay, new Runnable() {
            @Override
            public void run() {
                if (fire && System.currentTimeMillis() > time) {
                    bufferUpdated();
                    fire = false;
                }
                if (update && System.currentTimeMillis() > time) {
                	setupUpdate();
                    update = false;
                }
            }
        });
    }

    /**
     * Repeatedly executes the runnable
     * @param delay
     * @param runnable
     */
    private void repeat(final int delay, final Runnable runnable) {

        final Display display = Display.getCurrent();
        if (display != null) {
            display.timerExec(delay, new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                    display.timerExec(delay, this);
                }
            });
        } else {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            runnable.run();
                            Thread.sleep(delay);
                        }
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        }
    }

    /** 
     * Implement this to get notified when the buffer is updated
     */
    public abstract void bufferUpdated();

    /**
     * Implement this to get notified when the R process dies
     */
    public abstract void closed();
    
    /**
     * Implement this to get notified when the the version was loaded
     */
    public abstract void setupUpdate();

    /**
     * Internal method to fire an event
     */
    void fireBufferUpdatedEvent() {
        this.fire = true;
        this.time = System.currentTimeMillis() + delay;
    }
    
    void fireSetupUpdatedEvent() {
        this.update = true;
        this.time = System.currentTimeMillis() + delay;
    }

    
    /**
     * Internal method to fire an event
     */
    void fireClosedEvent() {
        final Display display = Display.getCurrent();
        if (display != null) {
            display.asyncExec(new Runnable() {
                @Override
                public void run() {
                    closed();
                }
            });
        } else {
            closed();
        }
    }
}