package com.android.settings.development;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.window.C0444R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class RebootConfirmationDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    private final RebootConfirmationDialogHost mHost;
    private final int mMessageId;

    public int getMetricsCategory() {
        return 1914;
    }

    public static void show(Fragment fragment, int i, RebootConfirmationDialogHost rebootConfirmationDialogHost) {
        FragmentManager supportFragmentManager = fragment.getActivity().getSupportFragmentManager();
        if (supportFragmentManager.findFragmentByTag("FreeformPrefRebootDlg") == null) {
            new RebootConfirmationDialogFragment(i, rebootConfirmationDialogHost).show(supportFragmentManager, "FreeformPrefRebootDlg");
        }
    }

    private RebootConfirmationDialogFragment(int i, RebootConfirmationDialogHost rebootConfirmationDialogHost) {
        this.mMessageId = i;
        this.mHost = rebootConfirmationDialogHost;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setMessage(this.mMessageId).setPositiveButton((int) C0444R.string.reboot_dialog_reboot_now, (DialogInterface.OnClickListener) this).setNegativeButton((int) C0444R.string.reboot_dialog_reboot_later, (DialogInterface.OnClickListener) null).create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.mHost.onRebootConfirmed();
    }
}
