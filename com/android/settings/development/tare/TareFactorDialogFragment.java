package com.android.settings.development.tare;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.window.C0444R;
import com.android.settings.Utils;

public class TareFactorDialogFragment extends DialogFragment {
    private int mFactorEditedValue;
    private final String mFactorTitle;
    private final int mFactorValue;
    private EditText mFactorValueView;

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
    }

    private String getFactorValue() {
        return Integer.toString(this.mFactorValue);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle((CharSequence) this.mFactorTitle).setView(createDialogView()).setPositiveButton((int) C0444R.string.tare_dialog_confirm_button_title, (DialogInterface.OnClickListener) new TareFactorDialogFragment$$ExternalSyntheticLambda0(this)).setNegativeButton(17039360, (DialogInterface.OnClickListener) TareFactorDialogFragment$$ExternalSyntheticLambda1.INSTANCE).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        String obj = this.mFactorValueView.getText().toString();
        this.mFactorEditedValue = this.mFactorValue;
        try {
            this.mFactorEditedValue = Integer.parseInt(obj);
        } catch (NumberFormatException e) {
            Log.e("TareDialogFragment", "Error converting '" + obj + "' to integer. Using " + this.mFactorValue + " instead", e);
        }
        throw null;
    }

    private View createDialogView() {
        View inflate = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(C0444R.C0450layout.dialog_edittext, (ViewGroup) null);
        EditText editText = (EditText) inflate.findViewById(C0444R.C0448id.edittext);
        this.mFactorValueView = editText;
        editText.setInputType(2);
        this.mFactorValueView.setText(getFactorValue());
        Utils.setEditTextCursorPosition(this.mFactorValueView);
        return inflate;
    }
}
