package com.android.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.window.C0444R;
import com.android.settingslib.HelpUtils;

final class ActionDisabledByAppOpsHelper {
    private final Activity mActivity;
    private final ViewGroup mDialogView;

    ActionDisabledByAppOpsHelper(Activity activity) {
        this.mActivity = activity;
        this.mDialogView = (ViewGroup) LayoutInflater.from(activity).inflate(C0444R.C0450layout.support_details_dialog, (ViewGroup) null);
    }

    public AlertDialog.Builder prepareDialogBuilder() {
        String string = this.mActivity.getString(C0444R.string.help_url_action_disabled_by_restricted_settings);
        AlertDialog.Builder view = new AlertDialog.Builder(this.mActivity).setPositiveButton((int) C0444R.string.okay, (DialogInterface.OnClickListener) null).setView((View) this.mDialogView);
        if (!TextUtils.isEmpty(string)) {
            view.setNeutralButton(C0444R.string.learn_more, new ActionDisabledByAppOpsHelper$$ExternalSyntheticLambda0(this, string));
        }
        initializeDialogViews(this.mDialogView);
        return view;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareDialogBuilder$0(String str, DialogInterface dialogInterface, int i) {
        Activity activity = this.mActivity;
        Intent helpIntent = HelpUtils.getHelpIntent(activity, str, activity.getClass().getName());
        if (helpIntent != null) {
            this.mActivity.startActivity(helpIntent);
        }
    }

    public void updateDialog() {
        initializeDialogViews(this.mDialogView);
    }

    private void initializeDialogViews(View view) {
        setSupportTitle(view);
        setSupportDetails(view);
    }

    /* access modifiers changed from: package-private */
    public void setSupportTitle(View view) {
        TextView textView = (TextView) view.findViewById(C0444R.C0448id.admin_support_dialog_title);
        if (textView != null) {
            textView.setText(C0444R.string.blocked_by_restricted_settings_title);
        }
    }

    /* access modifiers changed from: package-private */
    public void setSupportDetails(View view) {
        ((TextView) view.findViewById(C0444R.C0448id.admin_support_msg)).setText(C0444R.string.blocked_by_restricted_settings_content);
    }
}
