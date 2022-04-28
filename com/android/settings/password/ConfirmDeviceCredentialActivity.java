package com.android.settings.password;

import android.app.admin.DevicePolicyManager;
import android.app.trust.TrustManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.biometrics.PromptInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.window.C0444R;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.Utils;
import com.android.settings.password.ChooseLockSettingsHelper;
import java.util.concurrent.Executor;

public class ConfirmDeviceCredentialActivity extends FragmentActivity {
    public static final String TAG = "ConfirmDeviceCredentialActivity";
    private BiometricPrompt.AuthenticationCallback mAuthenticationCallback = new BiometricPrompt.AuthenticationCallback() {
        public void onAuthenticationError(int i, CharSequence charSequence) {
            if (!ConfirmDeviceCredentialActivity.this.mGoingToBackground) {
                ConfirmDeviceCredentialActivity.this.mWaitingForBiometricCallback = false;
                if (i == 10 || i == 5) {
                    ConfirmDeviceCredentialActivity.this.finish();
                } else if (ConfirmDeviceCredentialActivity.this.mUserManager.getUserInfo(ConfirmDeviceCredentialActivity.this.mUserId) == null) {
                    String str = ConfirmDeviceCredentialActivity.TAG;
                    Log.i(str, "Finishing, user no longer valid: " + ConfirmDeviceCredentialActivity.this.mUserId);
                    ConfirmDeviceCredentialActivity.this.finish();
                } else {
                    ConfirmDeviceCredentialActivity.this.showConfirmCredentials();
                }
            } else if (ConfirmDeviceCredentialActivity.this.mWaitingForBiometricCallback) {
                ConfirmDeviceCredentialActivity.this.mWaitingForBiometricCallback = false;
                ConfirmDeviceCredentialActivity.this.finish();
            }
        }

        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult authenticationResult) {
            boolean z = false;
            ConfirmDeviceCredentialActivity.this.mWaitingForBiometricCallback = false;
            ConfirmDeviceCredentialActivity.this.mTrustManager.setDeviceLockedForUser(ConfirmDeviceCredentialActivity.this.mUserId, false);
            if (authenticationResult.getAuthenticationType() == 1) {
                z = true;
            }
            ConfirmDeviceCredentialUtils.reportSuccessfulAttempt(ConfirmDeviceCredentialActivity.this.mLockPatternUtils, ConfirmDeviceCredentialActivity.this.mUserManager, ConfirmDeviceCredentialActivity.this.mDevicePolicyManager, ConfirmDeviceCredentialActivity.this.mUserId, z);
            ConfirmDeviceCredentialUtils.checkForPendingIntent(ConfirmDeviceCredentialActivity.this);
            ConfirmDeviceCredentialActivity.this.setResult(-1);
            ConfirmDeviceCredentialActivity.this.finish();
        }

        public void onAuthenticationFailed() {
            ConfirmDeviceCredentialActivity.this.mWaitingForBiometricCallback = false;
            ConfirmDeviceCredentialActivity.this.mDevicePolicyManager.reportFailedBiometricAttempt(ConfirmDeviceCredentialActivity.this.mUserId);
        }

