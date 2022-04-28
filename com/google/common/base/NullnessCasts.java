package com.google.common.base;

final class NullnessCasts {
    static <T> T uncheckedCastNullableTToT(T t) {
        return t;
    }
}
