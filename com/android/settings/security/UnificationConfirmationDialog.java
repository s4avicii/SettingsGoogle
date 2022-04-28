package com.android.settings.security;

import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.window.C0444R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class UnificationConfirmationDialog extends InstrumentedDialogFragment {
    public int getMetricsCategory() {
        return 532;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        SecuritySettings securitySettings = (SecuritySettings) getParentFragment();
        boolean z = getArguments().getBoolean("compliant");
        return new AlertDialog.Builder(getActivity()).setTitle((int) C0444R.string.lock_settings_profile_unification_dialog_title).setMessage((CharSequence) ((DevicePolicyManager) getContext().getSystemService(DevicePolicyManager.class)).getString(z ? "Settings.WORK_PROFILE_UNIFY_LOCKS_DETAIL" : "Settings.WORK_PROFILE_UNIFY_LOCKS_NONCOMPLIANT", new UnificationConfirmationDialog$$ExternalSyntheticLambda1(this, z ? C0444R.string.lock_settings_profile_unification_dialog_body : C0444R.string.lock_settings_profile_unification_dialog_uncompliant_body))).setPositiveButton(z ? C0444R.string.lock_settings_profile_unification_dialog_confirm : C0444R.string.lock_settings_profile_unification_dialog_uncompliant_confirm, (DialogInterface.OnClickListener) new UnificationConfirmationDialog$$ExternalSyntheticLambda0(securitySettings)).setNegativeButton((int) C0444R.string.cancel, (DialogInterface.OnClickListener) null).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$onCreateDialog$0(int i) throws Exception {
        return getString(i);
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        ((SecuritySettings) getParentFragment()).updateUnificationPreference();
    }
}
