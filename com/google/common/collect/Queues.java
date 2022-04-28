package com.google.common.collect;

import java.util.ArrayDeque;

public final class Queues {
    public static <E> ArrayDeque<E> newArrayDeque() {
        return new ArrayDeque<>();
    }
}
