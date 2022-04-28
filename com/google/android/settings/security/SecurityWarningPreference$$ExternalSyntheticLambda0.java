package com.google.android.settings.security;

import android.os.Bundle;
import android.view.View;
import androidx.window.C0444R;
import com.android.settings.SettingsPreferenceFragment;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SecurityWarningPreference$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ Bundle f$0;
    public final /* synthetic */ SettingsPreferenceFragment f$1;

    public /* synthetic */ SecurityWarningPreference$$ExternalSyntheticLambda0(Bundle bundle, SettingsPreferenceFragment settingsPreferenceFragment) {
        this.f$0 = bundle;
        this.f$1 = settingsPreferenceFragment;
    }

    public final void onClick(View view) {
        SecurityConfirmationDialogFragment.newInstance(C0444R.string.security_dismiss_dialog_title, 0, C0444R.string.security_dismiss_dialog_dismiss_button, 17039360, this.f$0).show(this.f$1.getParentFragmentManager(), "SecurityConfirmationDialogFragment");
    }
}
