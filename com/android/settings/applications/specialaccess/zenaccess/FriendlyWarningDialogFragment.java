package com.android.settings.applications.specialaccess.zenaccess;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.window.C0444R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class FriendlyWarningDialogFragment extends InstrumentedDialogFragment {
    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
    }

    public int getMetricsCategory() {
        return 555;
    }

    public FriendlyWarningDialogFragment setPkgInfo(String str, CharSequence charSequence, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("p", str);
        if (!TextUtils.isEmpty(charSequence)) {
            str = charSequence.toString();
        }
        bundle.putString("l", str);
        setTargetFragment(fragment, 0);
        setArguments(bundle);
        return this;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        String string = arguments.getString("p");
        String string2 = arguments.getString("l");
        return new AlertDialog.Builder(getContext()).setMessage((CharSequence) getResources().getString(C0444R.string.zen_access_revoke_warning_dialog_summary)).setTitle((CharSequence) getResources().getString(C0444R.string.zen_access_revoke_warning_dialog_title, new Object[]{string2})).setCancelable(true).setPositiveButton((int) C0444R.string.okay, (DialogInterface.OnClickListener) new FriendlyWarningDialogFragment$$ExternalSyntheticLambda0(this, string, (ZenAccessDetails) getTargetFragment())).setNegativeButton((int) C0444R.string.cancel, (DialogInterface.OnClickListener) FriendlyWarningDialogFragment$$ExternalSyntheticLambda1.INSTANCE).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(String str, ZenAccessDetails zenAccessDetails, DialogInterface dialogInterface, int i) {
        ZenAccessController.deleteRules(getContext(), str);
        ZenAccessController.setAccess(getContext(), str, false);
        zenAccessDetails.refreshUi();
    }
}
