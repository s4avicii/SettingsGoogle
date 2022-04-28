package com.google.android.settings.accessibility;

import android.content.ComponentName;
import java.util.Map;
import java.util.function.Predicate;

/* renamed from: com.google.android.settings.accessibility.AccessibilitySearchFeatureProviderGoogleImpl$SearchIndexableRawHelper$$ExternalSyntheticLambda2 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1747xf66b84bd implements Predicate {
    public final /* synthetic */ ComponentName f$0;

    public /* synthetic */ C1747xf66b84bd(ComponentName componentName) {
        this.f$0 = componentName;
    }

    public final boolean test(Object obj) {
        return this.f$0.equals(((Map.Entry) obj).getKey());
    }
}
