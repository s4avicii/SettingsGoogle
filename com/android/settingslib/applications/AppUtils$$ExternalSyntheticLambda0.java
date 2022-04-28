package com.android.settingslib.applications;

import android.content.Context;
import com.android.settingslib.applications.ApplicationsState;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AppUtils$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ ApplicationsState.AppEntry f$1;

    public /* synthetic */ AppUtils$$ExternalSyntheticLambda0(Context context, ApplicationsState.AppEntry appEntry) {
        this.f$0 = context;
        this.f$1 = appEntry;
    }

    public final void run() {
        AppUtils.getIcon(this.f$0, this.f$1);
    }
}
