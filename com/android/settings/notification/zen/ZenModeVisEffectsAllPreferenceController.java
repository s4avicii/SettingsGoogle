package com.android.settings.notification.zen;

import android.app.NotificationManager;
import android.content.Context;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.SelectorWithWidgetPreference;

public class ZenModeVisEffectsAllPreferenceController extends AbstractZenModePreferenceController implements SelectorWithWidgetPreference.OnClickListener {
    private SelectorWithWidgetPreference mPreference;

    public boolean isAvailable() {
        return true;
    }

    public ZenModeVisEffectsAllPreferenceController(Context context, Lifecycle lifecycle, String str) {
        super(context, str, lifecycle);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SelectorWithWidgetPreference selectorWithWidgetPreference = (SelectorWithWidgetPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = selectorWithWidgetPreference;
        selectorWithWidgetPreference.setOnClickListener(this);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        this.mPreference.setChecked(NotificationManager.Policy.areAllVisualEffectsSuppressed(this.mBackend.mPolicy.suppressedVisualEffects));
    }

    public void onRadioButtonClicked(SelectorWithWidgetPreference selectorWithWidgetPreference) {
        this.mMetricsFeatureProvider.action(this.mContext, 1397, true);
        this.mBackend.saveVisualEffectsPolicy(511, true);
    }
}
