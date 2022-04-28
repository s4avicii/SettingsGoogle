package com.google.android.wifitrackerlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import androidx.core.util.Preconditions;
import androidx.lifecycle.Lifecycle;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import com.google.android.wifitrackerlib.WsuManager;
import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GoogleWifiPickerTracker extends WifiPickerTracker {
    @VisibleForTesting
    public WsuManager mWsuManager;
    private final WsuManager.WsuProvidersLoadCallback mWsuProvidersLoadCallback = new GoogleWifiPickerTracker$$ExternalSyntheticLambda0(this);
    private WsuManager.WsuProvisionStatusUpdateCallback mWsuProvisonStatusCallback = new GoogleWifiPickerTracker$$ExternalSyntheticLambda1(this);
    private final Map<String, WsuWifiEntry> mWsuWifiEntryCache = new HashMap();

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.mWifiManager.getWifiState() != 1) {
            List<ScanResult> scanResults = this.mScanResultUpdater.getScanResults(this.mMaxScanAgeMillis);
            this.mWsuWifiEntryCache.clear();
            updateWsuWifiEntryScans(scanResults);
            updateWifiEntries();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(WsuProvider wsuProvider, int i) {
        Log.d("GoogleWifiPickerTracker", "WSU provision status update: " + i + ", for network: " + wsuProvider.networkGroupIdentity);
        if (i == 2) {
            List<ScanResult> scanResults = this.mScanResultUpdater.getScanResults();
            this.mWsuWifiEntryCache.clear();
            updateWsuWifiEntryScans(scanResults);
            processConfiguredNetworksChanged();
            return;
        }
        WsuWifiEntry wsuWifiEntry = this.mWsuWifiEntryCache.get(WsuWifiEntry.generateWsuWifiEntryKey(wsuProvider));
        if (wsuWifiEntry != null) {
            wsuWifiEntry.notifyProvisionStatusChanged(i);
        }
    }

    public GoogleWifiPickerTracker(Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2, Clock clock, long j, long j2, WifiPickerTracker.WifiPickerTrackerCallback wifiPickerTrackerCallback) {
        super(lifecycle, context, wifiManager, connectivityManager, handler, handler2, clock, j, j2, wifiPickerTrackerCallback);
        this.mWsuManager = new WsuManager(context, wifiManager, handler2);
    }

    public void onStart() {
        super.onStart();
        this.mWsuManager.addWsuProvidersLoadCallback(this.mWsuProvidersLoadCallback);
        this.mWsuManager.addWsuProvisionStatusUpdateCallback(this.mWsuProvisonStatusCallback);
        this.mWsuManager.bindAllServices();
    }

    public void onStop() {
        this.mWsuManager.removeWsuProvidersLoadCallback(this.mWsuProvidersLoadCallback);
        this.mWsuManager.removeWsuProvisionStatusUpdateCallback(this.mWsuProvisonStatusCallback);
        this.mWsuManager.unbindAllServices();
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public List<WifiEntry> getContextualWifiEntries() {
        return (List) this.mWsuWifiEntryCache.values().stream().collect(Collectors.toList());
    }

    /* access modifiers changed from: protected */
    public void updateContextualWifiEntryScans(List<ScanResult> list) {
        updateWsuWifiEntryScans(list);
    }

    private void updateWsuWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        Map<WsuProvider, List<ScanResult>> matchingWsuProviders = this.mWsuManager.getMatchingWsuProviders(list);
        for (WsuProvider next : matchingWsuProviders.keySet()) {
            String generateWsuWifiEntryKey = WsuWifiEntry.generateWsuWifiEntryKey(next);
            if (!this.mWsuWifiEntryCache.containsKey(generateWsuWifiEntryKey)) {
                this.mWsuWifiEntryCache.put(generateWsuWifiEntryKey, new WsuWifiEntry(this.mContext, this.mMainHandler, next, this.mWifiManager));
            }
            this.mWsuWifiEntryCache.get(generateWsuWifiEntryKey).updateScanResultInfo(matchingWsuProviders.get(next));
            this.mWsuWifiEntryCache.get(generateWsuWifiEntryKey).setSignupAction(this.mWsuManager.createSignupAction(next));
        }
        this.mWsuWifiEntryCache.entrySet().removeIf(GoogleWifiPickerTracker$$ExternalSyntheticLambda2.INSTANCE);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateWsuWifiEntryScans$2(Map.Entry entry) {
        return ((WsuWifiEntry) entry.getValue()).getLevel() == -1;
    }
}
