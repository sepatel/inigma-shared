package org.inigma.shared.job;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AsynchronousMonitor extends TimerTask {
    private static Logger logger = LoggerFactory.getLogger(AsynchronousMonitor.class);
    private Asynchronous async;

    AsynchronousMonitor(Asynchronous instance) {
        this.async = instance;
    }

    @Override
    public void run() {
        int size = async.size();
        int finished = async.completed.getAndSet(0);
        if (size > 0) {
            logger.info("{} Queue: Open {}, Completed {}", new Object[] { async.getLabel(), size, finished });
        }
    }
}
