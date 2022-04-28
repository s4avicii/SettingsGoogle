package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.view.accessibility.CaptioningManager;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import java.util.ArrayList;
import java.util.List;

public class CaptionPropertiesFragment extends DashboardFragment implements Preference.OnPreferenceChangeListener, OnMainSwitchChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.captioning_settings);
    private CaptioningManager mCaptioningManager;
    private Preference mMoreOptions;
    private final List<Preference> mPreferenceList = new ArrayList();
    private SettingsMainSwitchPreference mSwitch;
    private Preference mTextAppearance;

    public int getHelpResource() {
        return C0444R.string.help_url_caption;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "CaptionPropertiesFragment";
    }

    public int getMetricsCategory() {
        return 3;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.captioning_settings;
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        this.mCaptioningManager = (CaptioningManager) getSystemService("captioning");
        initializeAllPreferences();
        installUpdateListeners();
    }

    public void onResume() {
        super.onResume();
        this.mSwitch.setChecked(this.mCaptioningManager.isEnabled());
    }

    private void initializeAllPreferences() {
        this.mSwitch = (SettingsMainSwitchPreference) findPreference("captioning_preference_switch");
        this.mTextAppearance = findPreference("captioning_caption_appearance");
        this.mMoreOptions = findPreference("captioning_more_options");
        this.mPreferenceList.add(this.mTextAppearance);
        this.mPreferenceList.add(this.mMoreOptions);
    }

    private void installUpdateListeners() {
        this.mSwitch.setOnPreferenceChangeListener(this);
        this.mSwitch.addOnSwitchChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        if (this.mSwitch != preference) {
            return true;
        }
        Settings.Secure.putInt(contentResolver, "accessibility_captioning_enabled", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        Settings.Secure.putInt(getActivity().getContentResolver(), "accessibility_captioning_enabled", z ? 1 : 0);
    }
}
