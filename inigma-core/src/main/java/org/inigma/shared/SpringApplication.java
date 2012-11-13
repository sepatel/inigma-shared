package org.inigma.shared;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Simple Spring Based initialization of an application.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public class SpringApplication {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(args);
        context.registerShutdownHook();
    }
}
