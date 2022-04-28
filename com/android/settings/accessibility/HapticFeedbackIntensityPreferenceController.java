package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;

public class HapticFeedbackIntensityPreferenceController extends VibrationIntensityPreferenceController {
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

    public static final class HapticFeedbackVibrationPreferenceConfig extends VibrationPreferenceConfig {
        public boolean isRestrictedByRingerModeSilent() {
            return true;
        }

        public HapticFeedbackVibrationPreferenceConfig(Context context) {
            super(context, "haptic_feedback_intensity", 18);
        }

        public int readIntensity() {
            if (Settings.System.getInt(this.mContentResolver, "haptic_feedback_enabled", 1) == 0) {
                return 0;
            }
            return super.readIntensity();
        }

        public boolean updateIntensity(int i) {
            boolean updateIntensity = super.updateIntensity(i);
            int i2 = i == 0 ? 1 : 0;
            Settings.System.putInt(this.mContentResolver, "haptic_feedback_enabled", i2 ^ 1);
            ContentResolver contentResolver = this.mContentResolver;
            if (i2 != 0) {
                i = getDefaultIntensity();
            }
            Settings.System.putInt(contentResolver, "hardware_haptic_feedback_intensity", i);
            return updateIntensity;
        }
    }

    public HapticFeedbackIntensityPreferenceController(Context context, String str) {
        super(context, str, new HapticFeedbackVibrationPreferenceConfig(context));
    }

    protected HapticFeedbackIntensityPreferenceController(Context context, String str, int i) {
        super(context, str, new HapticFeedbackVibrationPreferenceConfig(context), i);
    }
}
