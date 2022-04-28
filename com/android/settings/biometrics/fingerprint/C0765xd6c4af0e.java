package com.android.settings.biometrics.fingerprint;

import com.android.settings.biometrics.fingerprint.FingerprintSettings;
import java.util.concurrent.Callable;

/* renamed from: com.android.settings.biometrics.fingerprint.FingerprintSettings$FingerprintSettingsFragment$ConfirmLastDeleteDialog$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0765xd6c4af0e implements Callable {
    public final /* synthetic */ FingerprintSettings.FingerprintSettingsFragment.ConfirmLastDeleteDialog f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ C0765xd6c4af0e(FingerprintSettings.FingerprintSettingsFragment.ConfirmLastDeleteDialog confirmLastDeleteDialog, int i) {
        this.f$0 = confirmLastDeleteDialog;
        this.f$1 = i;
    }

    public final Object call() {
        return this.f$0.lambda$onCreateDialog$0(this.f$1);
    }
}
