package com.android.settings.users;

import android.os.Handler;
import android.os.NewUserRequest;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddSupervisedUserActivity$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ AddSupervisedUserActivity f$0;
    public final /* synthetic */ NewUserRequest f$1;
    public final /* synthetic */ Handler f$2;
    public final /* synthetic */ Consumer f$3;

    public /* synthetic */ AddSupervisedUserActivity$$ExternalSyntheticLambda1(AddSupervisedUserActivity addSupervisedUserActivity, NewUserRequest newUserRequest, Handler handler, Consumer consumer) {
        this.f$0 = addSupervisedUserActivity;
        this.f$1 = newUserRequest;
        this.f$2 = handler;
        this.f$3 = consumer;
    }

    public final void run() {
        this.f$0.lambda$createUserAsync$2(this.f$1, this.f$2, this.f$3);
    }
}
