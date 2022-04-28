package com.android.settings.accessibility;

import android.content.Intent;
import android.view.View;

/* renamed from: com.android.settings.accessibility.AccessibilityFooterPreferenceController$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0570xaf7e55af implements View.OnClickListener {
    public final /* synthetic */ Intent f$0;

    public /* synthetic */ C0570xaf7e55af(Intent intent) {
        this.f$0 = intent;
    }

    public final void onClick(View view) {
        view.startActivityForResult(this.f$0, 0);
    }
}
