package com.google.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import androidx.lifecycle.Lifecycle;
import androidx.preference.Preference;
import com.android.settings.biometrics.fingerprint.FingerprintStatusPreferenceController;

public class FingerprintStatusGooglePreferenceController extends FingerprintStatusPreferenceController {
    private final SecurityContentManager mSecurityContentManager;

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

    public FingerprintStatusGooglePreferenceController(Context context, String str) {
        this(context, str, (Lifecycle) null);
    }

    public FingerprintStatusGooglePreferenceController(Context context, String str, Lifecycle lifecycle) {
        super(context, str, lifecycle);
        this.mSecurityContentManager = SecurityContentManager.getInstance(this.mContext);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setIcon(this.mSecurityContentManager.getBiometricSecurityLevel(hasEnrolledBiometrics()).getEntryIconResId());
        preference.setOrder(this.mSecurityContentManager.getBiometricOrder());
    }
}
