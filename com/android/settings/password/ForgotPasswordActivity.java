package com.android.settings.password;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.window.C0444R;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;

public class ForgotPasswordActivity extends Activity {
    public static final String TAG = "ForgotPasswordActivity";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int intExtra = getIntent().getIntExtra("android.intent.extra.USER_ID", -1);
        if (intExtra < 0) {
            Log.e(TAG, "No valid userId supplied, exiting");
            finish();
            return;
        }
        setContentView(C0444R.C0450layout.forgot_password_activity);
        ((TextView) findViewById(C0444R.C0448id.forgot_password_text)).setText(((DevicePolicyManager) getSystemService(DevicePolicyManager.class)).getString("Settings.FORGOT_PASSWORD_TEXT", new ForgotPasswordActivity$$ExternalSyntheticLambda1(this)));
        ((FooterBarMixin) ((GlifLayout) findViewById(C0444R.C0448id.setup_wizard_layout)).getMixin(FooterBarMixin.class)).setPrimaryButton(new FooterButton.Builder(this).setText(17039370).setListener(new ForgotPasswordActivity$$ExternalSyntheticLambda0(this)).setButtonType(4).setTheme(C0444R.style.SudGlifButton_Primary).build());
        UserManager.get(this).requestQuietModeEnabled(false, UserHandle.of(intExtra), 2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$onCreate$0() throws Exception {
        return getString(C0444R.string.forgot_password_text);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        finish();
    }
}
