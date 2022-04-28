package com.google.android.settings.fuelgauge.reversecharging;

import android.content.Context;

final class ReverseChargingUtils {
    static int getAvailability(Context context, ReverseChargingManager reverseChargingManager) {
        if (reverseChargingManager == null && context != null) {
            reverseChargingManager = ReverseChargingManager.getInstance(context);
        }
        return (reverseChargingManager == null || !reverseChargingManager.isSupportedReverseCharging()) ? 3 : 0;
    }
}
