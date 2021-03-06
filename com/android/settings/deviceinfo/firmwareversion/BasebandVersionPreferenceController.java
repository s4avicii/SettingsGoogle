package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.Utils;

public class BasebandVersionPreferenceController extends BasePreferenceController {
    static final String BASEBAND_PROPERTY = "gsm.version.baseband";

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
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

    public BasebandVersionPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return !Utils.isWifiOnly(this.mContext) ? 0 : 3;
    }

    public CharSequence getSummary() {
        return SystemProperties.get(BASEBAND_PROPERTY, this.mContext.getString(C0444R.string.device_info_default));
    }
}
