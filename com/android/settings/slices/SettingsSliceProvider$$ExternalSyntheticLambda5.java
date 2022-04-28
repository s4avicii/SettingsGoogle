package com.android.settings.slices;

import android.net.Uri;
import android.text.TextUtils;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SettingsSliceProvider$$ExternalSyntheticLambda5 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ SettingsSliceProvider$$ExternalSyntheticLambda5(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return TextUtils.equals(this.f$0, ((Uri) obj).getAuthority());
    }
}
