package com.android.settings.accessibility;

import android.view.accessibility.AccessibilityManager;

/* renamed from: com.android.settings.accessibility.AccessibilityShortcutPreferenceFragment$$ExternalSyntheticLambda3 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0584x835f3311 implements AccessibilityManager.TouchExplorationStateChangeListener {
    public final /* synthetic */ AccessibilityShortcutPreferenceFragment f$0;

    public /* synthetic */ C0584x835f3311(AccessibilityShortcutPreferenceFragment accessibilityShortcutPreferenceFragment) {
        this.f$0 = accessibilityShortcutPreferenceFragment;
    }

    public final void onTouchExplorationStateChanged(boolean z) {
        this.f$0.lambda$onCreateView$1(z);
    }
}
