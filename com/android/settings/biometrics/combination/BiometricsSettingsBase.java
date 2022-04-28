package com.android.settings.biometrics.combination;

import android.content.Context;
import android.content.Intent;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricUtils;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.password.ChooseLockSettingsHelper;

public abstract class BiometricsSettingsBase extends DashboardFragment {
    private boolean mConfirmCredential;
    private boolean mDoNotFinishActivity;
    private FaceManager mFaceManager;
    private FingerprintManager mFingerprintManager;
    protected long mGkPwHandle;
    protected int mUserId;

    private static int getUseBiometricSummaryRes(boolean z, boolean z2) {
        if (z && z2) {
            return C0444R.string.biometric_settings_use_face_or_fingerprint_preference_summary;
        }
        if (z) {
            return C0444R.string.biometric_settings_use_face_preference_summary;
        }
        if (z2) {
            return C0444R.string.biometric_settings_use_fingerprint_preference_summary;
        }
        return 0;
    }

    public abstract String getFacePreferenceKey();

    public abstract String getFingerprintPreferenceKey();

    public abstract String getUnlockPhonePreferenceKey();

    public abstract String getUseInAppsPreferenceKey();

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mUserId = getActivity().getIntent().getIntExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mFaceManager = Utils.getFaceManagerOrNull(getActivity());
        this.mFingerprintManager = Utils.getFingerprintManagerOrNull(getActivity());
        if (BiometricUtils.containsGatekeeperPasswordHandle(getIntent())) {
            this.mGkPwHandle = BiometricUtils.getGatekeeperPasswordHandle(getIntent());
        }
        if (bundle != null) {
            this.mConfirmCredential = bundle.getBoolean("confirm_credential");
            this.mDoNotFinishActivity = bundle.getBoolean("do_not_finish_activity");
            if (bundle.containsKey("request_gk_pw_handle")) {
                this.mGkPwHandle = bundle.getLong("request_gk_pw_handle");
            }
        }
        if (this.mGkPwHandle == 0 && !this.mConfirmCredential) {
            this.mConfirmCredential = true;
            launchChooseOrConfirmLock();
        }
        Preference findPreference = findPreference(getUnlockPhonePreferenceKey());
        if (findPreference != null) {
            findPreference.setSummary((CharSequence) getUseAnyBiometricSummary());
        }
        Preference findPreference2 = findPreference(getUseInAppsPreferenceKey());
        if (findPreference2 != null) {
            findPreference2.setSummary((CharSequence) getUseClass2BiometricSummary());
        }
    }

    public void onResume() {
        super.onResume();
        if (!this.mConfirmCredential) {
            this.mDoNotFinishActivity = false;
        }
    }

    public void onStop() {
        super.onStop();
        if (!getActivity().isChangingConfigurations() && !this.mDoNotFinishActivity) {
            BiometricUtils.removeGatekeeperPasswordHandle((Context) getActivity(), this.mGkPwHandle);
            getActivity().finish();
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        if (getFacePreferenceKey().equals(key)) {
            this.mDoNotFinishActivity = true;
            this.mFaceManager.generateChallenge(this.mUserId, new BiometricsSettingsBase$$ExternalSyntheticLambda0(this, preference));
            return true;
        } else if (!getFingerprintPreferenceKey().equals(key)) {
            return super.onPreferenceTreeClick(preference);
        } else {
            this.mDoNotFinishActivity = true;
            this.mFingerprintManager.generateChallenge(this.mUserId, new BiometricsSettingsBase$$ExternalSyntheticLambda1(this, preference));
            return true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPreferenceTreeClick$0(Preference preference, int i, int i2, long j) {
        byte[] requestGatekeeperHat = BiometricUtils.requestGatekeeperHat((Context) getActivity(), this.mGkPwHandle, this.mUserId, j);
        Bundle extras = preference.getExtras();
        extras.putByteArray("hw_auth_token", requestGatekeeperHat);
        extras.putInt("sensor_id", i);
        extras.putLong("challenge", j);
        super.onPreferenceTreeClick(preference);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPreferenceTreeClick$1(Preference preference, int i, int i2, long j) {
        byte[] requestGatekeeperHat = BiometricUtils.requestGatekeeperHat((Context) getActivity(), this.mGkPwHandle, this.mUserId, j);
        Bundle extras = preference.getExtras();
        extras.putByteArray("hw_auth_token", requestGatekeeperHat);
        extras.putLong("challenge", j);
        super.onPreferenceTreeClick(preference);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("confirm_credential", this.mConfirmCredential);
        bundle.putBoolean("do_not_finish_activity", this.mDoNotFinishActivity);
        long j = this.mGkPwHandle;
        if (j != 0) {
            bundle.putLong("request_gk_pw_handle", j);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2001 || i == 2002) {
            this.mConfirmCredential = false;
            this.mDoNotFinishActivity = false;
            if (i2 != 1 && i2 != -1) {
                Log.d(getLogTag(), "Password not confirmed.");
                finish();
            } else if (BiometricUtils.containsGatekeeperPasswordHandle(intent)) {
                this.mGkPwHandle = BiometricUtils.getGatekeeperPasswordHandle(intent);
            } else {
                Log.d(getLogTag(), "Data null or GK PW missing.");
                finish();
            }
        }
    }

    private void launchChooseOrConfirmLock() {
        ChooseLockSettingsHelper.Builder returnCredentials = new ChooseLockSettingsHelper.Builder(getActivity(), this).setRequestCode(2001).setTitle(getString(C0444R.string.security_settings_biometric_preference_title)).setRequestGatekeeperPasswordHandle(true).setForegroundOnly(true).setReturnCredentials(true);
        int i = this.mUserId;
        if (i != -10000) {
            returnCredentials.setUserId(i);
        }
        this.mDoNotFinishActivity = true;
        if (!returnCredentials.show()) {
            Intent chooseLockIntent = BiometricUtils.getChooseLockIntent(getActivity(), getIntent());
            chooseLockIntent.putExtra("hide_insecure_options", true);
            chooseLockIntent.putExtra("request_gk_pw_handle", true);
            chooseLockIntent.putExtra("for_biometrics", true);
            chooseLockIntent.putExtra("page_transition_type", 1);
            int i2 = this.mUserId;
            if (i2 != -10000) {
                chooseLockIntent.putExtra("android.intent.extra.USER_ID", i2);
            }
            startActivityForResult(chooseLockIntent, 2002);
        }
    }

    private String getUseAnyBiometricSummary() {
        FaceManager faceManager = this.mFaceManager;
        boolean z = true;
        boolean z2 = faceManager != null && faceManager.isHardwareDetected();
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager == null || !fingerprintManager.isHardwareDetected()) {
            z = false;
        }
        int useBiometricSummaryRes = getUseBiometricSummaryRes(z2, z);
        if (useBiometricSummaryRes == 0) {
            return "";
        }
        return getString(useBiometricSummaryRes);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0049 A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getUseClass2BiometricSummary() {
        /*
            r6 = this;
            android.hardware.face.FaceManager r0 = r6.mFaceManager
            r1 = 2
            r2 = 0
            r3 = 1
            if (r0 == 0) goto L_0x0023
            java.util.List r0 = r0.getSensorPropertiesInternal()
            java.util.Iterator r0 = r0.iterator()
        L_0x000f:
            boolean r4 = r0.hasNext()
            if (r4 == 0) goto L_0x0023
            java.lang.Object r4 = r0.next()
            android.hardware.face.FaceSensorPropertiesInternal r4 = (android.hardware.face.FaceSensorPropertiesInternal) r4
            int r4 = r4.sensorStrength
            if (r4 == r3) goto L_0x0021
            if (r4 != r1) goto L_0x000f
        L_0x0021:
            r0 = r3
            goto L_0x0024
        L_0x0023:
            r0 = r2
        L_0x0024:
            android.hardware.fingerprint.FingerprintManager r4 = r6.mFingerprintManager
            if (r4 == 0) goto L_0x0043
            java.util.List r4 = r4.getSensorPropertiesInternal()
            java.util.Iterator r4 = r4.iterator()
        L_0x0030:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x0043
            java.lang.Object r5 = r4.next()
            android.hardware.fingerprint.FingerprintSensorPropertiesInternal r5 = (android.hardware.fingerprint.FingerprintSensorPropertiesInternal) r5
            int r5 = r5.sensorStrength
            if (r5 == r3) goto L_0x0042
            if (r5 != r1) goto L_0x0030
        L_0x0042:
            r2 = r3
        L_0x0043:
            int r0 = getUseBiometricSummaryRes(r0, r2)
            if (r0 != 0) goto L_0x004c
            java.lang.String r6 = ""
            goto L_0x0050
        L_0x004c:
            java.lang.String r6 = r6.getString(r0)
        L_0x0050:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.biometrics.combination.BiometricsSettingsBase.getUseClass2BiometricSummary():java.lang.String");
    }
}
