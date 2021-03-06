package com.android.settings.notification.zen;

import android.app.Activity;
import android.app.AutomaticZenRule;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.service.notification.ZenModeConfig;
import android.util.Slog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.window.C0444R;
import com.android.settings.widget.EntityHeaderController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.LayoutPreference;

public class ZenAutomaticRuleHeaderPreferenceController extends AbstractZenModePreferenceController {
    private final String KEY = "pref_app_header";
    private EntityHeaderController mController;
    private final PreferenceFragmentCompat mFragment;
    private String mId;
    private AutomaticZenRule mRule;

    public String getPreferenceKey() {
        return "pref_app_header";
    }

    public ZenAutomaticRuleHeaderPreferenceController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, Lifecycle lifecycle) {
        super(context, "pref_app_header", lifecycle);
        this.mFragment = preferenceFragmentCompat;
    }

    public boolean isAvailable() {
        return this.mRule != null;
    }

    public void updateState(Preference preference) {
        PreferenceFragmentCompat preferenceFragmentCompat;
        if (this.mRule != null && (preferenceFragmentCompat = this.mFragment) != null) {
            if (this.mController == null) {
                this.mController = EntityHeaderController.newInstance(preferenceFragmentCompat.getActivity(), this.mFragment, ((LayoutPreference) preference).findViewById(C0444R.C0448id.entity_header));
            }
            this.mController.setIcon(getIcon()).setLabel((CharSequence) this.mRule.getName()).done((Activity) this.mFragment.getActivity(), false);
        }
    }

    private Drawable getIcon() {
        try {
            PackageManager packageManager = this.mContext.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.mRule.getOwner().getPackageName(), 0);
            if (applicationInfo.isSystemApp()) {
                if (ZenModeConfig.isValidScheduleConditionId(this.mRule.getConditionId())) {
                    return this.mContext.getDrawable(C0444R.C0447drawable.ic_timelapse);
                }
                if (ZenModeConfig.isValidEventConditionId(this.mRule.getConditionId())) {
                    return this.mContext.getDrawable(C0444R.C0447drawable.ic_event);
                }
            }
            return applicationInfo.loadIcon(packageManager);
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.w("PrefControllerMixin", "Unable to load icon - PackageManager.NameNotFoundException");
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onResume(AutomaticZenRule automaticZenRule, String str) {
        this.mRule = automaticZenRule;
        this.mId = str;
    }
}
