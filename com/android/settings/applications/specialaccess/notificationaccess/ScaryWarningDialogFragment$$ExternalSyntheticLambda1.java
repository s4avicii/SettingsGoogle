package com.android.settings.applications.specialaccess.notificationaccess;

import android.content.ComponentName;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScaryWarningDialogFragment$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ ScaryWarningDialogFragment f$0;
    public final /* synthetic */ NotificationAccessDetails f$1;
    public final /* synthetic */ ComponentName f$2;

    public /* synthetic */ ScaryWarningDialogFragment$$ExternalSyntheticLambda1(ScaryWarningDialogFragment scaryWarningDialogFragment, NotificationAccessDetails notificationAccessDetails, ComponentName componentName) {
        this.f$0 = scaryWarningDialogFragment;
        this.f$1 = notificationAccessDetails;
        this.f$2 = componentName;
    }

    public final void onClick(View view) {
        this.f$0.lambda$getDialogView$0(this.f$1, this.f$2, view);
    }
}
