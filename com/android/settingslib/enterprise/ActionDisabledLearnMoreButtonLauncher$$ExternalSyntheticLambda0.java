package com.android.settingslib.enterprise;

import android.content.pm.PackageManager;
import android.os.UserHandle;
import com.android.settingslib.enterprise.ActionDisabledLearnMoreButtonLauncher;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ActionDisabledLearnMoreButtonLauncher$$ExternalSyntheticLambda0 implements ActionDisabledLearnMoreButtonLauncher.ResolveActivityChecker {
    public static final /* synthetic */ ActionDisabledLearnMoreButtonLauncher$$ExternalSyntheticLambda0 INSTANCE = new ActionDisabledLearnMoreButtonLauncher$$ExternalSyntheticLambda0();

    private /* synthetic */ ActionDisabledLearnMoreButtonLauncher$$ExternalSyntheticLambda0() {
    }

    public final boolean canResolveActivityAsUser(PackageManager packageManager, String str, UserHandle userHandle) {
        return ActionDisabledLearnMoreButtonLauncher.lambda$static$0(packageManager, str, userHandle);
    }
}
