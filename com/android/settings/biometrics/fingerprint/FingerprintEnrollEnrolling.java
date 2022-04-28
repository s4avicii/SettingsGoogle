package com.android.settings.biometrics.fingerprint;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.window.C0444R;
import com.airbnb.lottie.LottieAnimationView;
import com.android.settings.biometrics.BiometricEnrollSidecar;
import com.android.settings.biometrics.BiometricUtils;
import com.android.settings.biometrics.BiometricsEnrollEnrolling;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.display.DisplayDensityUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.util.List;

public class FingerprintEnrollEnrolling extends BiometricsEnrollEnrolling {
    private static final AudioAttributes FINGERPRINT_ENROLLING_SONFICATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    private static final VibrationEffect VIBRATE_EFFECT_ERROR = VibrationEffect.createWaveform(new long[]{0, 5, 55, 60}, -1);
    private AccessibilityManager mAccessibilityManager;
    /* access modifiers changed from: private */
    public boolean mAnimationCancelled;
    private boolean mCanAssumeUdfps;
    /* access modifiers changed from: private */
    public final Runnable mDelayedFinishRunnable = new Runnable() {
        public void run() {
            FingerprintEnrollEnrolling fingerprintEnrollEnrolling = FingerprintEnrollEnrolling.this;
            fingerprintEnrollEnrolling.launchFinish(fingerprintEnrollEnrolling.mToken);
        }
    };
    private TextView mErrorText;
    private Interpolator mFastOutLinearInInterpolator;
    private Interpolator mFastOutSlowInInterpolator;
    private FingerprintManager mFingerprintManager;
    private boolean mHaveShownUdfpsSideLottie;
    private boolean mHaveShownUdfpsTipLottie;
    private final Animatable2.AnimationCallback mIconAnimationCallback = new Animatable2.AnimationCallback() {
        public void onAnimationEnd(Drawable drawable) {
            if (!FingerprintEnrollEnrolling.this.mAnimationCancelled) {
                FingerprintEnrollEnrolling.this.mProgressBar.post(new Runnable() {
                    public void run() {
                        FingerprintEnrollEnrolling.this.startIconAnimation();
                    }
                });
            }
        }
    };
    private AnimatedVectorDrawable mIconAnimationDrawable;
    private AnimatedVectorDrawable mIconBackgroundBlinksDrawable;
    private int mIconTouchCount;
    private LottieAnimationView mIllustrationLottie;
    private boolean mIsAccessibilityEnabled;
    private boolean mIsSetupWizard;
    private Interpolator mLinearOutSlowInInterpolator;
    private OrientationEventListener mOrientationEventListener;
    /* access modifiers changed from: private */
    public int mPreviousRotation = 0;
    private ObjectAnimator mProgressAnim;
    private final Animator.AnimatorListener mProgressAnimationListener = new Animator.AnimatorListener() {
        public void onAnimationCancel(Animator animator) {
        }

        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
        }

