package com.google.android.settings.dream;

import com.android.settingslib.dream.DreamBackend;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamSetupFragment$$ExternalSyntheticLambda2 implements Predicate {
    public static final /* synthetic */ DreamSetupFragment$$ExternalSyntheticLambda2 INSTANCE = new DreamSetupFragment$$ExternalSyntheticLambda2();

    private /* synthetic */ DreamSetupFragment$$ExternalSyntheticLambda2() {
    }

    public final boolean test(Object obj) {
        return ((DreamBackend.DreamInfo) obj).isActive;
    }
}
