package com.android.settings;

import android.content.DialogInterface;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ActionDisabledByAppOpsHelper$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ActionDisabledByAppOpsHelper f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ ActionDisabledByAppOpsHelper$$ExternalSyntheticLambda0(ActionDisabledByAppOpsHelper actionDisabledByAppOpsHelper, String str) {
        this.f$0 = actionDisabledByAppOpsHelper;
        this.f$1 = str;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$prepareDialogBuilder$0(this.f$1, dialogInterface, i);
    }
}
