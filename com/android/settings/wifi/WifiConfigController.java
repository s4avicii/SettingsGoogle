package com.android.settings.wifi;

import android.content.Context;
import android.content.res.Resources;
import android.net.InetAddresses;
import android.net.IpConfiguration;
import android.net.ProxyInfo;
import android.net.StaticIpConfiguration;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.window.C0444R;
import com.android.net.module.util.ProxyUtils;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.utils.AndroidKeystoreAliasLoader;
import com.android.settings.wifi.dpp.WifiDppUtils;
import com.android.settingslib.Utils;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.wifi.AccessPoint;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class WifiConfigController implements TextWatcher, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener, View.OnKeyListener {
    static final int PRIVACY_SPINNER_INDEX_DEVICE_MAC = 1;
    static final int PRIVACY_SPINNER_INDEX_RANDOMIZED_MAC = 0;
    static final String[] UNDESIRED_CERTIFICATES = {"MacRandSecret", "MacRandSapSecret"};
    private final AccessPoint mAccessPoint;
    int mAccessPointSecurity;
    private final List<SubscriptionInfo> mActiveSubscriptionInfos;
    private final WifiConfigUiBase mConfigUi;
    /* access modifiers changed from: private */
    public Context mContext;
    private TextView mDns1View;
    private TextView mDns2View;
    private String mDoNotProvideEapUserCertString;
    private TextView mEapAnonymousView;
    private Spinner mEapCaCertSpinner;
    private TextView mEapDomainView;
    private TextView mEapIdentityView;
    Spinner mEapMethodSpinner;
    private Spinner mEapOcspSpinner;
    Spinner mEapSimSpinner;
    private Spinner mEapUserCertSpinner;
    private TextView mGatewayView;
    private Spinner mHiddenSettingsSpinner;
    private TextView mHiddenWarningView;
    private ProxyInfo mHttpProxy;
    private TextView mIpAddressView;
    private IpConfiguration.IpAssignment mIpAssignment;
    private Spinner mIpSettingsSpinner;
    private String[] mLevels;
    private Spinner mMeteredSettingsSpinner;
    private int mMode;
    private String mMultipleCertSetString;
    private TextView mNetworkPrefixLengthView;
    private TextView mPasswordView;
    private ArrayAdapter<CharSequence> mPhase2Adapter;
    private ArrayAdapter<CharSequence> mPhase2PeapAdapter;
    private Spinner mPhase2Spinner;
    private ArrayAdapter<CharSequence> mPhase2TtlsAdapter;
    private Spinner mPrivacySettingsSpinner;
    private TextView mProxyExclusionListView;
    private TextView mProxyHostView;
    private TextView mProxyPacView;
    private TextView mProxyPortView;
    private IpConfiguration.ProxySettings mProxySettings;
    private Spinner mProxySettingsSpinner;
    private boolean mRequestFocus;
    Integer[] mSecurityInPosition;
    private Spinner mSecuritySpinner;
    private CheckBox mSharedCheckBox;
    private ImageButton mSsidScanButton;
    private TextView mSsidView;
    private StaticIpConfiguration mStaticIpConfiguration;
    private String mUnspecifiedCertString;
    private String mUseSystemCertsString;
    private final View mView;
    private final WifiManager mWifiManager;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public WifiConfigController(WifiConfigUiBase wifiConfigUiBase, View view, AccessPoint accessPoint, int i) {
        this(wifiConfigUiBase, view, accessPoint, i, true);
    }

    public WifiConfigController(WifiConfigUiBase wifiConfigUiBase, View view, AccessPoint accessPoint, int i, boolean z) {
        this.mIpAssignment = IpConfiguration.IpAssignment.UNASSIGNED;
        this.mProxySettings = IpConfiguration.ProxySettings.UNASSIGNED;
        this.mHttpProxy = null;
        this.mStaticIpConfiguration = null;
        this.mRequestFocus = true;
        this.mActiveSubscriptionInfos = new ArrayList();
        this.mConfigUi = wifiConfigUiBase;
        this.mView = view;
        this.mAccessPoint = accessPoint;
        Context context = wifiConfigUiBase.getContext();
        this.mContext = context;
        this.mRequestFocus = z;
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        initWifiConfigController(accessPoint, i);
    }

    public WifiConfigController(WifiConfigUiBase wifiConfigUiBase, View view, AccessPoint accessPoint, int i, WifiManager wifiManager) {
        this.mIpAssignment = IpConfiguration.IpAssignment.UNASSIGNED;
        this.mProxySettings = IpConfiguration.ProxySettings.UNASSIGNED;
        this.mHttpProxy = null;
        this.mStaticIpConfiguration = null;
        this.mRequestFocus = true;
        this.mActiveSubscriptionInfos = new ArrayList();
        this.mConfigUi = wifiConfigUiBase;
        this.mView = view;
        this.mAccessPoint = accessPoint;
        this.mContext = wifiConfigUiBase.getContext();
        this.mWifiManager = wifiManager;
        initWifiConfigController(accessPoint, i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:48:0x01d9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initWifiConfigController(com.android.settingslib.wifi.AccessPoint r11, int r12) {
        /*
            r10 = this;
            r0 = 0
            if (r11 != 0) goto L_0x0005
            r11 = r0
            goto L_0x0009
        L_0x0005:
            int r11 = r11.getSecurity()
        L_0x0009:
            r10.mAccessPointSecurity = r11
            r10.mMode = r12
            android.content.Context r11 = r10.mContext
            android.content.res.Resources r11 = r11.getResources()
            r12 = 2130772219(0x7f0100fb, float:1.714755E38)
            java.lang.String[] r12 = r11.getStringArray(r12)
            r10.mLevels = r12
            android.content.Context r12 = r10.mContext
            boolean r12 = com.android.settingslib.Utils.isWifiOnly(r12)
            if (r12 != 0) goto L_0x003e
            android.content.Context r12 = r10.mContext
            android.content.res.Resources r12 = r12.getResources()
            r1 = 17891617(0x1110121, float:2.6633104E-38)
            boolean r12 = r12.getBoolean(r1)
            if (r12 != 0) goto L_0x0034
            goto L_0x003e
        L_0x0034:
            r12 = 2130772215(0x7f0100f7, float:1.7147542E38)
            android.widget.ArrayAdapter r12 = r10.getSpinnerAdapterWithEapMethodsTts(r12)
            r10.mPhase2PeapAdapter = r12
            goto L_0x0047
        L_0x003e:
            r12 = 2130772214(0x7f0100f6, float:1.714754E38)
            android.widget.ArrayAdapter r12 = r10.getSpinnerAdapter((int) r12)
            r10.mPhase2PeapAdapter = r12
        L_0x0047:
            r12 = 2130772224(0x7f010100, float:1.714756E38)
            android.widget.ArrayAdapter r12 = r10.getSpinnerAdapter((int) r12)
            r10.mPhase2TtlsAdapter = r12
            android.content.Context r12 = r10.mContext
            r1 = 2130974691(0x7f0417e3, float:1.7558212E38)
            java.lang.String r12 = r12.getString(r1)
            r10.mUnspecifiedCertString = r12
            android.content.Context r12 = r10.mContext
            r1 = 2130974540(0x7f04174c, float:1.7557906E38)
            java.lang.String r12 = r12.getString(r1)
            r10.mMultipleCertSetString = r12
            android.content.Context r12 = r10.mContext
            r1 = 2130974693(0x7f0417e5, float:1.7558216E38)
            java.lang.String r12 = r12.getString(r1)
            r10.mUseSystemCertsString = r12
            android.content.Context r12 = r10.mContext
            r1 = 2130974429(0x7f0416dd, float:1.755768E38)
            java.lang.String r12 = r12.getString(r1)
            r10.mDoNotProvideEapUserCertString = r12
            android.view.View r12 = r10.mView
            r1 = 2131559772(0x7f0d055c, float:1.8744897E38)
            android.view.View r12 = r12.findViewById(r1)
            android.widget.ImageButton r12 = (android.widget.ImageButton) r12
            r10.mSsidScanButton = r12
            android.view.View r12 = r10.mView
            r1 = 2131559172(0x7f0d0304, float:1.874368E38)
            android.view.View r12 = r12.findViewById(r1)
            android.widget.Spinner r12 = (android.widget.Spinner) r12
            r10.mIpSettingsSpinner = r12
            r12.setOnItemSelectedListener(r10)
            android.view.View r12 = r10.mView
            r1 = 2131559575(0x7f0d0497, float:1.8744498E38)
            android.view.View r12 = r12.findViewById(r1)
            android.widget.Spinner r12 = (android.widget.Spinner) r12
            r10.mProxySettingsSpinner = r12
            r12.setOnItemSelectedListener(r10)
            android.view.View r12 = r10.mView
            r1 = 2131559708(0x7f0d051c, float:1.8744768E38)
            android.view.View r12 = r12.findViewById(r1)
            android.widget.CheckBox r12 = (android.widget.CheckBox) r12
            r10.mSharedCheckBox = r12
            android.view.View r12 = r10.mView
            r1 = 2131559300(0x7f0d0384, float:1.874394E38)
            android.view.View r12 = r12.findViewById(r1)
            android.widget.Spinner r12 = (android.widget.Spinner) r12
            r10.mMeteredSettingsSpinner = r12
            android.view.View r12 = r10.mView
            r1 = 2131559069(0x7f0d029d, float:1.8743472E38)
            android.view.View r12 = r12.findViewById(r1)
            android.widget.Spinner r12 = (android.widget.Spinner) r12
            r10.mHiddenSettingsSpinner = r12
            android.view.View r12 = r10.mView
            r1 = 2131559542(0x7f0d0476, float:1.874443E38)
            android.view.View r12 = r12.findViewById(r1)
            android.widget.Spinner r12 = (android.widget.Spinner) r12
            r10.mPrivacySettingsSpinner = r12
            android.net.wifi.WifiManager r12 = r10.mWifiManager
            boolean r12 = r12.isConnectedMacRandomizationSupported()
            if (r12 == 0) goto L_0x00f1
            android.view.View r12 = r10.mView
            r1 = 2131559543(0x7f0d0477, float:1.8744433E38)
            android.view.View r12 = r12.findViewById(r1)
            r12.setVisibility(r0)
        L_0x00f1:
            android.widget.Spinner r12 = r10.mHiddenSettingsSpinner
            r12.setOnItemSelectedListener(r10)
            android.view.View r12 = r10.mView
            r1 = 2131559072(0x7f0d02a0, float:1.8743478E38)
            android.view.View r12 = r12.findViewById(r1)
            android.widget.TextView r12 = (android.widget.TextView) r12
            r10.mHiddenWarningView = r12
            android.widget.Spinner r1 = r10.mHiddenSettingsSpinner
            int r1 = r1.getSelectedItemPosition()
            r2 = 8
            if (r1 != 0) goto L_0x010f
            r1 = r2
            goto L_0x0110
        L_0x010f:
            r1 = r0
        L_0x0110:
            r12.setVisibility(r1)
            java.lang.Integer[] r12 = new java.lang.Integer[r2]
            r10.mSecurityInPosition = r12
            com.android.settingslib.wifi.AccessPoint r12 = r10.mAccessPoint
            r1 = 2130974584(0x7f041778, float:1.7557995E38)
            if (r12 != 0) goto L_0x012c
            r10.configureSecuritySpinner()
            com.android.settings.wifi.WifiConfigUiBase r12 = r10.mConfigUi
            java.lang.String r0 = r11.getString(r1)
            r12.setSubmitButton(r0)
            goto L_0x039e
        L_0x012c:
            com.android.settings.wifi.WifiConfigUiBase r3 = r10.mConfigUi
            java.lang.String r12 = r12.getTitle()
            r3.setTitle((java.lang.CharSequence) r12)
            android.view.View r12 = r10.mView
            r3 = 2131559148(0x7f0d02ec, float:1.8743632E38)
            android.view.View r12 = r12.findViewById(r3)
            android.view.ViewGroup r12 = (android.view.ViewGroup) r12
            com.android.settingslib.wifi.AccessPoint r3 = r10.mAccessPoint
            boolean r3 = r3.isSaved()
            r4 = 2
            r5 = 1
            if (r3 == 0) goto L_0x01f3
            com.android.settingslib.wifi.AccessPoint r3 = r10.mAccessPoint
            android.net.wifi.WifiConfiguration r3 = r3.getConfig()
            android.widget.Spinner r6 = r10.mMeteredSettingsSpinner
            int r7 = r3.meteredOverride
            r6.setSelection(r7)
            android.widget.Spinner r6 = r10.mHiddenSettingsSpinner
            boolean r7 = r3.hiddenSSID
            r6.setSelection(r7)
            android.widget.Spinner r6 = r10.mPrivacySettingsSpinner
            int r7 = r3.macRandomizationSetting
            if (r7 != r5) goto L_0x0166
            r7 = r0
            goto L_0x0167
        L_0x0166:
            r7 = r5
        L_0x0167:
            r6.setSelection(r7)
            android.net.IpConfiguration r6 = r3.getIpConfiguration()
            android.net.IpConfiguration$IpAssignment r6 = r6.getIpAssignment()
            android.net.IpConfiguration$IpAssignment r7 = android.net.IpConfiguration.IpAssignment.STATIC
            if (r6 != r7) goto L_0x019f
            android.widget.Spinner r6 = r10.mIpSettingsSpinner
            r6.setSelection(r5)
            android.net.IpConfiguration r6 = r3.getIpConfiguration()
            android.net.StaticIpConfiguration r6 = r6.getStaticIpConfiguration()
            if (r6 == 0) goto L_0x019d
            android.net.LinkAddress r7 = r6.getIpAddress()
            if (r7 == 0) goto L_0x019d
            r7 = 2130974515(0x7f041733, float:1.7557855E38)
            android.net.LinkAddress r6 = r6.getIpAddress()
            java.net.InetAddress r6 = r6.getAddress()
            java.lang.String r6 = r6.getHostAddress()
            r10.addRow(r12, r7, r6)
        L_0x019d:
            r6 = r5
            goto L_0x01a5
        L_0x019f:
            android.widget.Spinner r6 = r10.mIpSettingsSpinner
            r6.setSelection(r0)
            r6 = r0
        L_0x01a5:
            android.widget.CheckBox r7 = r10.mSharedCheckBox
            boolean r8 = r3.shared
            r7.setEnabled(r8)
            boolean r7 = r3.shared
            if (r7 != 0) goto L_0x01b1
            r6 = r5
        L_0x01b1:
            android.net.IpConfiguration r7 = r3.getIpConfiguration()
            android.net.IpConfiguration$ProxySettings r7 = r7.getProxySettings()
            android.net.IpConfiguration$ProxySettings r8 = android.net.IpConfiguration.ProxySettings.STATIC
            if (r7 != r8) goto L_0x01c4
            android.widget.Spinner r6 = r10.mProxySettingsSpinner
            r6.setSelection(r5)
        L_0x01c2:
            r6 = r5
            goto L_0x01d3
        L_0x01c4:
            android.net.IpConfiguration$ProxySettings r8 = android.net.IpConfiguration.ProxySettings.PAC
            if (r7 != r8) goto L_0x01ce
            android.widget.Spinner r6 = r10.mProxySettingsSpinner
            r6.setSelection(r4)
            goto L_0x01c2
        L_0x01ce:
            android.widget.Spinner r7 = r10.mProxySettingsSpinner
            r7.setSelection(r0)
        L_0x01d3:
            boolean r7 = r3.isPasspoint()
            if (r7 == 0) goto L_0x01f4
            r7 = 2130972502(0x7f040f56, float:1.7553772E38)
            android.content.Context r8 = r10.mContext
            r9 = 2130972501(0x7f040f55, float:1.755377E38)
            java.lang.String r8 = r8.getString(r9)
            java.lang.Object[] r9 = new java.lang.Object[r5]
            java.lang.String r3 = r3.providerFriendlyName
            r9[r0] = r3
            java.lang.String r3 = java.lang.String.format(r8, r9)
            r10.addRow(r12, r7, r3)
            goto L_0x01f4
        L_0x01f3:
            r6 = r0
        L_0x01f4:
            com.android.settingslib.wifi.AccessPoint r3 = r10.mAccessPoint
            boolean r3 = r3.isSaved()
            if (r3 != 0) goto L_0x020c
            com.android.settingslib.wifi.AccessPoint r3 = r10.mAccessPoint
            boolean r3 = r3.isActive()
            if (r3 != 0) goto L_0x020c
            com.android.settingslib.wifi.AccessPoint r3 = r10.mAccessPoint
            boolean r3 = r3.isPasspointConfig()
            if (r3 == 0) goto L_0x0210
        L_0x020c:
            int r3 = r10.mMode
            if (r3 == 0) goto L_0x024c
        L_0x0210:
            r10.showSecurityFields(r5, r5)
            r10.showIpConfigFields()
            r10.showProxyFields()
            android.view.View r3 = r10.mView
            r7 = 2131560040(0x7f0d0668, float:1.8745441E38)
            android.view.View r3 = r3.findViewById(r7)
            android.widget.CheckBox r3 = (android.widget.CheckBox) r3
            if (r6 != 0) goto L_0x023b
            android.view.View r7 = r10.mView
            r8 = 2131560039(0x7f0d0667, float:1.874544E38)
            android.view.View r7 = r7.findViewById(r8)
            r7.setVisibility(r0)
            r3.setOnCheckedChangeListener(r10)
            r3.setChecked(r6)
            r10.setAdvancedOptionAccessibilityString()
        L_0x023b:
            android.view.View r3 = r10.mView
            r7 = 2131560038(0x7f0d0666, float:1.8745437E38)
            android.view.View r3 = r3.findViewById(r7)
            if (r6 == 0) goto L_0x0248
            r6 = r0
            goto L_0x0249
        L_0x0248:
            r6 = r2
        L_0x0249:
            r3.setVisibility(r6)
        L_0x024c:
            int r3 = r10.mMode
            if (r3 != r4) goto L_0x025b
            com.android.settings.wifi.WifiConfigUiBase r12 = r10.mConfigUi
            java.lang.String r0 = r11.getString(r1)
            r12.setSubmitButton(r0)
            goto L_0x0399
        L_0x025b:
            r1 = 2130974382(0x7f0416ae, float:1.7557586E38)
            if (r3 != r5) goto L_0x026b
            com.android.settings.wifi.WifiConfigUiBase r12 = r10.mConfigUi
            java.lang.String r0 = r11.getString(r1)
            r12.setSubmitButton(r0)
            goto L_0x0399
        L_0x026b:
            com.android.settingslib.wifi.AccessPoint r3 = r10.mAccessPoint
            android.net.NetworkInfo$DetailedState r3 = r3.getDetailedState()
            java.lang.String r4 = r10.getSignalString()
            if (r3 == 0) goto L_0x027b
            android.net.NetworkInfo$DetailedState r6 = android.net.NetworkInfo.DetailedState.DISCONNECTED
            if (r3 != r6) goto L_0x0288
        L_0x027b:
            if (r4 == 0) goto L_0x0288
            com.android.settings.wifi.WifiConfigUiBase r12 = r10.mConfigUi
            java.lang.String r0 = r11.getString(r1)
            r12.setSubmitButton(r0)
            goto L_0x0375
        L_0x0288:
            r1 = 0
            if (r3 == 0) goto L_0x02ba
            com.android.settingslib.wifi.AccessPoint r6 = r10.mAccessPoint
            boolean r6 = r6.isEphemeral()
            com.android.settingslib.wifi.AccessPoint r7 = r10.mAccessPoint
            android.net.wifi.WifiConfiguration r7 = r7.getConfig()
            if (r7 == 0) goto L_0x029c
            r7.isPasspoint()
        L_0x029c:
            if (r7 == 0) goto L_0x02a9
            boolean r8 = r7.fromWifiNetworkSpecifier
            if (r8 != 0) goto L_0x02a6
            boolean r8 = r7.fromWifiNetworkSuggestion
            if (r8 == 0) goto L_0x02a9
        L_0x02a6:
            java.lang.String r7 = r7.creatorName
            goto L_0x02aa
        L_0x02a9:
            r7 = r1
        L_0x02aa:
            com.android.settings.wifi.WifiConfigUiBase r8 = r10.mConfigUi
            android.content.Context r8 = r8.getContext()
            java.lang.String r3 = com.android.settingslib.wifi.AccessPoint.getSummary(r8, r1, r3, r6, r7)
            r6 = 2130974661(0x7f0417c5, float:1.7558151E38)
            r10.addRow(r12, r6, r3)
        L_0x02ba:
            if (r4 == 0) goto L_0x02c2
            r3 = 2130974649(0x7f0417b9, float:1.7558127E38)
            r10.addRow(r12, r3, r4)
        L_0x02c2:
            com.android.settingslib.wifi.AccessPoint r3 = r10.mAccessPoint
            android.net.wifi.WifiInfo r3 = r3.getInfo()
            r4 = -1
            if (r3 == 0) goto L_0x02ee
            int r6 = r3.getTxLinkSpeedMbps()
            if (r6 == r4) goto L_0x02ee
            r6 = 2130973921(0x7f0414e1, float:1.755665E38)
            r7 = 2130973919(0x7f0414df, float:1.7556646E38)
            java.lang.String r7 = r11.getString(r7)
            java.lang.Object[] r8 = new java.lang.Object[r5]
            int r9 = r3.getTxLinkSpeedMbps()
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            r8[r0] = r9
            java.lang.String r7 = java.lang.String.format(r7, r8)
            r10.addRow(r12, r6, r7)
        L_0x02ee:
            if (r3 == 0) goto L_0x0313
            int r6 = r3.getRxLinkSpeedMbps()
            if (r6 == r4) goto L_0x0313
            r6 = 2130972955(0x7f04111b, float:1.7554691E38)
            r7 = 2130972953(0x7f041119, float:1.7554687E38)
            java.lang.String r7 = r11.getString(r7)
            java.lang.Object[] r5 = new java.lang.Object[r5]
            int r8 = r3.getRxLinkSpeedMbps()
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            r5[r0] = r8
            java.lang.String r5 = java.lang.String.format(r7, r5)
            r10.addRow(r12, r6, r5)
        L_0x0313:
            if (r3 == 0) goto L_0x035d
            int r5 = r3.getFrequency()
            if (r5 == r4) goto L_0x035d
            int r3 = r3.getFrequency()
            r4 = 2400(0x960, float:3.363E-42)
            if (r3 < r4) goto L_0x032f
            r4 = 2500(0x9c4, float:3.503E-42)
            if (r3 >= r4) goto L_0x032f
            r1 = 2130974347(0x7f04168b, float:1.7557515E38)
            java.lang.String r1 = r11.getString(r1)
            goto L_0x0355
        L_0x032f:
            r4 = 4900(0x1324, float:6.866E-42)
            if (r3 < r4) goto L_0x033f
            r4 = 5900(0x170c, float:8.268E-42)
            if (r3 >= r4) goto L_0x033f
            r1 = 2130974348(0x7f04168c, float:1.7557517E38)
            java.lang.String r1 = r11.getString(r1)
            goto L_0x0355
        L_0x033f:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Unexpected frequency "
            r4.append(r5)
            r4.append(r3)
            java.lang.String r3 = r4.toString()
            java.lang.String r4 = "WifiConfigController"
            android.util.Log.e(r4, r3)
        L_0x0355:
            if (r1 == 0) goto L_0x035d
            r3 = 2130974484(0x7f041714, float:1.7557792E38)
            r10.addRow(r12, r3, r1)
        L_0x035d:
            r1 = 2130974598(0x7f041786, float:1.7558024E38)
            com.android.settingslib.wifi.AccessPoint r3 = r10.mAccessPoint
            java.lang.String r0 = r3.getSecurityString(r0)
            r10.addRow(r12, r1, r0)
            android.view.View r12 = r10.mView
            r0 = 2131559171(0x7f0d0303, float:1.8743679E38)
            android.view.View r12 = r12.findViewById(r0)
            r12.setVisibility(r2)
        L_0x0375:
            com.android.settingslib.wifi.AccessPoint r12 = r10.mAccessPoint
            boolean r12 = r12.isSaved()
            if (r12 != 0) goto L_0x038d
            com.android.settingslib.wifi.AccessPoint r12 = r10.mAccessPoint
            boolean r12 = r12.isActive()
            if (r12 != 0) goto L_0x038d
            com.android.settingslib.wifi.AccessPoint r12 = r10.mAccessPoint
            boolean r12 = r12.isPasspointConfig()
            if (r12 == 0) goto L_0x0399
        L_0x038d:
            com.android.settings.wifi.WifiConfigUiBase r12 = r10.mConfigUi
            r0 = 2130974481(0x7f041711, float:1.7557786E38)
            java.lang.String r0 = r11.getString(r0)
            r12.setForgetButton(r0)
        L_0x0399:
            android.widget.ImageButton r12 = r10.mSsidScanButton
            r12.setVisibility(r2)
        L_0x039e:
            android.widget.CheckBox r12 = r10.mSharedCheckBox
            r12.setVisibility(r2)
            com.android.settings.wifi.WifiConfigUiBase r12 = r10.mConfigUi
            r0 = 2130974369(0x7f0416a1, float:1.755756E38)
            java.lang.String r11 = r11.getString(r0)
            r12.setCancelButton(r11)
            com.android.settings.wifi.WifiConfigUiBase r11 = r10.mConfigUi
            android.widget.Button r11 = r11.getSubmitButton()
            if (r11 == 0) goto L_0x03ba
            r10.enableSubmitIfAppropriate()
        L_0x03ba:
            boolean r11 = r10.mRequestFocus
            if (r11 == 0) goto L_0x03ca
            android.view.View r10 = r10.mView
            r11 = 2131559210(0x7f0d032a, float:1.8743758E38)
            android.view.View r10 = r10.findViewById(r11)
            r10.requestFocus()
        L_0x03ca:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.wifi.WifiConfigController.initWifiConfigController(com.android.settingslib.wifi.AccessPoint, int):void");
    }

    private void addRow(ViewGroup viewGroup, int i, String str) {
        View inflate = this.mConfigUi.getLayoutInflater().inflate(C0444R.C0450layout.wifi_dialog_row, viewGroup, false);
        ((TextView) inflate.findViewById(C0444R.C0448id.name)).setText(i);
        ((TextView) inflate.findViewById(C0444R.C0448id.value)).setText(str);
        viewGroup.addView(inflate);
    }

    /* access modifiers changed from: package-private */
    public String getSignalString() {
        int level;
        if (!this.mAccessPoint.isReachable() || (level = this.mAccessPoint.getLevel()) <= -1) {
            return null;
        }
        String[] strArr = this.mLevels;
        if (level < strArr.length) {
            return strArr[level];
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void hideForgetButton() {
        Button forgetButton = this.mConfigUi.getForgetButton();
        if (forgetButton != null) {
            forgetButton.setVisibility(8);
        }
    }

    /* access modifiers changed from: package-private */
    public void hideSubmitButton() {
        Button submitButton = this.mConfigUi.getSubmitButton();
        if (submitButton != null) {
            submitButton.setVisibility(8);
        }
    }

    /* access modifiers changed from: package-private */
    public void enableSubmitIfAppropriate() {
        Button submitButton = this.mConfigUi.getSubmitButton();
        if (submitButton != null) {
            submitButton.setEnabled(isSubmittable());
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isValidPsk(String str) {
        if (str.length() == 64 && str.matches("[0-9A-Fa-f]{64}")) {
            return true;
        }
        if (str.length() < 8 || str.length() > 63) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isValidSaePassword(String str) {
        return str.length() >= 1 && str.length() <= 63;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0046, code lost:
        r0 = r8.mAccessPoint;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0052, code lost:
        r0 = r8.mAccessPoint;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isSubmittable() {
        /*
            r8 = this;
            android.widget.TextView r0 = r8.mPasswordView
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x003b
            int r3 = r8.mAccessPointSecurity
            if (r3 != r1) goto L_0x0010
            int r0 = r0.length()
            if (r0 == 0) goto L_0x003c
        L_0x0010:
            int r0 = r8.mAccessPointSecurity
            r3 = 2
            if (r0 != r3) goto L_0x0025
            android.widget.TextView r0 = r8.mPasswordView
            java.lang.CharSequence r0 = r0.getText()
            java.lang.String r0 = r0.toString()
            boolean r0 = r8.isValidPsk(r0)
            if (r0 == 0) goto L_0x003c
        L_0x0025:
            int r0 = r8.mAccessPointSecurity
            r3 = 5
            if (r0 != r3) goto L_0x003b
            android.widget.TextView r0 = r8.mPasswordView
            java.lang.CharSequence r0 = r0.getText()
            java.lang.String r0 = r0.toString()
            boolean r0 = r8.isValidSaePassword(r0)
            if (r0 != 0) goto L_0x003b
            goto L_0x003c
        L_0x003b:
            r1 = r2
        L_0x003c:
            android.widget.TextView r0 = r8.mSsidView
            if (r0 == 0) goto L_0x0046
            int r0 = r0.length()
            if (r0 == 0) goto L_0x006c
        L_0x0046:
            com.android.settingslib.wifi.AccessPoint r0 = r8.mAccessPoint
            if (r0 == 0) goto L_0x0050
            boolean r0 = r0.isSaved()
            if (r0 != 0) goto L_0x0052
        L_0x0050:
            if (r1 != 0) goto L_0x006c
        L_0x0052:
            com.android.settingslib.wifi.AccessPoint r0 = r8.mAccessPoint
            if (r0 == 0) goto L_0x0067
            boolean r0 = r0.isSaved()
            if (r0 == 0) goto L_0x0067
            if (r1 == 0) goto L_0x0067
            android.widget.TextView r0 = r8.mPasswordView
            int r0 = r0.length()
            if (r0 <= 0) goto L_0x0067
            goto L_0x006c
        L_0x0067:
            boolean r0 = r8.ipAndProxyFieldsAreValid()
            goto L_0x006d
        L_0x006c:
            r0 = r2
        L_0x006d:
            int r1 = r8.mAccessPointSecurity
            r3 = 6
            r4 = 7
            r5 = 3
            r6 = 8
            if (r1 == r5) goto L_0x007a
            if (r1 == r4) goto L_0x007a
            if (r1 != r3) goto L_0x00c3
        L_0x007a:
            android.widget.Spinner r1 = r8.mEapCaCertSpinner
            if (r1 == 0) goto L_0x00c3
            android.view.View r1 = r8.mView
            r7 = 2131559199(0x7f0d031f, float:1.8743735E38)
            android.view.View r1 = r1.findViewById(r7)
            int r1 = r1.getVisibility()
            if (r1 == r6) goto L_0x00c3
            android.widget.Spinner r1 = r8.mEapCaCertSpinner
            java.lang.Object r1 = r1.getSelectedItem()
            java.lang.String r1 = (java.lang.String) r1
            java.lang.String r7 = r8.mUnspecifiedCertString
            boolean r1 = r1.equals(r7)
            if (r1 == 0) goto L_0x009f
        L_0x009d:
            r0 = r2
            goto L_0x00c3
        L_0x009f:
            android.widget.TextView r1 = r8.mEapDomainView
            if (r1 == 0) goto L_0x00c3
            android.view.View r1 = r8.mView
            r7 = 2131559200(0x7f0d0320, float:1.8743737E38)
            android.view.View r1 = r1.findViewById(r7)
            int r1 = r1.getVisibility()
            if (r1 == r6) goto L_0x00c3
            android.widget.TextView r1 = r8.mEapDomainView
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x00c3
            goto L_0x009d
        L_0x00c3:
            int r1 = r8.mAccessPointSecurity
            if (r1 == r5) goto L_0x00cb
            if (r1 == r4) goto L_0x00cb
            if (r1 != r3) goto L_0x00ed
        L_0x00cb:
            android.widget.Spinner r1 = r8.mEapUserCertSpinner
            if (r1 == 0) goto L_0x00ed
            android.view.View r1 = r8.mView
            r3 = 2131559209(0x7f0d0329, float:1.8743756E38)
            android.view.View r1 = r1.findViewById(r3)
            int r1 = r1.getVisibility()
            if (r1 == r6) goto L_0x00ed
            android.widget.Spinner r1 = r8.mEapUserCertSpinner
            java.lang.Object r1 = r1.getSelectedItem()
            java.lang.String r8 = r8.mUnspecifiedCertString
            boolean r8 = r1.equals(r8)
            if (r8 == 0) goto L_0x00ed
            goto L_0x00ee
        L_0x00ed:
            r2 = r0
        L_0x00ee:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.wifi.WifiConfigController.isSubmittable():boolean");
    }

    /* access modifiers changed from: package-private */
    public void showWarningMessagesIfAppropriate() {
        this.mView.findViewById(C0444R.C0448id.no_user_cert_warning).setVisibility(8);
        this.mView.findViewById(C0444R.C0448id.no_domain_warning).setVisibility(8);
        this.mView.findViewById(C0444R.C0448id.ssid_too_long_warning).setVisibility(8);
        TextView textView = this.mSsidView;
        if (textView != null && WifiUtils.isSSIDTooLong(textView.getText().toString())) {
            this.mView.findViewById(C0444R.C0448id.ssid_too_long_warning).setVisibility(0);
        }
        if (!(this.mEapCaCertSpinner == null || this.mView.findViewById(C0444R.C0448id.l_ca_cert).getVisibility() == 8 || this.mEapDomainView == null || this.mView.findViewById(C0444R.C0448id.l_domain).getVisibility() == 8 || !TextUtils.isEmpty(this.mEapDomainView.getText().toString()))) {
            this.mView.findViewById(C0444R.C0448id.no_domain_warning).setVisibility(0);
        }
        if (this.mAccessPointSecurity == 6 && this.mEapMethodSpinner.getSelectedItemPosition() == 1 && ((String) this.mEapUserCertSpinner.getSelectedItem()).equals(this.mUnspecifiedCertString)) {
            this.mView.findViewById(C0444R.C0448id.no_user_cert_warning).setVisibility(0);
        }
    }

    public WifiConfiguration getConfig() {
        if (this.mMode == 0) {
            return null;
        }
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        AccessPoint accessPoint = this.mAccessPoint;
        int i = 0;
        if (accessPoint == null) {
            wifiConfiguration.SSID = AccessPoint.convertToQuotedString(this.mSsidView.getText().toString());
            wifiConfiguration.hiddenSSID = this.mHiddenSettingsSpinner.getSelectedItemPosition() == 1;
        } else if (!accessPoint.isSaved()) {
            wifiConfiguration.SSID = AccessPoint.convertToQuotedString(this.mAccessPoint.getSsidStr());
        } else {
            wifiConfiguration.networkId = this.mAccessPoint.getConfig().networkId;
            wifiConfiguration.hiddenSSID = this.mAccessPoint.getConfig().hiddenSSID;
        }
        wifiConfiguration.shared = this.mSharedCheckBox.isChecked();
        int i2 = this.mAccessPointSecurity;
        switch (i2) {
            case 0:
                wifiConfiguration.setSecurityParams(0);
                break;
            case 1:
                wifiConfiguration.setSecurityParams(1);
                if (this.mPasswordView.length() != 0) {
                    int length = this.mPasswordView.length();
                    String charSequence = this.mPasswordView.getText().toString();
                    if ((length != 10 && length != 26 && length != 58) || !charSequence.matches("[0-9A-Fa-f]*")) {
                        wifiConfiguration.wepKeys[0] = '\"' + charSequence + '\"';
                        break;
                    } else {
                        wifiConfiguration.wepKeys[0] = charSequence;
                        break;
                    }
                }
                break;
            case 2:
                wifiConfiguration.setSecurityParams(2);
                if (this.mPasswordView.length() != 0) {
                    String charSequence2 = this.mPasswordView.getText().toString();
                    if (!charSequence2.matches("[0-9A-Fa-f]{64}")) {
                        wifiConfiguration.preSharedKey = '\"' + charSequence2 + '\"';
                        break;
                    } else {
                        wifiConfiguration.preSharedKey = charSequence2;
                        break;
                    }
                }
                break;
            case 3:
            case 6:
            case 7:
                if (i2 == 6) {
                    wifiConfiguration.setSecurityParams(5);
                } else if (i2 == 7) {
                    wifiConfiguration.setSecurityParams(9);
                } else {
                    wifiConfiguration.setSecurityParams(3);
                }
                wifiConfiguration.enterpriseConfig = new WifiEnterpriseConfig();
                int selectedItemPosition = this.mEapMethodSpinner.getSelectedItemPosition();
                int selectedItemPosition2 = this.mPhase2Spinner.getSelectedItemPosition();
                wifiConfiguration.enterpriseConfig.setEapMethod(selectedItemPosition);
                if (selectedItemPosition != 0) {
                    if (selectedItemPosition == 2) {
                        if (selectedItemPosition2 == 0) {
                            wifiConfiguration.enterpriseConfig.setPhase2Method(1);
                        } else if (selectedItemPosition2 == 1) {
                            wifiConfiguration.enterpriseConfig.setPhase2Method(2);
                        } else if (selectedItemPosition2 == 2) {
                            wifiConfiguration.enterpriseConfig.setPhase2Method(3);
                        } else if (selectedItemPosition2 != 3) {
                            Log.e("WifiConfigController", "Unknown phase2 method" + selectedItemPosition2);
                        } else {
                            wifiConfiguration.enterpriseConfig.setPhase2Method(4);
                        }
                    }
                } else if (selectedItemPosition2 == 0) {
                    wifiConfiguration.enterpriseConfig.setPhase2Method(3);
                } else if (selectedItemPosition2 == 1) {
                    wifiConfiguration.enterpriseConfig.setPhase2Method(4);
                } else if (selectedItemPosition2 == 2) {
                    wifiConfiguration.enterpriseConfig.setPhase2Method(5);
                } else if (selectedItemPosition2 == 3) {
                    wifiConfiguration.enterpriseConfig.setPhase2Method(6);
                } else if (selectedItemPosition2 != 4) {
                    Log.e("WifiConfigController", "Unknown phase2 method" + selectedItemPosition2);
                } else {
                    wifiConfiguration.enterpriseConfig.setPhase2Method(7);
                }
                String str = (String) this.mEapCaCertSpinner.getSelectedItem();
                wifiConfiguration.enterpriseConfig.setCaCertificateAliases((String[]) null);
                wifiConfiguration.enterpriseConfig.setCaPath((String) null);
                wifiConfiguration.enterpriseConfig.setDomainSuffixMatch(this.mEapDomainView.getText().toString());
                if (!str.equals(this.mUnspecifiedCertString)) {
                    if (str.equals(this.mUseSystemCertsString)) {
                        wifiConfiguration.enterpriseConfig.setCaPath("/system/etc/security/cacerts");
                    } else if (str.equals(this.mMultipleCertSetString)) {
                        AccessPoint accessPoint2 = this.mAccessPoint;
                        if (accessPoint2 != null) {
                            if (!accessPoint2.isSaved()) {
                                Log.e("WifiConfigController", "Multiple certs can only be set when editing saved network");
                            }
                            wifiConfiguration.enterpriseConfig.setCaCertificateAliases(this.mAccessPoint.getConfig().enterpriseConfig.getCaCertificateAliases());
                        }
                    } else {
                        wifiConfiguration.enterpriseConfig.setCaCertificateAliases(new String[]{str});
                    }
                }
                if (!(wifiConfiguration.enterpriseConfig.getCaCertificateAliases() == null || wifiConfiguration.enterpriseConfig.getCaPath() == null)) {
                    Log.e("WifiConfigController", "ca_cert (" + wifiConfiguration.enterpriseConfig.getCaCertificateAliases() + ") and ca_path (" + wifiConfiguration.enterpriseConfig.getCaPath() + ") should not both be non-null");
                }
                if (str.equals(this.mUnspecifiedCertString)) {
                    wifiConfiguration.enterpriseConfig.setOcsp(0);
                } else {
                    wifiConfiguration.enterpriseConfig.setOcsp(this.mEapOcspSpinner.getSelectedItemPosition());
                }
                String str2 = (String) this.mEapUserCertSpinner.getSelectedItem();
                if (str2.equals(this.mUnspecifiedCertString) || str2.equals(this.mDoNotProvideEapUserCertString)) {
                    str2 = "";
                }
                wifiConfiguration.enterpriseConfig.setClientCertificateAlias(str2);
                if (selectedItemPosition == 4 || selectedItemPosition == 5 || selectedItemPosition == 6) {
                    wifiConfiguration.enterpriseConfig.setIdentity("");
                    wifiConfiguration.enterpriseConfig.setAnonymousIdentity("");
                } else if (selectedItemPosition == 3) {
                    wifiConfiguration.enterpriseConfig.setIdentity(this.mEapIdentityView.getText().toString());
                    wifiConfiguration.enterpriseConfig.setAnonymousIdentity("");
                } else {
                    wifiConfiguration.enterpriseConfig.setIdentity(this.mEapIdentityView.getText().toString());
                    wifiConfiguration.enterpriseConfig.setAnonymousIdentity(this.mEapAnonymousView.getText().toString());
                }
                if (this.mPasswordView.isShown()) {
                    if (this.mPasswordView.length() > 0) {
                        wifiConfiguration.enterpriseConfig.setPassword(this.mPasswordView.getText().toString());
                        break;
                    }
                } else {
                    wifiConfiguration.enterpriseConfig.setPassword(this.mPasswordView.getText().toString());
                    break;
                }
                break;
            case 4:
                wifiConfiguration.setSecurityParams(6);
                break;
            case 5:
                wifiConfiguration.setSecurityParams(4);
                if (this.mPasswordView.length() != 0) {
                    wifiConfiguration.preSharedKey = '\"' + this.mPasswordView.getText().toString() + '\"';
                    break;
                }
                break;
            default:
                return null;
        }
        if (wifiConfiguration.enterpriseConfig.isAuthenticationSimBased() && this.mActiveSubscriptionInfos.size() > 0) {
            wifiConfiguration.carrierId = this.mActiveSubscriptionInfos.get(this.mEapSimSpinner.getSelectedItemPosition()).getCarrierId();
        }
        IpConfiguration ipConfiguration = new IpConfiguration();
        ipConfiguration.setIpAssignment(this.mIpAssignment);
        ipConfiguration.setProxySettings(this.mProxySettings);
        ipConfiguration.setStaticIpConfiguration(this.mStaticIpConfiguration);
        ipConfiguration.setHttpProxy(this.mHttpProxy);
        wifiConfiguration.setIpConfiguration(ipConfiguration);
        Spinner spinner = this.mMeteredSettingsSpinner;
        if (spinner != null) {
            wifiConfiguration.meteredOverride = spinner.getSelectedItemPosition();
        }
        Spinner spinner2 = this.mPrivacySettingsSpinner;
        if (spinner2 != null) {
            if (spinner2.getSelectedItemPosition() == 0) {
                i = 1;
            }
            wifiConfiguration.macRandomizationSetting = i;
        }
        return wifiConfiguration;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0078 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean ipAndProxyFieldsAreValid() {
        /*
            r6 = this;
            android.widget.Spinner r0 = r6.mIpSettingsSpinner
            r1 = 1
            if (r0 == 0) goto L_0x000e
            int r0 = r0.getSelectedItemPosition()
            if (r0 != r1) goto L_0x000e
            android.net.IpConfiguration$IpAssignment r0 = android.net.IpConfiguration.IpAssignment.STATIC
            goto L_0x0010
        L_0x000e:
            android.net.IpConfiguration$IpAssignment r0 = android.net.IpConfiguration.IpAssignment.DHCP
        L_0x0010:
            r6.mIpAssignment = r0
            android.net.IpConfiguration$IpAssignment r2 = android.net.IpConfiguration.IpAssignment.STATIC
            r3 = 0
            if (r0 != r2) goto L_0x0025
            android.net.StaticIpConfiguration r0 = new android.net.StaticIpConfiguration
            r0.<init>()
            r6.mStaticIpConfiguration = r0
            int r0 = r6.validateIpConfigFields(r0)
            if (r0 == 0) goto L_0x0025
            return r3
        L_0x0025:
            android.widget.Spinner r0 = r6.mProxySettingsSpinner
            int r0 = r0.getSelectedItemPosition()
            android.net.IpConfiguration$ProxySettings r2 = android.net.IpConfiguration.ProxySettings.NONE
            r6.mProxySettings = r2
            r2 = 0
            r6.mHttpProxy = r2
            if (r0 != r1) goto L_0x0079
            android.widget.TextView r2 = r6.mProxyHostView
            if (r2 == 0) goto L_0x0079
            android.net.IpConfiguration$ProxySettings r0 = android.net.IpConfiguration.ProxySettings.STATIC
            r6.mProxySettings = r0
            java.lang.CharSequence r0 = r2.getText()
            java.lang.String r0 = r0.toString()
            android.widget.TextView r2 = r6.mProxyPortView
            java.lang.CharSequence r2 = r2.getText()
            java.lang.String r2 = r2.toString()
            android.widget.TextView r4 = r6.mProxyExclusionListView
            java.lang.CharSequence r4 = r4.getText()
            java.lang.String r4 = r4.toString()
            int r5 = java.lang.Integer.parseInt(r2)     // Catch:{ NumberFormatException -> 0x0061 }
            int r2 = com.android.settings.ProxySelector.validate(r0, r2, r4)     // Catch:{ NumberFormatException -> 0x0062 }
            goto L_0x0065
        L_0x0061:
            r5 = r3
        L_0x0062:
            r2 = 2130972754(0x7f041052, float:1.7554284E38)
        L_0x0065:
            if (r2 != 0) goto L_0x0078
            java.lang.String r2 = ","
            java.lang.String[] r2 = r4.split(r2)
            java.util.List r2 = java.util.Arrays.asList(r2)
            android.net.ProxyInfo r0 = android.net.ProxyInfo.buildDirectProxy(r0, r5, r2)
            r6.mHttpProxy = r0
            goto L_0x00a0
        L_0x0078:
            return r3
        L_0x0079:
            r2 = 2
            if (r0 != r2) goto L_0x00a0
            android.widget.TextView r0 = r6.mProxyPacView
            if (r0 == 0) goto L_0x00a0
            android.net.IpConfiguration$ProxySettings r2 = android.net.IpConfiguration.ProxySettings.PAC
            r6.mProxySettings = r2
            java.lang.CharSequence r0 = r0.getText()
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 == 0) goto L_0x008f
            return r3
        L_0x008f:
            java.lang.String r0 = r0.toString()
            android.net.Uri r0 = android.net.Uri.parse(r0)
            if (r0 != 0) goto L_0x009a
            return r3
        L_0x009a:
            android.net.ProxyInfo r0 = android.net.ProxyInfo.buildPacProxy(r0)
            r6.mHttpProxy = r0
        L_0x00a0:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.wifi.WifiConfigController.ipAndProxyFieldsAreValid():boolean");
    }

    private Inet4Address getIPv4Address(String str) {
        try {
            return (Inet4Address) InetAddresses.parseNumericAddress(str);
        } catch (ClassCastException | IllegalArgumentException unused) {
            return null;
        }
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x007a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int validateIpConfigFields(android.net.StaticIpConfiguration r7) {
        /*
            r6 = this;
            android.widget.TextView r0 = r6.mIpAddressView
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            java.lang.CharSequence r0 = r0.getText()
            java.lang.String r0 = r0.toString()
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            r3 = 2130974520(0x7f041738, float:1.7557865E38)
            if (r2 == 0) goto L_0x0018
            return r3
        L_0x0018:
            java.net.Inet4Address r0 = r6.getIPv4Address(r0)
            if (r0 == 0) goto L_0x0133
            java.net.InetAddress r2 = java.net.Inet4Address.ANY
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L_0x0028
            goto L_0x0133
        L_0x0028:
            android.net.StaticIpConfiguration$Builder r2 = new android.net.StaticIpConfiguration$Builder
            r2.<init>()
            java.util.List r4 = r7.getDnsServers()
            android.net.StaticIpConfiguration$Builder r2 = r2.setDnsServers(r4)
            java.lang.String r4 = r7.getDomains()
            android.net.StaticIpConfiguration$Builder r2 = r2.setDomains(r4)
            java.net.InetAddress r4 = r7.getGateway()
            android.net.StaticIpConfiguration$Builder r2 = r2.setGateway(r4)
            android.net.LinkAddress r7 = r7.getIpAddress()
            android.net.StaticIpConfiguration$Builder r7 = r2.setIpAddress(r7)
            r2 = -1
            android.widget.TextView r4 = r6.mNetworkPrefixLengthView     // Catch:{ NumberFormatException -> 0x007a, IllegalArgumentException -> 0x0076 }
            java.lang.CharSequence r4 = r4.getText()     // Catch:{ NumberFormatException -> 0x007a, IllegalArgumentException -> 0x0076 }
            java.lang.String r4 = r4.toString()     // Catch:{ NumberFormatException -> 0x007a, IllegalArgumentException -> 0x0076 }
            int r2 = java.lang.Integer.parseInt(r4)     // Catch:{ NumberFormatException -> 0x007a, IllegalArgumentException -> 0x0076 }
            if (r2 < 0) goto L_0x006c
            r4 = 32
            if (r2 <= r4) goto L_0x0063
            goto L_0x006c
        L_0x0063:
            android.net.LinkAddress r4 = new android.net.LinkAddress     // Catch:{ NumberFormatException -> 0x007a, IllegalArgumentException -> 0x0076 }
            r4.<init>(r0, r2)     // Catch:{ NumberFormatException -> 0x007a, IllegalArgumentException -> 0x0076 }
            r7.setIpAddress(r4)     // Catch:{ NumberFormatException -> 0x007a, IllegalArgumentException -> 0x0076 }
            goto L_0x008c
        L_0x006c:
            r6 = 2130974521(0x7f041739, float:1.7557867E38)
            r7.build()
            return r6
        L_0x0073:
            r6 = move-exception
            goto L_0x012f
        L_0x0076:
            r7.build()
            return r3
        L_0x007a:
            android.widget.TextView r3 = r6.mNetworkPrefixLengthView     // Catch:{ all -> 0x0073 }
            com.android.settings.wifi.WifiConfigUiBase r4 = r6.mConfigUi     // Catch:{ all -> 0x0073 }
            android.content.Context r4 = r4.getContext()     // Catch:{ all -> 0x0073 }
            r5 = 2130974542(0x7f04174e, float:1.755791E38)
            java.lang.String r4 = r4.getString(r5)     // Catch:{ all -> 0x0073 }
            r3.setText(r4)     // Catch:{ all -> 0x0073 }
        L_0x008c:
            android.widget.TextView r3 = r6.mGatewayView     // Catch:{ all -> 0x0073 }
            java.lang.CharSequence r3 = r3.getText()     // Catch:{ all -> 0x0073 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0073 }
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ all -> 0x0073 }
            if (r4 == 0) goto L_0x00b7
            java.net.InetAddress r0 = com.android.net.module.util.NetUtils.getNetworkPart(r0, r2)     // Catch:{ RuntimeException | UnknownHostException -> 0x00d1 }
            byte[] r0 = r0.getAddress()     // Catch:{ RuntimeException | UnknownHostException -> 0x00d1 }
            int r2 = r0.length     // Catch:{ RuntimeException | UnknownHostException -> 0x00d1 }
            r3 = 1
            int r2 = r2 - r3
            r0[r2] = r3     // Catch:{ RuntimeException | UnknownHostException -> 0x00d1 }
            android.widget.TextView r2 = r6.mGatewayView     // Catch:{ RuntimeException | UnknownHostException -> 0x00d1 }
            java.net.InetAddress r0 = java.net.InetAddress.getByAddress(r0)     // Catch:{ RuntimeException | UnknownHostException -> 0x00d1 }
            java.lang.String r0 = r0.getHostAddress()     // Catch:{ RuntimeException | UnknownHostException -> 0x00d1 }
            r2.setText(r0)     // Catch:{ RuntimeException | UnknownHostException -> 0x00d1 }
            goto L_0x00d1
        L_0x00b7:
            java.net.Inet4Address r0 = r6.getIPv4Address(r3)     // Catch:{ all -> 0x0073 }
            r2 = 2130974519(0x7f041737, float:1.7557863E38)
            if (r0 != 0) goto L_0x00c4
            r7.build()
            return r2
        L_0x00c4:
            boolean r3 = r0.isMulticastAddress()     // Catch:{ all -> 0x0073 }
            if (r3 == 0) goto L_0x00ce
            r7.build()
            return r2
        L_0x00ce:
            r7.setGateway(r0)     // Catch:{ all -> 0x0073 }
        L_0x00d1:
            android.widget.TextView r0 = r6.mDns1View     // Catch:{ all -> 0x0073 }
            java.lang.CharSequence r0 = r0.getText()     // Catch:{ all -> 0x0073 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0073 }
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0073 }
            r2.<init>()     // Catch:{ all -> 0x0073 }
            boolean r3 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x0073 }
            r4 = 2130974518(0x7f041736, float:1.7557861E38)
            if (r3 == 0) goto L_0x00fc
            android.widget.TextView r0 = r6.mDns1View     // Catch:{ all -> 0x0073 }
            com.android.settings.wifi.WifiConfigUiBase r3 = r6.mConfigUi     // Catch:{ all -> 0x0073 }
            android.content.Context r3 = r3.getContext()     // Catch:{ all -> 0x0073 }
            r5 = 2130974426(0x7f0416da, float:1.7557675E38)
            java.lang.String r3 = r3.getString(r5)     // Catch:{ all -> 0x0073 }
            r0.setText(r3)     // Catch:{ all -> 0x0073 }
            goto L_0x0109
        L_0x00fc:
            java.net.Inet4Address r0 = r6.getIPv4Address(r0)     // Catch:{ all -> 0x0073 }
            if (r0 != 0) goto L_0x0106
            r7.build()
            return r4
        L_0x0106:
            r2.add(r0)     // Catch:{ all -> 0x0073 }
        L_0x0109:
            android.widget.TextView r0 = r6.mDns2View     // Catch:{ all -> 0x0073 }
            int r0 = r0.length()     // Catch:{ all -> 0x0073 }
            if (r0 <= 0) goto L_0x0128
            android.widget.TextView r0 = r6.mDns2View     // Catch:{ all -> 0x0073 }
            java.lang.CharSequence r0 = r0.getText()     // Catch:{ all -> 0x0073 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0073 }
            java.net.Inet4Address r6 = r6.getIPv4Address(r0)     // Catch:{ all -> 0x0073 }
            if (r6 != 0) goto L_0x0125
            r7.build()
            return r4
        L_0x0125:
            r2.add(r6)     // Catch:{ all -> 0x0073 }
        L_0x0128:
            r7.setDnsServers(r2)     // Catch:{ all -> 0x0073 }
            r7.build()
            return r1
        L_0x012f:
            r7.build()
            throw r6
        L_0x0133:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.wifi.WifiConfigController.validateIpConfigFields(android.net.StaticIpConfiguration):int");
    }

    private void showSecurityFields(boolean z, boolean z2) {
        boolean z3;
        AccessPoint accessPoint;
        int i = this.mAccessPointSecurity;
        if (i == 0 || i == 4) {
            this.mView.findViewById(C0444R.C0448id.security_fields).setVisibility(8);
            return;
        }
        this.mView.findViewById(C0444R.C0448id.security_fields).setVisibility(0);
        if (this.mPasswordView == null) {
            TextView textView = (TextView) this.mView.findViewById(C0444R.C0448id.password);
            this.mPasswordView = textView;
            textView.addTextChangedListener(this);
            this.mPasswordView.setOnEditorActionListener(this);
            this.mPasswordView.setOnKeyListener(this);
            ((CheckBox) this.mView.findViewById(C0444R.C0448id.show_password)).setOnCheckedChangeListener(this);
            AccessPoint accessPoint2 = this.mAccessPoint;
            if (accessPoint2 != null && accessPoint2.isSaved()) {
                this.mPasswordView.setHint(C0444R.string.wifi_unchanged);
            }
        }
        int i2 = this.mAccessPointSecurity;
        if (i2 == 3 || i2 == 6) {
            this.mView.findViewById(C0444R.C0448id.eap).setVisibility(0);
            if (this.mEapMethodSpinner == null) {
                Spinner spinner = (Spinner) this.mView.findViewById(C0444R.C0448id.method);
                this.mEapMethodSpinner = spinner;
                spinner.setOnItemSelectedListener(this);
                this.mEapSimSpinner = (Spinner) this.mView.findViewById(C0444R.C0448id.sim);
                Spinner spinner2 = (Spinner) this.mView.findViewById(C0444R.C0448id.phase2);
                this.mPhase2Spinner = spinner2;
                spinner2.setOnItemSelectedListener(this);
                Spinner spinner3 = (Spinner) this.mView.findViewById(C0444R.C0448id.ca_cert);
                this.mEapCaCertSpinner = spinner3;
                spinner3.setOnItemSelectedListener(this);
                this.mEapOcspSpinner = (Spinner) this.mView.findViewById(C0444R.C0448id.ocsp);
                TextView textView2 = (TextView) this.mView.findViewById(C0444R.C0448id.domain);
                this.mEapDomainView = textView2;
                textView2.addTextChangedListener(this);
                Spinner spinner4 = (Spinner) this.mView.findViewById(C0444R.C0448id.user_cert);
                this.mEapUserCertSpinner = spinner4;
                spinner4.setOnItemSelectedListener(this);
                this.mEapIdentityView = (TextView) this.mView.findViewById(C0444R.C0448id.identity);
                this.mEapAnonymousView = (TextView) this.mView.findViewById(C0444R.C0448id.anonymous);
                setAccessibilityDelegateForSecuritySpinners();
                z3 = true;
            } else {
                z3 = false;
            }
            if (z) {
                if (this.mAccessPointSecurity == 6) {
                    this.mEapMethodSpinner.setAdapter(getSpinnerAdapter((int) C0444R.array.wifi_eap_method));
                    this.mEapMethodSpinner.setSelection(1);
                    this.mEapMethodSpinner.setEnabled(false);
                } else if (Utils.isWifiOnly(this.mContext) || !this.mContext.getResources().getBoolean(17891617)) {
                    this.mEapMethodSpinner.setAdapter(getSpinnerAdapter((int) C0444R.array.eap_method_without_sim_auth));
                    this.mEapMethodSpinner.setEnabled(true);
                } else {
                    this.mEapMethodSpinner.setAdapter(getSpinnerAdapterWithEapMethodsTts(C0444R.array.wifi_eap_method));
                    this.mEapMethodSpinner.setEnabled(true);
                }
            }
            if (z2) {
                loadSims();
                AndroidKeystoreAliasLoader androidKeystoreAliasLoader = getAndroidKeystoreAliasLoader();
                loadCertificates(this.mEapCaCertSpinner, androidKeystoreAliasLoader.getCaCertAliases(), (String) null, false, true);
                loadCertificates(this.mEapUserCertSpinner, androidKeystoreAliasLoader.getKeyCertAliases(), this.mDoNotProvideEapUserCertString, false, false);
                setSelection(this.mEapCaCertSpinner, this.mUseSystemCertsString);
            }
            if (!z3 || (accessPoint = this.mAccessPoint) == null || !accessPoint.isSaved()) {
                showEapFieldsByMethod(this.mEapMethodSpinner.getSelectedItemPosition());
                return;
            }
            WifiConfiguration config = this.mAccessPoint.getConfig();
            WifiEnterpriseConfig wifiEnterpriseConfig = config.enterpriseConfig;
            int eapMethod = wifiEnterpriseConfig.getEapMethod();
            int phase2Method = wifiEnterpriseConfig.getPhase2Method();
            this.mEapMethodSpinner.setSelection(eapMethod);
            showEapFieldsByMethod(eapMethod);
            if (eapMethod != 0) {
                if (eapMethod == 2) {
                    if (phase2Method == 1) {
                        this.mPhase2Spinner.setSelection(0);
                    } else if (phase2Method == 2) {
                        this.mPhase2Spinner.setSelection(1);
                    } else if (phase2Method == 3) {
                        this.mPhase2Spinner.setSelection(2);
                    } else if (phase2Method != 4) {
                        Log.e("WifiConfigController", "Invalid phase 2 method " + phase2Method);
                    } else {
                        this.mPhase2Spinner.setSelection(3);
                    }
                }
            } else if (phase2Method == 3) {
                this.mPhase2Spinner.setSelection(0);
            } else if (phase2Method == 4) {
                this.mPhase2Spinner.setSelection(1);
            } else if (phase2Method == 5) {
                this.mPhase2Spinner.setSelection(2);
            } else if (phase2Method == 6) {
                this.mPhase2Spinner.setSelection(3);
            } else if (phase2Method != 7) {
                Log.e("WifiConfigController", "Invalid phase 2 method " + phase2Method);
            } else {
                this.mPhase2Spinner.setSelection(4);
            }
            if (wifiEnterpriseConfig.isAuthenticationSimBased()) {
                int i3 = 0;
                while (true) {
                    if (i3 >= this.mActiveSubscriptionInfos.size()) {
                        break;
                    } else if (config.carrierId == this.mActiveSubscriptionInfos.get(i3).getCarrierId()) {
                        this.mEapSimSpinner.setSelection(i3);
                        break;
                    } else {
                        i3++;
                    }
                }
            }
            if (!TextUtils.isEmpty(wifiEnterpriseConfig.getCaPath())) {
                setSelection(this.mEapCaCertSpinner, this.mUseSystemCertsString);
            } else {
                String[] caCertificateAliases = wifiEnterpriseConfig.getCaCertificateAliases();
                if (caCertificateAliases == null) {
                    setSelection(this.mEapCaCertSpinner, this.mUnspecifiedCertString);
                } else if (caCertificateAliases.length == 1) {
                    setSelection(this.mEapCaCertSpinner, caCertificateAliases[0]);
                } else {
                    loadCertificates(this.mEapCaCertSpinner, getAndroidKeystoreAliasLoader().getCaCertAliases(), (String) null, true, true);
                    setSelection(this.mEapCaCertSpinner, this.mMultipleCertSetString);
                }
            }
            this.mEapOcspSpinner.setSelection(wifiEnterpriseConfig.getOcsp());
            this.mEapDomainView.setText(wifiEnterpriseConfig.getDomainSuffixMatch());
            String clientCertificateAlias = wifiEnterpriseConfig.getClientCertificateAlias();
            if (TextUtils.isEmpty(clientCertificateAlias)) {
                setSelection(this.mEapUserCertSpinner, this.mDoNotProvideEapUserCertString);
            } else {
                setSelection(this.mEapUserCertSpinner, clientCertificateAlias);
            }
            this.mEapIdentityView.setText(wifiEnterpriseConfig.getIdentity());
            this.mEapAnonymousView.setText(wifiEnterpriseConfig.getAnonymousIdentity());
            return;
        }
        this.mView.findViewById(C0444R.C0448id.eap).setVisibility(8);
    }

    private void setAccessibilityDelegateForSecuritySpinners() {
        C13591 r0 = new View.AccessibilityDelegate() {
            public void sendAccessibilityEvent(View view, int i) {
                if (i != 4) {
                    super.sendAccessibilityEvent(view, i);
                }
            }
        };
        this.mEapMethodSpinner.setAccessibilityDelegate(r0);
        this.mPhase2Spinner.setAccessibilityDelegate(r0);
        this.mEapCaCertSpinner.setAccessibilityDelegate(r0);
        this.mEapOcspSpinner.setAccessibilityDelegate(r0);
        this.mEapUserCertSpinner.setAccessibilityDelegate(r0);
    }

    private void showEapFieldsByMethod(int i) {
        this.mView.findViewById(C0444R.C0448id.l_method).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.l_identity).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.l_domain).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.l_ca_cert).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.l_ocsp).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.password_layout).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.show_password_layout).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.l_sim).setVisibility(0);
        this.mConfigUi.getContext();
        switch (i) {
            case 0:
                ArrayAdapter<CharSequence> arrayAdapter = this.mPhase2Adapter;
                ArrayAdapter<CharSequence> arrayAdapter2 = this.mPhase2PeapAdapter;
                if (arrayAdapter != arrayAdapter2) {
                    this.mPhase2Adapter = arrayAdapter2;
                    this.mPhase2Spinner.setAdapter(arrayAdapter2);
                }
                this.mView.findViewById(C0444R.C0448id.l_phase2).setVisibility(0);
                this.mView.findViewById(C0444R.C0448id.l_anonymous).setVisibility(0);
                showPeapFields();
                setUserCertInvisible();
                break;
            case 1:
                this.mView.findViewById(C0444R.C0448id.l_user_cert).setVisibility(0);
                setPhase2Invisible();
                setAnonymousIdentInvisible();
                setPasswordInvisible();
                this.mView.findViewById(C0444R.C0448id.l_sim).setVisibility(8);
                break;
            case 2:
                ArrayAdapter<CharSequence> arrayAdapter3 = this.mPhase2Adapter;
                ArrayAdapter<CharSequence> arrayAdapter4 = this.mPhase2TtlsAdapter;
                if (arrayAdapter3 != arrayAdapter4) {
                    this.mPhase2Adapter = arrayAdapter4;
                    this.mPhase2Spinner.setAdapter(arrayAdapter4);
                }
                this.mView.findViewById(C0444R.C0448id.l_phase2).setVisibility(0);
                this.mView.findViewById(C0444R.C0448id.l_anonymous).setVisibility(0);
                setUserCertInvisible();
                this.mView.findViewById(C0444R.C0448id.l_sim).setVisibility(8);
                break;
            case 3:
                setPhase2Invisible();
                setCaCertInvisible();
                setOcspInvisible();
                setDomainInvisible();
                setAnonymousIdentInvisible();
                setUserCertInvisible();
                this.mView.findViewById(C0444R.C0448id.l_sim).setVisibility(8);
                break;
            case 4:
            case 5:
            case 6:
                setPhase2Invisible();
                setAnonymousIdentInvisible();
                setCaCertInvisible();
                setOcspInvisible();
                setDomainInvisible();
                setUserCertInvisible();
                setPasswordInvisible();
                setIdentityInvisible();
                break;
        }
        if (this.mView.findViewById(C0444R.C0448id.l_ca_cert).getVisibility() != 8 && ((String) this.mEapCaCertSpinner.getSelectedItem()).equals(this.mUnspecifiedCertString)) {
            setDomainInvisible();
            setOcspInvisible();
        }
    }

    private void showPeapFields() {
        int selectedItemPosition = this.mPhase2Spinner.getSelectedItemPosition();
        if (selectedItemPosition == 2 || selectedItemPosition == 3 || selectedItemPosition == 4) {
            this.mEapIdentityView.setText("");
            this.mView.findViewById(C0444R.C0448id.l_identity).setVisibility(8);
            setPasswordInvisible();
            this.mView.findViewById(C0444R.C0448id.l_sim).setVisibility(0);
            return;
        }
        this.mView.findViewById(C0444R.C0448id.l_identity).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.l_anonymous).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.password_layout).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.show_password_layout).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.l_sim).setVisibility(8);
    }

    private void setIdentityInvisible() {
        this.mView.findViewById(C0444R.C0448id.l_identity).setVisibility(8);
    }

    private void setPhase2Invisible() {
        this.mView.findViewById(C0444R.C0448id.l_phase2).setVisibility(8);
    }

    private void setCaCertInvisible() {
        this.mView.findViewById(C0444R.C0448id.l_ca_cert).setVisibility(8);
        setSelection(this.mEapCaCertSpinner, this.mUnspecifiedCertString);
    }

    private void setOcspInvisible() {
        this.mView.findViewById(C0444R.C0448id.l_ocsp).setVisibility(8);
        this.mEapOcspSpinner.setSelection(0);
    }

    private void setDomainInvisible() {
        this.mView.findViewById(C0444R.C0448id.l_domain).setVisibility(8);
        this.mEapDomainView.setText("");
    }

    private void setUserCertInvisible() {
        this.mView.findViewById(C0444R.C0448id.l_user_cert).setVisibility(8);
        setSelection(this.mEapUserCertSpinner, this.mUnspecifiedCertString);
    }

    private void setAnonymousIdentInvisible() {
        this.mView.findViewById(C0444R.C0448id.l_anonymous).setVisibility(8);
        this.mEapAnonymousView.setText("");
    }

    private void setPasswordInvisible() {
        this.mPasswordView.setText("");
        this.mView.findViewById(C0444R.C0448id.password_layout).setVisibility(8);
        this.mView.findViewById(C0444R.C0448id.show_password_layout).setVisibility(8);
    }

    private void showIpConfigFields() {
        StaticIpConfiguration staticIpConfiguration;
        this.mView.findViewById(C0444R.C0448id.ip_fields).setVisibility(0);
        AccessPoint accessPoint = this.mAccessPoint;
        WifiConfiguration config = (accessPoint == null || !accessPoint.isSaved()) ? null : this.mAccessPoint.getConfig();
        if (this.mIpSettingsSpinner.getSelectedItemPosition() == 1) {
            this.mView.findViewById(C0444R.C0448id.staticip).setVisibility(0);
            if (this.mIpAddressView == null) {
                TextView textView = (TextView) this.mView.findViewById(C0444R.C0448id.ipaddress);
                this.mIpAddressView = textView;
                textView.addTextChangedListener(this);
                TextView textView2 = (TextView) this.mView.findViewById(C0444R.C0448id.gateway);
                this.mGatewayView = textView2;
                textView2.addTextChangedListener(this);
                TextView textView3 = (TextView) this.mView.findViewById(C0444R.C0448id.network_prefix_length);
                this.mNetworkPrefixLengthView = textView3;
                textView3.addTextChangedListener(this);
                TextView textView4 = (TextView) this.mView.findViewById(C0444R.C0448id.dns1);
                this.mDns1View = textView4;
                textView4.addTextChangedListener(this);
                TextView textView5 = (TextView) this.mView.findViewById(C0444R.C0448id.dns2);
                this.mDns2View = textView5;
                textView5.addTextChangedListener(this);
            }
            if (config != null && (staticIpConfiguration = config.getIpConfiguration().getStaticIpConfiguration()) != null) {
                if (staticIpConfiguration.getIpAddress() != null) {
                    this.mIpAddressView.setText(staticIpConfiguration.getIpAddress().getAddress().getHostAddress());
                    this.mNetworkPrefixLengthView.setText(Integer.toString(staticIpConfiguration.getIpAddress().getPrefixLength()));
                }
                if (staticIpConfiguration.getGateway() != null) {
                    this.mGatewayView.setText(staticIpConfiguration.getGateway().getHostAddress());
                }
                Iterator it = staticIpConfiguration.getDnsServers().iterator();
                if (it.hasNext()) {
                    this.mDns1View.setText(((InetAddress) it.next()).getHostAddress());
                }
                if (it.hasNext()) {
                    this.mDns2View.setText(((InetAddress) it.next()).getHostAddress());
                    return;
                }
                return;
            }
            return;
        }
        this.mView.findViewById(C0444R.C0448id.staticip).setVisibility(8);
    }

    private void showProxyFields() {
        ProxyInfo httpProxy;
        ProxyInfo httpProxy2;
        this.mView.findViewById(C0444R.C0448id.proxy_settings_fields).setVisibility(0);
        AccessPoint accessPoint = this.mAccessPoint;
        WifiConfiguration config = (accessPoint == null || !accessPoint.isSaved()) ? null : this.mAccessPoint.getConfig();
        if (this.mProxySettingsSpinner.getSelectedItemPosition() == 1) {
            setVisibility(C0444R.C0448id.proxy_warning_limited_support, 0);
            setVisibility(C0444R.C0448id.proxy_fields, 0);
            setVisibility(C0444R.C0448id.proxy_pac_field, 8);
            if (this.mProxyHostView == null) {
                TextView textView = (TextView) this.mView.findViewById(C0444R.C0448id.proxy_hostname);
                this.mProxyHostView = textView;
                textView.addTextChangedListener(this);
                TextView textView2 = (TextView) this.mView.findViewById(C0444R.C0448id.proxy_port);
                this.mProxyPortView = textView2;
                textView2.addTextChangedListener(this);
                TextView textView3 = (TextView) this.mView.findViewById(C0444R.C0448id.proxy_exclusionlist);
                this.mProxyExclusionListView = textView3;
                textView3.addTextChangedListener(this);
            }
            if (config != null && (httpProxy2 = config.getHttpProxy()) != null) {
                this.mProxyHostView.setText(httpProxy2.getHost());
                this.mProxyPortView.setText(Integer.toString(httpProxy2.getPort()));
                this.mProxyExclusionListView.setText(ProxyUtils.exclusionListAsString(httpProxy2.getExclusionList()));
            }
        } else if (this.mProxySettingsSpinner.getSelectedItemPosition() == 2) {
            setVisibility(C0444R.C0448id.proxy_warning_limited_support, 8);
            setVisibility(C0444R.C0448id.proxy_fields, 8);
            setVisibility(C0444R.C0448id.proxy_pac_field, 0);
            if (this.mProxyPacView == null) {
                TextView textView4 = (TextView) this.mView.findViewById(C0444R.C0448id.proxy_pac);
                this.mProxyPacView = textView4;
                textView4.addTextChangedListener(this);
            }
            if (config != null && (httpProxy = config.getHttpProxy()) != null) {
                this.mProxyPacView.setText(httpProxy.getPacFileUrl().toString());
            }
        } else {
            setVisibility(C0444R.C0448id.proxy_warning_limited_support, 8);
            setVisibility(C0444R.C0448id.proxy_fields, 8);
            setVisibility(C0444R.C0448id.proxy_pac_field, 8);
        }
    }

    private void setVisibility(int i, int i2) {
        View findViewById = this.mView.findViewById(i);
        if (findViewById != null) {
            findViewById.setVisibility(i2);
        }
    }

    /* access modifiers changed from: package-private */
    public AndroidKeystoreAliasLoader getAndroidKeystoreAliasLoader() {
        return new AndroidKeystoreAliasLoader(102);
    }

    /* access modifiers changed from: package-private */
    public void loadSims() {
        List<SubscriptionInfo> activeSubscriptionInfoList = ((SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class)).getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null) {
            activeSubscriptionInfoList = Collections.EMPTY_LIST;
        }
        this.mActiveSubscriptionInfos.clear();
        for (SubscriptionInfo next : activeSubscriptionInfoList) {
            for (SubscriptionInfo carrierId : this.mActiveSubscriptionInfos) {
                next.getCarrierId();
                carrierId.getCarrierId();
            }
            this.mActiveSubscriptionInfos.add(next);
        }
        if (this.mActiveSubscriptionInfos.size() == 0) {
            this.mEapSimSpinner.setAdapter(getSpinnerAdapter(new String[]{this.mContext.getString(C0444R.string.wifi_no_sim_card)}));
            this.mEapSimSpinner.setSelection(0);
            this.mEapSimSpinner.setEnabled(false);
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (SubscriptionInfo uniqueSubscriptionDisplayName : this.mActiveSubscriptionInfos) {
            arrayList.add(SubscriptionUtil.getUniqueSubscriptionDisplayName(uniqueSubscriptionDisplayName, this.mContext));
        }
        this.mEapSimSpinner.setAdapter(getSpinnerAdapter((String[]) arrayList.toArray(new String[arrayList.size()])));
        this.mEapSimSpinner.setSelection(0);
        if (arrayList.size() == 1) {
            this.mEapSimSpinner.setEnabled(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void loadCertificates(Spinner spinner, Collection<String> collection, String str, boolean z, boolean z2) {
        this.mConfigUi.getContext();
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mUnspecifiedCertString);
        if (z) {
            arrayList.add(this.mMultipleCertSetString);
        }
        if (z2) {
            arrayList.add(this.mUseSystemCertsString);
        }
        if (!(collection == null || collection.size() == 0)) {
            arrayList.addAll((Collection) collection.stream().filter(WifiConfigController$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList()));
        }
        if (!TextUtils.isEmpty(str) && this.mAccessPointSecurity != 6) {
            arrayList.add(str);
        }
        if (arrayList.size() == 2) {
            arrayList.remove(this.mUnspecifiedCertString);
            spinner.setEnabled(false);
        } else {
            spinner.setEnabled(true);
        }
        spinner.setAdapter(getSpinnerAdapter((String[]) arrayList.toArray(new String[arrayList.size()])));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$loadCertificates$0(String str) {
        for (String startsWith : UNDESIRED_CERTIFICATES) {
            if (str.startsWith(startsWith)) {
                return false;
            }
        }
        return true;
    }

    private void setSelection(Spinner spinner, String str) {
        if (str != null) {
            ArrayAdapter arrayAdapter = (ArrayAdapter) spinner.getAdapter();
            for (int count = arrayAdapter.getCount() - 1; count >= 0; count--) {
                if (str.equals(arrayAdapter.getItem(count))) {
                    spinner.setSelection(count);
                    return;
                }
            }
        }
    }

    public void afterTextChanged(Editable editable) {
        ThreadUtils.postOnMainThread(new WifiConfigController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$afterTextChanged$1() {
        showWarningMessagesIfAppropriate();
        enableSubmitIfAppropriate();
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (textView != this.mPasswordView || i != 6 || !isSubmittable()) {
            return false;
        }
        this.mConfigUi.dispatchSubmit();
        return true;
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (view != this.mPasswordView || i != 66 || !isSubmittable()) {
            return false;
        }
        this.mConfigUi.dispatchSubmit();
        return true;
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (compoundButton.getId() == C0444R.C0448id.show_password) {
            int selectionEnd = this.mPasswordView.getSelectionEnd();
            this.mPasswordView.setInputType((z ? 144 : 128) | 1);
            if (selectionEnd >= 0) {
                ((EditText) this.mPasswordView).setSelection(selectionEnd);
            }
        } else if (compoundButton.getId() == C0444R.C0448id.wifi_advanced_togglebox) {
            hideSoftKeyboard(this.mView.getWindowToken());
            compoundButton.setVisibility(8);
            this.mView.findViewById(C0444R.C0448id.wifi_advanced_fields).setVisibility(0);
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        int i2 = 8;
        if (adapterView == this.mSecuritySpinner) {
            this.mAccessPointSecurity = this.mSecurityInPosition[i].intValue();
            showSecurityFields(true, true);
            if (WifiDppUtils.isSupportEnrolleeQrCodeScanner(this.mContext, this.mAccessPointSecurity)) {
                this.mSsidScanButton.setVisibility(0);
            } else {
                this.mSsidScanButton.setVisibility(8);
            }
        } else {
            Spinner spinner = this.mEapMethodSpinner;
            if (adapterView == spinner) {
                showSecurityFields(false, true);
            } else if (adapterView == this.mEapCaCertSpinner) {
                showSecurityFields(false, false);
            } else if (adapterView == this.mPhase2Spinner && spinner.getSelectedItemPosition() == 0) {
                showPeapFields();
            } else if (adapterView == this.mProxySettingsSpinner) {
                showProxyFields();
            } else if (adapterView == this.mHiddenSettingsSpinner) {
                TextView textView = this.mHiddenWarningView;
                if (i != 0) {
                    i2 = 0;
                }
                textView.setVisibility(i2);
            } else {
                showIpConfigFields();
            }
        }
        showWarningMessagesIfAppropriate();
        enableSubmitIfAppropriate();
    }

    public void updatePassword() {
        ((TextView) this.mView.findViewById(C0444R.C0448id.password)).setInputType((((CheckBox) this.mView.findViewById(C0444R.C0448id.show_password)).isChecked() ? 144 : 128) | 1);
    }

    public AccessPoint getAccessPoint() {
        return this.mAccessPoint;
    }

    private void configureSecuritySpinner() {
        int i;
        int i2;
        this.mConfigUi.setTitle((int) C0444R.string.wifi_add_network);
        TextView textView = (TextView) this.mView.findViewById(C0444R.C0448id.ssid);
        this.mSsidView = textView;
        textView.addTextChangedListener(this);
        Spinner spinner = (Spinner) this.mView.findViewById(C0444R.C0448id.security);
        this.mSecuritySpinner = spinner;
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.mContext, 17367048, 16908308);
        arrayAdapter.setDropDownViewResource(17367049);
        this.mSecuritySpinner.setAdapter(arrayAdapter);
        arrayAdapter.add(this.mContext.getString(C0444R.string.wifi_security_none));
        this.mSecurityInPosition[0] = 0;
        if (this.mWifiManager.isEnhancedOpenSupported()) {
            arrayAdapter.add(this.mContext.getString(C0444R.string.wifi_security_owe));
            this.mSecurityInPosition[1] = 4;
            i = 2;
        } else {
            i = 1;
        }
        arrayAdapter.add(this.mContext.getString(C0444R.string.wifi_security_wep));
        int i3 = i + 1;
        this.mSecurityInPosition[i] = 1;
        arrayAdapter.add(this.mContext.getString(C0444R.string.wifi_security_wpa_wpa2));
        int i4 = i3 + 1;
        this.mSecurityInPosition[i3] = 2;
        if (this.mWifiManager.isWpa3SaeSupported()) {
            arrayAdapter.add(this.mContext.getString(C0444R.string.wifi_security_sae));
            int i5 = i4 + 1;
            this.mSecurityInPosition[i4] = 5;
            arrayAdapter.add(this.mContext.getString(C0444R.string.wifi_security_eap_wpa_wpa2));
            int i6 = i5 + 1;
            this.mSecurityInPosition[i5] = 3;
            arrayAdapter.add(this.mContext.getString(C0444R.string.wifi_security_eap_wpa3));
            i2 = i6 + 1;
            this.mSecurityInPosition[i6] = 7;
        } else {
            arrayAdapter.add(this.mContext.getString(C0444R.string.wifi_security_eap));
            this.mSecurityInPosition[i4] = 3;
            i2 = i4 + 1;
        }
        if (this.mWifiManager.isWpa3SuiteBSupported()) {
            arrayAdapter.add(this.mContext.getString(C0444R.string.wifi_security_eap_suiteb));
            this.mSecurityInPosition[i2] = 6;
        }
        arrayAdapter.notifyDataSetChanged();
        this.mView.findViewById(C0444R.C0448id.type).setVisibility(0);
        showIpConfigFields();
        showProxyFields();
        this.mView.findViewById(C0444R.C0448id.wifi_advanced_toggle).setVisibility(0);
        this.mView.findViewById(C0444R.C0448id.hidden_settings_field).setVisibility(0);
        ((CheckBox) this.mView.findViewById(C0444R.C0448id.wifi_advanced_togglebox)).setOnCheckedChangeListener(this);
        setAdvancedOptionAccessibilityString();
    }

    /* access modifiers changed from: package-private */
    public CharSequence[] findAndReplaceTargetStrings(CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, CharSequence[] charSequenceArr3) {
        if (charSequenceArr2.length != charSequenceArr3.length) {
            return charSequenceArr;
        }
        CharSequence[] charSequenceArr4 = new CharSequence[charSequenceArr.length];
        for (int i = 0; i < charSequenceArr.length; i++) {
            charSequenceArr4[i] = charSequenceArr[i];
            for (int i2 = 0; i2 < charSequenceArr2.length; i2++) {
                if (TextUtils.equals(charSequenceArr[i], charSequenceArr2[i2])) {
                    charSequenceArr4[i] = charSequenceArr3[i2];
                }
            }
        }
        return charSequenceArr4;
    }

    private ArrayAdapter<CharSequence> getSpinnerAdapter(int i) {
        return getSpinnerAdapter(this.mContext.getResources().getStringArray(i));
    }

    /* access modifiers changed from: package-private */
    public ArrayAdapter<CharSequence> getSpinnerAdapter(String[] strArr) {
        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<>(this.mContext, 17367048, strArr);
        arrayAdapter.setDropDownViewResource(17367049);
        return arrayAdapter;
    }

    private ArrayAdapter<CharSequence> getSpinnerAdapterWithEapMethodsTts(int i) {
        Resources resources = this.mContext.getResources();
        String[] stringArray = resources.getStringArray(i);
        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<>(this.mContext, 17367048, createAccessibleEntries(stringArray, findAndReplaceTargetStrings(stringArray, resources.getStringArray(C0444R.array.wifi_eap_method_target_strings), resources.getStringArray(C0444R.array.wifi_eap_method_tts_strings))));
        arrayAdapter.setDropDownViewResource(17367049);
        return arrayAdapter;
    }

    private SpannableString[] createAccessibleEntries(CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2) {
        SpannableString[] spannableStringArr = new SpannableString[charSequenceArr.length];
        for (int i = 0; i < charSequenceArr.length; i++) {
            spannableStringArr[i] = com.android.settings.Utils.createAccessibleSequence(charSequenceArr[i], charSequenceArr2[i].toString());
        }
        return spannableStringArr;
    }

    private void hideSoftKeyboard(IBinder iBinder) {
        ((InputMethodManager) this.mContext.getSystemService(InputMethodManager.class)).hideSoftInputFromWindow(iBinder, 0);
    }

    private void setAdvancedOptionAccessibilityString() {
        ((CheckBox) this.mView.findViewById(C0444R.C0448id.wifi_advanced_togglebox)).setAccessibilityDelegate(new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setCheckable(false);
                accessibilityNodeInfo.setClassName((CharSequence) null);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, WifiConfigController.this.mContext.getString(C0444R.string.wifi_advanced_toggle_description_collapsed)));
            }
        });
    }
}
