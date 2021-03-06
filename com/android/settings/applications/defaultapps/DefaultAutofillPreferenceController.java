package com.android.settings.applications.defaultapps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.autofill.AutofillManager;
import com.android.settings.applications.defaultapps.DefaultAutofillPicker;
import com.android.settingslib.applications.DefaultAppInfo;

public class DefaultAutofillPreferenceController extends DefaultAppPreferenceController {
    private final AutofillManager mAutofillManager = ((AutofillManager) this.mContext.getSystemService(AutofillManager.class));

    public String getPreferenceKey() {
        return "default_autofill_main";
    }

    /* access modifiers changed from: protected */
    public boolean showLabelAsTitle() {
        return true;
    }

    public DefaultAutofillPreferenceController(Context context) {
        super(context);
    }

    public boolean isAvailable() {
        AutofillManager autofillManager = this.mAutofillManager;
        return autofillManager != null && autofillManager.hasAutofillFeature() && this.mAutofillManager.isAutofillSupported();
    }

    /* access modifiers changed from: protected */
    public Intent getSettingIntent(DefaultAppInfo defaultAppInfo) {
        if (defaultAppInfo == null) {
            return null;
        }
        return new DefaultAutofillPicker.AutofillSettingIntentProvider(this.mContext, this.mUserId, defaultAppInfo.getKey()).getIntent();
    }

    /* access modifiers changed from: protected */
    public DefaultAppInfo getDefaultAppInfo() {
        String string = Settings.Secure.getString(this.mContext.getContentResolver(), "autofill_service");
        if (!TextUtils.isEmpty(string)) {
            return new DefaultAppInfo(this.mContext, this.mPackageManager, this.mUserId, ComponentName.unflattenFromString(string));
        }
        return null;
    }
}
