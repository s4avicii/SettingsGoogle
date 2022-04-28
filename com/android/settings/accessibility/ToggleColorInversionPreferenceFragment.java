package com.android.settings.accessibility;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.settings.widget.SettingsMainSwitchPreference;
import java.util.ArrayList;

public class ToggleColorInversionPreferenceFragment extends ToggleFeaturePreferenceFragment {
    public int getHelpResource() {
        return C0444R.string.help_url_color_inversion;
    }

    public int getMetricsCategory() {
        return 1817;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.accessibility_color_inversion_settings;
    }

    /* access modifiers changed from: protected */
    public void onPreferenceToggled(String str, boolean z) {
        if (z) {
            showQuickSettingsTooltipIfNeeded(1);
        }
        AccessibilityStatsLogUtils.logAccessibilityServiceEnabled(this.mComponentName, z);
        Settings.Secure.putInt(getContentResolver(), "accessibility_display_inversion_enabled", z ? 1 : 0);
    }

    /* access modifiers changed from: protected */
    public void onRemoveSwitchPreferenceToggleSwitch() {
        super.onRemoveSwitchPreferenceToggleSwitch();
        this.mToggleServiceSwitchPreference.setOnPreferenceClickListener((Preference.OnPreferenceClickListener) null);
    }

    /* access modifiers changed from: protected */
    public void updateToggleServiceTitle(SettingsMainSwitchPreference settingsMainSwitchPreference) {
        settingsMainSwitchPreference.setTitle((int) C0444R.string.accessibility_display_inversion_switch_title);
    }

    /* access modifiers changed from: protected */
    public void updateShortcutTitle(ShortcutPreference shortcutPreference) {
        shortcutPreference.setTitle((int) C0444R.string.accessibility_display_inversion_shortcut_title);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mComponentName = AccessibilityShortcutController.COLOR_INVERSION_COMPONENT_NAME;
        this.mPackageName = getText(C0444R.string.accessibility_display_inversion_preference_title);
        this.mHtmlDescription = getText(C0444R.string.accessibility_display_inversion_preference_subtitle);
        this.mTopIntroTitle = getText(C0444R.string.accessibility_display_inversion_preference_intro_text);
        this.mImageUri = new Uri.Builder().scheme("android.resource").authority(getPrefContext().getPackageName()).appendPath(String.valueOf(C0444R.raw.accessibility_color_inversion_banner)).build();
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        updateFooterPreference();
        return onCreateView;
    }

    /* access modifiers changed from: protected */
    public void registerKeysToObserverCallback(AccessibilitySettingsContentObserver accessibilitySettingsContentObserver) {
        super.registerKeysToObserverCallback(accessibilitySettingsContentObserver);
        ArrayList arrayList = new ArrayList(1);
        arrayList.add("accessibility_display_inversion_enabled");
        accessibilitySettingsContentObserver.registerKeysToObserverCallback(arrayList, new ToggleColorInversionPreferenceFragment$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerKeysToObserverCallback$0(String str) {
        updateSwitchBarToggleSwitch();
    }

    private void updateFooterPreference() {
        String string = getPrefContext().getString(C0444R.string.accessibility_color_inversion_about_title);
        String string2 = getPrefContext().getString(C0444R.string.f54xb513e1d2);
        this.mFooterPreferenceController.setIntroductionTitle(string);
        this.mFooterPreferenceController.setupHelpLink(getHelpResource(), string2);
        this.mFooterPreferenceController.displayPreference(getPreferenceScreen());
    }

    public void onResume() {
        super.onResume();
        updateSwitchBarToggleSwitch();
    }

    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: package-private */
    public int getUserShortcutTypes() {
        return AccessibilityUtil.getUserShortcutTypesFromSettings(getPrefContext(), this.mComponentName);
    }

    /* access modifiers changed from: package-private */
    public ComponentName getTileComponentName() {
        return AccessibilityShortcutController.COLOR_INVERSION_TILE_COMPONENT_NAME;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getTileName() {
        return getText(C0444R.string.accessibility_display_inversion_preference_title);
    }

    /* access modifiers changed from: protected */
    public void updateSwitchBarToggleSwitch() {
        boolean z = false;
        if (Settings.Secure.getInt(getContentResolver(), "accessibility_display_inversion_enabled", 0) == 1) {
            z = true;
        }
        if (this.mToggleServiceSwitchPreference.isChecked() != z) {
            this.mToggleServiceSwitchPreference.setChecked(z);
        }
    }
}
