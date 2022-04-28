package com.android.settings.users;

import android.app.AlertDialog;
import android.os.NewUserResponse;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddSupervisedUserActivity$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ AddSupervisedUserActivity f$0;
    public final /* synthetic */ AlertDialog f$1;

    public /* synthetic */ AddSupervisedUserActivity$$ExternalSyntheticLambda3(AddSupervisedUserActivity addSupervisedUserActivity, AlertDialog alertDialog) {
        this.f$0 = addSupervisedUserActivity;
        this.f$1 = alertDialog;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$createUser$3(this.f$1, (NewUserResponse) obj);
    }
}
