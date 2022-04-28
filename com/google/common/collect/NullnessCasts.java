package com.google.common.collect;

final class NullnessCasts {
    static <T> T uncheckedCastNullableTToT(T t) {
        return t;
    }

    static <T> T unsafeNull() {
        return null;
    }
}
