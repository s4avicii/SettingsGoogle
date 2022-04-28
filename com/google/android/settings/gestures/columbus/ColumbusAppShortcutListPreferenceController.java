package com.google.android.settings.gestures.columbus;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.SelectorWithWidgetPreference;
import java.util.List;

public class ColumbusAppShortcutListPreferenceController extends BasePreferenceController implements SelectorWithWidgetPreference.OnClickListener {
    static final String COLUMBUS_LAUNCH_APP_SHORTCUT_SECURE_KEY = "columbus_launch_app_shortcut";
    private ComponentName mApplication;
    private int mCurrentUser;
    private final LauncherApps mLauncherApps = ((LauncherApps) this.mContext.getSystemService(LauncherApps.class));
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final String mOpenAppValue;
    private PreferenceCategory mPreferenceCategory;
    private List<ShortcutInfo> mShortcutInfos;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ColumbusAppShortcutListPreferenceController(Context context, String str) {
        super(context, str);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mOpenAppValue = this.mContext.getString(C0444R.string.columbus_setting_action_launch_value);
    }

    public int getAvailabilityStatus() {
        return ColumbusPreferenceController.isColumbusSupported(this.mContext) ? 0 : 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            this.mCurrentUser = ActivityManager.getCurrentUser();
            this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
            createShortcutList();
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        int preferenceCount = this.mPreferenceCategory.getPreferenceCount();
        if (preferenceCount != 0) {
            String stringForUser = Settings.Secure.getStringForUser(this.mContext.getContentResolver(), COLUMBUS_LAUNCH_APP_SHORTCUT_SECURE_KEY, this.mCurrentUser);
            for (int i = 0; i < preferenceCount; i++) {
                Preference preference2 = this.mPreferenceCategory.getPreference(i);
                if (preference2 instanceof ColumbusRadioButtonPreference) {
                    ColumbusRadioButtonPreference columbusRadioButtonPreference = (ColumbusRadioButtonPreference) preference2;
                    columbusRadioButtonPreference.setChecked(TextUtils.equals(stringForUser, columbusRadioButtonPreference.getKey()));
                }
            }
        }
    }

    public void onRadioButtonClicked(SelectorWithWidgetPreference selectorWithWidgetPreference) {
        if (selectorWithWidgetPreference instanceof ColumbusRadioButtonPreference) {
            ColumbusRadioButtonPreference columbusRadioButtonPreference = (ColumbusRadioButtonPreference) selectorWithWidgetPreference;
            Settings.Secure.putStringForUser(this.mContext.getContentResolver(), "columbus_action", this.mOpenAppValue, this.mCurrentUser);
            Settings.Secure.putStringForUser(this.mContext.getContentResolver(), "columbus_launch_app", this.mApplication.flattenToString(), this.mCurrentUser);
            Settings.Secure.putStringForUser(this.mContext.getContentResolver(), COLUMBUS_LAUNCH_APP_SHORTCUT_SECURE_KEY, columbusRadioButtonPreference.getKey(), this.mCurrentUser);
            this.mMetricsFeatureProvider.action(this.mContext, 1760, columbusRadioButtonPreference.getKey());
            updateState(this.mPreferenceCategory);
        }
    }

    public void setApplicationPackageAndShortcuts(ComponentName componentName, List<ShortcutInfo> list) {
        this.mApplication = componentName;
        this.mShortcutInfos = list;
        createShortcutList();
    }

    private void createShortcutList() {
        PreferenceCategory preferenceCategory;
        if (this.mApplication != null && this.mShortcutInfos != null && (preferenceCategory = this.mPreferenceCategory) != null) {
            preferenceCategory.removeAll();
            makeRadioPreference(this.mApplication.flattenToString(), this.mContext.getString(C0444R.string.columbus_setting_action_open_app_title), (Drawable) this.mLauncherApps.getActivityList(this.mApplication.getPackageName(), UserHandle.of(this.mCurrentUser)).stream().filter(new C1807xb35db064(this)).findFirst().map(C1806xb35db063.INSTANCE).orElse((Object) null));
            for (ShortcutInfo next : this.mShortcutInfos) {
                makeRadioPreference(next.getId(), next.getLabel(), this.mLauncherApps.getShortcutIconDrawable(next, DisplayMetrics.DENSITY_DEVICE_STABLE));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createShortcutList$0(LauncherActivityInfo launcherActivityInfo) {
        return launcherActivityInfo.getComponentName().equals(this.mApplication);
    }

    private void makeRadioPreference(String str, CharSequence charSequence, Drawable drawable) {
        ColumbusRadioButtonPreference columbusRadioButtonPreference = new ColumbusRadioButtonPreference(this.mContext);
        columbusRadioButtonPreference.setKey(str);
        columbusRadioButtonPreference.setTitle(charSequence);
        columbusRadioButtonPreference.setIcon(drawable);
        columbusRadioButtonPreference.setOnClickListener(this);
        this.mPreferenceCategory.addPreference(columbusRadioButtonPreference);
    }
}
