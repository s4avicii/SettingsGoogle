package com.android.settings.security;

public interface SecuritySettingsFeatureProvider {
    String getAlternativeAdvancedSettingsCategoryKey();

    String getAlternativeSecuritySettingsFragmentClassname();

    boolean hasAlternativeSecuritySettingsFragment();
}
