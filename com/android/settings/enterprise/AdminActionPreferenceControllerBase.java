package com.android.settings.enterprise;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.text.format.DateUtils;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.Date;

public abstract class AdminActionPreferenceControllerBase extends AbstractPreferenceController implements PreferenceControllerMixin {
    protected final EnterprisePrivacyFeatureProvider mFeatureProvider;

    /* access modifiers changed from: protected */
    public abstract Date getAdminActionTimestamp();

    public boolean isAvailable() {
        return true;
    }

    public AdminActionPreferenceControllerBase(Context context) {
        super(context);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getEnterprisePrivacyFeatureProvider(context);
    }

    public void updateState(Preference preference) {
        String str;
        Date adminActionTimestamp = getAdminActionTimestamp();
        if (adminActionTimestamp == null) {
            str = getEnterprisePrivacyNone();
        } else {
            str = DateUtils.formatDateTime(this.mContext, adminActionTimestamp.getTime(), 17);
        }
        preference.setSummary((CharSequence) str);
    }

    private String getEnterprisePrivacyNone() {
        return ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)).getString("Settings.ADMIN_ACTION_NONE", new AdminActionPreferenceControllerBase$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getEnterprisePrivacyNone$0() throws Exception {
        return this.mContext.getString(C0444R.string.enterprise_privacy_none);
    }
}
