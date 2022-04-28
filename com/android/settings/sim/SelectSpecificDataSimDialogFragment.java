package com.android.settings.sim;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.window.C0444R;
import com.android.settings.network.SubscriptionUtil;
import java.util.List;

public class SelectSpecificDataSimDialogFragment extends SimDialogFragment implements DialogInterface.OnClickListener {
    private SubscriptionInfo mSubscriptionInfo;

    public int getMetricsCategory() {
        return 1709;
    }

    public static SelectSpecificDataSimDialogFragment newInstance() {
        SelectSpecificDataSimDialogFragment selectSpecificDataSimDialogFragment = new SelectSpecificDataSimDialogFragment();
        selectSpecificDataSimDialogFragment.setArguments(SimDialogFragment.initArguments(0, C0444R.string.select_specific_sim_for_data_title));
        return selectSpecificDataSimDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog create = new AlertDialog.Builder(getContext()).setNegativeButton((int) C0444R.string.sim_action_no_thanks, (DialogInterface.OnClickListener) null).create();
        updateDialog(create);
        return create;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            SimDialogActivity simDialogActivity = (SimDialogActivity) getActivity();
            SubscriptionInfo targetSubscriptionInfo = getTargetSubscriptionInfo();
            if (targetSubscriptionInfo != null) {
                simDialogActivity.onSubscriptionSelected(getDialogType(), targetSubscriptionInfo.getSubscriptionId());
            }
        }
    }

    private SubscriptionInfo getNonDefaultDataSubscriptionInfo(SubscriptionInfo subscriptionInfo) {
        List<SubscriptionInfo> activeSubscriptionInfoList = getSubscriptionManager().getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null || subscriptionInfo == null) {
            return null;
        }
        return (SubscriptionInfo) activeSubscriptionInfoList.stream().filter(new SelectSpecificDataSimDialogFragment$$ExternalSyntheticLambda0(subscriptionInfo)).findFirst().orElse((Object) null);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getNonDefaultDataSubscriptionInfo$0(SubscriptionInfo subscriptionInfo, SubscriptionInfo subscriptionInfo2) {
        return subscriptionInfo2.getSubscriptionId() != subscriptionInfo.getSubscriptionId();
    }

    private SubscriptionInfo getDefaultDataSubInfo() {
        return getSubscriptionManager().getDefaultDataSubscriptionInfo();
    }

    private void updateDialog(AlertDialog alertDialog) {
        Log.d("PreferredSimDialogFrag", "Dialog updated, dismiss status: " + this.mWasDismissed);
        if (!this.mWasDismissed) {
            SubscriptionInfo defaultDataSubInfo = getDefaultDataSubInfo();
            SubscriptionInfo nonDefaultDataSubscriptionInfo = getNonDefaultDataSubscriptionInfo(defaultDataSubInfo);
            if (nonDefaultDataSubscriptionInfo == null || defaultDataSubInfo == null) {
                Log.d("PreferredSimDialogFrag", "one of target SubscriptionInfos is null");
                dismiss();
                return;
            }
            Log.d("PreferredSimDialogFrag", "newSubId: " + nonDefaultDataSubscriptionInfo.getSubscriptionId() + "currentDataSubID: " + defaultDataSubInfo.getSubscriptionId());
            setTargetSubscriptionInfo(nonDefaultDataSubscriptionInfo);
            CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(nonDefaultDataSubscriptionInfo, getContext());
            CharSequence uniqueSubscriptionDisplayName2 = SubscriptionUtil.getUniqueSubscriptionDisplayName(defaultDataSubInfo, getContext());
            String string = getContext().getString(C0444R.string.select_specific_sim_for_data_button, new Object[]{uniqueSubscriptionDisplayName});
            String string2 = getContext().getString(C0444R.string.select_specific_sim_for_data_msg, new Object[]{uniqueSubscriptionDisplayName, uniqueSubscriptionDisplayName2});
            View inflate = LayoutInflater.from(getContext()).inflate(C0444R.C0450layout.sim_confirm_dialog_multiple_enabled_profiles_supported, (ViewGroup) null);
            TextView textView = inflate != null ? (TextView) inflate.findViewById(C0444R.C0448id.msg) : null;
            if (!TextUtils.isEmpty(string2) && textView != null) {
                textView.setText(string2);
                textView.setVisibility(0);
            }
            alertDialog.setView(inflate);
            TextView textView2 = (TextView) LayoutInflater.from(getContext()).inflate(C0444R.C0450layout.sim_confirm_dialog_title_multiple_enabled_profiles_supported, (ViewGroup) null).findViewById(C0444R.C0448id.title);
            textView2.setText(getContext().getString(getTitleResId(), new Object[]{uniqueSubscriptionDisplayName}));
            alertDialog.setCustomTitle(textView2);
            alertDialog.setButton(-1, string, this);
        }
    }

    private void setTargetSubscriptionInfo(SubscriptionInfo subscriptionInfo) {
        this.mSubscriptionInfo = subscriptionInfo;
    }

    private SubscriptionInfo getTargetSubscriptionInfo() {
        return this.mSubscriptionInfo;
    }

    public void updateDialog() {
        updateDialog((AlertDialog) getDialog());
    }

    /* access modifiers changed from: protected */
    public SubscriptionManager getSubscriptionManager() {
        return (SubscriptionManager) getContext().getSystemService(SubscriptionManager.class);
    }
}
