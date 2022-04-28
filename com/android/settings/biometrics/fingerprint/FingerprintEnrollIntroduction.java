package com.android.settings.biometrics.fingerprint;

import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.window.C0444R;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricEnrollIntroduction;
import com.android.settings.biometrics.BiometricUtils;
import com.android.settingslib.HelpUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.span.LinkSpan;
import java.util.Objects;

public class FingerprintEnrollIntroduction extends BiometricEnrollIntroduction {
    private DevicePolicyManager mDevicePolicyManager;
    private FingerprintManager mFingerprintManager;
    private FooterButton mPrimaryFooterButton;
    private FooterButton mSecondaryFooterButton;

    /* access modifiers changed from: protected */
    public int getAgreeButtonTextRes() {
        return C0444R.string.security_settings_fingerprint_enroll_introduction_agree;
    }

    /* access modifiers changed from: protected */
    public int getConfirmLockTitleResId() {
        return C0444R.string.security_settings_fingerprint_preference_title;
    }

    /* access modifiers changed from: protected */
    public String getExtraKeyForBiometric() {
        return "for_fingerprint";
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage2() {
        return C0444R.string.f87xdb87ba0d;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage3() {
        return C0444R.string.f88xdb87ba0e;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage4() {
        return C0444R.string.f89xdb87ba0f;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage5() {
        return C0444R.string.f90xdb87ba10;
    }

    /* access modifiers changed from: protected */
    public int getFooterTitle1() {
        return C0444R.string.security_settings_fingerprint_enroll_introduction_footer_title_1;
    }

    /* access modifiers changed from: protected */
    public int getFooterTitle2() {
        return C0444R.string.security_settings_fingerprint_enroll_introduction_footer_title_2;
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDefault() {
        return C0444R.string.security_settings_fingerprint_enroll_introduction_title;
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDisabledByAdmin() {
        return C0444R.string.f84x2980692c;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResource() {
        return C0444R.C0450layout.fingerprint_enroll_introduction;
    }

    public int getMetricsCategory() {
        return 243;
    }

    public int getModality() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public int getMoreButtonTextRes() {
        return C0444R.string.security_settings_face_enroll_introduction_more;
    }

    /* access modifiers changed from: package-private */
    public int getNegativeButtonTextId() {
        return C0444R.string.security_settings_fingerprint_enroll_introduction_no_thanks;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        FingerprintManager fingerprintManagerOrNull = Utils.getFingerprintManagerOrNull(this);
        this.mFingerprintManager = fingerprintManagerOrNull;
        if (fingerprintManagerOrNull == null) {
            Log.e("FingerprintIntro", "Null FingerprintManager");
            finish();
            return;
        }
        super.onCreate(bundle);
        this.mDevicePolicyManager = (DevicePolicyManager) getSystemService(DevicePolicyManager.class);
        ((ImageView) findViewById(C0444R.C0448id.icon_fingerprint)).getDrawable().setColorFilter(getIconColorFilter());
        ((ImageView) findViewById(C0444R.C0448id.icon_device_locked)).getDrawable().setColorFilter(getIconColorFilter());
        ((ImageView) findViewById(C0444R.C0448id.icon_trash_can)).getDrawable().setColorFilter(getIconColorFilter());
        ((ImageView) findViewById(C0444R.C0448id.icon_info)).getDrawable().setColorFilter(getIconColorFilter());
        ((ImageView) findViewById(C0444R.C0448id.icon_link)).getDrawable().setColorFilter(getIconColorFilter());
        ((TextView) findViewById(C0444R.C0448id.footer_message_2)).setText(getFooterMessage2());
        ((TextView) findViewById(C0444R.C0448id.footer_message_3)).setText(getFooterMessage3());
        ((TextView) findViewById(C0444R.C0448id.footer_message_4)).setText(getFooterMessage4());
        ((TextView) findViewById(C0444R.C0448id.footer_message_5)).setText(getFooterMessage5());
        ((TextView) findViewById(C0444R.C0448id.footer_title_1)).setText(getFooterTitle1());
        ((TextView) findViewById(C0444R.C0448id.footer_title_2)).setText(getFooterTitle2());
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        boolean z = false;
        boolean z2 = i == 2 || i == 6;
        if (i2 == 2 || i2 == 11 || i2 == 1) {
            z = true;
        }
        if (z2 && z) {
            intent = setSkipPendingEnroll(intent);
        }
        super.onActivityResult(i, i2, intent);
    }

    /* access modifiers changed from: protected */
    public void onCancelButtonClick(View view) {
        setResult(2, setSkipPendingEnroll(new Intent()));
        finish();
    }

    /* access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        onCancelButtonClick(view);
    }

    /* access modifiers changed from: protected */
    public boolean isDisabledByAdmin() {
        return RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(this, 32, this.mUserId) != null;
    }

    /* access modifiers changed from: protected */
    public String getDescriptionDisabledByAdmin() {
        return this.mDevicePolicyManager.getString("Settings.FINGERPRINT_UNLOCK_DISABLED", new FingerprintEnrollIntroduction$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getDescriptionDisabledByAdmin$0() throws Exception {
        return getString(C0444R.string.f83x4ab1f9db);
    }

    /* access modifiers changed from: protected */
    public FooterButton getCancelButton() {
        FooterBarMixin footerBarMixin = this.mFooterBarMixin;
        if (footerBarMixin != null) {
            return footerBarMixin.getSecondaryButton();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public FooterButton getNextButton() {
        FooterBarMixin footerBarMixin = this.mFooterBarMixin;
        if (footerBarMixin != null) {
            return footerBarMixin.getPrimaryButton();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public TextView getErrorTextView() {
        return (TextView) findViewById(C0444R.C0448id.error_text);
    }

    /* access modifiers changed from: protected */
    public int checkMaxEnrolled() {
        boolean isAnySetupWizard = WizardManagerHelper.isAnySetupWizard(getIntent());
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager == null) {
            return C0444R.string.fingerprint_intro_error_unknown;
        }
        int i = ((FingerprintSensorPropertiesInternal) fingerprintManager.getSensorPropertiesInternal().get(0)).maxEnrollmentsPerUser;
        int size = this.mFingerprintManager.getEnrolledFingerprints(this.mUserId).size();
        int integer = getApplicationContext().getResources().getInteger(C0444R.integer.suw_max_fingerprints_enrollable);
        if (isAnySetupWizard) {
            if (size >= integer) {
                return C0444R.string.fingerprint_intro_error_max;
            }
            return 0;
        } else if (size >= i) {
            return C0444R.string.fingerprint_intro_error_max;
        } else {
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    public void getChallenge(BiometricEnrollIntroduction.GenerateChallengeCallback generateChallengeCallback) {
        FingerprintManager fingerprintManagerOrNull = Utils.getFingerprintManagerOrNull(this);
        this.mFingerprintManager = fingerprintManagerOrNull;
        if (fingerprintManagerOrNull == null) {
            generateChallengeCallback.onChallengeGenerated(0, 0, 0);
            return;
        }
        int i = this.mUserId;
        Objects.requireNonNull(generateChallengeCallback);
        fingerprintManagerOrNull.generateChallenge(i, new FingerprintEnrollIntroduction$$ExternalSyntheticLambda0(generateChallengeCallback));
    }

    /* access modifiers changed from: protected */
    public Intent getEnrollingIntent() {
        Intent intent = new Intent(this, FingerprintEnrollFindSensor.class);
        if (BiometricUtils.containsGatekeeperPasswordHandle(getIntent())) {
            intent.putExtra("gk_pw_handle", BiometricUtils.getGatekeeperPasswordHandle(getIntent()));
        }
        return intent;
    }

    public void onClick(LinkSpan linkSpan) {
        if ("url".equals(linkSpan.getId())) {
            Intent helpIntent = HelpUtils.getHelpIntent(this, getString(C0444R.string.help_url_fingerprint), getClass().getName());
            if (helpIntent == null) {
                Log.w("FingerprintIntro", "Null help intent.");
                return;
            }
            try {
                startActivityForResult(helpIntent, 3);
            } catch (ActivityNotFoundException e) {
                Log.w("FingerprintIntro", "Activity was not found for intent, " + e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public FooterButton getPrimaryFooterButton() {
        if (this.mPrimaryFooterButton == null) {
            this.mPrimaryFooterButton = new FooterButton.Builder(this).setText((int) C0444R.string.security_settings_fingerprint_enroll_introduction_agree).setListener(new FingerprintEnrollIntroduction$$ExternalSyntheticLambda2(this)).setButtonType(6).setTheme(C0444R.style.SudGlifButton_Primary).build();
        }
        return this.mPrimaryFooterButton;
    }

    /* access modifiers changed from: protected */
    public FooterButton getSecondaryFooterButton() {
        if (this.mSecondaryFooterButton == null) {
            this.mSecondaryFooterButton = new FooterButton.Builder(this).setText(getNegativeButtonTextId()).setListener(new FingerprintEnrollIntroduction$$ExternalSyntheticLambda1(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build();
        }
        return this.mSecondaryFooterButton;
    }

    protected static Intent setSkipPendingEnroll(Intent intent) {
        if (intent == null) {
            intent = new Intent();
        }
        intent.putExtra("skip_pending_enroll", true);
        return intent;
    }
}
