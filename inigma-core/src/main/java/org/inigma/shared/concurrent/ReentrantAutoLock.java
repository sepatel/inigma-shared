package org.inigma.shared.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:grant.overby@gmail.com">Grant Overby</a>
 * @since 3/20/14 11:23 PM
 */
public class ReentrantAutoLock extends ReentrantLock implements AutoCloseable {
    public static final class LockNotAvailableException extends Exception {
        private LockNotAvailableException() {
        }
    }

    public ReentrantAutoLock() {
    }

    public ReentrantAutoLock(boolean fair) {
        super(fair);
    }

    public ReentrantAutoLock autoLock() {
        lock();
        return this;
    }

    public ReentrantAutoLock autoTryLock() throws LockNotAvailableException {
        if (!tryLock()) {
            throw new LockNotAvailableException();
        }
        return this;
    }

    @Override
    public void close() {
        unlock();
    }
}

