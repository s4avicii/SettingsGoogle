package com.android.settings.network;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.util.Log;
import androidx.window.C0444R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.network.helper.ConfirmationSimDeletionPredicate;
import com.android.settings.system.ResetDashboardFragment;
import com.android.settings.wifi.dpp.WifiDppUtils;

public class EraseEuiccDataDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    public int getMetricsCategory() {
        return 1857;
    }

    public static void show(ResetDashboardFragment resetDashboardFragment) {
        EraseEuiccDataDialogFragment eraseEuiccDataDialogFragment = new EraseEuiccDataDialogFragment();
        eraseEuiccDataDialogFragment.setTargetFragment(resetDashboardFragment, 0);
        eraseEuiccDataDialogFragment.show(resetDashboardFragment.getActivity().getSupportFragmentManager(), "EraseEuiccDataDlg");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle(C0444R.string.reset_esim_title).setMessage(C0444R.string.reset_esim_desc).setPositiveButton(C0444R.string.erase_euicc_data_button, this).setNegativeButton(C0444R.string.cancel, (DialogInterface.OnClickListener) null).setOnDismissListener(this).create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (!(getTargetFragment() instanceof ResetDashboardFragment)) {
            Log.e("EraseEuiccDataDlg", "getTargetFragment return unexpected type");
        }
        if (i == -1) {
            Context context = getContext();
            if (ConfirmationSimDeletionPredicate.getSingleton().test(context)) {
                WifiDppUtils.showLockScreen(context, new EraseEuiccDataDialogFragment$$ExternalSyntheticLambda0(this, context));
            } else {
                lambda$onClick$0(context);
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: runAsyncWipe */
    public void lambda$onClick$0(final Context context) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                RecoverySystem.wipeEuiccData(context, "com.android.settings.network");
            }
        });
    }
}
