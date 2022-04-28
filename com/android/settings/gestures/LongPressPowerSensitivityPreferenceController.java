package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.SliderPreferenceController;
import com.android.settings.widget.LabeledSeekBarPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class LongPressPowerSensitivityPreferenceController extends SliderPreferenceController implements LifecycleObserver, OnStart, OnStop {
    private final ContentObserver mPowerButtonObserver = new ContentObserver(Handler.getMain()) {
        public void onChange(boolean z) {
            if (LongPressPowerSensitivityPreferenceController.this.mPreference != null) {
                LongPressPowerSensitivityPreferenceController longPressPowerSensitivityPreferenceController = LongPressPowerSensitivityPreferenceController.this;
                longPressPowerSensitivityPreferenceController.updateState(longPressPowerSensitivityPreferenceController.mPreference);
            }
        }
    };
    /* access modifiers changed from: private */
    public LabeledSeekBarPreference mPreference;
    private final int[] mSensitivityValues;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getMin() {
        return 0;
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

    public LongPressPowerSensitivityPreferenceController(Context context, String str) {
        super(context, str);
        this.mSensitivityValues = context.getResources().getIntArray(17236079);
    }

    public void onStart() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("power_button_long_press"), false, this.mPowerButtonObserver);
    }

    public void onStop() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mPowerButtonObserver);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        LabeledSeekBarPreference labeledSeekBarPreference = (LabeledSeekBarPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = labeledSeekBarPreference;
        if (labeledSeekBarPreference != null) {
            labeledSeekBarPreference.setContinuousUpdates(false);
            this.mPreference.setHapticFeedbackMode(1);
            this.mPreference.setMin(getMin());
            this.mPreference.setMax(getMax());
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        LabeledSeekBarPreference labeledSeekBarPreference = (LabeledSeekBarPreference) preference;
        labeledSeekBarPreference.setEnabled(isAvailable() && PowerMenuSettingsUtils.isLongPressPowerForAssistEnabled(this.mContext));
        labeledSeekBarPreference.setProgress(getSliderPosition());
    }

    public int getAvailabilityStatus() {
        int[] iArr = this.mSensitivityValues;
        if (iArr == null || iArr.length < 2) {
            return 3;
        }
        return !PowerMenuSettingsUtils.isLongPressPowerForAssistEnabled(this.mContext) ? 5 : 0;
    }

    public int getSliderPosition() {
        int[] iArr = this.mSensitivityValues;
        if (iArr == null) {
            return 0;
        }
        return closestValueIndex(iArr, getCurrentSensitivityValue());
    }

    public boolean setSliderPosition(int i) {
        int[] iArr = this.mSensitivityValues;
        if (iArr == null || i < 0 || i >= iArr.length) {
            return false;
        }
        return Settings.Global.putInt(this.mContext.getContentResolver(), "power_button_long_press_duration_ms", this.mSensitivityValues[i]);
    }

    public int getMax() {
        int[] iArr = this.mSensitivityValues;
        if (iArr == null || iArr.length == 0) {
            return 0;
        }
        return iArr.length - 1;
    }

    private int getCurrentSensitivityValue() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "power_button_long_press_duration_ms", this.mContext.getResources().getInteger(17694853));
    }

    private static int closestValueIndex(int[] iArr, int i) {
        int i2 = Integer.MAX_VALUE;
        int i3 = 0;
        for (int i4 = 0; i4 < iArr.length; i4++) {
            int abs = Math.abs(iArr[i4] - i);
            if (abs < i2) {
                i3 = i4;
                i2 = abs;
            }
        }
        return i3;
    }
}