        public void onAnimationEnd(Animator animator) {
            if (FingerprintEnrollEnrolling.this.mProgressBar.getProgress() >= 10000) {
                FingerprintEnrollEnrolling.this.mProgressBar.postDelayed(FingerprintEnrollEnrolling.this.mDelayedFinishRunnable, FingerprintEnrollEnrolling.this.getFinishDelay());
            }
        }
    };
    /* access modifiers changed from: private */
    public ProgressBar mProgressBar;
    private boolean mRestoring;
    private boolean mShouldShowLottie;
    private final Runnable mShowDialogRunnable = new Runnable() {
        public void run() {
            FingerprintEnrollEnrolling.this.showIconTouchDialog();
        }
    };
    private final Runnable mTouchAgainRunnable = new Runnable() {
        public void run() {
            FingerprintEnrollEnrolling fingerprintEnrollEnrolling = FingerprintEnrollEnrolling.this;
            fingerprintEnrollEnrolling.showError(fingerprintEnrollEnrolling.getString(C0444R.string.security_settings_fingerprint_enroll_lift_touch_again));
        }
    };
    private Vibrator mVibrator;

    public int getMetricsCategory() {
        return 240;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FingerprintManager.class);
        this.mFingerprintManager = fingerprintManager;
        List sensorPropertiesInternal = fingerprintManager.getSensorPropertiesInternal();
        boolean z = false;
        this.mCanAssumeUdfps = sensorPropertiesInternal.size() == 1 && ((FingerprintSensorPropertiesInternal) sensorPropertiesInternal.get(0)).isAnyUdfpsType();
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(AccessibilityManager.class);
        this.mAccessibilityManager = accessibilityManager;
        this.mIsAccessibilityEnabled = accessibilityManager.isEnabled();
        listenOrientationEvent();
        if (this.mCanAssumeUdfps) {
            if (BiometricUtils.isReverseLandscape(getApplicationContext())) {
                setContentView((int) C0444R.C0450layout.udfps_enroll_enrolling_land);
            } else {
                setContentView((int) C0444R.C0450layout.udfps_enroll_enrolling);
            }
            setDescriptionText((int) C0444R.string.security_settings_udfps_enroll_start_message);
        } else {
            setContentView((int) C0444R.C0450layout.fingerprint_enroll_enrolling);
            setDescriptionText((int) C0444R.string.security_settings_fingerprint_enroll_start_message);
        }
        this.mIsSetupWizard = WizardManagerHelper.isAnySetupWizard(getIntent());
        if (this.mCanAssumeUdfps) {
            updateTitleAndDescription();
        } else {
            setHeaderText((int) C0444R.string.security_settings_fingerprint_enroll_repeat_title);
        }
        DisplayDensityUtils displayDensityUtils = new DisplayDensityUtils(getApplicationContext());
        this.mShouldShowLottie = displayDensityUtils.getDefaultDensity() == displayDensityUtils.getValues()[displayDensityUtils.getCurrentIndex()];
        updateOrientation(BiometricUtils.isReverseLandscape(getApplicationContext()) || BiometricUtils.isLandscape(getApplicationContext()) ? 2 : 1);
        this.mErrorText = (TextView) findViewById(C0444R.C0448id.error_text);
        this.mProgressBar = (ProgressBar) findViewById(C0444R.C0448id.fingerprint_progress_bar);
        this.mVibrator = (Vibrator) getSystemService(Vibrator.class);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.security_settings_fingerprint_enroll_enrolling_skip).setListener(new FingerprintEnrollEnrolling$$ExternalSyntheticLambda0(this)).setButtonType(7).setTheme(C0444R.style.SudGlifButton_Secondary).build());
        ProgressBar progressBar = this.mProgressBar;
        LayerDrawable layerDrawable = progressBar != null ? (LayerDrawable) progressBar.getBackground() : null;
        if (layerDrawable != null) {
            this.mIconAnimationDrawable = (AnimatedVectorDrawable) layerDrawable.findDrawableByLayerId(C0444R.C0448id.fingerprint_animation);
            this.mIconBackgroundBlinksDrawable = (AnimatedVectorDrawable) layerDrawable.findDrawableByLayerId(C0444R.C0448id.fingerprint_background);
            this.mIconAnimationDrawable.registerAnimationCallback(this.mIconAnimationCallback);
        }
        this.mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(this, 17563661);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(this, 17563662);
        this.mFastOutLinearInInterpolator = AnimationUtils.loadInterpolator(this, 17563663);
        ProgressBar progressBar2 = this.mProgressBar;
        if (progressBar2 != null) {
            progressBar2.setOnTouchListener(new FingerprintEnrollEnrolling$$ExternalSyntheticLambda1(this));
        }
        if (bundle != null) {
            z = true;
        }
        this.mRestoring = z;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreate$0(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            int i = this.mIconTouchCount + 1;
            this.mIconTouchCount = i;
            if (i == 3) {
                showIconTouchDialog();
            } else {
                this.mProgressBar.postDelayed(this.mShowDialogRunnable, 500);
            }
        } else if (motionEvent.getActionMasked() == 3 || motionEvent.getActionMasked() == 1) {
            this.mProgressBar.removeCallbacks(this.mShowDialogRunnable);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public BiometricEnrollSidecar getSidecar() {
        FingerprintEnrollSidecar fingerprintEnrollSidecar = new FingerprintEnrollSidecar();
        fingerprintEnrollSidecar.setEnrollReason(2);
        return fingerprintEnrollSidecar;
    }

    /* access modifiers changed from: protected */
    public boolean shouldStartAutomatically() {
        if (this.mCanAssumeUdfps) {
            return this.mRestoring;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        updateProgress(false);
        updateTitleAndDescription();
        if (this.mRestoring) {
            startIconAnimation();
        }
    }

    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        if (this.mCanAssumeUdfps) {
            startEnrollment();
        }
        this.mAnimationCancelled = false;
        startIconAnimation();
    }

    /* access modifiers changed from: private */
    public void startIconAnimation() {
        AnimatedVectorDrawable animatedVectorDrawable = this.mIconAnimationDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.start();
        }
    }

    private void stopIconAnimation() {
        this.mAnimationCancelled = true;
        AnimatedVectorDrawable animatedVectorDrawable = this.mIconAnimationDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.stop();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        stopIconAnimation();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        stopListenOrientationEvent();
        super.onDestroy();
    }

    private void animateProgress(int i) {
        if (!this.mCanAssumeUdfps) {
            ObjectAnimator objectAnimator = this.mProgressAnim;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            ProgressBar progressBar = this.mProgressBar;
            ObjectAnimator ofInt = ObjectAnimator.ofInt(progressBar, "progress", new int[]{progressBar.getProgress(), i});
            ofInt.addListener(this.mProgressAnimationListener);
            ofInt.setInterpolator(this.mFastOutSlowInInterpolator);
            ofInt.setDuration(250);
            ofInt.start();
            this.mProgressAnim = ofInt;
        } else if (i >= 10000) {
            getMainThreadHandler().postDelayed(this.mDelayedFinishRunnable, getFinishDelay());
        }
    }

    private void animateFlash() {
        AnimatedVectorDrawable animatedVectorDrawable = this.mIconBackgroundBlinksDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.start();
        }
    }

    /* access modifiers changed from: protected */
    public Intent getFinishIntent() {
        return new Intent(this, FingerprintEnrollFinish.class);
    }

    private void updateTitleAndDescription() {
        if (this.mCanAssumeUdfps) {
            updateTitleAndDescriptionForUdfps();
            return;
        }
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar == null || biometricEnrollSidecar.getEnrollmentSteps() == -1) {
            setDescriptionText((int) C0444R.string.security_settings_fingerprint_enroll_start_message);
        } else {
            setDescriptionText((int) C0444R.string.security_settings_fingerprint_enroll_repeat_message);
        }
    }

    private void updateTitleAndDescriptionForUdfps() {
        int currentStage = getCurrentStage();
        if (currentStage == 0) {
            setHeaderText((int) C0444R.string.security_settings_fingerprint_enroll_repeat_title);
            setDescriptionText((int) C0444R.string.security_settings_udfps_enroll_start_message);
        } else if (currentStage == 1) {
            setHeaderText((int) C0444R.string.security_settings_fingerprint_enroll_repeat_title);
            if (this.mIsAccessibilityEnabled) {
                setDescriptionText((int) C0444R.string.security_settings_udfps_enroll_repeat_a11y_message);
            } else {
                setDescriptionText((int) C0444R.string.security_settings_udfps_enroll_repeat_message);
            }
        } else if (currentStage == 2) {
            setHeaderText((int) C0444R.string.security_settings_udfps_enroll_fingertip_title);
            if (!this.mHaveShownUdfpsTipLottie && this.mIllustrationLottie != null) {
                this.mHaveShownUdfpsTipLottie = true;
                setDescriptionText((CharSequence) "");
                this.mIllustrationLottie.setAnimation((int) C0444R.raw.udfps_tip_hint_lottie);
                this.mIllustrationLottie.setVisibility(0);
                this.mIllustrationLottie.playAnimation();
                this.mIllustrationLottie.setContentDescription(getString(C0444R.string.security_settings_udfps_tip_fingerprint_help));
            }
        } else if (currentStage != 3) {
            getLayout().setHeaderText((int) C0444R.string.security_settings_fingerprint_enroll_udfps_title);
            setDescriptionText((int) C0444R.string.security_settings_udfps_enroll_start_message);
            String string = getString(C0444R.string.security_settings_udfps_enroll_a11y);
            getLayout().getHeaderTextView().setContentDescription(string);
            setTitle(string);
        } else {
            setHeaderText((int) C0444R.string.security_settings_udfps_enroll_edge_title);
            if (!this.mHaveShownUdfpsSideLottie && this.mIllustrationLottie != null) {
                this.mHaveShownUdfpsSideLottie = true;
                setDescriptionText((CharSequence) "");
                this.mIllustrationLottie.setAnimation((int) C0444R.raw.udfps_edge_hint_lottie);
                this.mIllustrationLottie.setVisibility(0);
                this.mIllustrationLottie.playAnimation();
                this.mIllustrationLottie.setContentDescription(getString(C0444R.string.security_settings_udfps_side_fingerprint_help));
            } else if (this.mIllustrationLottie != null) {
            } else {
                if (isStageHalfCompleted()) {
                    setDescriptionText((int) C0444R.string.security_settings_fingerprint_enroll_repeat_message);
                } else {
                    setDescriptionText((int) C0444R.string.security_settings_udfps_enroll_edge_message);
                }
            }
        }
    }

    private int getCurrentStage() {
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar == null || biometricEnrollSidecar.getEnrollmentSteps() == -1) {
            return -1;
        }
        int enrollmentSteps = this.mSidecar.getEnrollmentSteps() - this.mSidecar.getEnrollmentRemaining();
        if (enrollmentSteps < getStageThresholdSteps(0)) {
            return 0;
        }
        if (enrollmentSteps < getStageThresholdSteps(1)) {
            return 1;
        }
        if (enrollmentSteps < getStageThresholdSteps(2)) {
            return 2;
        }
        return 3;
    }

    private boolean isStageHalfCompleted() {
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar == null || biometricEnrollSidecar.getEnrollmentSteps() == -1) {
            return false;
        }
        int enrollmentSteps = this.mSidecar.getEnrollmentSteps() - this.mSidecar.getEnrollmentRemaining();
        int i = 0;
        int i2 = 0;
        while (i < this.mFingerprintManager.getEnrollStageCount()) {
            int stageThresholdSteps = getStageThresholdSteps(i);
            if (enrollmentSteps < i2 || enrollmentSteps >= stageThresholdSteps) {
                i++;
                i2 = stageThresholdSteps;
            } else if (enrollmentSteps - i2 >= (stageThresholdSteps - i2) / 2) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private int getStageThresholdSteps(int i) {
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar != null && biometricEnrollSidecar.getEnrollmentSteps() != -1) {
            return Math.round(((float) this.mSidecar.getEnrollmentSteps()) * this.mFingerprintManager.getEnrollStageThreshold(i));
        }
        Log.w("FingerprintEnrollEnrolling", "getStageThresholdSteps: Enrollment not started yet");
        return 1;
    }

    public void onEnrollmentHelp(int i, CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (!this.mCanAssumeUdfps) {
                this.mErrorText.removeCallbacks(this.mTouchAgainRunnable);
            }
            showError(charSequence);
        }
    }

    public void onEnrollmentError(int i, CharSequence charSequence) {
        FingerprintErrorDialog.showErrorDialog(this, i);
        stopIconAnimation();
        if (!this.mCanAssumeUdfps) {
            this.mErrorText.removeCallbacks(this.mTouchAgainRunnable);
        }
    }

    public void onEnrollmentProgressChange(int i, int i2) {
        updateProgress(true);
        updateTitleAndDescription();
        clearError();
        animateFlash();
        if (!this.mCanAssumeUdfps) {
            this.mErrorText.removeCallbacks(this.mTouchAgainRunnable);
            this.mErrorText.postDelayed(this.mTouchAgainRunnable, 2500);
        } else if (this.mIsAccessibilityEnabled) {
            String string = getString(C0444R.string.security_settings_udfps_enroll_progress_a11y_message, new Object[]{Integer.valueOf((int) ((((float) (i - i2)) / ((float) i)) * 100.0f))});
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(16384);
            obtain.setClassName(getClass().getName());
            obtain.setPackageName(getPackageName());
            obtain.getText().add(string);
            this.mAccessibilityManager.sendAccessibilityEvent(obtain);
        }
    }

    private void updateProgress(boolean z) {
        BiometricEnrollSidecar biometricEnrollSidecar = this.mSidecar;
        if (biometricEnrollSidecar == null || !biometricEnrollSidecar.isEnrolling()) {
            Log.d("FingerprintEnrollEnrolling", "Enrollment not started yet");
            return;
        }
        int progress = getProgress(this.mSidecar.getEnrollmentSteps(), this.mSidecar.getEnrollmentRemaining());
        if (z) {
            animateProgress(progress);
            return;
        }
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
        if (progress >= 10000) {
            this.mDelayedFinishRunnable.run();
        }
    }

    private int getProgress(int i, int i2) {
        if (i == -1) {
            return 0;
        }
        int i3 = i + 1;
        return (Math.max(0, i3 - i2) * 10000) / i3;
    }

    /* access modifiers changed from: private */
    public void showIconTouchDialog() {
        this.mIconTouchCount = 0;
        new IconTouchDialog().show(getSupportFragmentManager(), (String) null);
    }

    /* access modifiers changed from: private */
    public void showError(CharSequence charSequence) {
        if (this.mCanAssumeUdfps) {
            setHeaderText(charSequence);
            setDescriptionText((CharSequence) "");
        } else {
            this.mErrorText.setText(charSequence);
            if (this.mErrorText.getVisibility() == 4) {
                this.mErrorText.setVisibility(0);
                this.mErrorText.setTranslationY((float) getResources().getDimensionPixelSize(C0444R.dimen.fingerprint_error_text_appear_distance));
                this.mErrorText.setAlpha(0.0f);
                this.mErrorText.animate().alpha(1.0f).translationY(0.0f).setDuration(200).setInterpolator(this.mLinearOutSlowInInterpolator).start();
            } else {
                this.mErrorText.animate().cancel();
                this.mErrorText.setAlpha(1.0f);
                this.mErrorText.setTranslationY(0.0f);
            }
        }
        if (isResumed()) {
            this.mVibrator.vibrate(VIBRATE_EFFECT_ERROR, FINGERPRINT_ENROLLING_SONFICATION_ATTRIBUTES);
        }
    }

    private void clearError() {
        if (!this.mCanAssumeUdfps && this.mErrorText.getVisibility() == 0) {
            this.mErrorText.animate().alpha(0.0f).translationY((float) getResources().getDimensionPixelSize(C0444R.dimen.fingerprint_error_text_disappear_distance)).setDuration(100).setInterpolator(this.mFastOutLinearInInterpolator).withEndAction(new FingerprintEnrollEnrolling$$ExternalSyntheticLambda2(this)).start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$clearError$1() {
        this.mErrorText.setVisibility(4);
    }

    private void listenOrientationEvent() {
        C07301 r0 = new OrientationEventListener(this) {
            public void onOrientationChanged(int i) {
                int rotation = FingerprintEnrollEnrolling.this.getDisplay().getRotation();
                if ((FingerprintEnrollEnrolling.this.mPreviousRotation == 1 && rotation == 3) || (FingerprintEnrollEnrolling.this.mPreviousRotation == 3 && rotation == 1)) {
                    FingerprintEnrollEnrolling.this.mPreviousRotation = rotation;
                    FingerprintEnrollEnrolling.this.recreate();
                }
            }
        };
        this.mOrientationEventListener = r0;
        r0.enable();
        this.mPreviousRotation = getDisplay().getRotation();
    }

    private void stopListenOrientationEvent() {
        OrientationEventListener orientationEventListener = this.mOrientationEventListener;
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
        this.mOrientationEventListener = null;
    }

    /* access modifiers changed from: private */
    public long getFinishDelay() {
        return this.mCanAssumeUdfps ? 400 : 250;
    }

    private void updateOrientation(int i) {
        if (i != 1) {
            if (i != 2) {
                Log.e("FingerprintEnrollEnrolling", "Error unhandled configuration change");
            } else {
                this.mIllustrationLottie = null;
            }
        } else if (this.mShouldShowLottie) {
            this.mIllustrationLottie = (LottieAnimationView) findViewById(C0444R.C0448id.illustration_lottie);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        int i = configuration.orientation;
        if (i == 1) {
            updateOrientation(1);
        } else if (i != 2) {
            Log.e("FingerprintEnrollEnrolling", "Error unhandled configuration change");
        } else {
            updateOrientation(2);
        }
    }

    public static class IconTouchDialog extends InstrumentedDialogFragment {
        public int getMetricsCategory() {
            return 568;
        }

        public Dialog onCreateDialog(Bundle bundle) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle((int) C0444R.string.security_settings_fingerprint_enroll_touch_dialog_title).setMessage((int) C0444R.string.security_settings_fingerprint_enroll_touch_dialog_message).setPositiveButton((int) C0444R.string.security_settings_fingerprint_enroll_dialog_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            return builder.create();
        }
    }
}
