package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.icu.text.MessageFormat;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.core.TogglePreferenceController;

public class MagnificationGesturesPreferenceController extends TogglePreferenceController {
    private boolean mIsFromSUW = false;

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

    public int getSliceHighlightMenuRes() {
        return C0444R.string.menu_key_accessibility;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public MagnificationGesturesPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return MagnificationPreferenceFragment.isChecked(this.mContext.getContentResolver(), "accessibility_display_magnification_enabled");
    }

    public boolean setChecked(boolean z) {
        return MagnificationPreferenceFragment.setChecked(this.mContext.getContentResolver(), "accessibility_display_magnification_enabled", z);
    }

    public void setIsFromSUW(boolean z) {
        this.mIsFromSUW = z;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!getPreferenceKey().equals(preference.getKey())) {
            return false;
        }
        Bundle extras = preference.getExtras();
        populateMagnificationGesturesPreferenceExtras(extras, this.mContext);
        extras.putBoolean("checked", isChecked());
        extras.putBoolean("from_suw", this.mIsFromSUW);
        return true;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "screen_magnification_gestures_preference_screen");
    }

    public CharSequence getSummary() {
        int i;
        if (this.mIsFromSUW) {
            i = C0444R.string.accessibility_screen_magnification_short_summary;
        } else {
            i = isChecked() ? C0444R.string.accessibility_feature_state_on : C0444R.string.accessibility_feature_state_off;
        }
        return this.mContext.getString(i);
    }

    static void populateMagnificationGesturesPreferenceExtras(Bundle bundle, Context context) {
        bundle.putString("preference_key", "accessibility_display_magnification_enabled");
        bundle.putInt("title_res", C0444R.string.accessibility_screen_magnification_gestures_title);
        bundle.putCharSequence("intro", context.getString(C0444R.string.accessibility_screen_magnification_intro_text));
        bundle.putCharSequence("html_description", MessageFormat.format(context.getString(C0444R.string.accessibility_screen_magnification_summary), new Object[]{1, 2, 3, 4, 5}));
        bundle.putInt("video_resource", C0444R.raw.accessibility_screen_magnification);
    }
}
