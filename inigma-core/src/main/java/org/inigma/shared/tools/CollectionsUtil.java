package org.inigma.shared.tools;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public abstract class CollectionsUtil {
    private static class AppendOnlyList<T> extends ForwardingList<T> {
        private final List<T> delegate;
        private final List<T> unmodDelegate;

        private AppendOnlyList(List<T> delegate) {
            this.delegate = delegate;
            unmodDelegate = Collections.unmodifiableList(delegate);
        }

        @Override
        public boolean add(T element) {
            return delegate.add(element);
        }

        @Override
        public boolean addAll(Collection<? extends T> collection) {
            return delegate.addAll(collection);
        }

        @Override
        protected List<T> delegate() {
            return unmodDelegate;
        }
    }

    private static class AppendOnlyRandomAccessList<T> extends AppendOnlyList<T> implements RandomAccess {
        private AppendOnlyRandomAccessList(List<T> delegate) {
            super(delegate);
        }
    }

    public static <T> List<T> appendOnlyList() {
        return appendOnlyListView(Lists.<T>newArrayList());
    }

    public static <T> List<T> appendOnlyListView(List<T> delegate) {
        return delegate instanceof RandomAccess
                ? new AppendOnlyRandomAccessList<T>(delegate) : new AppendOnlyList<T>(delegate);
    }
}
