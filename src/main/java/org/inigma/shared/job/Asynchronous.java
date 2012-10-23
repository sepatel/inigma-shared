package org.inigma.shared.job;

import java.lang.reflect.Method;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ClassUtils;

public class Asynchronous {
    private static Logger logger = LoggerFactory.getLogger(Asynchronous.class);

    private BlockingDeque<FutureTask<?>> workQueue = new LinkedBlockingDeque<FutureTask<?>>();
    private ThreadPoolTaskExecutor executor;
    private long monitorInterval = 10000;
    private String label;

    public Asynchronous() {
        this(5);
    }

    public Asynchronous(int workers) {
        this.label = "WorkPool";
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(workers + 1);
        executor.setDaemon(true);
        initialize();
    }

    @PreDestroy
    public void close() {
        executor.shutdown();
    }

    public String getLabel() {
        return label;
    }

    public void initialize() {
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                logger.warn("{}'s runnable {} was rejected", label, r);
            }
        });
        executor.initialize();
        for (int i = 1; i < executor.getCorePoolSize(); i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    FutureTask<?> task = null;
                    while (true) {
                        try {
                            task = workQueue.take();
                            task.run();
                            task.get(); // this is to force exception captured to be thrown. maybe better way???
                        } catch (InterruptedException e) {
                            logger.warn("{} was interrupted with queue size at {}", label, workQueue.size());
                        } catch (Throwable e) {
                            logger.error("Unhandled Exception in {} on item {}", label, task, e);
                        }
                    }
                }
            });
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    monitor();
                    try {
                        Thread.sleep(monitorInterval);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
    }

    public <T> Future<T> invoke(final Object instance, final Method method, final Object... args) {
        FutureTask<T> task = new FutureTask<T>(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return (T) method.invoke(instance, args);
            }
        });
        try {
            workQueue.put(task);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    public <T> Future<T> invoke(final Object instance, final String method, final Object... args) {
        return invoke(instance, findMethod(instance, method, args), args);
    }

    public void monitor() {
        int size = workQueue.size();
        if (size > 0) {
            logger.info("{} Work Queue {}", label, size);
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setMonitorInterval(long monitorInterval) {
        this.monitorInterval = monitorInterval;
    }

    public void setWorkers(int workers) {
        executor.setCorePoolSize(workers + 1);
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    protected Method findMethod(Object instance, String method, Object... args) {
        Method actualMethod = null;
        for (Method m : instance.getClass().getMethods()) {
            if (method.equals(m.getName())) {
                Class<?>[] types = m.getParameterTypes();
                if (args.length == types.length) {
                    boolean valid = true;
                    for (int i = 0; i < args.length; i++) {
                        Class<?> paramType = ClassUtils.resolvePrimitiveIfNecessary(types[i]);
                        if (args[i] != null && !(paramType.isAssignableFrom(args[i].getClass()))) {
                            valid = false;
                        }
                    }
                    if (valid) {
                        actualMethod = m;
                        break;
                    }
                }
            }
        }
        if (actualMethod == null) {
            throw new IllegalStateException("Method " + method + " not found with given arguments");
        }

        return actualMethod;
    }
}
