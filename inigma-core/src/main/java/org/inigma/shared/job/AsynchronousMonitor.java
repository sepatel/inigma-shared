package org.inigma.shared.job;

import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsynchronousMonitor extends TimerTask {
    private static Logger logger = LoggerFactory.getLogger(AsynchronousMonitor.class);
    private Asynchronous async;
    private String label;
    private boolean verbose;

    public AsynchronousMonitor(Asynchronous instance) {
        this(instance, "AsyncPool");
    }

    public AsynchronousMonitor(Asynchronous instance, String label) {
        this.async = instance;
        this.label = label;
    }

    public boolean isVerbose() {
        return verbose;
    }

    @Override
    public void run() {
        int size = async.size();
        int finished = async.completed.getAndSet(0);
        StringBuilder message = new StringBuilder();
        if (size > 0) {
            if (!isVerbose()) {
                message.append(label).append(" Queue: Open ").append(size).append(", Completed ").append(finished);
            } else {
                message.append("Verbose ").append(label).append(" - ");
                message.append("Queue: Open ").append(size).append(", Completed ").append(finished);
                for (Entry<Method, ExecutionInfo> entry : async.info.entrySet()) {
                    ExecutionInfo value = entry.getValue();
                    int count = value.resetCount();
                    long duration = value.resetDuration();
                    if (count > 0) { // don't display noise even in verbose mode
                        message.append("\n\tMethod ").append(entry.getKey().getName());
                        message.append(" has ").append(count).append(" executions averaging ");
                        message.append(duration / count).append("ms each");
                    }
                }
            }
        }

        if (message.length() > 0) {
            logger.info(message.toString());
        }
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
