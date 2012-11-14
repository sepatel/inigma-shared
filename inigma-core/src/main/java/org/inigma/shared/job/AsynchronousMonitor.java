package org.inigma.shared.job;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsynchronousMonitor extends TimerTask {
    private static Logger logger = LoggerFactory.getLogger(AsynchronousMonitor.class);
    private Asynchronous async;
    private String label;

    public AsynchronousMonitor(Asynchronous instance) {
        this(instance, "AsyncPool");
    }

    public AsynchronousMonitor(Asynchronous instance, String label) {
        this.async = instance;
        this.label = label;
    }

    @Override
    public void run() {
        int size = async.size();
        int finished = async.completed.getAndSet(0);
        if (size > 0) {
            logger.info("{} Queue: Open {}, Completed {}", new Object[] { label, size, finished });
        }
    }
}
