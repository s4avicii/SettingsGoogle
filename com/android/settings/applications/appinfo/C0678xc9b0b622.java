package com.android.settings.applications.appinfo;

import com.android.settings.applications.appinfo.AppLocaleDetails;
import java.util.Locale;
import java.util.function.Consumer;

/* renamed from: com.android.settings.applications.appinfo.AppLocaleDetails$AppLocaleDetailsHelper$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0678xc9b0b622 implements Consumer {
    public final /* synthetic */ AppLocaleDetails.AppLocaleDetailsHelper f$0;
    public final /* synthetic */ Locale f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ C0678xc9b0b622(AppLocaleDetails.AppLocaleDetailsHelper appLocaleDetailsHelper, Locale locale, String str, String str2) {
        this.f$0 = appLocaleDetailsHelper;
        this.f$1 = locale;
        this.f$2 = str;
        this.f$3 = str2;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$handleSuggestedLocales$0(this.f$1, this.f$2, this.f$3, (Locale) obj);
    }
}
