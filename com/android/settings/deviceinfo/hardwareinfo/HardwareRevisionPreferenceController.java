package com.android.settings.deviceinfo.hardwareinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.text.TextUtils;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.Sliceable;

public class HardwareRevisionPreferenceController extends BasePreferenceController {
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

    public boolean useDynamicSliceSummary() {
        return true;
    }

    public HardwareRevisionPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return (!this.mContext.getResources().getBoolean(C0444R.bool.config_show_device_model) || TextUtils.isEmpty(getSummary())) ? 3 : 0;
    }

    public void copy() {
        Sliceable.setCopyContent(this.mContext, getSummary(), this.mContext.getText(C0444R.string.hardware_revision));
    }

    public CharSequence getSummary() {
        return SystemProperties.get("ro.boot.hardware.revision");
    }
}
