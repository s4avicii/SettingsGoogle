package com.android.settings.biometrics.fingerprint;

import android.content.Context;
import android.content.IntentFilter;
import androidx.lifecycle.Lifecycle;

public class FingerprintProfileStatusPreferenceController extends FingerprintStatusPreferenceController {
    public static final String KEY_FINGERPRINT_SETTINGS = "fingerprint_settings_profile";

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

    public FingerprintProfileStatusPreferenceController(Context context) {
        super(context, KEY_FINGERPRINT_SETTINGS);
    }

    public FingerprintProfileStatusPreferenceController(Context context, String str) {
        super(context, str);
    }

    public FingerprintProfileStatusPreferenceController(Context context, Lifecycle lifecycle) {
        super(context, KEY_FINGERPRINT_SETTINGS, lifecycle);
    }

    public FingerprintProfileStatusPreferenceController(Context context, String str, Lifecycle lifecycle) {
        super(context, str, lifecycle);
    }

    /* access modifiers changed from: protected */
    public boolean isUserSupported() {
        int i = this.mProfileChallengeUserId;
        return i != -10000 && this.mUm.isManagedProfile(i);
    }

    /* access modifiers changed from: protected */
    public int getUserId() {
        return this.mProfileChallengeUserId;
    }
}
