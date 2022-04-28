package com.google.android.settings.gestures.columbus;

import android.content.pm.LauncherActivityInfo;
import java.util.function.Predicate;

/* renamed from: com.google.android.settings.gestures.columbus.ColumbusAppShortcutListPreferenceController$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1807xb35db064 implements Predicate {
    public final /* synthetic */ ColumbusAppShortcutListPreferenceController f$0;

    public /* synthetic */ C1807xb35db064(ColumbusAppShortcutListPreferenceController columbusAppShortcutListPreferenceController) {
        this.f$0 = columbusAppShortcutListPreferenceController;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$createShortcutList$0((LauncherActivityInfo) obj);
    }
}
