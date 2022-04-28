package com.google.android.settings.network;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settingslib.HelpUtils;
import com.android.settingslib.widget.FooterPreference;

public class ConnectivityHelperCallQualityPreferenceController extends ConnectivityHelperBasePreferenceController {
    private static final String KEY_PREFERENCE_CATEGORY = "connectivity_helper_call_quality_category";
    private static final String KEY_PREFERENCE_FOOTER = "connectivity_helper_footer";
    private static final String LOG_TAG = "ch_callQuality";

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getKeyName() {
        return "on_device_notifications";
    }

    public String getLogTag() {
        return LOG_TAG;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ConnectivityHelperCallQualityPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ((PreferenceCategory) preferenceScreen.findPreference(KEY_PREFERENCE_CATEGORY)).setVisible(isAvailable());
        FooterPreference footerPreference = (FooterPreference) preferenceScreen.findPreference(KEY_PREFERENCE_FOOTER);
        footerPreference.setVisible(isAvailable());
        if (!TextUtils.isEmpty(this.mContext.getString(C0444R.string.help_url_connectivity_helper))) {
            footerPreference.setLearnMoreAction(new C1823xaf37df22(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(View view) {
        Context context = this.mContext;
        ((Activity) this.mContext).startActivityForResult(HelpUtils.getHelpIntent(context, context.getString(C0444R.string.help_url_connectivity_helper), getClass().getName()), 0);
    }

    public boolean getDefaultValue() {
        return this.mContext.getResources().getBoolean(C0444R.bool.config_connectivity_helper_call_quality_default_value);
    }

    public boolean getDeviceSupport() {
        return this.mContext.getResources().getBoolean(C0444R.bool.config_show_connectivity_helper_call_quality);
    }
}
