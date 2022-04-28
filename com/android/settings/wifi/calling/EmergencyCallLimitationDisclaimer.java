package com.android.settings.wifi.calling;

import android.content.Context;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;

public class EmergencyCallLimitationDisclaimer extends DisclaimerItem {
    @VisibleForTesting
    static final String KEY_HAS_AGREED_EMERGENCY_LIMITATION_DISCLAIMER = "key_has_agreed_emergency_limitation_disclaimer";

    /* access modifiers changed from: protected */
    public int getMessageId() {
        return C0444R.string.wfc_disclaimer_emergency_limitation_desc_text;
    }

    /* access modifiers changed from: protected */
    public String getName() {
        return "EmergencyCallLimitationDisclaimer";
    }

    /* access modifiers changed from: protected */
    public String getPrefKey() {
        return KEY_HAS_AGREED_EMERGENCY_LIMITATION_DISCLAIMER;
    }

    /* access modifiers changed from: protected */
    public int getTitleId() {
        return C0444R.string.wfc_disclaimer_emergency_limitation_title_text;
    }

    public EmergencyCallLimitationDisclaimer(Context context, int i) {
        super(context, i);
    }

    /* access modifiers changed from: package-private */
    public boolean shouldShow() {
        if (getCarrierConfig().getInt("emergency_notification_delay_int") != -1) {
            return super.shouldShow();
        }
        logd("shouldShow: false due to carrier config is default(-1).");
        return false;
    }
}
