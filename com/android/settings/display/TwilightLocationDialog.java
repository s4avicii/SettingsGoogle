package com.android.settings.display;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import androidx.window.C0444R;
import com.android.settings.Settings;

public class TwilightLocationDialog {
    public static String TAG = "TwilightLocationDialog";

    public static void show(Context context) {
        new AlertDialog.Builder(context).setPositiveButton(C0444R.string.twilight_mode_launch_location, new TwilightLocationDialog$$ExternalSyntheticLambda0(context)).setNegativeButton(C0444R.string.cancel, (DialogInterface.OnClickListener) null).setMessage(C0444R.string.twilight_mode_location_off_dialog_message).create().show();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$show$0(Context context, DialogInterface dialogInterface, int i) {
        Log.d(TAG, "clicked forget");
        Intent intent = new Intent();
        intent.setClass(context, Settings.LocationSettingsActivity.class);
        context.startActivity(intent);
    }
}
