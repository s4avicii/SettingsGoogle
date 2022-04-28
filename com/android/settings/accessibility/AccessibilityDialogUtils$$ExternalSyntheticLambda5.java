package com.android.settings.accessibility;

import android.util.Log;
import com.airbnb.lottie.LottieListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AccessibilityDialogUtils$$ExternalSyntheticLambda5 implements LottieListener {
    public final /* synthetic */ int f$0;

    public /* synthetic */ AccessibilityDialogUtils$$ExternalSyntheticLambda5(int i) {
        this.f$0 = i;
    }

    public final void onResult(Object obj) {
        Log.w("AccessibilityDialogUtils", "Invalid image raw resource id: " + this.f$0, (Throwable) obj);
    }
}
