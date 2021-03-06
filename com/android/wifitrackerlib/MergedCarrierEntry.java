package com.android.wifitrackerlib;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.wifitrackerlib.WifiEntry;
import java.util.StringJoiner;

public class MergedCarrierEntry extends WifiEntry {
    private final Context mContext;
    boolean mIsCellDefaultRoute;
    private final String mKey;
    private final int mSubscriptionId;

    MergedCarrierEntry(Handler handler, WifiManager wifiManager, boolean z, Context context, int i) throws IllegalArgumentException {
        super(handler, wifiManager, z);
        this.mContext = context;
        this.mSubscriptionId = i;
        this.mKey = "MergedCarrierEntry:" + i;
    }

    public String getKey() {
        return this.mKey;
    }

    public String getSummary(boolean z) {
        StringJoiner stringJoiner = new StringJoiner(this.mContext.getString(R$string.wifitrackerlib_summary_separator));
        if (!z) {
            String verboseLoggingDescription = Utils.getVerboseLoggingDescription(this);
            if (!TextUtils.isEmpty(verboseLoggingDescription)) {
                stringJoiner.add(verboseLoggingDescription);
            }
        }
        return stringJoiner.toString();
    }

    public synchronized String getSsid() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo == null) {
            return null;
        }
        return WifiInfo.sanitizeSsid(wifiInfo.getSSID());
    }

    public synchronized String getMacAddress() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            String macAddress = wifiInfo.getMacAddress();
            if (!TextUtils.isEmpty(macAddress) && !TextUtils.equals(macAddress, "02:00:00:00:00:00")) {
                return macAddress;
            }
        }
        return null;
    }

    public synchronized boolean canConnect() {
        return getConnectedState() == 0 && !this.mIsCellDefaultRoute;
    }

    public synchronized void connect(WifiEntry.ConnectCallback connectCallback) {
        connect(connectCallback, true);
    }

    public synchronized void connect(WifiEntry.ConnectCallback connectCallback, boolean z) {
        this.mConnectCallback = connectCallback;
        this.mWifiManager.startRestrictingAutoJoinToSubscriptionId(this.mSubscriptionId);
        if (z) {
            Toast.makeText(this.mContext, R$string.wifitrackerlib_wifi_wont_autoconnect_for_now, 0).show();
        }
        if (this.mConnectCallback != null) {
            this.mCallbackHandler.post(new MergedCarrierEntry$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$connect$0() {
        WifiEntry.ConnectCallback connectCallback = this.mConnectCallback;
        if (connectCallback != null) {
            connectCallback.onConnectResult(0);
        }
    }

    public boolean canDisconnect() {
        return getConnectedState() == 2;
    }

    public synchronized void disconnect(WifiEntry.DisconnectCallback disconnectCallback) {
        this.mDisconnectCallback = disconnectCallback;
        this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
        this.mWifiManager.startScan();
        if (this.mDisconnectCallback != null) {
            this.mCallbackHandler.post(new MergedCarrierEntry$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$disconnect$1() {
        WifiEntry.DisconnectCallback disconnectCallback = this.mDisconnectCallback;
        if (disconnectCallback != null) {
            disconnectCallback.onDisconnectResult(0);
        }
    }

    /* access modifiers changed from: protected */
    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return wifiInfo.isCarrierMerged() && this.mSubscriptionId == wifiInfo.getSubscriptionId();
    }

    public void setEnabled(boolean z) {
        this.mWifiManager.setCarrierNetworkOffloadEnabled(this.mSubscriptionId, true, z);
        if (!z) {
            this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
            this.mWifiManager.startScan();
        }
    }

    /* access modifiers changed from: package-private */
    public int getSubscriptionId() {
        return this.mSubscriptionId;
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateIsCellDefaultRoute(boolean z) {
        this.mIsCellDefaultRoute = z;
        notifyOnUpdated();
    }
}
