package com.android.settings.applications.appinfo;

import com.android.settings.applications.appinfo.AppLocaleDetails;
import java.util.Collection;
import java.util.Locale;
import java.util.function.Consumer;

/* renamed from: com.android.settings.applications.appinfo.AppLocaleDetails$AppLocaleDetailsHelper$$ExternalSyntheticLambda2 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0679xc9b0b623 implements Consumer {
    public final /* synthetic */ Locale f$0;
    public final /* synthetic */ Collection f$1;

    public /* synthetic */ C0679xc9b0b623(Locale locale, Collection collection) {
        this.f$0 = locale;
        this.f$1 = collection;
    }

    public final void accept(Object obj) {
        AppLocaleDetails.AppLocaleDetailsHelper.lambda$handleSuggestedLocales$1(this.f$0, this.f$1, (Locale) obj);
    }
}
