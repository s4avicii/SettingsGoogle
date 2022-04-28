package com.android.settings.fuelgauge;

import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerWhitelistManager;
import androidx.window.C0444R;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;

public class RequestIgnoreBatteryOptimizations extends AlertActivity implements DialogInterface.OnClickListener {
    private String mPackageName;
    private PowerWhitelistManager mPowerWhitelistManager;

    private static void debugLog(String str) {
    }

    public void onCreate(Bundle bundle) {
        RequestIgnoreBatteryOptimizations.super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        this.mPowerWhitelistManager = (PowerWhitelistManager) getSystemService(PowerWhitelistManager.class);
        Uri data = getIntent().getData();
        if (data == null) {
            debugLog("No data supplied for IGNORE_BATTERY_OPTIMIZATION_SETTINGS in: " + getIntent());
            finish();
            return;
        }
        String schemeSpecificPart = data.getSchemeSpecificPart();
        this.mPackageName = schemeSpecificPart;
        if (schemeSpecificPart == null) {
            debugLog("No data supplied for IGNORE_BATTERY_OPTIMIZATION_SETTINGS in: " + getIntent());
            finish();
        } else if (((PowerManager) getSystemService(PowerManager.class)).isIgnoringBatteryOptimizations(this.mPackageName)) {
            debugLog("Not should prompt, already ignoring optimizations: " + this.mPackageName);
            finish();
        } else {
            try {
                ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(this.mPackageName, 0);
                if (getPackageManager().checkPermission("android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS", this.mPackageName) != 0) {
                    debugLog("Requested package " + this.mPackageName + " does not hold permission " + "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                    finish();
                    return;
                }
                AlertController.AlertParams alertParams = this.mAlertParams;
                CharSequence loadSafeLabel = applicationInfo.loadSafeLabel(getPackageManager(), 1000.0f, 5);
                alertParams.mTitle = getText(C0444R.string.high_power_prompt_title);
                alertParams.mMessage = getString(C0444R.string.high_power_prompt_body, new Object[]{loadSafeLabel});
                alertParams.mPositiveButtonText = getText(C0444R.string.allow);
                alertParams.mNegativeButtonText = getText(C0444R.string.deny);
                alertParams.mPositiveButtonListener = this;
                alertParams.mNegativeButtonListener = this;
                setupAlert();
            } catch (PackageManager.NameNotFoundException unused) {
                debugLog("Requested package doesn't exist: " + this.mPackageName);
                finish();
            }
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            this.mPowerWhitelistManager.addToWhitelist(this.mPackageName);
        }
    }
}
