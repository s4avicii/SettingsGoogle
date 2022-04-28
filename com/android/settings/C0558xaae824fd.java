package com.android.settings;

import com.android.settings.TrustedCredentialsDialogBuilder;
import java.util.function.IntConsumer;

/* renamed from: com.android.settings.TrustedCredentialsDialogBuilder$DialogEventHandler$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0558xaae824fd implements IntConsumer {
    public final /* synthetic */ TrustedCredentialsDialogBuilder.DialogEventHandler f$0;

    public /* synthetic */ C0558xaae824fd(TrustedCredentialsDialogBuilder.DialogEventHandler dialogEventHandler) {
        this.f$0 = dialogEventHandler;
    }

    public final void accept(int i) {
        this.f$0.onCredentialConfirmed(i);
    }
}
