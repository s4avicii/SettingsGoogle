package com.google.android.settings.gestures.columbus;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.SynchronousUserSwitchObserver;
import android.app.UserSwitchObserver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.SubSettings;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.widget.SelectorWithWidgetPreference;
import com.google.android.settings.gestures.columbus.ColumbusRadioButtonPreference;
import java.util.HashMap;
import java.util.Map;

public class ColumbusActionsPreferenceController extends BasePreferenceController implements SelectorWithWidgetPreference.OnClickListener, LifecycleObserver, OnStart, OnStop {
    @VisibleForTesting
    static final int[] ACTION_METRICS = {1743, 1742, 1744, 1745, 1746, 1787, 1756};
    static final ColumbusRadioButtonPreference.ContextualSummaryProvider[] ACTION_SUMMARIES = {null, null, null, null, null, null, ColumbusActionsPreferenceController$$ExternalSyntheticLambda1.INSTANCE};
    @VisibleForTesting
    static final int[] ACTION_TITLE_RES_IDS = {C0444R.string.columbus_setting_action_screenshot_title, C0444R.string.columbus_setting_action_assistant_title, C0444R.string.columbus_setting_action_play_pause_title, C0444R.string.columbus_setting_action_overview_title, C0444R.string.columbus_setting_action_notification_title, C0444R.string.columbus_setting_action_flashlight_title, C0444R.string.columbus_setting_action_launch_title};
    @VisibleForTesting
    static final int[] ACTION_VALUE_RES_IDS = {C0444R.string.columbus_setting_action_screenshot_value, C0444R.string.columbus_setting_action_assistant_value, C0444R.string.columbus_setting_action_play_pause_value, C0444R.string.columbus_setting_action_overview_value, C0444R.string.columbus_setting_action_notification_value, C0444R.string.columbus_setting_action_flashlight_value, C0444R.string.columbus_setting_action_launch_value};
    /* access modifiers changed from: private */
    public static final Uri COLUMBUS_ENABLED_URI = Settings.Secure.getUriFor("columbus_enabled");
    /* access modifiers changed from: private */
    public static final Uri COLUMBUS_LAUNCH_APP_URI = Settings.Secure.getUriFor(SECURE_KEY_COLUMBUS_LAUNCH_APP);
    static final String SECURE_KEY_COLUMBUS_ACTION = "columbus_action";
    static final String SECURE_KEY_COLUMBUS_LAUNCH_APP = "columbus_launch_app";
    private static final String TAG = "ColumbusActionsPreference";
    private static final Map<String, String> VALUE_TO_TITLE_MAP = new HashMap();
    private static String sDefaultAction;
    private final View.OnClickListener[] mActionExtraOnClick;
    private final Map<String, ColumbusRadioButtonPreference> mActionPreferences = new HashMap();
    private final IActivityManager mActivityManager = ActivityManager.getService();
    /* access modifiers changed from: private */
    public final Context mContext;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private PreferenceCategory mPreferenceCategory;
    /* access modifiers changed from: private */
    public SettingObserver mSettingObserver;
    private final UserSwitchObserver mUserSwitchObserver = new SynchronousUserSwitchObserver() {
        public void onUserSwitching(int i) throws RemoteException {
            if (ColumbusActionsPreferenceController.this.mSettingObserver != null) {
                ColumbusActionsPreferenceController.this.mSettingObserver.unregister(ColumbusActionsPreferenceController.this.mContext.getContentResolver());
                ColumbusActionsPreferenceController.this.mSettingObserver.register(ColumbusActionsPreferenceController.this.mContext.getContentResolver());
            }
        }
    };

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

    /* access modifiers changed from: private */
    public static /* synthetic */ CharSequence lambda$static$0(Context context) {
        String stringForUser = Settings.Secure.getStringForUser(context.getContentResolver(), SECURE_KEY_COLUMBUS_LAUNCH_APP, ActivityManager.getCurrentUser());
        if (stringForUser == null || stringForUser.isEmpty()) {
            return context.getString(C0444R.string.columbus_setting_action_launch_summary_no_selection);
        }
        ComponentName unflattenFromString = ComponentName.unflattenFromString(stringForUser);
        if (unflattenFromString == null) {
            return context.getString(C0444R.string.columbus_setting_action_launch_summary_no_selection);
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getApplicationLabel(packageManager.getActivityInfo(unflattenFromString, 0).applicationInfo);
        } catch (PackageManager.NameNotFoundException unused) {
            return context.getString(C0444R.string.columbus_setting_action_launch_summary_not_installed);
        }
    }

