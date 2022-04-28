package com.google.common.util.concurrent;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class SettableFuture<V> extends AbstractFuture.TrustedFuture<V> {
    @CanIgnoreReturnValue
    public /* bridge */ /* synthetic */ Object get() throws InterruptedException, ExecutionException {
        return super.get();
    }

    @CanIgnoreReturnValue
    public /* bridge */ /* synthetic */ Object get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return super.get(j, timeUnit);
    }

    public static <V> SettableFuture<V> create() {
        return new SettableFuture<>();
    }

    @CanIgnoreReturnValue
    public boolean set(V v) {
        return super.set(v);
    }

    @CanIgnoreReturnValue
    public boolean setException(Throwable th) {
        return super.setException(th);
    }

    private SettableFuture() {
    }
}
