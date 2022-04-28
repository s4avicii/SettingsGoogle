package com.android.settings.development;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemProperties;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.window.C0444R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class RebootWithMteDialog extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    private Context mContext;

    public int getMetricsCategory() {
        return 1913;
    }

    public RebootWithMteDialog(Context context) {
        this.mContext = context;
    }

    public static void show(Context context, Fragment fragment) {
        FragmentManager supportFragmentManager = fragment.getActivity().getSupportFragmentManager();
        if (supportFragmentManager.findFragmentByTag("RebootWithMteDlg") == null) {
            RebootWithMteDialog rebootWithMteDialog = new RebootWithMteDialog(context);
            rebootWithMteDialog.setTargetFragment(fragment, 0);
            rebootWithMteDialog.show(supportFragmentManager, "RebootWithMteDlg");
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle((int) C0444R.string.reboot_with_mte_title).setMessage((int) C0444R.string.reboot_with_mte_message).setPositiveButton(17039370, (DialogInterface.OnClickListener) this).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setIcon(17301543).create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        SystemProperties.set("arm64.memtag.bootctl", "memtag-once");
        ((PowerManager) this.mContext.getSystemService(PowerManager.class)).reboot((String) null);
    }
}
