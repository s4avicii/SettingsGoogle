package com.android.settings.password;

import android.content.Context;
import com.android.settings.password.ChooseLockPassword;
import java.util.concurrent.Callable;

/* renamed from: com.android.settings.password.ChooseLockPassword$ChooseLockPasswordFragment$Stage$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1190x36b0f3fd implements Callable {
    public final /* synthetic */ ChooseLockPassword.ChooseLockPasswordFragment.Stage f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ C1190x36b0f3fd(ChooseLockPassword.ChooseLockPasswordFragment.Stage stage, Context context) {
        this.f$0 = stage;
        this.f$1 = context;
    }

    public final Object call() {
        return this.f$0.lambda$getHint$1(this.f$1);
    }
}
