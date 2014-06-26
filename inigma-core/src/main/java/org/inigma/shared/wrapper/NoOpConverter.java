package org.inigma.shared.wrapper;

import org.springframework.core.convert.converter.Converter;

/**
 * This is in case a simple wrapping of the DBCursor into a Collection is desired then the NoOpConverter may be used.
 *
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 6/24/14 11:25 AM
 */
public class NoOpConverter<T> implements Converter<T, T> {
    public static final NoOpConverter SINGLETON = new NoOpConverter();

    @Override
    public T convert(T source) {
        return source;
    }
}
