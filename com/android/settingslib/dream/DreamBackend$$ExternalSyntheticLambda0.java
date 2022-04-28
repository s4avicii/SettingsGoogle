package com.android.settingslib.dream;

import android.content.ComponentName;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamBackend$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ DreamBackend$$ExternalSyntheticLambda0 INSTANCE = new DreamBackend$$ExternalSyntheticLambda0();

    private /* synthetic */ DreamBackend$$ExternalSyntheticLambda0() {
    }

    public final Object apply(Object obj) {
        return ComponentName.unflattenFromString((String) obj);
    }
}
