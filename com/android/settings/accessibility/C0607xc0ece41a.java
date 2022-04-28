package com.android.settings.accessibility;

import com.android.settings.accessibility.AccessibilityServiceWarning;

/* renamed from: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment$$ExternalSyntheticLambda5 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0607xc0ece41a implements AccessibilityServiceWarning.UninstallActionPerformer {
    public final /* synthetic */ ToggleAccessibilityServicePreferenceFragment f$0;

    public /* synthetic */ C0607xc0ece41a(ToggleAccessibilityServicePreferenceFragment toggleAccessibilityServicePreferenceFragment) {
        this.f$0 = toggleAccessibilityServicePreferenceFragment;
    }

    public final void uninstallPackage() {
        this.f$0.onDialogButtonFromUninstallClicked();
    }
}
