package com.android.settings.deviceinfo.storage;

import android.os.storage.DiskInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StorageUtils$$ExternalSyntheticLambda4 implements Predicate {
    public static final /* synthetic */ StorageUtils$$ExternalSyntheticLambda4 INSTANCE = new StorageUtils$$ExternalSyntheticLambda4();

    private /* synthetic */ StorageUtils$$ExternalSyntheticLambda4() {
    }

    public final boolean test(Object obj) {
        return StorageUtils.isDiskUnsupported((DiskInfo) obj);
    }
}
