package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Switch;
import androidx.window.C0444R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.accessibility.AccessibilityUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ToggleAccessibilityServicePreferenceFragment extends ToggleFeaturePreferenceFragment {
    private boolean mDisabledStateLogged = false;
    private AtomicBoolean mIsDialogShown = new AtomicBoolean(false);
    private BroadcastReceiver mPackageRemovedReceiver;
    private long mStartTimeMillsForLogging = 0;
    private ComponentName mTileComponentName;
    private Dialog mWarningDialog;

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
    }

    public int getMetricsCategory() {
        return FeatureFactory.getFactory(getActivity().getApplicationContext()).getAccessibilityMetricsFeatureProvider().getDownloadedFeatureMetricsCategory((ComponentName) getArguments().getParcelable("component_name"));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null && bundle.containsKey("has_logged")) {
            this.mDisabledStateLogged = bundle.getBoolean("has_logged");
        }
    }

    /* access modifiers changed from: protected */
    public void registerKeysToObserverCallback(AccessibilitySettingsContentObserver accessibilitySettingsContentObserver) {
        super.registerKeysToObserverCallback(accessibilitySettingsContentObserver);
        accessibilitySettingsContentObserver.registerObserverCallback(new C0608xc0ece41b(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerKeysToObserverCallback$0(String str) {
        updateSwitchBarToggleSwitch();
    }

    public void onStart() {
        super.onStart();
        AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
        if (accessibilityServiceInfo == null) {
            getActivity().finishAndRemoveTask();
        } else if (!AccessibilityUtil.isSystemApp(accessibilityServiceInfo)) {
            registerPackageRemoveReceiver();
        }
    }

    public void onResume() {
        super.onResume();
        updateSwitchBarToggleSwitch();
    }

    public void onPause() {
        super.onPause();
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.mStartTimeMillsForLogging > 0) {
            bundle.putBoolean("has_logged", this.mDisabledStateLogged);
        }
        super.onSaveInstanceState(bundle);
    }

    public void onPreferenceToggled(String str, boolean z) {
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
        AccessibilityStatsLogUtils.logAccessibilityServiceEnabled(unflattenFromString, z);
        if (!z) {
            logDisabledState(unflattenFromString.getPackageName());
        }
        AccessibilityUtils.setAccessibilityServiceState(getPrefContext(), unflattenFromString, z);
    }

    /* access modifiers changed from: package-private */
    public AccessibilityServiceInfo getAccessibilityServiceInfo() {
        List<AccessibilityServiceInfo> installedAccessibilityServiceList = AccessibilityManager.getInstance(getPrefContext()).getInstalledAccessibilityServiceList();
        int size = installedAccessibilityServiceList.size();
        for (int i = 0; i < size; i++) {
            AccessibilityServiceInfo accessibilityServiceInfo = installedAccessibilityServiceList.get(i);
            ResolveInfo resolveInfo = accessibilityServiceInfo.getResolveInfo();
            if (this.mComponentName.getPackageName().equals(resolveInfo.serviceInfo.packageName) && this.mComponentName.getClassName().equals(resolveInfo.serviceInfo.name)) {
                return accessibilityServiceInfo;
            }
        }
        return null;
    }

    public Dialog onCreateDialog(int i) {
        AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
        switch (i) {
            case 1002:
                if (accessibilityServiceInfo == null) {
                    return null;
                }
                Dialog createCapabilitiesDialog = AccessibilityServiceWarning.createCapabilitiesDialog(getPrefContext(), accessibilityServiceInfo, new C0606xc0ece419(this), new C0607xc0ece41a(this));
                this.mWarningDialog = createCapabilitiesDialog;
                return createCapabilitiesDialog;
            case 1003:
                if (accessibilityServiceInfo == null) {
                    return null;
                }
                Dialog createCapabilitiesDialog2 = AccessibilityServiceWarning.createCapabilitiesDialog(getPrefContext(), accessibilityServiceInfo, new C0604xc0ece417(this), new C0607xc0ece41a(this));
                this.mWarningDialog = createCapabilitiesDialog2;
                return createCapabilitiesDialog2;
            case 1004:
                if (accessibilityServiceInfo == null) {
                    return null;
                }
                Dialog createCapabilitiesDialog3 = AccessibilityServiceWarning.createCapabilitiesDialog(getPrefContext(), accessibilityServiceInfo, new C0605xc0ece418(this), new C0607xc0ece41a(this));
                this.mWarningDialog = createCapabilitiesDialog3;
                return createCapabilitiesDialog3;
            case 1005:
                if (accessibilityServiceInfo == null) {
                    return null;
                }
                Dialog createDisableDialog = AccessibilityServiceWarning.createDisableDialog(getPrefContext(), accessibilityServiceInfo, new C0602xc0ece415(this));
                this.mWarningDialog = createDisableDialog;
                return createDisableDialog;
            default:
                return super.onCreateDialog(i);
        }
    }

    public int getDialogMetricsCategory(int i) {
        if (i == 1008) {
            return 1810;
        }
        switch (i) {
            case 1002:
            case 1003:
            case 1004:
                return 583;
            case 1005:
                return 584;
            default:
                return super.getDialogMetricsCategory(i);
        }
    }

    /* access modifiers changed from: package-private */
    public int getUserShortcutTypes() {
        return AccessibilityUtil.getUserShortcutTypesFromSettings(getPrefContext(), this.mComponentName);
    }

    /* access modifiers changed from: package-private */
    public ComponentName getTileComponentName() {
        return this.mTileComponentName;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getTileName() {
        ComponentName tileComponentName = getTileComponentName();
        if (tileComponentName == null) {
            return null;
        }
        return loadTileLabel(getPrefContext(), tileComponentName);
    }

    /* access modifiers changed from: protected */
    public void updateSwitchBarToggleSwitch() {
        boolean isAccessibilityServiceEnabled = isAccessibilityServiceEnabled();
        if (this.mToggleServiceSwitchPreference.isChecked() != isAccessibilityServiceEnabled) {
            this.mToggleServiceSwitchPreference.setChecked(isAccessibilityServiceEnabled);
        }
    }

    private boolean isAccessibilityServiceEnabled() {
        return AccessibilityUtils.getEnabledServicesFromSettings(getPrefContext()).contains(this.mComponentName);
    }

    private void registerPackageRemoveReceiver() {
        if (this.mPackageRemovedReceiver == null && getContext() != null) {
            this.mPackageRemovedReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (TextUtils.equals(ToggleAccessibilityServicePreferenceFragment.this.mComponentName.getPackageName(), intent.getData().getSchemeSpecificPart())) {
                        ToggleAccessibilityServicePreferenceFragment.this.getActivity().finishAndRemoveTask();
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            getContext().registerReceiver(this.mPackageRemovedReceiver, intentFilter);
        }
    }

    private void unregisterPackageRemoveReceiver() {
        if (this.mPackageRemovedReceiver != null && getContext() != null) {
            getContext().unregisterReceiver(this.mPackageRemovedReceiver);
            this.mPackageRemovedReceiver = null;
        }
    }

    private boolean isServiceSupportAccessibilityButton() {
        ServiceInfo serviceInfo;
        for (AccessibilityServiceInfo next : ((AccessibilityManager) getPrefContext().getSystemService(AccessibilityManager.class)).getInstalledAccessibilityServiceList()) {
            if ((next.flags & 256) != 0 && (serviceInfo = next.getResolveInfo().serviceInfo) != null && TextUtils.equals(serviceInfo.name, getAccessibilityServiceInfo().getResolveInfo().serviceInfo.name)) {
                return true;
            }
        }
        return false;
    }

    private void handleConfirmServiceEnabled(boolean z) {
        getArguments().putBoolean("checked", z);
        onPreferenceToggled(this.mPreferenceKey, z);
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        if (z != isAccessibilityServiceEnabled()) {
            onPreferenceClick(z);
        }
    }

    public void onToggleClicked(ShortcutPreference shortcutPreference) {
        int retrieveUserShortcutType = PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), this.mComponentName.flattenToString(), 1);
        if (!shortcutPreference.isChecked()) {
            AccessibilityUtil.optOutAllValuesFromSettings(getPrefContext(), retrieveUserShortcutType, this.mComponentName);
        } else if (!this.mToggleServiceSwitchPreference.isChecked()) {
            shortcutPreference.setChecked(false);
            showPopupDialog(1004);
        } else {
            AccessibilityUtil.optInAllValuesToSettings(getPrefContext(), retrieveUserShortcutType, this.mComponentName);
            showPopupDialog(1008);
        }
        this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
    }

    public void onSettingsClicked(ShortcutPreference shortcutPreference) {
        int i = 1;
        if (!(this.mShortcutPreference.isChecked() || this.mToggleServiceSwitchPreference.isChecked())) {
            i = 1003;
        }
        showPopupDialog(i);
    }

    /* access modifiers changed from: protected */
    public void onProcessArguments(Bundle bundle) {
        super.onProcessArguments(bundle);
        String string = bundle.getString("settings_title");
        String string2 = bundle.getString("settings_component_name");
        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2)) {
            Intent component = new Intent("android.intent.action.MAIN").setComponent(ComponentName.unflattenFromString(string2.toString()));
            if (!getPackageManager().queryIntentActivities(component, 0).isEmpty()) {
                this.mSettingsTitle = string;
                this.mSettingsIntent = component;
                setHasOptionsMenu(true);
            }
        }
        this.mComponentName = (ComponentName) bundle.getParcelable("component_name");
        int i = bundle.getInt("animated_image_res");
        if (i > 0) {
            this.mImageUri = new Uri.Builder().scheme("android.resource").authority(this.mComponentName.getPackageName()).appendPath(String.valueOf(i)).build();
        }
        this.mPackageName = getAccessibilityServiceInfo().getResolveInfo().loadLabel(getPackageManager());
        if (bundle.containsKey("tile_service_component_name")) {
            this.mTileComponentName = ComponentName.unflattenFromString(bundle.getString("tile_service_component_name"));
        }
        this.mStartTimeMillsForLogging = bundle.getLong("start_time_to_log_a11y_tool");
    }

    /* access modifiers changed from: private */
    public void onDialogButtonFromDisableToggleClicked(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            handleConfirmServiceEnabled(true);
        } else if (i == -1) {
            handleConfirmServiceEnabled(false);
        } else {
            throw new IllegalArgumentException("Unexpected button identifier");
        }
    }

    /* access modifiers changed from: private */
    public void onDialogButtonFromEnableToggleClicked(View view) {
        int id = view.getId();
        if (id == C0444R.C0448id.permission_enable_allow_button) {
            onAllowButtonFromEnableToggleClicked();
        } else if (id == C0444R.C0448id.permission_enable_deny_button) {
            onDenyButtonFromEnableToggleClicked();
        } else {
            throw new IllegalArgumentException("Unexpected view id");
        }
    }

    /* access modifiers changed from: private */
    public void onDialogButtonFromUninstallClicked() {
        this.mWarningDialog.dismiss();
        Intent createUninstallPackageActivityIntent = createUninstallPackageActivityIntent();
        if (createUninstallPackageActivityIntent != null) {
            startActivity(createUninstallPackageActivityIntent);
        }
    }

    private Intent createUninstallPackageActivityIntent() {
        AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
        if (accessibilityServiceInfo == null) {
            Log.w("ToggleAccessibilityServicePreferenceFragment", "createUnInstallIntent -- invalid a11yServiceInfo");
            return null;
        }
        ApplicationInfo applicationInfo = accessibilityServiceInfo.getResolveInfo().serviceInfo.applicationInfo;
        return new Intent("android.intent.action.UNINSTALL_PACKAGE", Uri.parse("package:" + applicationInfo.packageName));
    }

    public void onStop() {
        super.onStop();
        unregisterPackageRemoveReceiver();
    }

    private void onAllowButtonFromEnableToggleClicked() {
        handleConfirmServiceEnabled(true);
        if (isServiceSupportAccessibilityButton()) {
            this.mIsDialogShown.set(false);
            showPopupDialog(1008);
        }
        this.mWarningDialog.dismiss();
    }

    private void onDenyButtonFromEnableToggleClicked() {
        handleConfirmServiceEnabled(false);
        this.mWarningDialog.dismiss();
    }

    /* access modifiers changed from: package-private */
    public void onDialogButtonFromShortcutToggleClicked(View view) {
        int id = view.getId();
        if (id == C0444R.C0448id.permission_enable_allow_button) {
            onAllowButtonFromShortcutToggleClicked();
        } else if (id == C0444R.C0448id.permission_enable_deny_button) {
            onDenyButtonFromShortcutToggleClicked();
        } else {
            throw new IllegalArgumentException("Unexpected view id");
        }
    }

    private void onAllowButtonFromShortcutToggleClicked() {
        this.mShortcutPreference.setChecked(true);
        AccessibilityUtil.optInAllValuesToSettings(getPrefContext(), PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), this.mComponentName.flattenToString(), 1), this.mComponentName);
        this.mIsDialogShown.set(false);
        showPopupDialog(1008);
        this.mWarningDialog.dismiss();
        this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
    }

    private void onDenyButtonFromShortcutToggleClicked() {
        this.mShortcutPreference.setChecked(false);
        this.mWarningDialog.dismiss();
    }

    /* access modifiers changed from: package-private */
    public void onDialogButtonFromShortcutClicked(View view) {
        int id = view.getId();
        if (id == C0444R.C0448id.permission_enable_allow_button) {
            onAllowButtonFromShortcutClicked();
        } else if (id == C0444R.C0448id.permission_enable_deny_button) {
            onDenyButtonFromShortcutClicked();
        } else {
            throw new IllegalArgumentException("Unexpected view id");
        }
    }

    private void onAllowButtonFromShortcutClicked() {
        this.mIsDialogShown.set(false);
        showPopupDialog(1);
        this.mWarningDialog.dismiss();
    }

    private void onDenyButtonFromShortcutClicked() {
        this.mWarningDialog.dismiss();
    }

    private boolean onPreferenceClick(boolean z) {
        if (z) {
            this.mToggleServiceSwitchPreference.setChecked(false);
            getArguments().putBoolean("checked", false);
            if (!this.mShortcutPreference.isChecked()) {
                showPopupDialog(1002);
            } else {
                handleConfirmServiceEnabled(true);
                if (isServiceSupportAccessibilityButton()) {
                    showPopupDialog(1008);
                }
            }
        } else {
            this.mToggleServiceSwitchPreference.setChecked(true);
            getArguments().putBoolean("checked", true);
            showDialog(1005);
        }
        return true;
    }

    private void showPopupDialog(int i) {
        if (this.mIsDialogShown.compareAndSet(false, true)) {
            showDialog(i);
            setOnDismissListener(new C0603xc0ece416(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showPopupDialog$1(DialogInterface dialogInterface) {
        this.mIsDialogShown.compareAndSet(true, false);
    }

    private void logDisabledState(String str) {
        if (this.mStartTimeMillsForLogging > 0 && !this.mDisabledStateLogged) {
            AccessibilityStatsLogUtils.logDisableNonA11yCategoryService(str, SystemClock.elapsedRealtime() - this.mStartTimeMillsForLogging);
            this.mDisabledStateLogged = true;
        }
    }
}
