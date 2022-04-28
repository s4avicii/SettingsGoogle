package com.android.settings.applications.appinfo;

import com.android.settings.applications.appinfo.AppLocaleDetails;
import java.util.Collection;
import java.util.Locale;
import java.util.function.Consumer;

/* renamed from: com.android.settings.applications.appinfo.AppLocaleDetails$AppLocaleDetailsHelper$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0677xc9b0b621 implements Consumer {
    public final /* synthetic */ AppLocaleDetails.AppLocaleDetailsHelper f$0;
    public final /* synthetic */ Collection f$1;

    public /* synthetic */ C0677xc9b0b621(AppLocaleDetails.AppLocaleDetailsHelper appLocaleDetailsHelper, Collection collection) {
        this.f$0 = appLocaleDetailsHelper;
        this.f$1 = collection;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$handleSuggestedLocales$2(this.f$1, (Locale) obj);
    }
}
