package com.google.android.settings.security;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.widget.BannerMessagePreference;

public class SecurityWarningPreference extends BannerMessagePreference {
    private SecurityContentManager mSecurityContentManager;

    public SecurityWarningPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSecurityContentManager = SecurityContentManager.getInstance(context);
    }

    public void setSecurityWarning(SecurityWarning securityWarning, SettingsPreferenceFragment settingsPreferenceFragment) {
        setTitle((CharSequence) securityWarning.getTitle());
        setSubtitle(securityWarning.getSubtitle());
        setSummary((CharSequence) securityWarning.getSummary());
        Bundle primaryButtonClickBundle = securityWarning.getPrimaryButtonClickBundle();
        if (primaryButtonClickBundle != null) {
            setPositiveButtonOnClickListener(new SecurityWarningPreference$$ExternalSyntheticLambda3(this, primaryButtonClickBundle, settingsPreferenceFragment));
            setPositiveButtonText(securityWarning.getPrimaryButtonText());
        }
        Bundle secondaryButtonClickBundle = securityWarning.getSecondaryButtonClickBundle();
        if (secondaryButtonClickBundle != null) {
            setNegativeButtonOnClickListener(new SecurityWarningPreference$$ExternalSyntheticLambda2(this, secondaryButtonClickBundle, settingsPreferenceFragment));
            setNegativeButtonText(securityWarning.getSecondaryButtonText());
        }
        Bundle dismissButtonClickBundle = securityWarning.getDismissButtonClickBundle();
        if (dismissButtonClickBundle != null) {
            if (securityWarning.showConfirmationDialogOnDismiss()) {
                setDismissButtonOnClickListener(new SecurityWarningPreference$$ExternalSyntheticLambda0(dismissButtonClickBundle, settingsPreferenceFragment));
            } else {
                setDismissButtonOnClickListener(new SecurityWarningPreference$$ExternalSyntheticLambda1(this, dismissButtonClickBundle, settingsPreferenceFragment));
            }
        }
        SecurityLevel securityLevel = securityWarning.getSecurityLevel();
        if (securityLevel != null) {
            setIcon(getContext().getResources().getDrawable(securityLevel.getWarningCardIconResId(), getContext().getTheme()));
            setAttentionLevel(securityLevel.getAttentionLevel());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setSecurityWarning$0(Bundle bundle, SettingsPreferenceFragment settingsPreferenceFragment, View view) {
        this.mSecurityContentManager.handleClick(bundle, settingsPreferenceFragment.getActivity());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setSecurityWarning$1(Bundle bundle, SettingsPreferenceFragment settingsPreferenceFragment, View view) {
        this.mSecurityContentManager.handleClick(bundle, settingsPreferenceFragment.getActivity());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setSecurityWarning$3(Bundle bundle, SettingsPreferenceFragment settingsPreferenceFragment, View view) {
        this.mSecurityContentManager.handleClick(bundle, settingsPreferenceFragment.getActivity());
    }
}
