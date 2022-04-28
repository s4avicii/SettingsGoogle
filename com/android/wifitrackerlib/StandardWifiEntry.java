package com.android.wifitrackerlib;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.app.admin.WifiSsidPolicy;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiSsid;
import android.os.Handler;
import android.os.SystemClock;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import androidx.core.p002os.BuildCompat;
import com.android.wifitrackerlib.WifiEntry;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StandardWifiEntry extends WifiEntry {
    private final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;
    private boolean mHasAddConfigUserRestriction;
    private final WifiTrackerInjector mInjector;
    private boolean mIsAdminRestricted;
    private final boolean mIsEnhancedOpenSupported;
    private boolean mIsUserShareable;
    private final boolean mIsWpa3SaeSupported;
    private final boolean mIsWpa3SuiteBSupported;
    private final StandardWifiEntryKey mKey;
    private final Map<Integer, List<ScanResult>> mMatchingScanResults;
    private final Map<Integer, WifiConfiguration> mMatchingWifiConfigs;
    private boolean mShouldAutoOpenCaptivePortal;
    private final List<ScanResult> mTargetScanResults;
    private List<Integer> mTargetSecurityTypes;
    private WifiConfiguration mTargetWifiConfig;
    private final UserManager mUserManager;

    StandardWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, WifiManager wifiManager, boolean z) {
        super(handler, wifiManager, z);
        this.mMatchingScanResults = new HashMap();
        this.mMatchingWifiConfigs = new HashMap();
        this.mTargetScanResults = new ArrayList();
        this.mTargetSecurityTypes = new ArrayList();
        this.mIsUserShareable = false;
        this.mShouldAutoOpenCaptivePortal = false;
        this.mIsAdminRestricted = false;
        this.mHasAddConfigUserRestriction = false;
        this.mInjector = wifiTrackerInjector;
        this.mContext = context;
        this.mKey = standardWifiEntryKey;
        this.mIsWpa3SaeSupported = wifiManager.isWpa3SaeSupported();
        this.mIsWpa3SuiteBSupported = wifiManager.isWpa3SuiteBSupported();
        this.mIsEnhancedOpenSupported = wifiManager.isEnhancedOpenSupported();
        this.mUserManager = wifiTrackerInjector.getUserManager();
        this.mDevicePolicyManager = wifiTrackerInjector.getDevicePolicyManager();
        updateSecurityTypes();
        if (BuildCompat.isAtLeastT()) {
            updateAdminRestrictions();
        }
    }

    StandardWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, List<WifiConfiguration> list, List<ScanResult> list2, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        this(wifiTrackerInjector, context, handler, standardWifiEntryKey, wifiManager, z);
        if (list != null && !list.isEmpty()) {
            updateConfig(list);
        }
        if (list2 != null && !list2.isEmpty()) {
            updateScanResultInfo(list2);
        }
    }

    public String getKey() {
        return this.mKey.toString();
    }

    /* access modifiers changed from: package-private */
    public StandardWifiEntryKey getStandardWifiEntryKey() {
        return this.mKey;
    }

    public String getTitle() {
        return this.mKey.getScanResultKey().getSsid();
    }

    public synchronized String getSummary(boolean z) {
        String str;
        if (hasAdminRestrictions()) {
            return this.mContext.getString(R$string.wifitrackerlib_admin_restricted_network);
        }
        StringJoiner stringJoiner = new StringJoiner(this.mContext.getString(R$string.wifitrackerlib_summary_separator));
        int connectedState = getConnectedState();
        if (connectedState == 0) {
            str = Utils.getDisconnectedDescription(this.mInjector, this.mContext, this.mTargetWifiConfig, this.mForSavedNetworksPage, z);
        } else if (connectedState == 1) {
            str = Utils.getConnectingDescription(this.mContext, this.mNetworkInfo);
        } else if (connectedState != 2) {
            Log.e("StandardWifiEntry", "getConnectedState() returned unknown state: " + connectedState);
            str = null;
        } else {
            str = Utils.getConnectedDescription(this.mContext, this.mTargetWifiConfig, this.mNetworkCapabilities, this.mIsDefaultNetwork, this.mIsLowQuality);
        }
        if (!TextUtils.isEmpty(str)) {
            stringJoiner.add(str);
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

    public CharSequence getSecondSummary() {
        return getConnectedState() == 2 ? Utils.getImsiProtectionDescription(this.mContext, getWifiConfiguration()) : "";
    }

    public String getSsid() {
        return this.mKey.getScanResultKey().getSsid();
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
        if (this.mTargetWifiConfig != null) {
            if (getPrivacy() == 1) {
                return this.mTargetWifiConfig.getRandomizedMacAddress().toString();
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
        if (getMeteredChoice() != 1 && ((wifiConfiguration = this.mTargetWifiConfig) == null || !wifiConfiguration.meteredHint)) {
            z = false;
        }
        return z;
    }

    public synchronized boolean isSaved() {
        WifiConfiguration wifiConfiguration;
        wifiConfiguration = this.mTargetWifiConfig;
        return wifiConfiguration != null && !wifiConfiguration.fromWifiNetworkSuggestion && !wifiConfiguration.isEphemeral();
    }

    public synchronized boolean isSuggestion() {
        WifiConfiguration wifiConfiguration;
        wifiConfiguration = this.mTargetWifiConfig;
        return wifiConfiguration != null && wifiConfiguration.fromWifiNetworkSuggestion;
    }

    public synchronized WifiConfiguration getWifiConfiguration() {
        if (!isSaved()) {
            return null;
        }
        return this.mTargetWifiConfig;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0073, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0075, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0077, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean canConnect() {
        /*
            r5 = this;
            monitor-enter(r5)
            int r0 = r5.mLevel     // Catch:{ all -> 0x0078 }
            r1 = -1
            r2 = 0
            if (r0 == r1) goto L_0x0076
            int r0 = r5.getConnectedState()     // Catch:{ all -> 0x0078 }
            if (r0 == 0) goto L_0x000e
            goto L_0x0076
        L_0x000e:
            boolean r0 = r5.hasAdminRestrictions()     // Catch:{ all -> 0x0078 }
            if (r0 == 0) goto L_0x0016
            monitor-exit(r5)
            return r2
        L_0x0016:
            java.util.List<java.lang.Integer> r0 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x0078 }
            r3 = 3
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x0078 }
            boolean r0 = r0.contains(r3)     // Catch:{ all -> 0x0078 }
            r3 = 1
            if (r0 == 0) goto L_0x0074
            android.net.wifi.WifiConfiguration r0 = r5.mTargetWifiConfig     // Catch:{ all -> 0x0078 }
            if (r0 == 0) goto L_0x0074
            android.net.wifi.WifiEnterpriseConfig r0 = r0.enterpriseConfig     // Catch:{ all -> 0x0078 }
            if (r0 == 0) goto L_0x0074
            boolean r0 = r0.isAuthenticationSimBased()     // Catch:{ all -> 0x0078 }
            if (r0 != 0) goto L_0x0034
            monitor-exit(r5)
            return r3
        L_0x0034:
            android.content.Context r0 = r5.mContext     // Catch:{ all -> 0x0078 }
            java.lang.String r4 = "telephony_subscription_service"
            java.lang.Object r0 = r0.getSystemService(r4)     // Catch:{ all -> 0x0078 }
            android.telephony.SubscriptionManager r0 = (android.telephony.SubscriptionManager) r0     // Catch:{ all -> 0x0078 }
            java.util.List r0 = r0.getActiveSubscriptionInfoList()     // Catch:{ all -> 0x0078 }
            if (r0 == 0) goto L_0x0072
            int r4 = r0.size()     // Catch:{ all -> 0x0078 }
            if (r4 != 0) goto L_0x004c
            goto L_0x0072
        L_0x004c:
            android.net.wifi.WifiConfiguration r4 = r5.mTargetWifiConfig     // Catch:{ all -> 0x0078 }
            int r4 = r4.carrierId     // Catch:{ all -> 0x0078 }
            if (r4 != r1) goto L_0x0054
            monitor-exit(r5)
            return r3
        L_0x0054:
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x0078 }
        L_0x0058:
            boolean r1 = r0.hasNext()     // Catch:{ all -> 0x0078 }
            if (r1 == 0) goto L_0x0070
            java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x0078 }
            android.telephony.SubscriptionInfo r1 = (android.telephony.SubscriptionInfo) r1     // Catch:{ all -> 0x0078 }
            int r1 = r1.getCarrierId()     // Catch:{ all -> 0x0078 }
            android.net.wifi.WifiConfiguration r4 = r5.mTargetWifiConfig     // Catch:{ all -> 0x0078 }
            int r4 = r4.carrierId     // Catch:{ all -> 0x0078 }
            if (r1 != r4) goto L_0x0058
            monitor-exit(r5)
            return r3
        L_0x0070:
            monitor-exit(r5)
            return r2
        L_0x0072:
            monitor-exit(r5)
            return r2
        L_0x0074:
            monitor-exit(r5)
            return r3
        L_0x0076:
            monitor-exit(r5)
            return r2
        L_0x0078:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.canConnect():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0108, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0118, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void connect(com.android.wifitrackerlib.WifiEntry.ConnectCallback r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            r4.mConnectCallback = r5     // Catch:{ all -> 0x0119 }
            r0 = 1
            r4.mShouldAutoOpenCaptivePortal = r0     // Catch:{ all -> 0x0119 }
            android.net.wifi.WifiManager r0 = r4.mWifiManager     // Catch:{ all -> 0x0119 }
            r0.stopRestrictingAutoJoinToSubscriptionId()     // Catch:{ all -> 0x0119 }
            boolean r0 = r4.isSaved()     // Catch:{ all -> 0x0119 }
            if (r0 != 0) goto L_0x00e7
            boolean r0 = r4.isSuggestion()     // Catch:{ all -> 0x0119 }
            if (r0 == 0) goto L_0x0019
            goto L_0x00e7
        L_0x0019:
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x0119 }
            r1 = 6
            java.lang.Integer r2 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0119 }
            boolean r0 = r0.contains(r2)     // Catch:{ all -> 0x0119 }
            r2 = 0
            if (r0 == 0) goto L_0x0099
            android.net.wifi.WifiConfiguration r5 = new android.net.wifi.WifiConfiguration     // Catch:{ all -> 0x0119 }
            r5.<init>()     // Catch:{ all -> 0x0119 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0119 }
            r0.<init>()     // Catch:{ all -> 0x0119 }
            java.lang.String r3 = "\""
            r0.append(r3)     // Catch:{ all -> 0x0119 }
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r3 = r4.mKey     // Catch:{ all -> 0x0119 }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r3 = r3.getScanResultKey()     // Catch:{ all -> 0x0119 }
            java.lang.String r3 = r3.getSsid()     // Catch:{ all -> 0x0119 }
            r0.append(r3)     // Catch:{ all -> 0x0119 }
            java.lang.String r3 = "\""
            r0.append(r3)     // Catch:{ all -> 0x0119 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0119 }
            r5.SSID = r0     // Catch:{ all -> 0x0119 }
            r5.setSecurityParams(r1)     // Catch:{ all -> 0x0119 }
            android.net.wifi.WifiManager r0 = r4.mWifiManager     // Catch:{ all -> 0x0119 }
            com.android.wifitrackerlib.WifiEntry$ConnectActionListener r1 = new com.android.wifitrackerlib.WifiEntry$ConnectActionListener     // Catch:{ all -> 0x0119 }
            r1.<init>()     // Catch:{ all -> 0x0119 }
            r0.connect(r5, r1)     // Catch:{ all -> 0x0119 }
            java.util.List<java.lang.Integer> r5 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x0119 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x0119 }
            boolean r5 = r5.contains(r0)     // Catch:{ all -> 0x0119 }
            if (r5 == 0) goto L_0x0117
            android.net.wifi.WifiConfiguration r5 = new android.net.wifi.WifiConfiguration     // Catch:{ all -> 0x0119 }
            r5.<init>()     // Catch:{ all -> 0x0119 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0119 }
            r0.<init>()     // Catch:{ all -> 0x0119 }
            java.lang.String r1 = "\""
            r0.append(r1)     // Catch:{ all -> 0x0119 }
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r1 = r4.mKey     // Catch:{ all -> 0x0119 }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r1 = r1.getScanResultKey()     // Catch:{ all -> 0x0119 }
            java.lang.String r1 = r1.getSsid()     // Catch:{ all -> 0x0119 }
            r0.append(r1)     // Catch:{ all -> 0x0119 }
            java.lang.String r1 = "\""
            r0.append(r1)     // Catch:{ all -> 0x0119 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0119 }
            r5.SSID = r0     // Catch:{ all -> 0x0119 }
            r5.setSecurityParams(r2)     // Catch:{ all -> 0x0119 }
            android.net.wifi.WifiManager r0 = r4.mWifiManager     // Catch:{ all -> 0x0119 }
            r1 = 0
            r0.save(r5, r1)     // Catch:{ all -> 0x0119 }
            goto L_0x0117
        L_0x0099:
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x0119 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x0119 }
            boolean r0 = r0.contains(r1)     // Catch:{ all -> 0x0119 }
            if (r0 == 0) goto L_0x00da
            android.net.wifi.WifiConfiguration r5 = new android.net.wifi.WifiConfiguration     // Catch:{ all -> 0x0119 }
            r5.<init>()     // Catch:{ all -> 0x0119 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0119 }
            r0.<init>()     // Catch:{ all -> 0x0119 }
            java.lang.String r1 = "\""
            r0.append(r1)     // Catch:{ all -> 0x0119 }
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r1 = r4.mKey     // Catch:{ all -> 0x0119 }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r1 = r1.getScanResultKey()     // Catch:{ all -> 0x0119 }
            java.lang.String r1 = r1.getSsid()     // Catch:{ all -> 0x0119 }
            r0.append(r1)     // Catch:{ all -> 0x0119 }
            java.lang.String r1 = "\""
            r0.append(r1)     // Catch:{ all -> 0x0119 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0119 }
            r5.SSID = r0     // Catch:{ all -> 0x0119 }
            r5.setSecurityParams(r2)     // Catch:{ all -> 0x0119 }
            android.net.wifi.WifiManager r0 = r4.mWifiManager     // Catch:{ all -> 0x0119 }
            com.android.wifitrackerlib.WifiEntry$ConnectActionListener r1 = new com.android.wifitrackerlib.WifiEntry$ConnectActionListener     // Catch:{ all -> 0x0119 }
            r1.<init>()     // Catch:{ all -> 0x0119 }
            r0.connect(r5, r1)     // Catch:{ all -> 0x0119 }
            goto L_0x0117
        L_0x00da:
            if (r5 == 0) goto L_0x0117
            android.os.Handler r0 = r4.mCallbackHandler     // Catch:{ all -> 0x0119 }
            com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda2 r1 = new com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda2     // Catch:{ all -> 0x0119 }
            r1.<init>(r5)     // Catch:{ all -> 0x0119 }
            r0.post(r1)     // Catch:{ all -> 0x0119 }
            goto L_0x0117
        L_0x00e7:
            android.net.wifi.WifiConfiguration r0 = r4.mTargetWifiConfig     // Catch:{ all -> 0x0119 }
            boolean r0 = com.android.wifitrackerlib.Utils.isSimCredential(r0)     // Catch:{ all -> 0x0119 }
            if (r0 == 0) goto L_0x0109
            android.content.Context r0 = r4.mContext     // Catch:{ all -> 0x0119 }
            android.net.wifi.WifiConfiguration r1 = r4.mTargetWifiConfig     // Catch:{ all -> 0x0119 }
            int r1 = r1.carrierId     // Catch:{ all -> 0x0119 }
            boolean r0 = com.android.wifitrackerlib.Utils.isSimPresent(r0, r1)     // Catch:{ all -> 0x0119 }
            if (r0 != 0) goto L_0x0109
            if (r5 == 0) goto L_0x0107
            android.os.Handler r0 = r4.mCallbackHandler     // Catch:{ all -> 0x0119 }
            com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda1 r1 = new com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda1     // Catch:{ all -> 0x0119 }
            r1.<init>(r5)     // Catch:{ all -> 0x0119 }
            r0.post(r1)     // Catch:{ all -> 0x0119 }
        L_0x0107:
            monitor-exit(r4)
            return
        L_0x0109:
            android.net.wifi.WifiManager r5 = r4.mWifiManager     // Catch:{ all -> 0x0119 }
            android.net.wifi.WifiConfiguration r0 = r4.mTargetWifiConfig     // Catch:{ all -> 0x0119 }
            int r0 = r0.networkId     // Catch:{ all -> 0x0119 }
            com.android.wifitrackerlib.WifiEntry$ConnectActionListener r1 = new com.android.wifitrackerlib.WifiEntry$ConnectActionListener     // Catch:{ all -> 0x0119 }
            r1.<init>()     // Catch:{ all -> 0x0119 }
            r5.connect(r0, r1)     // Catch:{ all -> 0x0119 }
        L_0x0117:
            monitor-exit(r4)
            return
        L_0x0119:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.connect(com.android.wifitrackerlib.WifiEntry$ConnectCallback):void");
    }

    public boolean canDisconnect() {
        return getConnectedState() == 2;
    }

    public synchronized void disconnect(WifiEntry.DisconnectCallback disconnectCallback) {
        if (canDisconnect()) {
            this.mCalledDisconnect = true;
            this.mDisconnectCallback = disconnectCallback;
            this.mCallbackHandler.postDelayed(new StandardWifiEntry$$ExternalSyntheticLambda0(this, disconnectCallback), 10000);
            WifiManager wifiManager = this.mWifiManager;
            wifiManager.disableEphemeralNetwork("\"" + this.mKey.getScanResultKey().getSsid() + "\"");
            this.mWifiManager.disconnect();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$disconnect$2(WifiEntry.DisconnectCallback disconnectCallback) {
        if (disconnectCallback != null && this.mCalledDisconnect) {
            disconnectCallback.onDisconnectResult(1);
        }
    }

    public boolean canForget() {
        return getWifiConfiguration() != null;
    }

    public synchronized void forget(WifiEntry.ForgetCallback forgetCallback) {
        if (canForget()) {
            this.mForgetCallback = forgetCallback;
            this.mWifiManager.forget(this.mTargetWifiConfig.networkId, new WifiEntry.ForgetActionListener());
        }
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

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x005e, code lost:
        return true;
     */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0044  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean canShare() {
        /*
            r5 = this;
            monitor-enter(r5)
            com.android.wifitrackerlib.WifiTrackerInjector r0 = r5.mInjector     // Catch:{ all -> 0x0061 }
            boolean r0 = r0.isDemoMode()     // Catch:{ all -> 0x0061 }
            r1 = 0
            if (r0 == 0) goto L_0x000c
            monitor-exit(r5)
            return r1
        L_0x000c:
            android.net.wifi.WifiConfiguration r0 = r5.getWifiConfiguration()     // Catch:{ all -> 0x0061 }
            if (r0 != 0) goto L_0x0014
            monitor-exit(r5)
            return r1
        L_0x0014:
            boolean r2 = androidx.core.p002os.BuildCompat.isAtLeastT()     // Catch:{ all -> 0x0061 }
            if (r2 == 0) goto L_0x0038
            android.os.UserManager r2 = r5.mUserManager     // Catch:{ all -> 0x0061 }
            java.lang.String r3 = "no_sharing_admin_configured_wifi"
            int r4 = r0.creatorUid     // Catch:{ all -> 0x0061 }
            android.os.UserHandle r4 = android.os.UserHandle.getUserHandleForUid(r4)     // Catch:{ all -> 0x0061 }
            boolean r2 = r2.hasUserRestrictionForUser(r3, r4)     // Catch:{ all -> 0x0061 }
            if (r2 == 0) goto L_0x0038
            int r2 = r0.creatorUid     // Catch:{ all -> 0x0061 }
            java.lang.String r0 = r0.creatorName     // Catch:{ all -> 0x0061 }
            android.content.Context r3 = r5.mContext     // Catch:{ all -> 0x0061 }
            boolean r0 = com.android.wifitrackerlib.Utils.isDeviceOrProfileOwner(r2, r0, r3)     // Catch:{ all -> 0x0061 }
            if (r0 == 0) goto L_0x0038
            monitor-exit(r5)
            return r1
        L_0x0038:
            java.util.List<java.lang.Integer> r0 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x0061 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x0061 }
        L_0x003e:
            boolean r2 = r0.hasNext()     // Catch:{ all -> 0x0061 }
            if (r2 == 0) goto L_0x005f
            java.lang.Object r2 = r0.next()     // Catch:{ all -> 0x0061 }
            java.lang.Integer r2 = (java.lang.Integer) r2     // Catch:{ all -> 0x0061 }
            int r2 = r2.intValue()     // Catch:{ all -> 0x0061 }
            r3 = 1
            if (r2 == 0) goto L_0x005d
            if (r2 == r3) goto L_0x005d
            r4 = 2
            if (r2 == r4) goto L_0x005d
            r4 = 4
            if (r2 == r4) goto L_0x005d
            r4 = 6
            if (r2 == r4) goto L_0x005d
            goto L_0x003e
        L_0x005d:
            monitor-exit(r5)
            return r3
        L_0x005f:
            monitor-exit(r5)
            return r1
        L_0x0061:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.canShare():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x005e, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean canEasyConnect() {
        /*
            r5 = this;
            monitor-enter(r5)
            com.android.wifitrackerlib.WifiTrackerInjector r0 = r5.mInjector     // Catch:{ all -> 0x005f }
            boolean r0 = r0.isDemoMode()     // Catch:{ all -> 0x005f }
            r1 = 0
            if (r0 == 0) goto L_0x000c
            monitor-exit(r5)
            return r1
        L_0x000c:
            android.net.wifi.WifiConfiguration r0 = r5.getWifiConfiguration()     // Catch:{ all -> 0x005f }
            if (r0 != 0) goto L_0x0014
            monitor-exit(r5)
            return r1
        L_0x0014:
            android.net.wifi.WifiManager r2 = r5.mWifiManager     // Catch:{ all -> 0x005f }
            boolean r2 = r2.isEasyConnectSupported()     // Catch:{ all -> 0x005f }
            if (r2 != 0) goto L_0x001e
            monitor-exit(r5)
            return r1
        L_0x001e:
            boolean r2 = androidx.core.p002os.BuildCompat.isAtLeastT()     // Catch:{ all -> 0x005f }
            if (r2 == 0) goto L_0x0042
            android.os.UserManager r2 = r5.mUserManager     // Catch:{ all -> 0x005f }
            java.lang.String r3 = "no_sharing_admin_configured_wifi"
            int r4 = r0.creatorUid     // Catch:{ all -> 0x005f }
            android.os.UserHandle r4 = android.os.UserHandle.getUserHandleForUid(r4)     // Catch:{ all -> 0x005f }
            boolean r2 = r2.hasUserRestrictionForUser(r3, r4)     // Catch:{ all -> 0x005f }
            if (r2 == 0) goto L_0x0042
            int r2 = r0.creatorUid     // Catch:{ all -> 0x005f }
            java.lang.String r0 = r0.creatorName     // Catch:{ all -> 0x005f }
            android.content.Context r3 = r5.mContext     // Catch:{ all -> 0x005f }
            boolean r0 = com.android.wifitrackerlib.Utils.isDeviceOrProfileOwner(r2, r0, r3)     // Catch:{ all -> 0x005f }
            if (r0 == 0) goto L_0x0042
            monitor-exit(r5)
            return r1
        L_0x0042:
            java.util.List<java.lang.Integer> r0 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x005f }
            r2 = 2
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x005f }
            boolean r0 = r0.contains(r2)     // Catch:{ all -> 0x005f }
            if (r0 != 0) goto L_0x005c
            java.util.List<java.lang.Integer> r0 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x005f }
            r2 = 4
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x005f }
            boolean r0 = r0.contains(r2)     // Catch:{ all -> 0x005f }
            if (r0 == 0) goto L_0x005d
        L_0x005c:
            r1 = 1
        L_0x005d:
            monitor-exit(r5)
            return r1
        L_0x005f:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.canEasyConnect():boolean");
    }

    public synchronized int getMeteredChoice() {
        WifiConfiguration wifiConfiguration;
        if (!isSuggestion() && (wifiConfiguration = this.mTargetWifiConfig) != null) {
            int i = wifiConfiguration.meteredOverride;
            if (i == 1) {
                return 1;
            }
            if (i == 2) {
                return 2;
            }
        }
        return 0;
    }

    public boolean canSetMeteredChoice() {
        return getWifiConfiguration() != null;
    }

    public synchronized void setMeteredChoice(int i) {
        if (canSetMeteredChoice()) {
            if (i == 0) {
                this.mTargetWifiConfig.meteredOverride = 0;
            } else if (i == 1) {
                this.mTargetWifiConfig.meteredOverride = 1;
            } else if (i == 2) {
                this.mTargetWifiConfig.meteredOverride = 2;
            }
            this.mWifiManager.save(this.mTargetWifiConfig, (WifiManager.ActionListener) null);
        }
    }

    public boolean canSetPrivacy() {
        return isSaved();
    }

    public synchronized int getPrivacy() {
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        if (wifiConfiguration == null || wifiConfiguration.macRandomizationSetting != 0) {
            return 1;
        }
        return 0;
    }

    public synchronized void setPrivacy(int i) {
        if (canSetPrivacy()) {
            WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
            wifiConfiguration.macRandomizationSetting = i == 1 ? 3 : 0;
            this.mWifiManager.save(wifiConfiguration, (WifiManager.ActionListener) null);
        }
    }

    public synchronized boolean isAutoJoinEnabled() {
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        if (wifiConfiguration == null) {
            return false;
        }
        return wifiConfiguration.allowAutojoin;
    }

    public boolean canSetAutoJoinEnabled() {
        return isSaved() || isSuggestion();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0018, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setAutoJoinEnabled(boolean r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            android.net.wifi.WifiConfiguration r0 = r2.mTargetWifiConfig     // Catch:{ all -> 0x0019 }
            if (r0 == 0) goto L_0x0017
            boolean r0 = r2.canSetAutoJoinEnabled()     // Catch:{ all -> 0x0019 }
            if (r0 != 0) goto L_0x000c
            goto L_0x0017
        L_0x000c:
            android.net.wifi.WifiManager r0 = r2.mWifiManager     // Catch:{ all -> 0x0019 }
            android.net.wifi.WifiConfiguration r1 = r2.mTargetWifiConfig     // Catch:{ all -> 0x0019 }
            int r1 = r1.networkId     // Catch:{ all -> 0x0019 }
            r0.allowAutojoin(r1, r3)     // Catch:{ all -> 0x0019 }
            monitor-exit(r2)
            return
        L_0x0017:
            monitor-exit(r2)
            return
        L_0x0019:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.setAutoJoinEnabled(boolean):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:102:0x01a0, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004b, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0060, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0075, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x008a, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x009f, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00b8, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00cd, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x014b, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0017, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0179, code lost:
        return r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getSecurityString(boolean r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x01a1 }
            int r0 = r0.size()     // Catch:{ all -> 0x01a1 }
            if (r0 != 0) goto L_0x0018
            if (r5 == 0) goto L_0x000e
            java.lang.String r5 = ""
            goto L_0x0016
        L_0x000e:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_none     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
        L_0x0016:
            monitor-exit(r4)
            return r5
        L_0x0018:
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x01a1 }
            int r0 = r0.size()     // Catch:{ all -> 0x01a1 }
            r1 = 1
            r2 = 9
            r3 = 0
            if (r0 != r1) goto L_0x00ce
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x01a1 }
            java.lang.Object r0 = r0.get(r3)     // Catch:{ all -> 0x01a1 }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ all -> 0x01a1 }
            int r0 = r0.intValue()     // Catch:{ all -> 0x01a1 }
            if (r0 == r2) goto L_0x00b9
            switch(r0) {
                case 0: goto L_0x00aa;
                case 1: goto L_0x00a0;
                case 2: goto L_0x008b;
                case 3: goto L_0x0076;
                case 4: goto L_0x0061;
                case 5: goto L_0x004c;
                case 6: goto L_0x0037;
                default: goto L_0x0035;
            }     // Catch:{ all -> 0x01a1 }
        L_0x0035:
            goto L_0x00ce
        L_0x0037:
            if (r5 == 0) goto L_0x0042
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_short_owe     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
            goto L_0x004a
        L_0x0042:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_owe     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
        L_0x004a:
            monitor-exit(r4)
            return r5
        L_0x004c:
            if (r5 == 0) goto L_0x0057
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_short_eap_suiteb     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
            goto L_0x005f
        L_0x0057:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_eap_suiteb     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
        L_0x005f:
            monitor-exit(r4)
            return r5
        L_0x0061:
            if (r5 == 0) goto L_0x006c
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_short_sae     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
            goto L_0x0074
        L_0x006c:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_sae     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
        L_0x0074:
            monitor-exit(r4)
            return r5
        L_0x0076:
            if (r5 == 0) goto L_0x0081
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_short_eap_wpa_wpa2     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
            goto L_0x0089
        L_0x0081:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_eap_wpa_wpa2     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
        L_0x0089:
            monitor-exit(r4)
            return r5
        L_0x008b:
            if (r5 == 0) goto L_0x0096
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_short_wpa_wpa2     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
            goto L_0x009e
        L_0x0096:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_wpa_wpa2     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
        L_0x009e:
            monitor-exit(r4)
            return r5
        L_0x00a0:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_wep     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
            monitor-exit(r4)
            return r5
        L_0x00aa:
            if (r5 == 0) goto L_0x00af
            java.lang.String r5 = ""
            goto L_0x00b7
        L_0x00af:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_none     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
        L_0x00b7:
            monitor-exit(r4)
            return r5
        L_0x00b9:
            if (r5 == 0) goto L_0x00c4
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_short_eap_wpa3     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
            goto L_0x00cc
        L_0x00c4:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_eap_wpa3     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
        L_0x00cc:
            monitor-exit(r4)
            return r5
        L_0x00ce:
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x01a1 }
            int r0 = r0.size()     // Catch:{ all -> 0x01a1 }
            r1 = 2
            if (r0 != r1) goto L_0x017a
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x01a1 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x01a1 }
            boolean r0 = r0.contains(r3)     // Catch:{ all -> 0x01a1 }
            if (r0 == 0) goto L_0x011e
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x01a1 }
            r3 = 6
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x01a1 }
            boolean r0 = r0.contains(r3)     // Catch:{ all -> 0x01a1 }
            if (r0 == 0) goto L_0x011e
            java.util.StringJoiner r0 = new java.util.StringJoiner     // Catch:{ all -> 0x01a1 }
            java.lang.String r1 = "/"
            r0.<init>(r1)     // Catch:{ all -> 0x01a1 }
            android.content.Context r1 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r2 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_none     // Catch:{ all -> 0x01a1 }
            java.lang.String r1 = r1.getString(r2)     // Catch:{ all -> 0x01a1 }
            r0.add(r1)     // Catch:{ all -> 0x01a1 }
            if (r5 == 0) goto L_0x010d
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r1 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_short_owe     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r1)     // Catch:{ all -> 0x01a1 }
            goto L_0x0115
        L_0x010d:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r1 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_owe     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r1)     // Catch:{ all -> 0x01a1 }
        L_0x0115:
            r0.add(r5)     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r0.toString()     // Catch:{ all -> 0x01a1 }
            monitor-exit(r4)
            return r5
        L_0x011e:
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x01a1 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x01a1 }
            boolean r0 = r0.contains(r1)     // Catch:{ all -> 0x01a1 }
            if (r0 == 0) goto L_0x014c
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x01a1 }
            r1 = 4
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x01a1 }
            boolean r0 = r0.contains(r1)     // Catch:{ all -> 0x01a1 }
            if (r0 == 0) goto L_0x014c
            if (r5 == 0) goto L_0x0142
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_short_wpa_wpa2_wpa3     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
            goto L_0x014a
        L_0x0142:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_wpa_wpa2_wpa3     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
        L_0x014a:
            monitor-exit(r4)
            return r5
        L_0x014c:
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x01a1 }
            r1 = 3
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x01a1 }
            boolean r0 = r0.contains(r1)     // Catch:{ all -> 0x01a1 }
            if (r0 == 0) goto L_0x017a
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x01a1 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x01a1 }
            boolean r0 = r0.contains(r1)     // Catch:{ all -> 0x01a1 }
            if (r0 == 0) goto L_0x017a
            if (r5 == 0) goto L_0x0170
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_short_eap_wpa_wpa2_wpa3     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
            goto L_0x0178
        L_0x0170:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_eap_wpa_wpa2_wpa3     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
        L_0x0178:
            monitor-exit(r4)
            return r5
        L_0x017a:
            java.lang.String r0 = "StandardWifiEntry"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x01a1 }
            r1.<init>()     // Catch:{ all -> 0x01a1 }
            java.lang.String r2 = "Couldn't get string for security types: "
            r1.append(r2)     // Catch:{ all -> 0x01a1 }
            java.util.List<java.lang.Integer> r2 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x01a1 }
            r1.append(r2)     // Catch:{ all -> 0x01a1 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x01a1 }
            android.util.Log.e(r0, r1)     // Catch:{ all -> 0x01a1 }
            if (r5 == 0) goto L_0x0197
            java.lang.String r5 = ""
            goto L_0x019f
        L_0x0197:
            android.content.Context r5 = r4.mContext     // Catch:{ all -> 0x01a1 }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_security_none     // Catch:{ all -> 0x01a1 }
            java.lang.String r5 = r5.getString(r0)     // Catch:{ all -> 0x01a1 }
        L_0x019f:
            monitor-exit(r4)
            return r5
        L_0x01a1:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.getSecurityString(boolean):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002e, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean shouldEditBeforeConnect() {
        /*
            r3 = this;
            monitor-enter(r3)
            android.net.wifi.WifiConfiguration r0 = r3.getWifiConfiguration()     // Catch:{ all -> 0x002f }
            r1 = 0
            if (r0 != 0) goto L_0x000a
            monitor-exit(r3)
            return r1
        L_0x000a:
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r0 = r0.getNetworkSelectionStatus()     // Catch:{ all -> 0x002f }
            int r2 = r0.getNetworkSelectionStatus()     // Catch:{ all -> 0x002f }
            if (r2 == 0) goto L_0x002d
            r2 = 2
            int r2 = r0.getDisableReasonCounter(r2)     // Catch:{ all -> 0x002f }
            if (r2 > 0) goto L_0x002a
            r2 = 8
            int r2 = r0.getDisableReasonCounter(r2)     // Catch:{ all -> 0x002f }
            if (r2 > 0) goto L_0x002a
            r2 = 5
            int r0 = r0.getDisableReasonCounter(r2)     // Catch:{ all -> 0x002f }
            if (r0 <= 0) goto L_0x002d
        L_0x002a:
            r0 = 1
            monitor-exit(r3)
            return r0
        L_0x002d:
            monitor-exit(r3)
            return r1
        L_0x002f:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.shouldEditBeforeConnect():boolean");
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateScanResultInfo(List<ScanResult> list) throws IllegalArgumentException {
        if (list == null) {
            list = new ArrayList<>();
        }
        String ssid = this.mKey.getScanResultKey().getSsid();
        for (ScanResult next : list) {
            if (!TextUtils.equals(next.SSID, ssid)) {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + ssid + ", Actual: " + next.SSID + ", ScanResult: " + next);
            }
        }
        this.mMatchingScanResults.clear();
        Set<Integer> securityTypes = this.mKey.getScanResultKey().getSecurityTypes();
        for (ScanResult next2 : list) {
            for (Integer intValue : Utils.getSecurityTypesFromScanResult(next2)) {
                int intValue2 = intValue.intValue();
                if (securityTypes.contains(Integer.valueOf(intValue2))) {
                    if (isSecurityTypeSupported(intValue2)) {
                        if (!this.mMatchingScanResults.containsKey(Integer.valueOf(intValue2))) {
                            this.mMatchingScanResults.put(Integer.valueOf(intValue2), new ArrayList());
                        }
                        this.mMatchingScanResults.get(Integer.valueOf(intValue2)).add(next2);
                    }
                }
            }
        }
        updateSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
    }

    private synchronized void updateTargetScanResultInfo() {
        ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(this.mTargetScanResults);
        if (getConnectedState() == 0) {
            this.mLevel = bestScanResultByLevel != null ? this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level) : -1;
        }
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
    public synchronized void updateConfig(List<WifiConfiguration> list) throws IllegalArgumentException {
        if (list == null) {
            list = Collections.emptyList();
        }
        ScanResultKey scanResultKey = this.mKey.getScanResultKey();
        String ssid = scanResultKey.getSsid();
        Set<Integer> securityTypes = scanResultKey.getSecurityTypes();
        this.mMatchingWifiConfigs.clear();
        for (WifiConfiguration next : list) {
            if (TextUtils.equals(ssid, WifiInfo.sanitizeSsid(next.SSID))) {
                Iterator<Integer> it = Utils.getSecurityTypesFromWifiConfiguration(next).iterator();
                while (true) {
                    if (it.hasNext()) {
                        int intValue = it.next().intValue();
                        if (!securityTypes.contains(Integer.valueOf(intValue))) {
                            throw new IllegalArgumentException("Attempted to update with wrong security! Expected one of: " + securityTypes + ", Actual: " + intValue + ", Config: " + next);
                        } else if (isSecurityTypeSupported(intValue)) {
                            this.mMatchingWifiConfigs.put(Integer.valueOf(intValue), next);
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + ssid + ", Actual: " + WifiInfo.sanitizeSsid(next.SSID) + ", Config: " + next);
            }
        }
        updateSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
    }

    private boolean isSecurityTypeSupported(int i) {
        if (i == 4) {
            return this.mIsWpa3SaeSupported;
        }
        if (i == 5) {
            return this.mIsWpa3SuiteBSupported;
        }
        if (i != 6) {
            return true;
        }
        return this.mIsEnhancedOpenSupported;
    }

    /* access modifiers changed from: protected */
    public synchronized void updateSecurityTypes() {
        this.mTargetSecurityTypes.clear();
        WifiInfo wifiInfo = this.mWifiInfo;
        if (!(wifiInfo == null || wifiInfo.getCurrentSecurityType() == -1)) {
            this.mTargetSecurityTypes.add(Integer.valueOf(this.mWifiInfo.getCurrentSecurityType()));
        }
        Set<Integer> keySet = this.mMatchingWifiConfigs.keySet();
        if (this.mTargetSecurityTypes.isEmpty() && this.mKey.isTargetingNewNetworks()) {
            boolean z = false;
            Set<Integer> keySet2 = this.mMatchingScanResults.keySet();
            Iterator<Integer> it = keySet.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (keySet2.contains(Integer.valueOf(it.next().intValue()))) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!z) {
                this.mTargetSecurityTypes.addAll(keySet2);
            }
        }
        if (this.mTargetSecurityTypes.isEmpty()) {
            this.mTargetSecurityTypes.addAll(keySet);
        }
        if (this.mTargetSecurityTypes.isEmpty()) {
            this.mTargetSecurityTypes.addAll(this.mKey.getScanResultKey().getSecurityTypes());
        }
        this.mTargetWifiConfig = this.mMatchingWifiConfigs.get(Integer.valueOf(Utils.getSingleSecurityTypeFromMultipleSecurityTypes(this.mTargetSecurityTypes)));
        ArraySet arraySet = new ArraySet();
        for (Integer intValue : this.mTargetSecurityTypes) {
            int intValue2 = intValue.intValue();
            if (this.mMatchingScanResults.containsKey(Integer.valueOf(intValue2))) {
                arraySet.addAll(this.mMatchingScanResults.get(Integer.valueOf(intValue2)));
            }
        }
        this.mTargetScanResults.clear();
        this.mTargetScanResults.addAll(arraySet);
    }

    /* access modifiers changed from: package-private */
    public synchronized void setUserShareable(boolean z) {
        this.mIsUserShareable = z;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean isUserShareable() {
        return this.mIsUserShareable;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0033, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean connectionInfoMatches(android.net.wifi.WifiInfo r4, android.net.NetworkInfo r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r5 = r4.isPasspointAp()     // Catch:{ all -> 0x0034 }
            r0 = 0
            if (r5 != 0) goto L_0x0032
            boolean r5 = r4.isOsuAp()     // Catch:{ all -> 0x0034 }
            if (r5 == 0) goto L_0x000f
            goto L_0x0032
        L_0x000f:
            java.util.Map<java.lang.Integer, android.net.wifi.WifiConfiguration> r5 = r3.mMatchingWifiConfigs     // Catch:{ all -> 0x0034 }
            java.util.Collection r5 = r5.values()     // Catch:{ all -> 0x0034 }
            java.util.Iterator r5 = r5.iterator()     // Catch:{ all -> 0x0034 }
        L_0x0019:
            boolean r1 = r5.hasNext()     // Catch:{ all -> 0x0034 }
            if (r1 == 0) goto L_0x0030
            java.lang.Object r1 = r5.next()     // Catch:{ all -> 0x0034 }
            android.net.wifi.WifiConfiguration r1 = (android.net.wifi.WifiConfiguration) r1     // Catch:{ all -> 0x0034 }
            int r1 = r1.networkId     // Catch:{ all -> 0x0034 }
            int r2 = r4.getNetworkId()     // Catch:{ all -> 0x0034 }
            if (r1 != r2) goto L_0x0019
            r4 = 1
            monitor-exit(r3)
            return r4
        L_0x0030:
            monitor-exit(r3)
            return r0
        L_0x0032:
            monitor-exit(r3)
            return r0
        L_0x0034:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.connectionInfoMatches(android.net.wifi.WifiInfo, android.net.NetworkInfo):boolean");
    }

    /* access modifiers changed from: protected */
    public synchronized String getScanResultDescription() {
        if (this.mTargetScanResults.size() == 0) {
            return "";
        }
        return "[" + getScanResultDescription(2400, 2500) + ";" + getScanResultDescription(4900, 5900) + ";" + getScanResultDescription(5925, 7125) + ";" + getScanResultDescription(58320, 70200) + "]";
    }

    private synchronized String getScanResultDescription(int i, int i2) {
        List list = (List) this.mTargetScanResults.stream().filter(new StandardWifiEntry$$ExternalSyntheticLambda4(i, i2)).sorted(Comparator.comparingInt(StandardWifiEntry$$ExternalSyntheticLambda6.INSTANCE)).collect(Collectors.toList());
        int size = list.size();
        if (size == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(size);
        sb.append(")");
        if (size > 4) {
            int asInt = list.stream().mapToInt(StandardWifiEntry$$ExternalSyntheticLambda5.INSTANCE).max().getAsInt();
            sb.append("max=");
            sb.append(asInt);
            sb.append(",");
        }
        list.forEach(new StandardWifiEntry$$ExternalSyntheticLambda3(this, sb, SystemClock.elapsedRealtime()));
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getScanResultDescription$3(int i, int i2, ScanResult scanResult) {
        int i3 = scanResult.frequency;
        return i3 >= i && i3 <= i2;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ int lambda$getScanResultDescription$4(ScanResult scanResult) {
        return scanResult.level * -1;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getScanResultDescription$6(StringBuilder sb, long j, ScanResult scanResult) {
        sb.append(getScanResultDescription(scanResult, j));
    }

    private synchronized String getScanResultDescription(ScanResult scanResult, long j) {
        StringBuilder sb;
        sb = new StringBuilder();
        sb.append(" \n{");
        sb.append(scanResult.BSSID);
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null && scanResult.BSSID.equals(wifiInfo.getBSSID())) {
            sb.append("*");
        }
        sb.append("=");
        sb.append(scanResult.frequency);
        sb.append(",");
        sb.append(scanResult.level);
        int i = ((int) (j - (scanResult.timestamp / 1000))) / SecurityContentManager.DEFAULT_ORDER;
        sb.append(",");
        sb.append(i);
        sb.append("s");
        sb.append("}");
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public String getNetworkSelectionDescription() {
        return Utils.getNetworkSelectionDescription(getWifiConfiguration());
    }

    @SuppressLint({"NewApi"})
    private void updateAdminRestrictions() {
        UserManager userManager = this.mUserManager;
        if (userManager != null) {
            this.mHasAddConfigUserRestriction = userManager.hasUserRestriction("no_add_wifi_config");
        }
        DevicePolicyManager devicePolicyManager = this.mDevicePolicyManager;
        if (devicePolicyManager != null) {
            int minimumRequiredWifiSecurityLevel = devicePolicyManager.getMinimumRequiredWifiSecurityLevel();
            if (minimumRequiredWifiSecurityLevel != 0) {
                boolean z = false;
                Iterator<Integer> it = getSecurityTypes().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    int convertSecurityTypeToDpmWifiSecurity = Utils.convertSecurityTypeToDpmWifiSecurity(it.next().intValue());
                    if (convertSecurityTypeToDpmWifiSecurity != -1 && minimumRequiredWifiSecurityLevel <= convertSecurityTypeToDpmWifiSecurity) {
                        z = true;
                        break;
                    }
                }
                if (!z) {
                    this.mIsAdminRestricted = true;
                    return;
                }
            }
            WifiSsidPolicy wifiSsidPolicy = this.mDevicePolicyManager.getWifiSsidPolicy();
            if (wifiSsidPolicy != null) {
                int policyType = wifiSsidPolicy.getPolicyType();
                Set ssids = wifiSsidPolicy.getSsids();
                if (policyType == 0 && !ssids.contains(WifiSsid.fromBytes(getSsid().getBytes(StandardCharsets.UTF_8)))) {
                    this.mIsAdminRestricted = true;
                } else if (policyType == 1 && ssids.contains(WifiSsid.fromBytes(getSsid().getBytes(StandardCharsets.UTF_8)))) {
                    this.mIsAdminRestricted = true;
                }
            }
        }
    }

    private boolean hasAdminRestrictions() {
        return (this.mHasAddConfigUserRestriction && !isSaved() && !isSuggestion()) || this.mIsAdminRestricted;
    }

    static class StandardWifiEntryKey {
        private boolean mIsNetworkRequest;
        private boolean mIsTargetingNewNetworks;
        private ScanResultKey mScanResultKey;
        private String mSuggestionProfileKey;

        StandardWifiEntryKey(ScanResultKey scanResultKey, boolean z) {
            this.mScanResultKey = scanResultKey;
            this.mIsTargetingNewNetworks = z;
        }

        StandardWifiEntryKey(WifiConfiguration wifiConfiguration) {
            this(wifiConfiguration, false);
        }

        StandardWifiEntryKey(WifiConfiguration wifiConfiguration, boolean z) {
            this.mIsTargetingNewNetworks = false;
            this.mScanResultKey = new ScanResultKey(wifiConfiguration);
            if (wifiConfiguration.fromWifiNetworkSuggestion) {
                this.mSuggestionProfileKey = new StringJoiner(",").add(wifiConfiguration.creatorName).add(String.valueOf(wifiConfiguration.carrierId)).add(String.valueOf(wifiConfiguration.subscriptionId)).toString();
            } else if (wifiConfiguration.fromWifiNetworkSpecifier) {
                this.mIsNetworkRequest = true;
            }
            this.mIsTargetingNewNetworks = z;
        }

        StandardWifiEntryKey(String str) {
            this.mIsTargetingNewNetworks = false;
            this.mScanResultKey = new ScanResultKey();
            if (!str.startsWith("StandardWifiEntry:")) {
                Log.e("StandardWifiEntry", "String key does not start with key prefix!");
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject(str.substring(18));
                if (jSONObject.has("SCAN_RESULT_KEY")) {
                    this.mScanResultKey = new ScanResultKey(jSONObject.getString("SCAN_RESULT_KEY"));
                }
                if (jSONObject.has("SUGGESTION_PROFILE_KEY")) {
                    this.mSuggestionProfileKey = jSONObject.getString("SUGGESTION_PROFILE_KEY");
                }
                if (jSONObject.has("IS_NETWORK_REQUEST")) {
                    this.mIsNetworkRequest = jSONObject.getBoolean("IS_NETWORK_REQUEST");
                }
                if (jSONObject.has("IS_TARGETING_NEW_NETWORKS")) {
                    this.mIsTargetingNewNetworks = jSONObject.getBoolean("IS_TARGETING_NEW_NETWORKS");
                }
            } catch (JSONException e) {
                Log.e("StandardWifiEntry", "JSONException while converting StandardWifiEntryKey to string: " + e);
            }
        }

        public String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                ScanResultKey scanResultKey = this.mScanResultKey;
                if (scanResultKey != null) {
                    jSONObject.put("SCAN_RESULT_KEY", scanResultKey.toString());
                }
                String str = this.mSuggestionProfileKey;
                if (str != null) {
                    jSONObject.put("SUGGESTION_PROFILE_KEY", str);
                }
                boolean z = this.mIsNetworkRequest;
                if (z) {
                    jSONObject.put("IS_NETWORK_REQUEST", z);
                }
                boolean z2 = this.mIsTargetingNewNetworks;
                if (z2) {
                    jSONObject.put("IS_TARGETING_NEW_NETWORKS", z2);
                }
            } catch (JSONException e) {
                Log.wtf("StandardWifiEntry", "JSONException while converting StandardWifiEntryKey to string: " + e);
            }
            return "StandardWifiEntry:" + jSONObject.toString();
        }

        /* access modifiers changed from: package-private */
        public ScanResultKey getScanResultKey() {
            return this.mScanResultKey;
        }

        /* access modifiers changed from: package-private */
        public boolean isNetworkRequest() {
            return this.mIsNetworkRequest;
        }

        /* access modifiers changed from: package-private */
        public boolean isTargetingNewNetworks() {
            return this.mIsTargetingNewNetworks;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            StandardWifiEntryKey standardWifiEntryKey = (StandardWifiEntryKey) obj;
            if (!Objects.equals(this.mScanResultKey, standardWifiEntryKey.mScanResultKey) || !TextUtils.equals(this.mSuggestionProfileKey, standardWifiEntryKey.mSuggestionProfileKey) || this.mIsNetworkRequest != standardWifiEntryKey.mIsNetworkRequest) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mScanResultKey, this.mSuggestionProfileKey, Boolean.valueOf(this.mIsNetworkRequest)});
        }
    }

    static class ScanResultKey {
        private Set<Integer> mSecurityTypes;
        private String mSsid;

        ScanResultKey() {
            this.mSecurityTypes = new ArraySet();
        }

        ScanResultKey(String str, List<Integer> list) {
            this.mSecurityTypes = new ArraySet();
            this.mSsid = str;
            for (Integer intValue : list) {
                int intValue2 = intValue.intValue();
                this.mSecurityTypes.add(Integer.valueOf(intValue2));
                if (intValue2 == 0) {
                    this.mSecurityTypes.add(6);
                } else if (intValue2 == 6) {
                    this.mSecurityTypes.add(0);
                } else if (intValue2 == 9) {
                    this.mSecurityTypes.add(3);
                } else if (intValue2 == 2) {
                    this.mSecurityTypes.add(4);
                } else if (intValue2 == 3) {
                    this.mSecurityTypes.add(9);
                } else if (intValue2 == 4) {
                    this.mSecurityTypes.add(2);
                }
            }
        }

        ScanResultKey(ScanResult scanResult) {
            this(scanResult.SSID, Utils.getSecurityTypesFromScanResult(scanResult));
        }

        ScanResultKey(WifiConfiguration wifiConfiguration) {
            this(WifiInfo.sanitizeSsid(wifiConfiguration.SSID), Utils.getSecurityTypesFromWifiConfiguration(wifiConfiguration));
        }

        ScanResultKey(String str) {
            this.mSecurityTypes = new ArraySet();
            try {
                JSONObject jSONObject = new JSONObject(str);
                this.mSsid = jSONObject.getString("SSID");
                JSONArray jSONArray = jSONObject.getJSONArray("SECURITY_TYPES");
                for (int i = 0; i < jSONArray.length(); i++) {
                    this.mSecurityTypes.add(Integer.valueOf(jSONArray.getInt(i)));
                }
            } catch (JSONException e) {
                Log.wtf("StandardWifiEntry", "JSONException while constructing ScanResultKey from string: " + e);
            }
        }

        public String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                String str = this.mSsid;
                if (str != null) {
                    jSONObject.put("SSID", str);
                }
                if (!this.mSecurityTypes.isEmpty()) {
                    JSONArray jSONArray = new JSONArray();
                    for (Integer intValue : this.mSecurityTypes) {
                        jSONArray.put(intValue.intValue());
                    }
                    jSONObject.put("SECURITY_TYPES", jSONArray);
                }
            } catch (JSONException e) {
                Log.e("StandardWifiEntry", "JSONException while converting ScanResultKey to string: " + e);
            }
            return jSONObject.toString();
        }

        /* access modifiers changed from: package-private */
        public String getSsid() {
            return this.mSsid;
        }

        /* access modifiers changed from: package-private */
        public Set<Integer> getSecurityTypes() {
            return this.mSecurityTypes;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ScanResultKey scanResultKey = (ScanResultKey) obj;
            if (!TextUtils.equals(this.mSsid, scanResultKey.mSsid) || !this.mSecurityTypes.equals(scanResultKey.mSecurityTypes)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mSsid, this.mSecurityTypes});
        }
    }
}
