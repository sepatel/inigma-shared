package org.inigma.shared.job;

import java.util.concurrent.Future;

/**
 * @author <a href='mailto:sejal@sejal.org">Sejal Patel</a>
 *         Date: 2/28/13 9:22 PM
 */
public interface AsynchronousCallback<T> {
    void onCompletion(T value, Future<T> future);

    void onException(Exception e, Future<T> future);
}
