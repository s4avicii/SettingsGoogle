package com.android.settings.gestures;

import android.widget.Switch;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OneHandedSettings$$ExternalSyntheticLambda1 implements OnMainSwitchChangeListener {
    public final /* synthetic */ OneHandedSettings f$0;

    public /* synthetic */ OneHandedSettings$$ExternalSyntheticLambda1(OneHandedSettings oneHandedSettings) {
        this.f$0 = oneHandedSettings;
    }

    public final void onSwitchChanged(Switch switchR, boolean z) {
        this.f$0.lambda$updatePreferenceStates$0(switchR, z);
    }
}
