package com.android.settings.accessibility;

import android.content.DialogInterface;

/* renamed from: com.android.settings.accessibility.AccessibilityShortcutPreferenceFragment$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0582x835f330f implements DialogInterface.OnClickListener {
    public final /* synthetic */ AccessibilityShortcutPreferenceFragment f$0;

    public /* synthetic */ C0582x835f330f(AccessibilityShortcutPreferenceFragment accessibilityShortcutPreferenceFragment) {
        this.f$0 = accessibilityShortcutPreferenceFragment;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.callOnTutorialDialogButtonClicked(dialogInterface, i);
    }
}
