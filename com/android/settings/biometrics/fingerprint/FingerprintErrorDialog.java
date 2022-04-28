package com.android.settings.biometrics.fingerprint;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.window.C0444R;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.android.settings.biometrics.BiometricErrorDialog;

public class FingerprintErrorDialog extends BiometricErrorDialog {
    private static int getErrorMessage(int i) {
        return i != 3 ? i != 18 ? C0444R.string.f75x537c56b0 : C0444R.string.security_settings_fingerprint_bad_calibration : C0444R.string.f76xe7f94de6;
    }

    public int getMetricsCategory() {
        return 569;
    }

    public int getOkButtonTextResId() {
        return C0444R.string.security_settings_fingerprint_enroll_dialog_ok;
    }

    public int getTitleResId() {
        return C0444R.string.security_settings_fingerprint_enroll_error_dialog_title;
    }

    public static void showErrorDialog(BiometricEnrollBase biometricEnrollBase, int i) {
        if (!biometricEnrollBase.isFinishing()) {
            FragmentManager supportFragmentManager = biometricEnrollBase.getSupportFragmentManager();
            if (!supportFragmentManager.isDestroyed() && !supportFragmentManager.isStateSaved()) {
                newInstance(biometricEnrollBase.getText(getErrorMessage(i)), i).show(supportFragmentManager, FingerprintErrorDialog.class.getName());
            }
        }
    }

    private static FingerprintErrorDialog newInstance(CharSequence charSequence, int i) {
        FingerprintErrorDialog fingerprintErrorDialog = new FingerprintErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putCharSequence("error_msg", charSequence);
        bundle.putInt("error_id", i);
        fingerprintErrorDialog.setArguments(bundle);
        return fingerprintErrorDialog;
    }
}
