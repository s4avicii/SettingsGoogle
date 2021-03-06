package com.android.settings.fuelgauge.batterytip;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.C0444R;
import com.android.settings.SettingsActivity;
import com.android.settings.Utils;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.fuelgauge.batterytip.BatteryTipPreferenceController;
import com.android.settings.fuelgauge.batterytip.actions.BatteryTipAction;
import com.android.settings.fuelgauge.batterytip.tips.BatteryTip;
import com.android.settings.fuelgauge.batterytip.tips.HighUsageTip;
import com.android.settings.fuelgauge.batterytip.tips.RestrictAppTip;
import com.android.settings.fuelgauge.batterytip.tips.UnrestrictAppTip;
import java.text.NumberFormat;
import java.util.List;

public class BatteryTipDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    BatteryTip mBatteryTip;
    int mMetricsKey;

    public int getMetricsCategory() {
        return 1323;
    }

    public static BatteryTipDialogFragment newInstance(BatteryTip batteryTip, int i) {
        BatteryTipDialogFragment batteryTipDialogFragment = new BatteryTipDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putParcelable(BatteryTipPreferenceController.PREF_NAME, batteryTip);
        bundle.putInt("metrics_key", i);
        batteryTipDialogFragment.setArguments(bundle);
        return batteryTipDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        Context context = getContext();
        this.mBatteryTip = (BatteryTip) arguments.getParcelable(BatteryTipPreferenceController.PREF_NAME);
        this.mMetricsKey = arguments.getInt("metrics_key");
        int type = this.mBatteryTip.getType();
        if (type == 1) {
            RestrictAppTip restrictAppTip = (RestrictAppTip) this.mBatteryTip;
            List<AppInfo> restrictAppList = restrictAppTip.getRestrictAppList();
            int size = restrictAppList.size();
            CharSequence applicationLabel = Utils.getApplicationLabel(context, restrictAppList.get(0).packageName);
            AlertDialog.Builder negativeButton = new AlertDialog.Builder(context).setTitle((CharSequence) context.getResources().getQuantityString(C0444R.plurals.battery_tip_restrict_app_dialog_title, size, new Object[]{Integer.valueOf(size)})).setPositiveButton((int) C0444R.string.battery_tip_restrict_app_dialog_ok, (DialogInterface.OnClickListener) this).setNegativeButton(17039360, (DialogInterface.OnClickListener) null);
            if (size == 1) {
                negativeButton.setMessage((CharSequence) getString(C0444R.string.battery_tip_restrict_app_dialog_message, applicationLabel));
            } else if (size <= 5) {
                negativeButton.setMessage((CharSequence) getString(C0444R.string.battery_tip_restrict_apps_less_than_5_dialog_message));
                RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(context).inflate(C0444R.C0450layout.recycler_view, (ViewGroup) null);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new HighUsageAdapter(context, restrictAppList));
                negativeButton.setView((View) recyclerView);
            } else {
                negativeButton.setMessage((CharSequence) context.getString(C0444R.string.battery_tip_restrict_apps_more_than_5_dialog_message, new Object[]{restrictAppTip.getRestrictAppsString(context)}));
            }
            return negativeButton.create();
        } else if (type == 2) {
            HighUsageTip highUsageTip = (HighUsageTip) this.mBatteryTip;
            RecyclerView recyclerView2 = (RecyclerView) LayoutInflater.from(context).inflate(C0444R.C0450layout.recycler_view, (ViewGroup) null);
            recyclerView2.setLayoutManager(new LinearLayoutManager(context));
            recyclerView2.setAdapter(new HighUsageAdapter(context, highUsageTip.getHighUsageAppList()));
            return new AlertDialog.Builder(context).setMessage((CharSequence) getString(C0444R.string.battery_tip_dialog_message, Integer.valueOf(highUsageTip.getHighUsageAppList().size()))).setView((View) recyclerView2).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).create();
        } else if (type == 6) {
            return new AlertDialog.Builder(context).setMessage((int) C0444R.string.battery_tip_dialog_summary_message).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).create();
        } else {
            if (type == 7) {
                Utils.getApplicationLabel(context, ((UnrestrictAppTip) this.mBatteryTip).getPackageName());
                return new AlertDialog.Builder(context).setTitle((CharSequence) getString(C0444R.string.battery_tip_unrestrict_app_dialog_title)).setMessage((int) C0444R.string.battery_tip_unrestrict_app_dialog_message).setPositiveButton((int) C0444R.string.battery_tip_unrestrict_app_dialog_ok, (DialogInterface.OnClickListener) this).setNegativeButton((int) C0444R.string.battery_tip_unrestrict_app_dialog_cancel, (DialogInterface.OnClickListener) null).create();
            } else if (type == 8) {
                this.mMetricsFeatureProvider.action(context, 1771, this.mMetricsKey);
                String string = context.getString(C0444R.string.battery_tip_limited_temporarily_dialog_msg, new Object[]{NumberFormat.getPercentInstance().format(0.800000011920929d)});
                boolean isPluggedIn = isPluggedIn();
                AlertDialog.Builder message = new AlertDialog.Builder(context).setTitle((int) C0444R.string.battery_tip_limited_temporarily_title).setMessage((CharSequence) string);
                if (isPluggedIn) {
                    message.setPositiveButton((int) C0444R.string.battery_tip_limited_temporarily_dialog_resume_charge, (DialogInterface.OnClickListener) this).setNegativeButton((int) C0444R.string.okay, (DialogInterface.OnClickListener) null);
                } else {
                    message.setPositiveButton((int) C0444R.string.okay, (DialogInterface.OnClickListener) null);
                }
                return message.create();
            } else {
                throw new IllegalArgumentException("unknown type " + this.mBatteryTip.getType());
            }
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        BatteryTipPreferenceController.BatteryTipListener batteryTipListener = (BatteryTipPreferenceController.BatteryTipListener) getTargetFragment();
        if (batteryTipListener != null) {
            BatteryTipAction actionForBatteryTip = BatteryTipUtils.getActionForBatteryTip(this.mBatteryTip, (SettingsActivity) getActivity(), (InstrumentedPreferenceFragment) getTargetFragment());
            if (actionForBatteryTip != null) {
                actionForBatteryTip.handlePositiveAction(this.mMetricsKey);
            }
            batteryTipListener.onBatteryTipHandled(this.mBatteryTip);
        }
    }

    private boolean isPluggedIn() {
        Intent registerReceiver = getContext().registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver == null || registerReceiver.getIntExtra("plugged", 0) == 0) {
            return false;
        }
        return true;
    }
}
