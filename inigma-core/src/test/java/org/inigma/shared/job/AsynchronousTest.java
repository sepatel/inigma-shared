package org.inigma.shared.job;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

public class AsynchronousTest {
    private Asynchronous async = new Asynchronous(1);
    private int counter;

    public int getInt() {
        return 42;
    }

    public String getString() {
        return "Hello World";
    }

    public void incrementCounter(String stuff) {
        counter++;
    }

    public void incrementCounter(int amount) {
        counter += amount;
    }

    public void incrementCounter() {
        counter++;
    }

    public void incrementCounterByArgCount(Object... args) {
        counter += args.length;
    }

    @Test
    public void invokeMethodReturningParamUsingCallback() throws Exception {
        final AtomicInteger answer = new AtomicInteger();
        final Lock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();
        AsynchronousCallback<Integer> callback = new AsynchronousCallback<Integer>() {
            @Override
            public void onCompletion(Integer value, Future<Integer> future) {
                answer.set(value);
                condition.signalAll();
            }

            @Override
            public void onException(Exception e, Future<Integer> future) {
                fail("Should not have thrown an exception!");
            }
        };
        lock.lock();
        async.invokeWithCallback(callback, this, "getInt");
        //condition.await();
        condition.await(1, TimeUnit.SECONDS);
        lock.unlock();
        assertEquals(42, answer.get());
    }

    @Test(expected = ExecutionException.class)
    public void invokeMethodThrowingException() throws Exception {
        Future<Object> future = async.invoke(this, "exceptionThrowingMethod", 42, "My String", new Date());
        Object result = future.get();
        fail("Should not have reached this point with " + result);
    }

    @Test
    public void invokeMethodThrowingExceptionUsingCallback() throws Exception {
        final Lock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();
        final AtomicInteger counter = new AtomicInteger();
        AsynchronousCallback<Void> callback = new AsynchronousCallback<Void>() {
            @Override
            public void onCompletion(Void value, Future<Void> future) {
                fail("Should not have completed successfully.");
            }

            @Override
            public void onException(Exception e, Future<Void> future) {
                counter.incrementAndGet();
                condition.signalAll();
            }
        };
        async.invokeWithCallback(callback, this, "exceptionThrowingMethod", 42, "My String", new Date());
        lock.lock();
        condition.await(1, TimeUnit.SECONDS);
        assertEquals(1, counter.get());
        lock.unlock();
    }

    @Test
    public void invokeMethodWithObjectParam() throws Exception {
        int currentValue = counter;
        Future<Object> future = async.invoke(this, "incrementCounter", "Hello World");
        Object result = future.get();
        assertNull(result);
        assertEquals(currentValue + 1, counter);
    }

    @Test
    public void invokeMethodWithObjectReturn() throws Exception {
        Future<String> future = async.invoke(this, "getString");
        String result = future.get();
        assertEquals("Hello World", result);
    }

    @Test
    public void invokeMethodWithPrimitiveParam() throws Exception {
        int currentValue = counter;
        Future<Object> future = async.invoke(this, "incrementCounter", 42);
        Object result = future.get();
        assertNull(result);
        assertEquals(currentValue + 42, counter);
    }

    @Test
    public void invokeMethodWithPrimitiveReturn() throws Exception {
        Future<Integer> future = async.invoke(this, "getInt");
        int result = future.get();
        assertEquals(42, result);
    }

    @Test
    @Ignore
    public void invokeMethodWithVarargsParam() throws Exception {
        int currentValue = counter;
        Future<Object> future = async.invoke(this, "incrementCounterByArgCount", new Object[]{"Apple", "Orange", "Pear"});
        Object result = future.get();
        assertNull(result);
        assertEquals(currentValue + 3, counter);
    }

    @Test
    public void invokePrivateMethod() throws Exception {
        int currentValue = counter;
        Future<Object> future = async.invoke(this, "incrementHiddenCounter");
        Object result = future.get();
        assertNull(result);
        assertEquals(currentValue + 1, counter);
    }

    @Test
    public void invokePublicMethodNoReturn() throws Exception {
        int currentValue = counter;
        Future<Object> future = async.invoke(this, "incrementCounter");
        Object result = future.get();
        assertNull(result);
        assertEquals(currentValue + 1, counter);
    }

    private void exceptionThrowingMethod(int number, String text, Date time) {
        throw new IllegalStateException("It works!");
    }

    private void incrementHiddenCounter() {
        counter++;
    }
}