    public ColumbusActionsPreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mActionExtraOnClick = new View.OnClickListener[]{null, null, null, null, null, null, new ColumbusActionsPreferenceController$$ExternalSyntheticLambda0(this)};
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClass(this.mContext, SubSettings.class);
        intent.putExtra(":settings:show_fragment", ColumbusGestureLaunchSettingsFragment.class.getName());
        intent.putExtra(":settings:source_metrics", getMetricsCategory());
        this.mContext.startActivity(intent);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
            this.mPreferenceCategory = preferenceCategory;
            if (preferenceCategory != null) {
                this.mSettingObserver = new SettingObserver(this.mPreferenceCategory);
            }
            this.mActionPreferences.clear();
            VALUE_TO_TITLE_MAP.clear();
            int length = ACTION_VALUE_RES_IDS.length;
            for (int i = 0; i < length; i++) {
                String string = this.mContext.getString(ACTION_VALUE_RES_IDS[i]);
                String string2 = this.mContext.getString(ACTION_TITLE_RES_IDS[i]);
                this.mActionPreferences.put(string, makeRadioPreference(string, string2, ACTION_SUMMARIES[i], ACTION_METRICS[i], this.mActionExtraOnClick[i]));
                VALUE_TO_TITLE_MAP.put(string, string2);
                if (i == 0) {
                    sDefaultAction = string;
                }
            }
        }
    }

    private ColumbusRadioButtonPreference makeRadioPreference(String str, String str2, ColumbusRadioButtonPreference.ContextualSummaryProvider contextualSummaryProvider, int i, View.OnClickListener onClickListener) {
        ColumbusRadioButtonPreference columbusRadioButtonPreference = new ColumbusRadioButtonPreference(this.mPreferenceCategory.getContext());
        columbusRadioButtonPreference.setKey(str);
        columbusRadioButtonPreference.setTitle(str2);
        columbusRadioButtonPreference.setContextualSummaryProvider(contextualSummaryProvider);
        columbusRadioButtonPreference.updateSummary(this.mContext);
        columbusRadioButtonPreference.setMetric(i);
        columbusRadioButtonPreference.setOnClickListener(this);
        columbusRadioButtonPreference.setExtraWidgetOnClickListener(onClickListener);
        this.mPreferenceCategory.addPreference(columbusRadioButtonPreference);
        return columbusRadioButtonPreference;
    }

    public int getAvailabilityStatus() {
        return ColumbusPreferenceController.isColumbusSupported(this.mContext) ? 0 : 3;
    }

    public void onRadioButtonClicked(SelectorWithWidgetPreference selectorWithWidgetPreference) {
        String key = selectorWithWidgetPreference.getKey();
        if (!key.equals(Settings.Secure.getStringForUser(this.mContext.getContentResolver(), SECURE_KEY_COLUMBUS_ACTION, ActivityManager.getCurrentUser()))) {
            Settings.Secure.putStringForUser(this.mContext.getContentResolver(), SECURE_KEY_COLUMBUS_ACTION, key, ActivityManager.getCurrentUser());
            updateState(this.mPreferenceCategory);
            if (selectorWithWidgetPreference instanceof ColumbusRadioButtonPreference) {
                this.mMetricsFeatureProvider.action(this.mContext, ((ColumbusRadioButtonPreference) selectorWithWidgetPreference).getMetric(), (Pair<Integer, Object>[]) new Pair[0]);
            }
        }
    }

    public void updateState(Preference preference) {
        if (!this.mActionPreferences.isEmpty()) {
            String stringForUser = Settings.Secure.getStringForUser(this.mContext.getContentResolver(), SECURE_KEY_COLUMBUS_ACTION, ActivityManager.getCurrentUser());
            if (stringForUser == null || !this.mActionPreferences.containsKey(stringForUser)) {
                stringForUser = sDefaultAction;
            }
            boolean isColumbusEnabled = ColumbusPreferenceController.isColumbusEnabled(this.mContext);
            for (ColumbusRadioButtonPreference next : this.mActionPreferences.values()) {
                boolean equals = next.getKey().equals(stringForUser);
                if (next.isChecked() != equals) {
                    next.setChecked(equals);
                }
                next.setEnabled(isColumbusEnabled);
                next.updateSummary(this.mContext);
            }
        }
    }

    public void onStart() {
        try {
            this.mActivityManager.registerUserSwitchObserver(this.mUserSwitchObserver, TAG);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to register user switch observer", e);
        }
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.register(this.mContext.getContentResolver());
            this.mSettingObserver.onChange(false);
        }
    }

    public void onStop() {
        try {
            this.mActivityManager.unregisterUserSwitchObserver(this.mUserSwitchObserver);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed  to unregister user switch observer", e);
        }
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.unregister(this.mContext.getContentResolver());
        }
    }

    static String getColumbusAction(Context context) {
        populateValueToTitleMapIfEmpty(context);
        return VALUE_TO_TITLE_MAP.getOrDefault(Settings.Secure.getStringForUser(context.getContentResolver(), SECURE_KEY_COLUMBUS_ACTION, ActivityManager.getCurrentUser()), sDefaultAction);
    }

    private static void populateValueToTitleMapIfEmpty(Context context) {
        if (VALUE_TO_TITLE_MAP.isEmpty()) {
            int length = ACTION_VALUE_RES_IDS.length;
            for (int i = 0; i < length; i++) {
                String string = context.getString(ACTION_VALUE_RES_IDS[i]);
                VALUE_TO_TITLE_MAP.put(string, context.getString(ACTION_TITLE_RES_IDS[i]));
                if (i == 0) {
                    sDefaultAction = string;
                }
            }
        }
    }

    private class SettingObserver extends ContentObserver {
        private final Preference mPreference;

        SettingObserver(Preference preference) {
            super(new Handler(Looper.myLooper()));
            this.mPreference = preference;
        }

        public void register(ContentResolver contentResolver) {
            contentResolver.registerContentObserver(ColumbusActionsPreferenceController.COLUMBUS_ENABLED_URI, false, this, ActivityManager.getCurrentUser());
            contentResolver.registerContentObserver(ColumbusActionsPreferenceController.COLUMBUS_LAUNCH_APP_URI, false, this, ActivityManager.getCurrentUser());
        }

        public void unregister(ContentResolver contentResolver) {
            contentResolver.unregisterContentObserver(this);
        }

        public void onChange(boolean z) {
            ColumbusActionsPreferenceController.this.updateState(this.mPreference);
        }
    }
}
