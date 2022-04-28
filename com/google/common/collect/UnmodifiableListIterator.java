package com.google.common.collect;

import com.google.errorprone.annotations.DoNotCall;
import java.util.ListIterator;

public abstract class UnmodifiableListIterator<E> extends UnmodifiableIterator<E> implements ListIterator<E> {
    protected UnmodifiableListIterator() {
    }

    @DoNotCall("Always throws UnsupportedOperationException")
    @Deprecated
    public final void add(E e) {
        throw new UnsupportedOperationException();
    }

    @DoNotCall("Always throws UnsupportedOperationException")
    @Deprecated
    public final void set(E e) {
        throw new UnsupportedOperationException();
    }
}
