/*
 */

package com.chaudhuri.ooliteaddonscanner2;

import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A thread factory emitting threads with configurable names.
 * This makes identifying the thread's purpose in logs or debugging a lot easier.
 *
 * @author hiran
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final Logger log = LogManager.getLogger();
    
    private String name;
    private int count;
    
    /**
     * Creates a new NamedThreadFactory.
     * 
     * @param name all threads from this factory will contain this name fragment
     */
    public NamedThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        count++;
        return new Thread(r, name + "-" + count);
    }

}
