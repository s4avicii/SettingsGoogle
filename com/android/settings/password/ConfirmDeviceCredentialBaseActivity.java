package com.android.settings.password;

import android.app.KeyguardManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserManager;
import android.util.Log;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.window.C0444R;
import com.android.settings.SettingsActivity;
import com.android.settings.SetupWizardUtils;
import com.android.settings.Utils;
import com.android.settings.password.ConfirmLockPassword;
import com.android.settings.password.ConfirmLockPattern;
import com.google.android.setupdesign.util.ThemeHelper;

public abstract class ConfirmDeviceCredentialBaseActivity extends SettingsActivity {
    private ConfirmCredentialTheme mConfirmCredentialTheme;
    private boolean mEnterAnimationPending;
    private boolean mFirstTimeVisible = true;
    private boolean mIsKeyguardLocked = false;
    private boolean mRestoring;

    enum ConfirmCredentialTheme {
        NORMAL,
        DARK,
        WORK
    }

    /* access modifiers changed from: protected */
    public boolean isToolbarEnabled() {
        return false;
    }

    private boolean isInternalActivity() {
        return (this instanceof ConfirmLockPassword.InternalActivity) || (this instanceof ConfirmLockPattern.InternalActivity);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        boolean z;
        try {
            boolean z2 = false;
            if (UserManager.get(this).isManagedProfile(Utils.getCredentialOwnerUserId(this, Utils.getUserIdFromBundle(this, getIntent().getExtras(), isInternalActivity())))) {
                setTheme(SetupWizardUtils.getTheme(this, getIntent()));
                this.mConfirmCredentialTheme = ConfirmCredentialTheme.WORK;
            } else if (getIntent().getBooleanExtra("com.android.settings.ConfirmCredentials.darkTheme", false)) {
                setTheme(C0444R.style.Theme_ConfirmDeviceCredentialsDark);
                this.mConfirmCredentialTheme = ConfirmCredentialTheme.DARK;
            } else {
                setTheme(SetupWizardUtils.getTheme(this, getIntent()));
                this.mConfirmCredentialTheme = ConfirmCredentialTheme.NORMAL;
            }
            ThemeHelper.trySetDynamicColor(this);
            super.onCreate(bundle);
            if (this.mConfirmCredentialTheme == ConfirmCredentialTheme.NORMAL) {
                findViewById(C0444R.C0448id.content_parent).setFitsSystemWindows(false);
            }
            getWindow().addFlags(8192);
            if (bundle == null) {
                z = ((KeyguardManager) getSystemService(KeyguardManager.class)).isKeyguardLocked();
            } else {
                z = bundle.getBoolean("STATE_IS_KEYGUARD_LOCKED", false);
            }
            this.mIsKeyguardLocked = z;
            if (z && getIntent().getBooleanExtra("com.android.settings.ConfirmCredentials.showWhenLocked", false)) {
                getWindow().addFlags(524288);
            }
            setTitle((CharSequence) getIntent().getStringExtra("com.android.settings.ConfirmCredentials.title"));
            if (getActionBar() != null) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeButtonEnabled(true);
            }
            if (bundle != null) {
                z2 = true;
            }
            this.mRestoring = z2;
        } catch (SecurityException e) {
            Log.e("ConfirmDeviceCredentialBaseActivity", "Invalid user Id supplied", e);
            finish();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("STATE_IS_KEYGUARD_LOCKED", this.mIsKeyguardLocked);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }

    public void onResume() {
        super.onResume();
        if (!isChangingConfigurations() && !this.mRestoring && this.mConfirmCredentialTheme == ConfirmCredentialTheme.DARK && this.mFirstTimeVisible) {
            this.mFirstTimeVisible = false;
            prepareEnterAnimation();
            this.mEnterAnimationPending = true;
        }
    }

    private ConfirmDeviceCredentialBaseFragment getFragment() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(C0444R.C0448id.main_content);
        if (findFragmentById == null || !(findFragmentById instanceof ConfirmDeviceCredentialBaseFragment)) {
            return null;
        }
        return (ConfirmDeviceCredentialBaseFragment) findFragmentById;
    }

    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        if (this.mEnterAnimationPending) {
            startEnterAnimation();
            this.mEnterAnimationPending = false;
        }
    }

    public void onStop() {
        super.onStop();
        boolean booleanExtra = getIntent().getBooleanExtra("foreground_only", false);
        if (!isChangingConfigurations() && booleanExtra) {
            finish();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        new Handler(Looper.myLooper()).postDelayed(ConfirmDeviceCredentialBaseActivity$$ExternalSyntheticLambda0.INSTANCE, 5000);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onDestroy$0() {
        System.gc();
        System.runFinalization();
        System.gc();
    }

    public void finish() {
        super.finish();
        if (getIntent().getBooleanExtra("com.android.settings.ConfirmCredentials.useFadeAnimation", false)) {
            overridePendingTransition(0, C0444R.C0445anim.confirm_credential_biometric_transition_exit);
        }
    }

    public void prepareEnterAnimation() {
        getFragment().prepareEnterAnimation();
    }

    public void startEnterAnimation() {
        getFragment().startEnterAnimation();
    }

    public ConfirmCredentialTheme getConfirmCredentialTheme() {
        return this.mConfirmCredentialTheme;
    }
}
