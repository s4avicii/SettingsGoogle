package com.google.android.settings.gestures.columbus;

import android.content.pm.LauncherActivityInfo;
import android.util.DisplayMetrics;
import java.util.function.Function;

/* renamed from: com.google.android.settings.gestures.columbus.ColumbusAppShortcutListPreferenceController$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1806xb35db063 implements Function {
    public static final /* synthetic */ C1806xb35db063 INSTANCE = new C1806xb35db063();

    private /* synthetic */ C1806xb35db063() {
    }

    public final Object apply(Object obj) {
        return ((LauncherActivityInfo) obj).getIcon(DisplayMetrics.DENSITY_DEVICE_STABLE);
    }
}
