package com.android.settings.network;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.VpnManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.security.LegacyVpnProfileStore;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.vpn2.VpnInfoPreference;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.utils.ThreadUtils;
import java.util.Arrays;

public class VpnPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnResume, OnPause {
    private static final NetworkRequest REQUEST = new NetworkRequest.Builder().removeCapability(15).removeCapability(13).removeCapability(14).build();
    private ConnectivityManager mConnectivityManager;
    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        public void onAvailable(Network network) {
            VpnPreferenceController.this.updateSummary();
        }

        public void onLost(Network network) {
            VpnPreferenceController.this.updateSummary();
        }
    };
    private Preference mPreference;

    public String getPreferenceKey() {
        return "vpn_settings";
    }

    public VpnPreferenceController(Context context) {
        super(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = getEffectivePreference(preferenceScreen);
    }

    /* access modifiers changed from: protected */
    public Preference getEffectivePreference(PreferenceScreen preferenceScreen) {
        Preference findPreference = preferenceScreen.findPreference("vpn_settings");
        if (findPreference == null) {
            return null;
        }
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "airplane_mode_toggleable_radios");
        if (string == null || !string.contains("wifi")) {
            findPreference.setDependency("airplane_mode");
        }
        return findPreference;
    }

    public boolean isAvailable() {
        return !RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, "no_config_vpn", UserHandle.myUserId());
    }

    public void onPause() {
        ConnectivityManager connectivityManager = this.mConnectivityManager;
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
            this.mConnectivityManager = null;
        }
    }

    public void onResume() {
        if (isAvailable()) {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class);
            this.mConnectivityManager = connectivityManager;
            connectivityManager.registerNetworkCallback(REQUEST, this.mNetworkCallback);
            return;
        }
        this.mConnectivityManager = null;
    }

    /* access modifiers changed from: package-private */
    public void updateSummary() {
        int i;
        LegacyVpnInfo legacyVpnInfo;
        if (this.mPreference != null) {
            UserManager userManager = (UserManager) this.mContext.getSystemService(UserManager.class);
            VpnManager vpnManager = (VpnManager) this.mContext.getSystemService(VpnManager.class);
            String insecureVpnSummaryOverride = getInsecureVpnSummaryOverride(userManager, vpnManager);
            if (insecureVpnSummaryOverride == null) {
                UserInfo userInfo = userManager.getUserInfo(UserHandle.myUserId());
                if (userInfo.isRestricted()) {
                    i = userInfo.restrictedProfileParentId;
                } else {
                    i = userInfo.id;
                }
                VpnConfig vpnConfig = vpnManager.getVpnConfig(i);
                if (vpnConfig != null && vpnConfig.legacy && ((legacyVpnInfo = vpnManager.getLegacyVpnInfo(i)) == null || legacyVpnInfo.state != 3)) {
                    vpnConfig = null;
                }
                if (vpnConfig == null) {
                    insecureVpnSummaryOverride = this.mContext.getString(C0444R.string.vpn_disconnected_summary);
                } else {
                    insecureVpnSummaryOverride = getNameForVpnConfig(vpnConfig, UserHandle.of(i));
                }
            }
            ThreadUtils.postOnMainThread(new VpnPreferenceController$$ExternalSyntheticLambda0(this, insecureVpnSummaryOverride));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSummary$0(String str) {
        this.mPreference.setSummary((CharSequence) str);
    }

    /* access modifiers changed from: protected */
    public int getNumberOfNonLegacyVpn(UserManager userManager, VpnManager vpnManager) {
        return (int) userManager.getUsers().stream().map(new VpnPreferenceController$$ExternalSyntheticLambda1(vpnManager)).filter(VpnPreferenceController$$ExternalSyntheticLambda3.INSTANCE).count();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getNumberOfNonLegacyVpn$2(VpnConfig vpnConfig) {
        return vpnConfig != null && !vpnConfig.legacy;
    }

    /* access modifiers changed from: protected */
    public String getInsecureVpnSummaryOverride(UserManager userManager, VpnManager vpnManager) {
        if (!(this.mPreference instanceof VpnInfoPreference)) {
            return null;
        }
        String[] list = LegacyVpnProfileStore.list("VPN_");
        int insecureVpnCount = getInsecureVpnCount(list);
        boolean z = insecureVpnCount > 0;
        ((VpnInfoPreference) this.mPreference).setInsecureVpn(z);
        if (!z) {
            return null;
        }
        int length = list.length;
        if (length <= 1 && length + getNumberOfNonLegacyVpn(userManager, vpnManager) == 1) {
            return this.mContext.getString(C0444R.string.vpn_settings_insecure_single);
        }
        if (insecureVpnCount == 1) {
            return this.mContext.getString(C0444R.string.vpn_settings_single_insecure_multiple_total, new Object[]{Integer.valueOf(insecureVpnCount)});
        }
        return this.mContext.getString(C0444R.string.vpn_settings_multiple_insecure_multiple_total, new Object[]{Integer.valueOf(insecureVpnCount)});
    }

    /* access modifiers changed from: package-private */
    public String getNameForVpnConfig(VpnConfig vpnConfig, UserHandle userHandle) {
        if (vpnConfig.legacy) {
            return this.mContext.getString(C0444R.string.wifi_display_status_connected);
        }
        String str = vpnConfig.user;
        try {
            Context context = this.mContext;
            return VpnConfig.getVpnLabel(context.createPackageContextAsUser(context.getPackageName(), 0, userHandle), str).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("VpnPreferenceController", "Package " + str + " is not present", e);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public int getInsecureVpnCount(String[] strArr) {
        return (int) Arrays.stream(strArr).map(VpnPreferenceController$$ExternalSyntheticLambda2.INSTANCE).filter(VpnPreferenceController$$ExternalSyntheticLambda4.INSTANCE).count();
    }
}
