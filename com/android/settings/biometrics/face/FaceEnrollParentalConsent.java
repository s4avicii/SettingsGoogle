package com.android.settings.biometrics.face;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.window.C0444R;

public class FaceEnrollParentalConsent extends FaceEnrollIntroduction {
    public static final int[] CONSENT_STRING_RESOURCES = {C0444R.string.security_settings_face_enroll_consent_introduction_title, C0444R.string.security_settings_face_enroll_introduction_consent_message, C0444R.string.security_settings_face_enroll_introduction_info_consent_glasses, C0444R.string.security_settings_face_enroll_introduction_info_consent_looking, C0444R.string.security_settings_face_enroll_introduction_info_consent_gaze, C0444R.string.security_settings_face_enroll_introduction_how_consent_message, C0444R.string.security_settings_face_enroll_introduction_control_consent_title, C0444R.string.f71xf37cee4f};

    /* access modifiers changed from: protected */
    public boolean generateChallengeOnCreate() {
        return false;
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDefault() {
        return C0444R.string.security_settings_face_enroll_consent_introduction_title;
    }

    /* access modifiers changed from: protected */
    public int getHowMessage() {
        return C0444R.string.security_settings_face_enroll_introduction_how_consent_message;
    }

    /* access modifiers changed from: protected */
    public int getInControlMessage() {
        return C0444R.string.f71xf37cee4f;
    }

    /* access modifiers changed from: protected */
    public int getInControlTitle() {
        return C0444R.string.security_settings_face_enroll_introduction_control_consent_title;
    }

    /* access modifiers changed from: protected */
    public int getInfoMessageGlasses() {
        return C0444R.string.security_settings_face_enroll_introduction_info_consent_glasses;
    }

    /* access modifiers changed from: protected */
    public int getInfoMessageLooking() {
        return C0444R.string.security_settings_face_enroll_introduction_info_consent_looking;
    }

    /* access modifiers changed from: protected */
    public int getInfoMessageRequireEyes() {
        return C0444R.string.security_settings_face_enroll_introduction_info_consent_gaze;
    }

    public int getMetricsCategory() {
        return 1893;
    }

    /* access modifiers changed from: protected */
    public boolean onSetOrConfirmCredentials(Intent intent) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setDescriptionText((int) C0444R.string.security_settings_face_enroll_introduction_consent_message);
    }

    /* access modifiers changed from: protected */
    public void onNextButtonClick(View view) {
        onConsentResult(true);
    }

    /* access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        onConsentResult(false);
    }

    /* access modifiers changed from: protected */
    public void onEnrollmentSkipped(Intent intent) {
        onConsentResult(false);
    }

    /* access modifiers changed from: protected */
    public void onFinishedEnrolling(Intent intent) {
        onConsentResult(true);
    }

    private void onConsentResult(boolean z) {
        Intent intent = new Intent();
        intent.putExtra("sensor_modality", 8);
        setResult(z ? 4 : 5, intent);
        finish();
    }
}