        public void onSystemEvent(int i) {
            String str = ConfirmDeviceCredentialActivity.TAG;
            Log.d(str, "SystemEvent: " + i);
            if (i == 1) {
                ConfirmDeviceCredentialActivity.this.finish();
            }
        }
    };
    private BiometricFragment mBiometricFragment;
    private boolean mCheckDevicePolicyManager;
    private Context mContext;
    private int mCredentialMode;
    private String mDetails;
    /* access modifiers changed from: private */
    public DevicePolicyManager mDevicePolicyManager;
    private Executor mExecutor = new ConfirmDeviceCredentialActivity$$ExternalSyntheticLambda6(this);
    /* access modifiers changed from: private */
    public boolean mGoingToBackground;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public LockPatternUtils mLockPatternUtils;
    private String mTitle;
    /* access modifiers changed from: private */
    public TrustManager mTrustManager;
    /* access modifiers changed from: private */
    public int mUserId;
    /* access modifiers changed from: private */
    public UserManager mUserManager;
    /* access modifiers changed from: private */
    public boolean mWaitingForBiometricCallback;

    public static class InternalActivity extends ConfirmDeviceCredentialActivity {
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        boolean z;
        super.onCreate(bundle);
        getWindow().addFlags(Integer.MIN_VALUE);
        boolean z2 = false;
        getWindow().setStatusBarColor(0);
        this.mDevicePolicyManager = (DevicePolicyManager) getSystemService(DevicePolicyManager.class);
        this.mUserManager = UserManager.get(this);
        this.mTrustManager = (TrustManager) getSystemService(TrustManager.class);
        this.mLockPatternUtils = new LockPatternUtils(this);
        Intent intent = getIntent();
        this.mContext = this;
        this.mCheckDevicePolicyManager = intent.getBooleanExtra("check_dpm", false);
        this.mTitle = intent.getStringExtra("android.app.extra.TITLE");
        this.mDetails = intent.getStringExtra("android.app.extra.DESCRIPTION");
        String stringExtra = intent.getStringExtra("android.app.extra.ALTERNATE_BUTTON_LABEL");
        boolean equals = "android.app.action.CONFIRM_FRP_CREDENTIAL".equals(intent.getAction());
        this.mUserId = UserHandle.myUserId();
        if (isInternalActivity()) {
            try {
                this.mUserId = Utils.getUserIdFromBundle(this, intent.getExtras());
            } catch (SecurityException e) {
                Log.e(TAG, "Invalid intent extra", e);
            }
        }
        int credentialOwnerProfile = this.mUserManager.getCredentialOwnerProfile(this.mUserId);
        boolean isManagedProfile = UserManager.get(this).isManagedProfile(credentialOwnerProfile);
        if (this.mTitle == null && isManagedProfile) {
            this.mTitle = getTitleFromOrganizationName(this.mUserId);
        }
        PromptInfo promptInfo = new PromptInfo();
        promptInfo.setTitle(this.mTitle);
        promptInfo.setDescription(this.mDetails);
        promptInfo.setDisallowBiometricsIfPolicyExists(this.mCheckDevicePolicyManager);
        int credentialType = Utils.getCredentialType(this.mContext, credentialOwnerProfile);
        if (this.mTitle == null) {
            promptInfo.setDeviceCredentialTitle(getTitleFromCredentialType(credentialType, isManagedProfile));
        }
        if (this.mDetails == null) {
            promptInfo.setSubtitle(getDetailsFromCredentialType(credentialType, isManagedProfile));
        }
        if (equals) {
            z2 = new ChooseLockSettingsHelper.Builder(this).setHeader(this.mTitle).setDescription(this.mDetails).setAlternateButton(stringExtra).setExternal(true).setUserId(-9999).show();
            z = false;
        } else {
            if (!isManagedProfile || !isInternalActivity()) {
                this.mCredentialMode = 1;
                if (isBiometricAllowed(credentialOwnerProfile, this.mUserId)) {
                    showBiometricPrompt(promptInfo);
                } else {
                    showConfirmCredentials();
                    z = false;
                    z2 = true;
                }
            } else {
                this.mCredentialMode = 2;
                if (isBiometricAllowed(credentialOwnerProfile, this.mUserId)) {
                    showBiometricPrompt(promptInfo);
                } else {
                    showConfirmCredentials();
                    z = false;
                    z2 = true;
                }
            }
            z = true;
        }
        if (z2) {
            finish();
        } else if (z) {
            this.mWaitingForBiometricCallback = true;
        } else {
            Log.d(TAG, "No pattern, password or PIN set.");
            setResult(-1);
            finish();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getTitleFromCredentialType$1() throws Exception {
        return getString(C0444R.string.lockpassword_confirm_your_work_pin_header);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getTitleFromCredentialType$2() throws Exception {
        return getString(C0444R.string.lockpassword_confirm_your_work_pattern_header);
    }

    private String getTitleFromCredentialType(int i, boolean z) {
        if (i != 1) {
            if (i != 3) {
                if (i != 4) {
                    return null;
                }
                if (z) {
                    return this.mDevicePolicyManager.getString("Settings.CONFIRM_WORK_PROFILE_PASSWORD_HEADER", new ConfirmDeviceCredentialActivity$$ExternalSyntheticLambda2(this));
                }
                return getString(C0444R.string.lockpassword_confirm_your_password_header);
            } else if (z) {
                return this.mDevicePolicyManager.getString("Settings.CONFIRM_WORK_PROFILE_PIN_HEADER", new ConfirmDeviceCredentialActivity$$ExternalSyntheticLambda3(this));
            } else {
                return getString(C0444R.string.lockpassword_confirm_your_pin_header);
            }
        } else if (z) {
            return this.mDevicePolicyManager.getString("Settings.CONFIRM_WORK_PROFILE_PATTERN_HEADER", new ConfirmDeviceCredentialActivity$$ExternalSyntheticLambda5(this));
        } else {
            return getString(C0444R.string.lockpassword_confirm_your_pattern_header);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getTitleFromCredentialType$3() throws Exception {
        return getString(C0444R.string.lockpassword_confirm_your_work_password_header);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getDetailsFromCredentialType$4() throws Exception {
        return getString(C0444R.string.lockpassword_confirm_your_pin_generic_profile);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getDetailsFromCredentialType$5() throws Exception {
        return getString(C0444R.string.lockpassword_confirm_your_pattern_generic_profile);
    }

    private String getDetailsFromCredentialType(int i, boolean z) {
        if (i != 1) {
            if (i != 3) {
                if (i != 4) {
                    return null;
                }
                if (z) {
                    return this.mDevicePolicyManager.getString("Settings.WORK_PROFILE_CONFIRM_PASSWORD", new ConfirmDeviceCredentialActivity$$ExternalSyntheticLambda0(this));
                }
                return getString(C0444R.string.lockpassword_confirm_your_password_generic);
            } else if (z) {
                return this.mDevicePolicyManager.getString("Settings.WORK_PROFILE_CONFIRM_PIN", new ConfirmDeviceCredentialActivity$$ExternalSyntheticLambda4(this));
            } else {
                return getString(C0444R.string.lockpassword_confirm_your_pin_generic);
            }
        } else if (z) {
            return this.mDevicePolicyManager.getString("Settings.WORK_PROFILE_CONFIRM_PATTERN", new ConfirmDeviceCredentialActivity$$ExternalSyntheticLambda1(this));
        } else {
            return getString(C0444R.string.lockpassword_confirm_your_pattern_generic);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getDetailsFromCredentialType$6() throws Exception {
        return getString(C0444R.string.lockpassword_confirm_your_password_generic_profile);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        setVisible(true);
    }

    public void onPause() {
        super.onPause();
        if (!isChangingConfigurations()) {
            this.mGoingToBackground = true;
            if (!this.mWaitingForBiometricCallback) {
                finish();
                return;
            }
            return;
        }
        this.mGoingToBackground = false;
    }

    private boolean isStrongAuthRequired(int i) {
        return !this.mLockPatternUtils.isBiometricAllowedForUser(i) || !this.mUserManager.isUserUnlocked(this.mUserId);
    }

    private boolean isBiometricAllowed(int i, int i2) {
        return !isStrongAuthRequired(i) && !this.mLockPatternUtils.hasPendingEscrowToken(i2);
    }

    private void showBiometricPrompt(PromptInfo promptInfo) {
        boolean z;
        BiometricFragment biometricFragment = (BiometricFragment) getSupportFragmentManager().findFragmentByTag("fragment");
        this.mBiometricFragment = biometricFragment;
        if (biometricFragment == null) {
            this.mBiometricFragment = BiometricFragment.newInstance(promptInfo);
            z = true;
        } else {
            z = false;
        }
        this.mBiometricFragment.setCallbacks(this.mExecutor, this.mAuthenticationCallback);
        this.mBiometricFragment.setUser(this.mUserId);
        if (z) {
            getSupportFragmentManager().beginTransaction().add((Fragment) this.mBiometricFragment, "fragment").commit();
        }
    }

    /* access modifiers changed from: private */
    public void showConfirmCredentials() {
        boolean z;
        int i = this.mCredentialMode;
        if (i == 2) {
            z = new ChooseLockSettingsHelper.Builder(this).setHeader(this.mTitle).setDescription(this.mDetails).setExternal(true).setUserId(this.mUserId).setForceVerifyPath(true).show();
        } else {
            z = i == 1 ? new ChooseLockSettingsHelper.Builder(this).setHeader(this.mTitle).setDescription(this.mDetails).setExternal(true).setUserId(this.mUserId).show() : false;
        }
        if (!z) {
            Log.d(TAG, "No pin/pattern/pass set");
            setResult(-1);
        }
        finish();
    }

    private boolean isInternalActivity() {
        return this instanceof InternalActivity;
    }

    private String getTitleFromOrganizationName(int i) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService("device_policy");
        CharSequence organizationNameForUser = devicePolicyManager != null ? devicePolicyManager.getOrganizationNameForUser(i) : null;
        if (organizationNameForUser != null) {
            return organizationNameForUser.toString();
        }
        return null;
    }
}
