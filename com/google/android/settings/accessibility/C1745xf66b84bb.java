package com.google.android.settings.accessibility;

import android.content.pm.ActivityInfo;
import com.google.android.settings.accessibility.AccessibilitySearchFeatureProviderGoogleImpl;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/* renamed from: com.google.android.settings.accessibility.AccessibilitySearchFeatureProviderGoogleImpl$SearchIndexableRawHelper$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1745xf66b84bb implements Consumer {
    public final /* synthetic */ AccessibilitySearchFeatureProviderGoogleImpl.SearchIndexableRawHelper f$0;
    public final /* synthetic */ ActivityInfo f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ C1745xf66b84bb(AccessibilitySearchFeatureProviderGoogleImpl.SearchIndexableRawHelper searchIndexableRawHelper, ActivityInfo activityInfo, List list) {
        this.f$0 = searchIndexableRawHelper;
        this.f$1 = activityInfo;
        this.f$2 = list;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$buildSupportedActivitySearchIndex$3(this.f$1, this.f$2, (Map.Entry) obj);
    }
}
