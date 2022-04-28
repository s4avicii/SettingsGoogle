package com.android.settings.dream;

import com.android.settingslib.dream.DreamBackend;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamPickerController$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ DreamPickerController$$ExternalSyntheticLambda1 INSTANCE = new DreamPickerController$$ExternalSyntheticLambda1();

    private /* synthetic */ DreamPickerController$$ExternalSyntheticLambda1() {
    }

    public final boolean test(Object obj) {
        return ((DreamBackend.DreamInfo) obj).isActive;
    }
}
