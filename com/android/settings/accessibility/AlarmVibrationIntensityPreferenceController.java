package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;

public class AlarmVibrationIntensityPreferenceController extends VibrationIntensityPreferenceController {
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

    public static final class AlarmVibrationPreferenceConfig extends VibrationPreferenceConfig {
        public AlarmVibrationPreferenceConfig(Context context) {
            super(context, "alarm_vibration_intensity", 17);
        }
    }

    public AlarmVibrationIntensityPreferenceController(Context context, String str) {
        super(context, str, new AlarmVibrationPreferenceConfig(context));
    }

    protected AlarmVibrationIntensityPreferenceController(Context context, String str, int i) {
        super(context, str, new AlarmVibrationPreferenceConfig(context), i);
    }
}
