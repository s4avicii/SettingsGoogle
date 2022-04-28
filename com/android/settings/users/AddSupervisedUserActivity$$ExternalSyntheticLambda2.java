package com.android.settings.users;

import android.os.NewUserResponse;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddSupervisedUserActivity$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ Consumer f$0;
    public final /* synthetic */ NewUserResponse f$1;

    public /* synthetic */ AddSupervisedUserActivity$$ExternalSyntheticLambda2(Consumer consumer, NewUserResponse newUserResponse) {
        this.f$0 = consumer;
        this.f$1 = newUserResponse;
    }

    public final void run() {
        this.f$0.accept(this.f$1);
    }
}
