package com.android.settings.network.telephony;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.window.C0444R;
import java.util.ArrayList;

public class ConfirmDialogFragment extends BaseDialogFragment implements DialogInterface.OnClickListener {

    public interface OnConfirmListener {
        void onConfirm(int i, boolean z, int i2);
    }

    public static <T> void show(FragmentActivity fragmentActivity, Class<T> cls, int i, String str, String str2, String str3, String str4) {
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", str);
        bundle.putCharSequence("msg", str2);
        bundle.putString("pos_button_string", str3);
        bundle.putString("neg_button_string", str4);
        BaseDialogFragment.setListener(fragmentActivity, (Fragment) null, cls, i, bundle);
        confirmDialogFragment.setArguments(bundle);
        confirmDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "ConfirmDialogFragment");
    }

    public static <T> void show(FragmentActivity fragmentActivity, Class<T> cls, int i, String str, String str2, String str3, String str4, ArrayList<String> arrayList) {
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", str);
        bundle.putCharSequence("msg", str2);
        bundle.putString("pos_button_string", str3);
        bundle.putString("neg_button_string", str4);
        bundle.putStringArrayList("list", arrayList);
        BaseDialogFragment.setListener(fragmentActivity, (Fragment) null, cls, i, bundle);
        confirmDialogFragment.setArguments(bundle);
        confirmDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "ConfirmDialogFragment");
    }

    public final Dialog onCreateDialog(Bundle bundle) {
        String string = getArguments().getString("title");
        String string2 = getArguments().getString("msg");
        String string3 = getArguments().getString("pos_button_string");
        String string4 = getArguments().getString("neg_button_string");
        final ArrayList<String> stringArrayList = getArguments().getStringArrayList("list");
        Log.i("ConfirmDialogFragment", "Showing dialog with title =" + string);
        AlertDialog.Builder negativeButton = new AlertDialog.Builder(getContext()).setPositiveButton((CharSequence) string3, (DialogInterface.OnClickListener) this).setNegativeButton((CharSequence) string4, (DialogInterface.OnClickListener) this);
        View inflate = LayoutInflater.from(getContext()).inflate(C0444R.C0450layout.sim_confirm_dialog_multiple_enabled_profiles_supported, (ViewGroup) null);
        if (stringArrayList == null || stringArrayList.isEmpty() || inflate == null) {
            if (!TextUtils.isEmpty(string)) {
                negativeButton.setTitle((CharSequence) string);
            }
            if (!TextUtils.isEmpty(string2)) {
                negativeButton.setMessage((CharSequence) string2);
            }
        } else {
            Log.i("ConfirmDialogFragment", "list =" + stringArrayList.toString());
            if (!TextUtils.isEmpty(string)) {
                TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(C0444R.C0450layout.sim_confirm_dialog_title_multiple_enabled_profiles_supported, (ViewGroup) null).findViewById(C0444R.C0448id.title);
                textView.setText(string);
                negativeButton.setCustomTitle(textView);
            }
            TextView textView2 = (TextView) inflate.findViewById(C0444R.C0448id.msg);
            if (!TextUtils.isEmpty(string2) && textView2 != null) {
                textView2.setText(string2);
                textView2.setVisibility(0);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), C0444R.C0450layout.sim_confirm_dialog_item_multiple_enabled_profiles_supported, stringArrayList);
            ListView listView = (ListView) inflate.findViewById(C0444R.C0448id.carrier_list);
            if (listView != null) {
                listView.setVisibility(0);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        Log.i("ConfirmDialogFragment", "list onClick =" + i);
                        Log.i("ConfirmDialogFragment", "list item =" + ((String) stringArrayList.get(i)));
                        if (i == stringArrayList.size() - 1) {
                            ConfirmDialogFragment.this.informCaller(false, -1);
                        } else {
                            ConfirmDialogFragment.this.informCaller(true, i);
                        }
                    }
                });
            }
            LinearLayout linearLayout = (LinearLayout) inflate.findViewById(C0444R.C0448id.info_outline_layout);
            if (linearLayout != null) {
                linearLayout.setVisibility(0);
            }
            negativeButton.setView(inflate);
        }
        AlertDialog create = negativeButton.create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        Log.i("ConfirmDialogFragment", "dialog onClick =" + i);
        informCaller(i == -1, -1);
    }

    public void onCancel(DialogInterface dialogInterface) {
        informCaller(false, -1);
    }

    /* access modifiers changed from: private */
    public void informCaller(boolean z, int i) {
        OnConfirmListener onConfirmListener = (OnConfirmListener) getListener(OnConfirmListener.class);
        if (onConfirmListener != null) {
            onConfirmListener.onConfirm(getTagInCaller(), z, i);
        }
    }
}
