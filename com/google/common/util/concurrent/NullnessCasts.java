package com.google.common.util.concurrent;

final class NullnessCasts {
    static <T> T uncheckedCastNullableTToT(T t) {
        return t;
    }

    static <T> T uncheckedNull() {
        return null;
    }
}
