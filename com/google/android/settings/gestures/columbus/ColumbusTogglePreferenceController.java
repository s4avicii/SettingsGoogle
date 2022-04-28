package com.google.android.settings.gestures.columbus;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.SynchronousUserSwitchObserver;
import android.app.UserSwitchObserver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.window.C0444R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public abstract class ColumbusTogglePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    /* access modifiers changed from: private */
    public static final Uri COLUMBUS_ENABLED_URI = Settings.Secure.getUriFor("columbus_enabled");
    private static final int DISABLED = 0;
    private static final int ENABLED = 1;
    private static final String TAG = "ColumbusTogglePreference";
    private final IActivityManager mActivityManager = ActivityManager.getService();
    /* access modifiers changed from: private */
    public final Context mContext;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    /* access modifiers changed from: private */
    public SettingObserver mSettingObserver;
    private SwitchPreference mSwitchPreference;
    private final UserSwitchObserver mUserSwitchObserver = new SynchronousUserSwitchObserver() {
        public void onUserSwitching(int i) throws RemoteException {
            if (ColumbusTogglePreferenceController.this.mSettingObserver != null) {
                ColumbusTogglePreferenceController.this.mSettingObserver.unregister();
                ColumbusTogglePreferenceController.this.mSettingObserver.register();
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

    public int getSliceHighlightMenuRes() {
        return C0444R.string.menu_key_system;
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

    public ColumbusTogglePreferenceController(Context context, String str, int i) {
        super(context, str);
        this.mContext = context;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        setMetricsCategory(i);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SwitchPreference switchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mSwitchPreference = switchPreference;
        if (switchPreference != null) {
            this.mSettingObserver = new SettingObserver(this.mSwitchPreference);
        }
    }

    public boolean isChecked() {
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), getPreferenceKey(), 0, ActivityManager.getCurrentUser()) != 0) {
            return true;
        }
        return false;
    }

    public boolean setChecked(boolean z) {
        this.mMetricsFeatureProvider.action(this.mContext, getMetricsCategory(), z);
        return Settings.Secure.putIntForUser(this.mContext.getContentResolver(), getPreferenceKey(), z ? 1 : 0, ActivityManager.getCurrentUser());
    }

    public int getAvailabilityStatus() {
        return ColumbusPreferenceController.isColumbusSupported(this.mContext) ? 0 : 3;
    }

    public void onStart() {
        try {
            this.mActivityManager.registerUserSwitchObserver(this.mUserSwitchObserver, TAG);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to register user switch observer", e);
        }
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.register();
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
            settingObserver.unregister();
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        SwitchPreference switchPreference = this.mSwitchPreference;
        if (switchPreference != null) {
            switchPreference.setEnabled(ColumbusPreferenceController.isColumbusEnabled(this.mContext));
        }
    }

    private class SettingObserver extends ContentObserver {
        private final Preference mPreference;

        SettingObserver(Preference preference) {
            super(new Handler(Looper.myLooper()));
            this.mPreference = preference;
        }

        public void register() {
            ColumbusTogglePreferenceController.this.mContext.getContentResolver().registerContentObserver(ColumbusTogglePreferenceController.COLUMBUS_ENABLED_URI, false, this, ActivityManager.getCurrentUser());
        }

        public void unregister() {
            ColumbusTogglePreferenceController.this.mContext.getContentResolver().unregisterContentObserver(this);
        }

        public void onChange(boolean z) {
            ColumbusTogglePreferenceController.this.updateState(this.mPreference);
        }
    }
}
