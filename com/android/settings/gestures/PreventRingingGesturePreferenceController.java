package com.android.settings.gestures;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.widget.SelectorWithWidgetPreference;

public class PreventRingingGesturePreferenceController extends AbstractPreferenceController implements SelectorWithWidgetPreference.OnClickListener, LifecycleObserver, OnResume, OnPause, PreferenceControllerMixin {
    @VisibleForTesting
    static final String KEY_MUTE = "prevent_ringing_option_mute";
    @VisibleForTesting
    static final String KEY_VIBRATE = "prevent_ringing_option_vibrate";
    private final String KEY = "gesture_prevent_ringing_category";
    private final String PREF_KEY_VIDEO = "gesture_prevent_ringing_video";
    private final Context mContext;
    @VisibleForTesting
    SelectorWithWidgetPreference mMutePref;
    @VisibleForTesting
    PreferenceCategory mPreferenceCategory;
    private SettingObserver mSettingObserver;
    @VisibleForTesting
    SelectorWithWidgetPreference mVibratePref;

    public String getPreferenceKey() {
        return "gesture_prevent_ringing_category";
    }

    public PreventRingingGesturePreferenceController(Context context, Lifecycle lifecycle) {
        super(context);
        this.mContext = context;
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
            this.mVibratePref = makeRadioPreference(KEY_VIBRATE, C0444R.string.prevent_ringing_option_vibrate);
            this.mMutePref = makeRadioPreference(KEY_MUTE, C0444R.string.prevent_ringing_option_mute);
            if (this.mPreferenceCategory != null) {
                this.mSettingObserver = new SettingObserver(this.mPreferenceCategory);
            }
        }
    }

    public boolean isAvailable() {
        return this.mContext.getResources().getBoolean(17891814);
    }

    public void onRadioButtonClicked(SelectorWithWidgetPreference selectorWithWidgetPreference) {
        int keyToSetting = keyToSetting(selectorWithWidgetPreference.getKey());
        if (keyToSetting != Settings.Secure.getInt(this.mContext.getContentResolver(), "volume_hush_gesture", 1)) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), "volume_hush_gesture", keyToSetting);
        }
    }

    public void updateState(Preference preference) {
        int i = Settings.Secure.getInt(this.mContext.getContentResolver(), "volume_hush_gesture", 1);
        boolean z = i == 1;
        boolean z2 = i == 2;
        SelectorWithWidgetPreference selectorWithWidgetPreference = this.mVibratePref;
        if (!(selectorWithWidgetPreference == null || selectorWithWidgetPreference.isChecked() == z)) {
            this.mVibratePref.setChecked(z);
        }
        SelectorWithWidgetPreference selectorWithWidgetPreference2 = this.mMutePref;
        if (!(selectorWithWidgetPreference2 == null || selectorWithWidgetPreference2.isChecked() == z2)) {
            this.mMutePref.setChecked(z2);
        }
        if (i == 0) {
            this.mVibratePref.setEnabled(false);
            this.mMutePref.setEnabled(false);
            return;
        }
        this.mVibratePref.setEnabled(true);
        this.mMutePref.setEnabled(true);
    }

    public void onResume() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.register(this.mContext.getContentResolver());
            this.mSettingObserver.onChange(false, (Uri) null);
        }
    }

    public void onPause() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.unregister(this.mContext.getContentResolver());
        }
    }

    private int keyToSetting(String str) {
        str.hashCode();
        if (!str.equals(KEY_MUTE)) {
            return !str.equals(KEY_VIBRATE) ? 0 : 1;
        }
        return 2;
    }

    private SelectorWithWidgetPreference makeRadioPreference(String str, int i) {
        SelectorWithWidgetPreference selectorWithWidgetPreference = new SelectorWithWidgetPreference(this.mPreferenceCategory.getContext());
        selectorWithWidgetPreference.setKey(str);
        selectorWithWidgetPreference.setTitle(i);
        selectorWithWidgetPreference.setOnClickListener(this);
        this.mPreferenceCategory.addPreference(selectorWithWidgetPreference);
        return selectorWithWidgetPreference;
    }

    private class SettingObserver extends ContentObserver {
        private final Uri VOLUME_HUSH_GESTURE = Settings.Secure.getUriFor("volume_hush_gesture");
        private final Preference mPreference;

        public SettingObserver(Preference preference) {
            super(new Handler());
            this.mPreference = preference;
        }

        public void register(ContentResolver contentResolver) {
            contentResolver.registerContentObserver(this.VOLUME_HUSH_GESTURE, false, this);
        }

        public void unregister(ContentResolver contentResolver) {
            contentResolver.unregisterContentObserver(this);
        }

        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            if (uri == null || this.VOLUME_HUSH_GESTURE.equals(uri)) {
                PreventRingingGesturePreferenceController.this.updateState(this.mPreference);
            }
        }
    }
}
