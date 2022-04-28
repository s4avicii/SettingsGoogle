package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import androidx.preference.Preference;

/* renamed from: com.android.settings.network.NetworkProviderDownloadedSimListController$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1021xc6269a implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ NetworkProviderDownloadedSimListController f$0;
    public final /* synthetic */ SubscriptionInfo f$1;

    public /* synthetic */ C1021xc6269a(NetworkProviderDownloadedSimListController networkProviderDownloadedSimListController, SubscriptionInfo subscriptionInfo) {
        this.f$0 = networkProviderDownloadedSimListController;
        this.f$1 = subscriptionInfo;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$update$0(this.f$1, preference);
    }
}
