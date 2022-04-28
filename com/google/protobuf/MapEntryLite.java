package com.google.protobuf;

import java.io.IOException;

public class MapEntryLite<K, V> {

    static class Metadata<K, V> {
    }

    /* access modifiers changed from: package-private */
    public Metadata<K, V> getMetadata() {
        return null;
    }

    static <K, V> void writeTo(CodedOutputStream codedOutputStream, Metadata<K, V> metadata, K k, V v) throws IOException {
        throw null;
    }

    static <K, V> int computeSerializedSize(Metadata<K, V> metadata, K k, V v) {
        throw null;
    }

    public int computeMessageSize(int i, K k, V v) {
        return CodedOutputStream.computeTagSize(i) + CodedOutputStream.computeLengthDelimitedFieldSize(computeSerializedSize((Metadata) null, k, v));
    }
}
