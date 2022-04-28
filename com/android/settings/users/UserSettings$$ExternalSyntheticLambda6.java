package com.android.settings.users;

import android.app.Activity;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import java.util.function.BiConsumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UserSettings$$ExternalSyntheticLambda6 implements BiConsumer {
    public final /* synthetic */ UserSettings f$0;
    public final /* synthetic */ Drawable f$1;
    public final /* synthetic */ UserInfo f$2;
    public final /* synthetic */ Activity f$3;

    public /* synthetic */ UserSettings$$ExternalSyntheticLambda6(UserSettings userSettings, Drawable drawable, UserInfo userInfo, Activity activity) {
        this.f$0 = userSettings;
        this.f$1 = drawable;
        this.f$2 = userInfo;
        this.f$3 = activity;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.lambda$buildEditCurrentUserDialog$2(this.f$1, this.f$2, this.f$3, (String) obj, (Drawable) obj2);
    }
}
