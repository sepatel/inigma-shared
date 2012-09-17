package org.inigma.shared.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test annotation to indicate that a test is enabled for a specific testing category. If defined then the category name
 * must be defined as a system variable in order for the test to qualify for execution.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface TestCategory {
    String value();
}
