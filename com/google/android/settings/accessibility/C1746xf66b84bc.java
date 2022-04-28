package com.google.android.settings.accessibility;

import android.content.pm.ResolveInfo;
import com.google.android.settings.accessibility.AccessibilitySearchFeatureProviderGoogleImpl;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/* renamed from: com.google.android.settings.accessibility.AccessibilitySearchFeatureProviderGoogleImpl$SearchIndexableRawHelper$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1746xf66b84bc implements Consumer {
    public final /* synthetic */ AccessibilitySearchFeatureProviderGoogleImpl.SearchIndexableRawHelper f$0;
    public final /* synthetic */ ResolveInfo f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ C1746xf66b84bc(AccessibilitySearchFeatureProviderGoogleImpl.SearchIndexableRawHelper searchIndexableRawHelper, ResolveInfo resolveInfo, List list) {
        this.f$0 = searchIndexableRawHelper;
        this.f$1 = resolveInfo;
        this.f$2 = list;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$buildSupportedServiceSearchIndex$1(this.f$1, this.f$2, (Map.Entry) obj);
    }
}
