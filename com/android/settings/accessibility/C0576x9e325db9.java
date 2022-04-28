package com.android.settings.accessibility;

import android.view.ViewTreeObserver;
import android.widget.ScrollView;

/* renamed from: com.android.settings.accessibility.AccessibilityScreenSizeForSetupWizardActivity$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0576x9e325db9 implements ViewTreeObserver.OnGlobalLayoutListener {
    public final /* synthetic */ AccessibilityScreenSizeForSetupWizardActivity f$0;
    public final /* synthetic */ ScrollView f$1;

    public /* synthetic */ C0576x9e325db9(AccessibilityScreenSizeForSetupWizardActivity accessibilityScreenSizeForSetupWizardActivity, ScrollView scrollView) {
        this.f$0 = accessibilityScreenSizeForSetupWizardActivity;
        this.f$1 = scrollView;
    }

    public final void onGlobalLayout() {
        this.f$0.lambda$scrollToBottom$2(this.f$1);
    }
}
