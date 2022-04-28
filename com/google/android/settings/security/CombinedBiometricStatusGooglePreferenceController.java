package com.google.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import androidx.lifecycle.Lifecycle;
import androidx.preference.Preference;
import com.android.settings.Utils;
import com.android.settings.biometrics.combination.CombinedBiometricStatusPreferenceController;

public class CombinedBiometricStatusGooglePreferenceController extends CombinedBiometricStatusPreferenceController {
    FaceManager mFaceManager;
    FingerprintManager mFingerprintManager;
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

    public CombinedBiometricStatusGooglePreferenceController(Context context, String str) {
        this(context, str, (Lifecycle) null);
    }

    public CombinedBiometricStatusGooglePreferenceController(Context context, String str, Lifecycle lifecycle) {
        super(context, str, lifecycle);
        this.mSecurityContentManager = SecurityContentManager.getInstance(this.mContext);
        this.mFingerprintManager = Utils.getFingerprintManagerOrNull(context);
        this.mFaceManager = Utils.getFaceManagerOrNull(context);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        boolean z = true;
        boolean z2 = fingerprintManager != null && fingerprintManager.hasEnrolledFingerprints(getUserId());
        FaceManager faceManager = this.mFaceManager;
        boolean z3 = faceManager != null && faceManager.hasEnrolledTemplates(getUserId());
        if (!z2 && !z3) {
            z = false;
        }
        preference.setIcon(this.mSecurityContentManager.getBiometricSecurityLevel(z).getEntryIconResId());
        preference.setOrder(this.mSecurityContentManager.getBiometricOrder());
    }
}
