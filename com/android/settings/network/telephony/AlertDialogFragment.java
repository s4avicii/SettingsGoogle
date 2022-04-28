package com.android.settings.network.telephony;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

public class AlertDialogFragment extends BaseDialogFragment implements DialogInterface.OnClickListener {
    public static void show(FragmentActivity fragmentActivity, String str, String str2) {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", str);
        bundle.putString("msg", str2);
        alertDialogFragment.setArguments(bundle);
        alertDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "AlertDialogFragment");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder positiveButton = new AlertDialog.Builder(getContext()).setTitle((CharSequence) getArguments().getString("title")).setPositiveButton(17039370, (DialogInterface.OnClickListener) this);
        if (!TextUtils.isEmpty(getArguments().getString("msg"))) {
            positiveButton.setMessage((CharSequence) getArguments().getString("msg"));
        }
        return positiveButton.create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (getActivity() != null) {
            getActivity().finish();
        }
        super.dismiss();
    }
}
