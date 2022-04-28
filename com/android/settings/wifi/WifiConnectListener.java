package com.android.settings.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Toast;
import androidx.window.C0444R;

public class WifiConnectListener implements WifiManager.ActionListener {
    private final Context mContext;

    public void onSuccess() {
    }

    public WifiConnectListener(Context context) {
        this.mContext = context;
    }

    public void onFailure(int i) {
        Context context = this.mContext;
        if (context != null) {
            Toast.makeText(context, C0444R.string.wifi_failed_connect_message, 0).show();
        }
    }
}
