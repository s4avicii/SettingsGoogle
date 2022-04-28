package com.android.settings.safetycenter;

import android.content.Context;

public final class BiometricsSafetySource {
    public static void sendSafetyData(Context context) {
        SafetyCenterStatusHolder.get().isEnabled(context);
    }
}
