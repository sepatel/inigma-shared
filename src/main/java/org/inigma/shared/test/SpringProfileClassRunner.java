package org.inigma.shared.test;

import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSource;
import org.springframework.test.annotation.ProfileValueUtils;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class SpringProfileClassRunner extends SpringJUnit4ClassRunner {
    public SpringProfileClassRunner(Class<?> clazz) throws InitializationError {
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
        IfProfileValue ifProfileValue = testClass.getAnnotation(IfProfileValue.class);
        if (ifProfileValue == null) {
            System.err.println("Not executing profile because IfProfileValue is null");
            return true;
        }

        String[] annotatedValues = ifProfileValue.values();
        if (StringUtils.hasLength(ifProfileValue.value())) {
            if (annotatedValues.length > 0) {
                throw new IllegalArgumentException("Setting both the 'value' and 'values' attributes "
                        + "of @IfProfileValue is not allowed: choose one or the other.");
            }
            annotatedValues = new String[] { ifProfileValue.value() };
        }

        ProfileValueSource pvs = ProfileValueUtils.retrieveProfileValueSource(testClass);
        String value = pvs.get(ifProfileValue.name());
        if (value.contains(",")) {
            for (String key : value.split(" *, *")) {
                for (String annotated : annotatedValues) {
                    if (ObjectUtils.nullSafeEquals(annotated, key)) {
                        System.err.printf("Executing profile triggered by '%s' and profile '%s'\n",
                                annotated, key);
                        return true;
                    }
                }
            }
            return false;
        }
        for (String annotated : annotatedValues) {
            if (ObjectUtils.nullSafeEquals(annotated, value)) {
                System.err.printf("Executing profile triggered by '%s' and profile '%s'\n", annotated, value);
                return true;
            }
        }
        System.err.printf("No profile triggered from keys: %s in annotations %s\n", value,
                CollectionUtils.arrayToList(annotatedValues));
        return false;
    }
}
