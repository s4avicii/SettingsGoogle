package com.android.settings.enterprise;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import androidx.window.C0444R;
import com.android.settingslib.enterprise.DeviceAdminStringProvider;
import java.util.Objects;

class DeviceAdminStringProviderImpl implements DeviceAdminStringProvider {
    private final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;

    DeviceAdminStringProviderImpl(Context context) {
        Objects.requireNonNull(context);
        Context context2 = context;
        this.mContext = context;
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
    }

    public String getDefaultDisabledByPolicyTitle() {
        return this.mDevicePolicyManager.getString("Settings.DISABLED_BY_IT_ADMIN_TITLE", new DeviceAdminStringProviderImpl$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getDefaultDisabledByPolicyTitle$0() throws Exception {
        return this.mContext.getString(C0444R.string.disabled_by_policy_title);
    }

    public String getDisallowAdjustVolumeTitle() {
        return this.mContext.getString(C0444R.string.disabled_by_policy_title_adjust_volume);
    }

    public String getDisallowOutgoingCallsTitle() {
        return this.mContext.getString(C0444R.string.disabled_by_policy_title_outgoing_calls);
    }

    public String getDisallowSmsTitle() {
        return this.mContext.getString(C0444R.string.disabled_by_policy_title_sms);
    }

    public String getDisableCameraTitle() {
        return this.mContext.getString(C0444R.string.disabled_by_policy_title_camera);
    }

    public String getDisableScreenCaptureTitle() {
        return this.mContext.getString(C0444R.string.disabled_by_policy_title_screen_capture);
    }

    public String getSuspendPackagesTitle() {
        return this.mContext.getString(C0444R.string.disabled_by_policy_title_suspend_packages);
    }

    public String getDefaultDisabledByPolicyContent() {
        return this.mDevicePolicyManager.getString("Settings.CONTACT_YOUR_IT_ADMIN", new DeviceAdminStringProviderImpl$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getDefaultDisabledByPolicyContent$1() throws Exception {
        return this.mContext.getString(C0444R.string.default_admin_support_msg);
    }

    public String getLearnMoreHelpPageUrl() {
        return this.mDevicePolicyManager.getString("Settings.IT_ADMIN_POLICY_DISABLING_INFO_URL", new DeviceAdminStringProviderImpl$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getLearnMoreHelpPageUrl$2() throws Exception {
        return this.mContext.getString(C0444R.string.help_url_action_disabled_by_it_admin);
    }

    public String getDisabledByPolicyTitleForFinancedDevice() {
        return this.mContext.getString(C0444R.string.disabled_by_policy_title_financed_device);
    }

    public String getDisabledBiometricsParentConsentTitle() {
        return this.mContext.getString(C0444R.string.disabled_by_policy_title_biometric_parental_consent);
    }

    public String getDisabledBiometricsParentConsentContent() {
        return this.mContext.getString(C0444R.string.disabled_by_policy_content_biometric_parental_consent);
    }
}
