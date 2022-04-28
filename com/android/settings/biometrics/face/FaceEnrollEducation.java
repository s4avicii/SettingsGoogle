package com.android.settings.biometrics.face;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.face.FaceManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.CompoundButton;
import androidx.window.C0444R;
import com.airbnb.lottie.LottieAnimationView;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.android.settings.biometrics.BiometricUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.view.IllustrationVideoView;

public class FaceEnrollEducation extends BiometricEnrollBase {
    private boolean mAccessibilityEnabled;
    private FaceManager mFaceManager;
    /* access modifiers changed from: private */
    public View mIllustrationAccessibility;
    private IllustrationVideoView mIllustrationDefault;
    private LottieAnimationView mIllustrationLottie;
    private boolean mIsUsingLottie;
    private boolean mNextClicked;
    private Intent mResultIntent;
    private FaceEnrollAccessibilityToggle mSwitchDiversity;
    private final CompoundButton.OnCheckedChangeListener mSwitchDiversityListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            FaceEnrollEducation.this.setDescriptionText(z ? C0444R.string.security_settings_face_enroll_education_message_accessibility : C0444R.string.security_settings_face_enroll_education_message);
            if (z) {
                FaceEnrollEducation.this.hideDefaultIllustration();
                FaceEnrollEducation.this.mIllustrationAccessibility.setVisibility(0);
                return;
            }
            FaceEnrollEducation.this.showDefaultIllustration();
            FaceEnrollEducation.this.mIllustrationAccessibility.setVisibility(4);
        }
    };

    public int getMetricsCategory() {
        return 1506;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0444R.C0450layout.face_enroll_education);
        setTitle(C0444R.string.security_settings_face_enroll_education_title);
        setDescriptionText((int) C0444R.string.security_settings_face_enroll_education_message);
        this.mFaceManager = Utils.getFaceManagerOrNull(this);
        this.mIllustrationDefault = (IllustrationVideoView) findViewById(C0444R.C0448id.illustration_default);
        this.mIllustrationLottie = (LottieAnimationView) findViewById(C0444R.C0448id.illustration_lottie);
        this.mIllustrationAccessibility = findViewById(C0444R.C0448id.illustration_accessibility);
        boolean z = getResources().getBoolean(C0444R.bool.config_face_education_use_lottie);
        this.mIsUsingLottie = z;
        boolean z2 = false;
        if (z) {
            this.mIllustrationDefault.stop();
            this.mIllustrationDefault.setVisibility(4);
            this.mIllustrationLottie.setAnimation((int) C0444R.raw.face_education_lottie);
            this.mIllustrationLottie.setVisibility(0);
            this.mIllustrationLottie.playAnimation();
        }
        this.mFooterBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        if (WizardManagerHelper.isAnySetupWizard(getIntent())) {
            this.mFooterBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.skip_label).setListener(new FaceEnrollEducation$$ExternalSyntheticLambda2(this)).setButtonType(7).setTheme(C0444R.style.SudGlifButton_Secondary).build());
        } else {
            this.mFooterBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.security_settings_face_enroll_introduction_cancel).setListener(new FaceEnrollEducation$$ExternalSyntheticLambda2(this)).setButtonType(2).setTheme(C0444R.style.SudGlifButton_Secondary).build());
        }
        FooterButton build = new FooterButton.Builder(this).setText((int) C0444R.string.security_settings_face_enroll_education_start).setListener(new FaceEnrollEducation$$ExternalSyntheticLambda1(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build();
        AccessibilityManager accessibilityManager = (AccessibilityManager) getApplicationContext().getSystemService(AccessibilityManager.class);
        if (accessibilityManager != null) {
            if (accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled()) {
                z2 = true;
            }
            this.mAccessibilityEnabled = z2;
        }
        this.mFooterBarMixin.setPrimaryButton(build);
        Button button = (Button) findViewById(C0444R.C0448id.accessibility_button);
        button.setOnClickListener(new FaceEnrollEducation$$ExternalSyntheticLambda4(this, button));
        FaceEnrollAccessibilityToggle faceEnrollAccessibilityToggle = (FaceEnrollAccessibilityToggle) findViewById(C0444R.C0448id.toggle_diversity);
        this.mSwitchDiversity = faceEnrollAccessibilityToggle;
        faceEnrollAccessibilityToggle.setListener(this.mSwitchDiversityListener);
        this.mSwitchDiversity.setOnClickListener(new FaceEnrollEducation$$ExternalSyntheticLambda3(this));
        if (this.mAccessibilityEnabled) {
            button.callOnClick();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(Button button, View view) {
        this.mSwitchDiversity.setChecked(true);
        button.setVisibility(8);
        this.mSwitchDiversity.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        this.mSwitchDiversity.getSwitch().toggle();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mSwitchDiversityListener.onCheckedChanged(this.mSwitchDiversity.getSwitch(), this.mSwitchDiversity.isChecked());
        if (this.mFaceManager.getEnrolledFaces(this.mUserId).size() >= getResources().getInteger(17694829)) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldFinishWhenBackgrounded() {
        return super.shouldFinishWhenBackgrounded() && !this.mNextClicked;
    }

    /* access modifiers changed from: protected */
    public void onNextButtonClick(View view) {
        Intent intent = new Intent();
        byte[] bArr = this.mToken;
        if (bArr != null) {
            intent.putExtra("hw_auth_token", bArr);
        }
        int i = this.mUserId;
        if (i != -10000) {
            intent.putExtra("android.intent.extra.USER_ID", i);
        }
        intent.putExtra("challenge", this.mChallenge);
        intent.putExtra("sensor_id", this.mSensorId);
        intent.putExtra("from_settings_summary", this.mFromSettingsSummary);
        BiometricUtils.copyMultiBiometricExtras(getIntent(), intent);
        String string = getString(C0444R.string.config_face_enroll);
        if (!TextUtils.isEmpty(string)) {
            intent.setComponent(ComponentName.unflattenFromString(string));
        } else {
            intent.setClass(this, FaceEnrollEnrolling.class);
        }
        WizardManagerHelper.copyWizardManagerExtras(getIntent(), intent);
        Intent intent2 = this.mResultIntent;
        if (intent2 != null) {
            intent.putExtras(intent2);
        }
        intent.putExtra("accessibility_diversity", !this.mSwitchDiversity.isChecked());
        if (this.mSwitchDiversity.isChecked() || !this.mAccessibilityEnabled) {
            startActivityForResult(intent, 2);
            this.mNextClicked = true;
            return;
        }
        FaceEnrollAccessibilityDialog newInstance = FaceEnrollAccessibilityDialog.newInstance();
        newInstance.setPositiveButtonListener(new FaceEnrollEducation$$ExternalSyntheticLambda0(this, intent));
        newInstance.show(getSupportFragmentManager(), FaceEnrollAccessibilityDialog.class.getName());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNextButtonClick$2(Intent intent, DialogInterface dialogInterface, int i) {
        startActivityForResult(intent, 2);
        this.mNextClicked = true;
    }

    /* access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        if (!BiometricUtils.tryStartingNextBiometricEnroll(this, 6, "edu_skip")) {
            setResult(2);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mResultIntent = intent;
        if (i2 == 3) {
            setResult(i2, intent);
            finish();
        } else if (i != 2 && i != 6) {
        } else {
            if (i2 == 2 || i2 == 1 || i2 == 11) {
                setResult(i2, intent);
                finish();
            }
        }
    }

    /* access modifiers changed from: private */
    public void hideDefaultIllustration() {
        if (this.mIsUsingLottie) {
            this.mIllustrationLottie.cancelAnimation();
            this.mIllustrationLottie.setVisibility(4);
            return;
        }
        this.mIllustrationDefault.stop();
        this.mIllustrationDefault.setVisibility(4);
    }

    /* access modifiers changed from: private */
    public void showDefaultIllustration() {
        if (this.mIsUsingLottie) {
            this.mIllustrationLottie.setVisibility(0);
            this.mIllustrationLottie.playAnimation();
            return;
        }
        this.mIllustrationDefault.setVisibility(0);
        this.mIllustrationDefault.start();
    }
}
