package org.inigma.shared.job;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

class ExecutionInfo {
    private AtomicInteger count = new AtomicInteger();
    private AtomicLong duration = new AtomicLong();

    public void addDuration(long duration) {
        this.duration.addAndGet(duration);
    }

    public void incrementCount() {
        this.count.incrementAndGet();
    }

    public int resetCount() {
        return count.getAndSet(0);
    }

    public long resetDuration() {
        return duration.getAndSet(0);
    }
}
