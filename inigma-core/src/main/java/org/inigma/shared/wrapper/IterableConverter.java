package org.inigma.shared.wrapper;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.core.convert.converter.Converter;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 6/26/14 10:29 AM
 */
public class IterableConverter<S, T> extends AbstractCollection<T> implements Iterable<T> {
    private final Iterable<S> source;
    private final Converter<S, T> converter;

    public IterableConverter(final Iterable<S> ref, final Converter<S, T> converter) {
        this.source = ref;
        this.converter = converter;
    }

    public Iterable<S> getSource() {
        return source;
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<S> iterator = source.iterator();
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return converter.convert(iterator.next());
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    @Override
    public int size() {
        if (source instanceof Collection) {
            return ((Collection) source).size();
        }
        throw new UnsupportedOperationException("Source " + source.getClass() + " does not support size determination");
    }
}
