package com.android.settings.users;

import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.window.C0444R;
import com.android.settings.Utils;

public final class UserDialogs {
    public static Dialog createRemoveDialog(Context context, int i, DialogInterface.OnClickListener onClickListener) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        UserInfo userInfo = ((UserManager) context.getSystemService("user")).getUserInfo(i);
        AlertDialog.Builder negativeButton = new AlertDialog.Builder(context).setPositiveButton((int) C0444R.string.user_delete_button, onClickListener).setNegativeButton(17039360, (DialogInterface.OnClickListener) null);
        if (userInfo.isManagedProfile()) {
            negativeButton.setTitle((CharSequence) devicePolicyManager.getString("Settings.WORK_PROFILE_CONFIRM_REMOVE_TITLE", new UserDialogs$$ExternalSyntheticLambda3(context)));
            View createRemoveManagedUserDialogView = createRemoveManagedUserDialogView(context, i);
            if (createRemoveManagedUserDialogView != null) {
                negativeButton.setView(createRemoveManagedUserDialogView);
            } else {
                negativeButton.setMessage((CharSequence) devicePolicyManager.getString("Settings.WORK_PROFILE_CONFIRM_REMOVE_MESSAGE", new UserDialogs$$ExternalSyntheticLambda2(context)));
            }
        } else if (UserHandle.myUserId() == i) {
            negativeButton.setTitle((int) C0444R.string.user_confirm_remove_self_title);
            negativeButton.setMessage((int) C0444R.string.user_confirm_remove_self_message);
        } else if (userInfo.isRestricted()) {
            negativeButton.setTitle((int) C0444R.string.user_profile_confirm_remove_title);
            negativeButton.setMessage((int) C0444R.string.user_profile_confirm_remove_message);
        } else {
            negativeButton.setTitle((int) C0444R.string.user_confirm_remove_title);
            negativeButton.setMessage((int) C0444R.string.user_confirm_remove_message);
        }
        return negativeButton.create();
    }

    private static View createRemoveManagedUserDialogView(Context context, int i) {
        PackageManager packageManager = context.getPackageManager();
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        ApplicationInfo adminApplicationInfo = Utils.getAdminApplicationInfo(context, i);
        if (adminApplicationInfo == null) {
            return null;
        }
        View inflate = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0444R.C0450layout.delete_managed_profile_dialog, (ViewGroup) null);
        ((ImageView) inflate.findViewById(C0444R.C0448id.delete_managed_profile_mdm_icon_view)).setImageDrawable(packageManager.getApplicationIcon(adminApplicationInfo));
        ((TextView) inflate.findViewById(C0444R.C0448id.delete_managed_profile_opening_paragraph)).setText(devicePolicyManager.getString("Settings.WORK_PROFILE_MANAGED_BY", new UserDialogs$$ExternalSyntheticLambda1(context)));
        ((TextView) inflate.findViewById(C0444R.C0448id.delete_managed_profile_closing_paragraph)).setText(devicePolicyManager.getString("Settings.WORK_PROFILE_CONFIRM_REMOVE_MESSAGE", new UserDialogs$$ExternalSyntheticLambda0(context)));
        CharSequence applicationLabel = packageManager.getApplicationLabel(adminApplicationInfo);
        CharSequence userBadgedLabel = packageManager.getUserBadgedLabel(applicationLabel, new UserHandle(i));
        TextView textView = (TextView) inflate.findViewById(C0444R.C0448id.delete_managed_profile_device_manager_name);
        textView.setText(applicationLabel);
        if (!applicationLabel.toString().contentEquals(userBadgedLabel)) {
            textView.setContentDescription(userBadgedLabel);
        }
        return inflate;
    }

    public static Dialog createEnablePhoneCallsAndSmsDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context).setTitle((int) C0444R.string.user_enable_calling_and_sms_confirm_title).setMessage((int) C0444R.string.user_enable_calling_and_sms_confirm_message).setPositiveButton((int) C0444R.string.okay, onClickListener).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
    }

    public static Dialog createEnablePhoneCallsDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context).setTitle((int) C0444R.string.user_enable_calling_confirm_title).setMessage((int) C0444R.string.user_enable_calling_confirm_message).setPositiveButton((int) C0444R.string.okay, onClickListener).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
    }

    public static Dialog createSetupUserDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context).setTitle((int) C0444R.string.user_setup_dialog_title).setMessage((int) C0444R.string.user_setup_dialog_message).setPositiveButton((int) C0444R.string.user_setup_button_setup_now, onClickListener).setNegativeButton((int) C0444R.string.user_setup_button_setup_later, (DialogInterface.OnClickListener) null).create();
    }

    public static Dialog createResetGuestDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context).setTitle((int) C0444R.string.guest_reset_guest_dialog_title).setMessage((int) C0444R.string.user_exit_guest_confirm_message).setPositiveButton((int) C0444R.string.guest_reset_guest_confirm_button, onClickListener).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
    }
}
