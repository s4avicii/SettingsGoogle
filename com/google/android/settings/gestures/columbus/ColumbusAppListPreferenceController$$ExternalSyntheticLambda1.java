package com.google.android.settings.gestures.columbus;

import android.content.pm.LauncherActivityInfo;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ColumbusAppListPreferenceController$$ExternalSyntheticLambda1 implements Function {
    public static final /* synthetic */ ColumbusAppListPreferenceController$$ExternalSyntheticLambda1 INSTANCE = new ColumbusAppListPreferenceController$$ExternalSyntheticLambda1();

    private /* synthetic */ ColumbusAppListPreferenceController$$ExternalSyntheticLambda1() {
    }

    public final Object apply(Object obj) {
        return ((LauncherActivityInfo) obj).getLabel().toString();
    }
}
