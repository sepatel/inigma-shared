package org.inigma.shared.tools;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;

public abstract class ClassUtil {
    public static boolean instanceOf(Object object, String className) {
        if (object == null) {
            return false;
        }
        HashSet<Class> visited = new HashSet<Class>();
        Deque<Class> deque = new LinkedList<Class>();
        deque.push(object.getClass());
        visited.add(object.getClass());
        while (!deque.isEmpty()) {
            Class cls = deque.removeFirst();
            if (cls.getName().equals(className)) {
                return true;
            }
            Class parent = cls.getSuperclass();
            if (parent != null && visited.add(parent)) {
                deque.addLast(parent);
            }
            for (Class iface : cls.getInterfaces()) {
                if (visited.add(iface)) {
                    deque.addLast(iface);
                }
            }
        }
        return false;
    }

}
