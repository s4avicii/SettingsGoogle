package com.android.settings.accessibility;

import android.content.ComponentName;
import android.content.Context;
import android.hardware.display.ColorDisplayManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreference;
import androidx.window.C0444R;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.SeekBarPreference;
import com.android.settings.widget.SettingsMainSwitchPreference;
import java.util.ArrayList;

public class ToggleReduceBrightColorsPreferenceFragment extends ToggleFeaturePreferenceFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.reduce_bright_colors_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return ColorDisplayManager.isReduceBrightColorsAvailable(context);
        }
    };
    private ColorDisplayManager mColorDisplayManager;
    private ReduceBrightColorsIntensityPreferenceController mRbcIntensityPreferenceController;
    private ReduceBrightColorsPersistencePreferenceController mRbcPersistencePreferenceController;

    public int getHelpResource() {
        return 0;
    }

    public int getMetricsCategory() {
        return 1853;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.reduce_bright_colors_settings;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mImageUri = new Uri.Builder().scheme("android.resource").authority(getPrefContext().getPackageName()).appendPath(String.valueOf(C0444R.raw.extra_dim_banner)).build();
        this.mComponentName = AccessibilityShortcutController.REDUCE_BRIGHT_COLORS_COMPONENT_NAME;
        this.mPackageName = getText(C0444R.string.reduce_bright_colors_preference_title);
        this.mHtmlDescription = getText(C0444R.string.reduce_bright_colors_preference_subtitle);
        this.mTopIntroTitle = getText(C0444R.string.reduce_bright_colors_preference_intro_text);
        this.mRbcIntensityPreferenceController = new ReduceBrightColorsIntensityPreferenceController(getContext(), "rbc_intensity");
        this.mRbcPersistencePreferenceController = new ReduceBrightColorsPersistencePreferenceController(getContext(), "rbc_persist");
        this.mRbcIntensityPreferenceController.displayPreference(getPreferenceScreen());
        this.mRbcPersistencePreferenceController.displayPreference(getPreferenceScreen());
        this.mColorDisplayManager = (ColorDisplayManager) getContext().getSystemService(ColorDisplayManager.class);
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        this.mToggleServiceSwitchPreference.setTitle((int) C0444R.string.reduce_bright_colors_switch_title);
        updateGeneralCategoryOrder();
        updateFooterPreference();
        return onCreateView;
    }

    /* access modifiers changed from: protected */
    public void registerKeysToObserverCallback(AccessibilitySettingsContentObserver accessibilitySettingsContentObserver) {
        super.registerKeysToObserverCallback(accessibilitySettingsContentObserver);
        ArrayList arrayList = new ArrayList(1);
        arrayList.add("reduce_bright_colors_activated");
        accessibilitySettingsContentObserver.registerKeysToObserverCallback(arrayList, new C0612x73e77b9e(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerKeysToObserverCallback$0(String str) {
        updateSwitchBarToggleSwitch();
    }

    private void updateGeneralCategoryOrder() {
        PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("general_categories");
        SeekBarPreference seekBarPreference = (SeekBarPreference) findPreference("rbc_intensity");
        getPreferenceScreen().removePreference(seekBarPreference);
        seekBarPreference.setOrder(this.mShortcutPreference.getOrder() - 2);
        preferenceCategory.addPreference(seekBarPreference);
        SwitchPreference switchPreference = (SwitchPreference) findPreference("rbc_persist");
        getPreferenceScreen().removePreference(switchPreference);
        switchPreference.setOrder(this.mShortcutPreference.getOrder() - 1);
        preferenceCategory.addPreference(switchPreference);
    }

    private void updateFooterPreference() {
        this.mFooterPreferenceController.setIntroductionTitle(getPrefContext().getString(C0444R.string.reduce_bright_colors_about_title));
        this.mFooterPreferenceController.displayPreference(getPreferenceScreen());
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onResume() {
        super.onResume();
        updateSwitchBarToggleSwitch();
    }

    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onPreferenceToggled(String str, boolean z) {
        if (z) {
            showQuickSettingsTooltipIfNeeded(1);
        }
        AccessibilityStatsLogUtils.logAccessibilityServiceEnabled(this.mComponentName, z);
        this.mColorDisplayManager.setReduceBrightColorsActivated(z);
    }

    /* access modifiers changed from: protected */
    public void onRemoveSwitchPreferenceToggleSwitch() {
        super.onRemoveSwitchPreferenceToggleSwitch();
        this.mToggleServiceSwitchPreference.setOnPreferenceClickListener((Preference.OnPreferenceClickListener) null);
    }

    /* access modifiers changed from: protected */
    public void updateToggleServiceTitle(SettingsMainSwitchPreference settingsMainSwitchPreference) {
        settingsMainSwitchPreference.setTitle((int) C0444R.string.reduce_bright_colors_preference_title);
    }

    /* access modifiers changed from: protected */
    public void updateShortcutTitle(ShortcutPreference shortcutPreference) {
        shortcutPreference.setTitle((int) C0444R.string.reduce_bright_colors_shortcut_title);
    }

    /* access modifiers changed from: package-private */
    public int getUserShortcutTypes() {
        return AccessibilityUtil.getUserShortcutTypesFromSettings(getPrefContext(), this.mComponentName);
    }

    /* access modifiers changed from: package-private */
    public ComponentName getTileComponentName() {
        return AccessibilityShortcutController.REDUCE_BRIGHT_COLORS_TILE_SERVICE_COMPONENT_NAME;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getTileName() {
        return getText(C0444R.string.reduce_bright_colors_preference_title);
    }

    /* access modifiers changed from: protected */
    public void updateSwitchBarToggleSwitch() {
        boolean isReduceBrightColorsActivated = this.mColorDisplayManager.isReduceBrightColorsActivated();
        this.mRbcIntensityPreferenceController.updateState(getPreferenceScreen().findPreference("rbc_intensity"));
        this.mRbcPersistencePreferenceController.updateState(getPreferenceScreen().findPreference("rbc_persist"));
        if (this.mToggleServiceSwitchPreference.isChecked() != isReduceBrightColorsActivated) {
            this.mToggleServiceSwitchPreference.setChecked(isReduceBrightColorsActivated);
        }
    }
}
