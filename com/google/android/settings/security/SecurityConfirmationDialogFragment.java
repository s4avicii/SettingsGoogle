package com.google.android.settings.security;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class SecurityConfirmationDialogFragment extends InstrumentedDialogFragment {
    private SecurityContentManager mSecurityContentManager;

    public int getMetricsCategory() {
        return 1891;
    }

    public static SecurityConfirmationDialogFragment newInstance(int i, int i2, int i3, int i4, Bundle bundle) {
        SecurityConfirmationDialogFragment securityConfirmationDialogFragment = new SecurityConfirmationDialogFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("title", i);
        bundle2.putInt("body", i2);
        bundle2.putInt("continue", i3);
        bundle2.putInt("cancel", i4);
        bundle2.putBundle("continueBundle", bundle);
        securityConfirmationDialogFragment.setArguments(bundle2);
        return securityConfirmationDialogFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSecurityContentManager = SecurityContentManager.getInstance(getContext());
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(arguments.getInt("title")).setPositiveButton(arguments.getInt("continue"), (DialogInterface.OnClickListener) new SecurityConfirmationDialogFragment$$ExternalSyntheticLambda1(this, arguments)).setNegativeButton(arguments.getInt("cancel"), (DialogInterface.OnClickListener) new SecurityConfirmationDialogFragment$$ExternalSyntheticLambda0(this));
        int i = arguments.getInt("body");
        if (i != 0) {
            builder.setMessage(i);
        }
        return builder.create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(Bundle bundle, DialogInterface dialogInterface, int i) {
        this.mSecurityContentManager.handleClick(bundle.getBundle("continueBundle"), getActivity());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        dismiss();
    }

    public void show(FragmentManager fragmentManager, String str) {
        if (fragmentManager.findFragmentByTag(str) == null) {
            super.show(fragmentManager, str);
        }
    }
}
