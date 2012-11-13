package org.inigma.shared.job;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Ignore;
import org.junit.Test;

public class AsynchronousTest {
    private Asynchronous async = new Asynchronous(1);
    private int counter;

    @Test
    public void invokePublicMethodNoReturn() throws Exception {
        int currentValue = counter;
        Future<Object> future = async.invoke(this, "incrementCounter");
        Object result = future.get();
        assertNull(result);
        assertEquals(currentValue + 1, counter);
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
    public void invokeMethodWithObjectReturn() throws Exception {
        Future<String> future = async.invoke(this, "getString");
        String result = future.get();
        assertEquals("Hello World", result);
    }

    @Test
    public void invokeMethodWithPrimitiveReturn() throws Exception {
        Future<Integer> future = async.invoke(this, "getInt");
        int result = future.get();
        assertEquals(42, result);
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
    public void invokeMethodWithPrimitiveParam() throws Exception {
        int currentValue = counter;
        Future<Object> future = async.invoke(this, "incrementCounter", 42);
        Object result = future.get();
        assertNull(result);
        assertEquals(currentValue + 42, counter);
    }

    @Test
    @Ignore
    public void invokeMethodWithVarargsParam() throws Exception {
        int currentValue = counter;
        Future<Object> future = async.invoke(this, "incrementCounterByArgCount", new Object[] { "Apple", "Orange",
                "Pear" });
        Object result = future.get();
        assertNull(result);
        assertEquals(currentValue + 3, counter);
    }

    @Test(expected = ExecutionException.class)
    public void invokeMethodThrowingException() throws Exception {
        int currentValue = counter;
        Future<Object> future = async.invoke(this, "exceptionThrowingMethod", 42, "My String", new Date());
        Object result = future.get();
        fail("Should not have reached this point with " + result);
    }

    private void exceptionThrowingMethod(int number, String text, Date time) {
        throw new IllegalStateException("It works!");
    }

    public void incrementCounterByArgCount(Object... args) {
        counter += args.length;
    }

    public void incrementCounter(String stuff) {
        counter++;
    }

    public void incrementCounter(int amount) {
        counter += amount;
    }

    public String getString() {
        return "Hello World";
    }

    public int getInt() {
        return 42;
    }

    public void incrementCounter() {
        counter++;
    }

    private void incrementHiddenCounter() {
        counter++;
    }
}
