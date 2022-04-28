package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;

public class NotificationVibrationIntensityPreferenceController extends VibrationIntensityPreferenceController {
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public static final class NotificationVibrationPreferenceConfig extends VibrationPreferenceConfig {
        public boolean isRestrictedByRingerModeSilent() {
            return true;
        }

        public NotificationVibrationPreferenceConfig(Context context) {
            super(context, "notification_vibration_intensity", 49);
        }
    }

    public NotificationVibrationIntensityPreferenceController(Context context, String str) {
        super(context, str, new NotificationVibrationPreferenceConfig(context));
    }

    protected NotificationVibrationIntensityPreferenceController(Context context, String str, int i) {
        super(context, str, new NotificationVibrationPreferenceConfig(context), i);
    }
}
