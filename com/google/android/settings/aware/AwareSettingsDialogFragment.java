package com.google.android.settings.aware;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.window.C0444R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class AwareSettingsDialogFragment extends InstrumentedDialogFragment {
    private static DialogInterface.OnClickListener mClickListener;

    public int getMetricsCategory() {
        return 1633;
    }

    public static void show(Fragment fragment, DialogInterface.OnClickListener onClickListener) {
        mClickListener = onClickListener;
        AwareSettingsDialogFragment awareSettingsDialogFragment = new AwareSettingsDialogFragment();
        awareSettingsDialogFragment.setTargetFragment(fragment, 0);
        awareSettingsDialogFragment.show(fragment.getFragmentManager(), "AwareSettingsDialog");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getContext()).setTitle((int) C0444R.string.dialog_aware_settings_title).setMessage((int) C0444R.string.dialog_aware_settings_message).setPositiveButton((int) C0444R.string.condition_turn_off, mClickListener).setNegativeButton((int) C0444R.string.cancel, mClickListener).create();
    }
}
