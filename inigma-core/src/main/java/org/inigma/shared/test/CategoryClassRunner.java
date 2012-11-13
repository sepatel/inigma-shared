package org.inigma.shared.test;

import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.annotation.ProfileValueSource;
import org.springframework.test.annotation.ProfileValueUtils;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class CategoryClassRunner extends SpringJUnit4ClassRunner {
    public CategoryClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    public void run(RunNotifier notifier) {
        if (!shouldExecute()) {
            notifier.fireTestIgnored(getDescription());
            return;
        }
        EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
        try {
            Statement statement = classBlock(notifier);
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            testNotifier.fireTestIgnored();
        } catch (StoppedByUserException e) {
            throw e;
        } catch (Throwable e) {
            testNotifier.addFailure(e);
        }
    }

    private boolean shouldExecute() {
        Class<?> testClass = getTestClass().getJavaClass();
        TestCategory category = testClass.getAnnotation(TestCategory.class);
        System.err.println("Category: " + category);
        if (category != null) { // any reason to not execute because of category?
            ProfileValueSource pvs = ProfileValueUtils.retrieveProfileValueSource(testClass);
            String value = pvs.get(category.value());
            if (value == null) {
                return false;
            }
        }

        return true;
    }
}
