package org.inigma.shared.job;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

class AsynchronousFutureTask<V> extends FutureTask<V> {
    private final Method method;
    private final Object[] args;

    public AsynchronousFutureTask(final Object instance, final Method method, final Object... args) {
        super(new Callable<V>() {
            @Override
            public V call() throws Exception {
                method.setAccessible(true);
                return (V) method.invoke(instance, args);
            }
        });
        this.method = method;
        this.args = args;
    }

    public Collection<Object> getArguments() {
        if (args == null) {
            return new LinkedList<Object>();
        }
        return Arrays.asList(args);
    }
    
    public Method getMethod() {
        return method;
    }
}
