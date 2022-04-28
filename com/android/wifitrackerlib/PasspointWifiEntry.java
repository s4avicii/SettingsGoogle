package com.android.wifitrackerlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.util.Preconditions;
import com.android.wifitrackerlib.WifiEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class PasspointWifiEntry extends WifiEntry implements WifiEntry.WifiEntryCallback {
    private final Context mContext;
    private final List<ScanResult> mCurrentHomeScanResults = new ArrayList();
    private final List<ScanResult> mCurrentRoamingScanResults = new ArrayList();
    private final String mFqdn;
    private final String mFriendlyName;
    private final WifiTrackerInjector mInjector;
    private boolean mIsRoaming = false;
    private final String mKey;
    private int mMeteredOverride = 0;
    private OsuWifiEntry mOsuWifiEntry;
    private PasspointConfiguration mPasspointConfig;
    private boolean mShouldAutoOpenCaptivePortal = false;
    protected long mSubscriptionExpirationTimeInMillis;
    private List<Integer> mTargetSecurityTypes = List.of(11, 12);
    private WifiConfiguration mWifiConfig;

    /* access modifiers changed from: protected */
    public String getScanResultDescription() {
        return "";
    }

    PasspointWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, PasspointConfiguration passpointConfiguration, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        super(handler, wifiManager, z);
        Preconditions.checkNotNull(passpointConfiguration, "Cannot construct with null PasspointConfiguration!");
        this.mInjector = wifiTrackerInjector;
        this.mContext = context;
        this.mPasspointConfig = passpointConfiguration;
        this.mKey = uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId());
        String fqdn = passpointConfiguration.getHomeSp().getFqdn();
        this.mFqdn = fqdn;
        Preconditions.checkNotNull(fqdn, "Cannot construct with null PasspointConfiguration FQDN!");
        this.mFriendlyName = passpointConfiguration.getHomeSp().getFriendlyName();
        this.mSubscriptionExpirationTimeInMillis = passpointConfiguration.getSubscriptionExpirationTimeMillis();
        this.mMeteredOverride = this.mPasspointConfig.getMeteredOverride();
    }

    PasspointWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, WifiConfiguration wifiConfiguration, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        super(handler, wifiManager, z);
        Preconditions.checkNotNull(wifiConfiguration, "Cannot construct with null WifiConfiguration!");
        if (wifiConfiguration.isPasspoint()) {
            this.mInjector = wifiTrackerInjector;
            this.mContext = context;
            this.mWifiConfig = wifiConfiguration;
            this.mKey = uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey());
            String str = wifiConfiguration.FQDN;
            this.mFqdn = str;
            Preconditions.checkNotNull(str, "Cannot construct with null WifiConfiguration FQDN!");
            this.mFriendlyName = this.mWifiConfig.providerFriendlyName;
            return;
        }
        throw new IllegalArgumentException("Given WifiConfiguration is not for Passpoint!");
    }

    public String getKey() {
        return this.mKey;
    }

    public synchronized int getConnectedState() {
        OsuWifiEntry osuWifiEntry;
        if (!isExpired() || super.getConnectedState() != 0 || (osuWifiEntry = this.mOsuWifiEntry) == null) {
            return super.getConnectedState();
        }
        return osuWifiEntry.getConnectedState();
    }

    public String getTitle() {
        return this.mFriendlyName;
    }

    public synchronized String getSummary(boolean z) {
        StringJoiner stringJoiner;
        String str;
        stringJoiner = new StringJoiner(this.mContext.getString(R$string.wifitrackerlib_summary_separator));
        if (isExpired()) {
            OsuWifiEntry osuWifiEntry = this.mOsuWifiEntry;
            if (osuWifiEntry != null) {
                stringJoiner.add(osuWifiEntry.getSummary(z));
            } else {
                stringJoiner.add(this.mContext.getString(R$string.wifitrackerlib_wifi_passpoint_expired));
            }
        } else {
            int connectedState = getConnectedState();
            if (connectedState == 0) {
                str = Utils.getDisconnectedDescription(this.mInjector, this.mContext, this.mWifiConfig, this.mForSavedNetworksPage, z);
            } else if (connectedState == 1) {
                str = Utils.getConnectingDescription(this.mContext, this.mNetworkInfo);
            } else if (connectedState != 2) {
                Log.e("PasspointWifiEntry", "getConnectedState() returned unknown state: " + connectedState);
                str = null;
            } else {
                str = Utils.getConnectedDescription(this.mContext, this.mWifiConfig, this.mNetworkCapabilities, this.mIsDefaultNetwork, this.mIsLowQuality);
            }
            if (!TextUtils.isEmpty(str)) {
                stringJoiner.add(str);
            }
        }
        String autoConnectDescription = Utils.getAutoConnectDescription(this.mContext, this);
        if (!TextUtils.isEmpty(autoConnectDescription)) {
            stringJoiner.add(autoConnectDescription);
        }
        String meteredDescription = Utils.getMeteredDescription(this.mContext, this);
        if (!TextUtils.isEmpty(meteredDescription)) {
            stringJoiner.add(meteredDescription);
        }
        if (!z) {
            String verboseLoggingDescription = Utils.getVerboseLoggingDescription(this);
            if (!TextUtils.isEmpty(verboseLoggingDescription)) {
                stringJoiner.add(verboseLoggingDescription);
            }
        }
        return stringJoiner.toString();
    }

    public synchronized CharSequence getSecondSummary() {
        return getConnectedState() == 2 ? Utils.getImsiProtectionDescription(this.mContext, this.mWifiConfig) : "";
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001c, code lost:
        return r0 != null ? android.net.wifi.WifiInfo.sanitizeSsid(r0.SSID) : null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getSsid() {
        /*
            r1 = this;
            monitor-enter(r1)
            android.net.wifi.WifiInfo r0 = r1.mWifiInfo     // Catch:{ all -> 0x001d }
            if (r0 == 0) goto L_0x000f
            java.lang.String r0 = r0.getSSID()     // Catch:{ all -> 0x001d }
            java.lang.String r0 = android.net.wifi.WifiInfo.sanitizeSsid(r0)     // Catch:{ all -> 0x001d }
            monitor-exit(r1)
            return r0
        L_0x000f:
            android.net.wifi.WifiConfiguration r0 = r1.mWifiConfig     // Catch:{ all -> 0x001d }
            if (r0 == 0) goto L_0x001a
            java.lang.String r0 = r0.SSID     // Catch:{ all -> 0x001d }
            java.lang.String r0 = android.net.wifi.WifiInfo.sanitizeSsid(r0)     // Catch:{ all -> 0x001d }
            goto L_0x001b
        L_0x001a:
            r0 = 0
        L_0x001b:
            monitor-exit(r1)
            return r0
        L_0x001d:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.PasspointWifiEntry.getSsid():java.lang.String");
    }

    public synchronized List<Integer> getSecurityTypes() {
        return new ArrayList(this.mTargetSecurityTypes);
    }

    public synchronized String getMacAddress() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            String macAddress = wifiInfo.getMacAddress();
            if (!TextUtils.isEmpty(macAddress) && !TextUtils.equals(macAddress, "02:00:00:00:00:00")) {
                return macAddress;
            }
        }
        if (this.mWifiConfig != null) {
            if (getPrivacy() == 1) {
                return this.mWifiConfig.getRandomizedMacAddress().toString();
            }
        }
        String[] factoryMacAddresses = this.mWifiManager.getFactoryMacAddresses();
        if (factoryMacAddresses.length <= 0) {
            return null;
        }
        return factoryMacAddresses[0];
    }

    public synchronized boolean isMetered() {
        boolean z;
        WifiConfiguration wifiConfiguration;
        z = true;
        if (getMeteredChoice() != 1 && ((wifiConfiguration = this.mWifiConfig) == null || !wifiConfiguration.meteredHint)) {
            z = false;
        }
        return z;
    }

    public synchronized boolean isSuggestion() {
        WifiConfiguration wifiConfiguration;
        wifiConfiguration = this.mWifiConfig;
        return wifiConfiguration != null && wifiConfiguration.fromWifiNetworkSuggestion;
    }

    public synchronized boolean isSubscription() {
        return this.mPasspointConfig != null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0016, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0029, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean canConnect() {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = r4.isExpired()     // Catch:{ all -> 0x002a }
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0017
            com.android.wifitrackerlib.OsuWifiEntry r0 = r4.mOsuWifiEntry     // Catch:{ all -> 0x002a }
            if (r0 == 0) goto L_0x0014
            boolean r0 = r0.canConnect()     // Catch:{ all -> 0x002a }
            if (r0 == 0) goto L_0x0014
            goto L_0x0015
        L_0x0014:
            r1 = r2
        L_0x0015:
            monitor-exit(r4)
            return r1
        L_0x0017:
            int r0 = r4.mLevel     // Catch:{ all -> 0x002a }
            r3 = -1
            if (r0 == r3) goto L_0x0027
            int r0 = r4.getConnectedState()     // Catch:{ all -> 0x002a }
            if (r0 != 0) goto L_0x0027
            android.net.wifi.WifiConfiguration r0 = r4.mWifiConfig     // Catch:{ all -> 0x002a }
            if (r0 == 0) goto L_0x0027
            goto L_0x0028
        L_0x0027:
            r1 = r2
        L_0x0028:
            monitor-exit(r4)
            return r1
        L_0x002a:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.PasspointWifiEntry.canConnect():boolean");
    }

    public synchronized void connect(WifiEntry.ConnectCallback connectCallback) {
        OsuWifiEntry osuWifiEntry;
        if (!isExpired() || (osuWifiEntry = this.mOsuWifiEntry) == null) {
            this.mShouldAutoOpenCaptivePortal = true;
            this.mConnectCallback = connectCallback;
            if (this.mWifiConfig == null) {
                new WifiEntry.ConnectActionListener().onFailure(0);
            }
            this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
            this.mWifiManager.connect(this.mWifiConfig, new WifiEntry.ConnectActionListener());
            return;
        }
        osuWifiEntry.connect(connectCallback);
    }

    public boolean canDisconnect() {
        return getConnectedState() == 2;
    }

    public synchronized void disconnect(WifiEntry.DisconnectCallback disconnectCallback) {
        if (canDisconnect()) {
            this.mCalledDisconnect = true;
            this.mDisconnectCallback = disconnectCallback;
            this.mCallbackHandler.postDelayed(new PasspointWifiEntry$$ExternalSyntheticLambda0(this, disconnectCallback), 10000);
            this.mWifiManager.disableEphemeralNetwork(this.mFqdn);
            this.mWifiManager.disconnect();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$disconnect$0(WifiEntry.DisconnectCallback disconnectCallback) {
        if (disconnectCallback != null && this.mCalledDisconnect) {
            disconnectCallback.onDisconnectResult(1);
        }
    }

    public synchronized boolean canForget() {
        return !isSuggestion() && this.mPasspointConfig != null;
    }

    public synchronized void forget(WifiEntry.ForgetCallback forgetCallback) {
        if (canForget()) {
            this.mForgetCallback = forgetCallback;
            this.mWifiManager.removePasspointConfiguration(this.mPasspointConfig.getHomeSp().getFqdn());
            new WifiEntry.ForgetActionListener().onSuccess();
        }
    }

    public synchronized int getMeteredChoice() {
        int i = this.mMeteredOverride;
        if (i == 1) {
            return 1;
        }
        if (i == 2) {
            return 2;
        }
        return 0;
    }

    public synchronized boolean canSetMeteredChoice() {
        return !isSuggestion() && this.mPasspointConfig != null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0033, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setMeteredChoice(int r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            android.net.wifi.hotspot2.PasspointConfiguration r0 = r2.mPasspointConfig     // Catch:{ all -> 0x0034 }
            if (r0 == 0) goto L_0x0032
            boolean r0 = r2.canSetMeteredChoice()     // Catch:{ all -> 0x0034 }
            if (r0 != 0) goto L_0x000c
            goto L_0x0032
        L_0x000c:
            if (r3 == 0) goto L_0x001c
            r0 = 1
            if (r3 == r0) goto L_0x0019
            r0 = 2
            if (r3 == r0) goto L_0x0016
            monitor-exit(r2)
            return
        L_0x0016:
            r2.mMeteredOverride = r0     // Catch:{ all -> 0x0034 }
            goto L_0x001f
        L_0x0019:
            r2.mMeteredOverride = r0     // Catch:{ all -> 0x0034 }
            goto L_0x001f
        L_0x001c:
            r3 = 0
            r2.mMeteredOverride = r3     // Catch:{ all -> 0x0034 }
        L_0x001f:
            android.net.wifi.WifiManager r3 = r2.mWifiManager     // Catch:{ all -> 0x0034 }
            android.net.wifi.hotspot2.PasspointConfiguration r0 = r2.mPasspointConfig     // Catch:{ all -> 0x0034 }
            android.net.wifi.hotspot2.pps.HomeSp r0 = r0.getHomeSp()     // Catch:{ all -> 0x0034 }
            java.lang.String r0 = r0.getFqdn()     // Catch:{ all -> 0x0034 }
            int r1 = r2.mMeteredOverride     // Catch:{ all -> 0x0034 }
            r3.setPasspointMeteredOverride(r0, r1)     // Catch:{ all -> 0x0034 }
            monitor-exit(r2)
            return
        L_0x0032:
            monitor-exit(r2)
            return
        L_0x0034:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.PasspointWifiEntry.setMeteredChoice(int):void");
    }

    public synchronized boolean canSetPrivacy() {
        return !isSuggestion() && this.mPasspointConfig != null;
    }

    public synchronized int getPrivacy() {
        PasspointConfiguration passpointConfiguration = this.mPasspointConfig;
        if (passpointConfiguration == null) {
            return 1;
        }
        return passpointConfiguration.isMacRandomizationEnabled() ? 1 : 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0023, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setPrivacy(int r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            android.net.wifi.hotspot2.PasspointConfiguration r0 = r2.mPasspointConfig     // Catch:{ all -> 0x0024 }
            if (r0 == 0) goto L_0x0022
            boolean r0 = r2.canSetPrivacy()     // Catch:{ all -> 0x0024 }
            if (r0 != 0) goto L_0x000c
            goto L_0x0022
        L_0x000c:
            android.net.wifi.WifiManager r0 = r2.mWifiManager     // Catch:{ all -> 0x0024 }
            android.net.wifi.hotspot2.PasspointConfiguration r1 = r2.mPasspointConfig     // Catch:{ all -> 0x0024 }
            android.net.wifi.hotspot2.pps.HomeSp r1 = r1.getHomeSp()     // Catch:{ all -> 0x0024 }
            java.lang.String r1 = r1.getFqdn()     // Catch:{ all -> 0x0024 }
            if (r3 != 0) goto L_0x001c
            r3 = 0
            goto L_0x001d
        L_0x001c:
            r3 = 1
        L_0x001d:
            r0.setMacRandomizationSettingPasspointEnabled(r1, r3)     // Catch:{ all -> 0x0024 }
            monitor-exit(r2)
            return
        L_0x0022:
            monitor-exit(r2)
            return
        L_0x0024:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.PasspointWifiEntry.setPrivacy(int):void");
    }

    public synchronized boolean isAutoJoinEnabled() {
        PasspointConfiguration passpointConfiguration = this.mPasspointConfig;
        if (passpointConfiguration != null) {
            return passpointConfiguration.isAutojoinEnabled();
        }
        WifiConfiguration wifiConfiguration = this.mWifiConfig;
        if (wifiConfiguration == null) {
            return false;
        }
        return wifiConfiguration.allowAutojoin;
    }

    public synchronized boolean canSetAutoJoinEnabled() {
        return (this.mPasspointConfig == null && this.mWifiConfig == null) ? false : true;
    }

    public synchronized void setAutoJoinEnabled(boolean z) {
        PasspointConfiguration passpointConfiguration = this.mPasspointConfig;
        if (passpointConfiguration != null) {
            this.mWifiManager.allowAutojoinPasspoint(passpointConfiguration.getHomeSp().getFqdn(), z);
        } else {
            WifiConfiguration wifiConfiguration = this.mWifiConfig;
            if (wifiConfiguration != null) {
                this.mWifiManager.allowAutojoin(wifiConfiguration.networkId, z);
            }
        }
    }

    public String getSecurityString(boolean z) {
        return this.mContext.getString(R$string.wifitrackerlib_wifi_security_passpoint);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0018, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean isExpired() {
        /*
            r6 = this;
            monitor-enter(r6)
            long r0 = r6.mSubscriptionExpirationTimeInMillis     // Catch:{ all -> 0x0019 }
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            r1 = 0
            if (r0 > 0) goto L_0x000c
            monitor-exit(r6)
            return r1
        L_0x000c:
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0019 }
            long r4 = r6.mSubscriptionExpirationTimeInMillis     // Catch:{ all -> 0x0019 }
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 < 0) goto L_0x0017
            r1 = 1
        L_0x0017:
            monitor-exit(r6)
            return r1
        L_0x0019:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.PasspointWifiEntry.isExpired():boolean");
    }

    /* access modifiers changed from: package-private */
    public synchronized void updatePasspointConfig(PasspointConfiguration passpointConfiguration) {
        this.mPasspointConfig = passpointConfiguration;
        if (passpointConfiguration != null) {
            this.mSubscriptionExpirationTimeInMillis = passpointConfiguration.getSubscriptionExpirationTimeMillis();
            this.mMeteredOverride = passpointConfiguration.getMeteredOverride();
        }
        notifyOnUpdated();
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateScanResultInfo(WifiConfiguration wifiConfiguration, List<ScanResult> list, List<ScanResult> list2) throws IllegalArgumentException {
        this.mIsRoaming = false;
        this.mWifiConfig = wifiConfiguration;
        this.mCurrentHomeScanResults.clear();
        this.mCurrentRoamingScanResults.clear();
        if (list != null) {
            this.mCurrentHomeScanResults.addAll(list);
        }
        if (list2 != null) {
            this.mCurrentRoamingScanResults.addAll(list2);
        }
        int i = -1;
        if (this.mWifiConfig != null) {
            ArrayList arrayList = new ArrayList();
            if (list != null && !list.isEmpty()) {
                arrayList.addAll(list);
            } else if (list2 != null && !list2.isEmpty()) {
                arrayList.addAll(list2);
                this.mIsRoaming = true;
            }
            ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(arrayList);
            if (bestScanResultByLevel != null) {
                WifiConfiguration wifiConfiguration2 = this.mWifiConfig;
                wifiConfiguration2.SSID = "\"" + bestScanResultByLevel.SSID + "\"";
            }
            if (getConnectedState() == 0) {
                if (bestScanResultByLevel != null) {
                    i = this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level);
                }
                this.mLevel = i;
            }
        } else {
            this.mLevel = -1;
        }
        notifyOnUpdated();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void updateSecurityTypes() {
        /*
            r2 = this;
            monitor-enter(r2)
            android.net.wifi.WifiInfo r0 = r2.mWifiInfo     // Catch:{ all -> 0x001a }
            if (r0 == 0) goto L_0x0018
            int r0 = r0.getCurrentSecurityType()     // Catch:{ all -> 0x001a }
            r1 = -1
            if (r0 == r1) goto L_0x0018
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x001a }
            java.util.List r0 = java.util.Collections.singletonList(r0)     // Catch:{ all -> 0x001a }
            r2.mTargetSecurityTypes = r0     // Catch:{ all -> 0x001a }
            monitor-exit(r2)
            return
        L_0x0018:
            monitor-exit(r2)
            return
        L_0x001a:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.PasspointWifiEntry.updateSecurityTypes():void");
    }

    /* access modifiers changed from: protected */
    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (!wifiInfo.isPasspointAp()) {
            return false;
        }
        return TextUtils.equals(wifiInfo.getPasspointFqdn(), this.mFqdn);
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        super.updateNetworkCapabilities(networkCapabilities);
        if (canSignIn() && this.mShouldAutoOpenCaptivePortal) {
            this.mShouldAutoOpenCaptivePortal = false;
            signIn((WifiEntry.SignInCallback) null);
        }
    }

    /* access modifiers changed from: package-private */
    public static String uniqueIdToPasspointWifiEntryKey(String str) {
        Preconditions.checkNotNull(str, "Cannot create key with null unique id!");
        return "PasspointWifiEntry:" + str;
    }

    /* access modifiers changed from: package-private */
    public synchronized String getNetworkSelectionDescription() {
        return Utils.getNetworkSelectionDescription(this.mWifiConfig);
    }

    /* access modifiers changed from: package-private */
    public synchronized void setOsuWifiEntry(OsuWifiEntry osuWifiEntry) {
        this.mOsuWifiEntry = osuWifiEntry;
        if (osuWifiEntry != null) {
            osuWifiEntry.setListener(this);
        }
    }

    public void onUpdated() {
        notifyOnUpdated();
    }

    public synchronized boolean canSignIn() {
        NetworkCapabilities networkCapabilities;
        networkCapabilities = this.mNetworkCapabilities;
        return networkCapabilities != null && networkCapabilities.hasCapability(17);
    }

    public void signIn(WifiEntry.SignInCallback signInCallback) {
        if (canSignIn()) {
            HiddenApiWrapper.startCaptivePortalApp((ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class), this.mWifiManager.getCurrentNetwork());
        }
    }

    public PasspointConfiguration getPasspointConfig() {
        return this.mPasspointConfig;
    }
}
