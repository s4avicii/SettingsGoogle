package com.android.settings.biometrics.face;

import android.os.Bundle;
import android.view.View;
import androidx.window.C0444R;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;

public class FaceEnrollFinish extends BiometricEnrollBase {
    public int getMetricsCategory() {
        return 1508;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0444R.C0450layout.face_enroll_finish);
        setHeaderText((int) C0444R.string.security_settings_face_enroll_finish_title);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.security_settings_face_enroll_done).setListener(new FaceEnrollFinish$$ExternalSyntheticLambda0(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build());
    }

    public void onNextButtonClick(View view) {
        setResult(1);
        finish();
    }
}
