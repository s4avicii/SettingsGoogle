package com.android.settings.utils;

import android.hardware.SensorPrivacyManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SensorPrivacyManagerHelper$$ExternalSyntheticLambda1 implements SensorPrivacyManager.OnSensorPrivacyChangedListener {
    public final /* synthetic */ SensorPrivacyManagerHelper f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ SensorPrivacyManagerHelper$$ExternalSyntheticLambda1(SensorPrivacyManagerHelper sensorPrivacyManagerHelper, int i, int i2) {
        this.f$0 = sensorPrivacyManagerHelper;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void onSensorPrivacyChanged(int i, boolean z) {
        this.f$0.lambda$registerListenerIfNeeded$2(this.f$1, this.f$2, i, z);
    }
}
