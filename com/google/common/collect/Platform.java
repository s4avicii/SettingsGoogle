package com.google.common.collect;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

final class Platform {
    static <K, V> Map<K, V> newLinkedHashMapWithExpectedSize(int i) {
        return CompactLinkedHashMap.createWithExpectedSize(i);
    }

    static <E> Set<E> newLinkedHashSetWithExpectedSize(int i) {
        return CompactLinkedHashSet.createWithExpectedSize(i);
    }

    static <T> T[] newArray(T[] tArr, int i) {
        return (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i);
    }

    static <T> T[] copy(Object[] objArr, int i, int i2, T[] tArr) {
        return Arrays.copyOfRange(objArr, i, i2, tArr.getClass());
    }

    static MapMaker tryWeakKeys(MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }
}
