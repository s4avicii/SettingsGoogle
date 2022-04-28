package com.google.android.settings.gestures.columbus;

import android.content.pm.LauncherActivityInfo;
import android.content.pm.ShortcutInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ColumbusAppListPreferenceController$$ExternalSyntheticLambda2 implements Predicate {
    public final /* synthetic */ LauncherActivityInfo f$0;

    public /* synthetic */ ColumbusAppListPreferenceController$$ExternalSyntheticLambda2(LauncherActivityInfo launcherActivityInfo) {
        this.f$0 = launcherActivityInfo;
    }

    public final boolean test(Object obj) {
        return ((ShortcutInfo) obj).getPackage().equals(this.f$0.getComponentName().getPackageName());
    }
}
