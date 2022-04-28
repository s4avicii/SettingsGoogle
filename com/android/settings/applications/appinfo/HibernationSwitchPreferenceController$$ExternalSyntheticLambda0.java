package com.android.settings.applications.appinfo;

import androidx.preference.Preference;
import java.util.function.IntConsumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class HibernationSwitchPreferenceController$$ExternalSyntheticLambda0 implements IntConsumer {
    public final /* synthetic */ HibernationSwitchPreferenceController f$0;
    public final /* synthetic */ Preference f$1;

    public /* synthetic */ HibernationSwitchPreferenceController$$ExternalSyntheticLambda0(HibernationSwitchPreferenceController hibernationSwitchPreferenceController, Preference preference) {
        this.f$0 = hibernationSwitchPreferenceController;
        this.f$1 = preference;
    }

    public final void accept(int i) {
        this.f$0.lambda$updateState$0(this.f$1, i);
    }
}
