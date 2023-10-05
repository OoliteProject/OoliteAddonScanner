/*
 */

package com.chaudhuri.ooliteaddonscanner2;

import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final Logger log = LogManager.getLogger();
    
    private String name;
    private int count;
    
    public NamedThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        count++;
        return new Thread(r, name + "-" + count);
    }

}
