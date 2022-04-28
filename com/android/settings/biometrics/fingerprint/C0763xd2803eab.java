package com.android.settings.biometrics.fingerprint;

import android.app.Activity;
import android.view.View;
import com.android.settingslib.RestrictedLockUtils;

/* renamed from: com.android.settings.biometrics.fingerprint.FingerprintSettings$FingerprintSettingsFragment$$ExternalSyntheticLambda2 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0763xd2803eab implements View.OnClickListener {
    public final /* synthetic */ Activity f$0;
    public final /* synthetic */ RestrictedLockUtils.EnforcedAdmin f$1;

    public /* synthetic */ C0763xd2803eab(Activity activity, RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.f$0 = activity;
        this.f$1 = enforcedAdmin;
    }

    public final void onClick(View view) {
        RestrictedLockUtils.sendShowAdminSupportDetailsIntent(this.f$0, this.f$1);
    }
}
