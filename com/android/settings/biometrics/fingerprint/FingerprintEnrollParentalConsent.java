package com.android.settings.biometrics.fingerprint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.window.C0444R;

public class FingerprintEnrollParentalConsent extends FingerprintEnrollIntroduction {
    public static final int[] CONSENT_STRING_RESOURCES = {C0444R.string.security_settings_fingerprint_enroll_consent_introduction_title, C0444R.string.f78xb68b81de, C0444R.string.f82x4f718905, C0444R.string.f91x9550dc28, C0444R.string.f92x9550dc29, C0444R.string.f93x9550dc2a, C0444R.string.f94x9550dc2b};

    /* access modifiers changed from: protected */
    public int getFooterMessage2() {
        return C0444R.string.f91x9550dc28;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage3() {
        return C0444R.string.f92x9550dc29;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage4() {
        return C0444R.string.f93x9550dc2a;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage5() {
        return C0444R.string.f94x9550dc2b;
    }

    /* access modifiers changed from: protected */
    public int getFooterTitle1() {
        return C0444R.string.f82x4f718905;
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDefault() {
        return C0444R.string.security_settings_fingerprint_enroll_consent_introduction_title;
    }

    public int getMetricsCategory() {
        return 1892;
    }

    /* access modifiers changed from: protected */
    public boolean onSetOrConfirmCredentials(Intent intent) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setDescriptionText((int) C0444R.string.f78xb68b81de);
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
        intent.putExtra("sensor_modality", 2);
        setResult(z ? 4 : 5, intent);
        finish();
    }
}
