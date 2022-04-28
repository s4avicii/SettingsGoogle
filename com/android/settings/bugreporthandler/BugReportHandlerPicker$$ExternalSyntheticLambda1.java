package com.android.settings.bugreporthandler;

import android.content.Context;
import androidx.window.C0444R;
import java.util.concurrent.Callable;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BugReportHandlerPicker$$ExternalSyntheticLambda1 implements Callable {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ BugReportHandlerPicker$$ExternalSyntheticLambda1(Context context) {
        this.f$0 = context;
    }

    public final Object call() {
        return this.f$0.getString(C0444R.string.work_profile_app_subtext);
    }
}
