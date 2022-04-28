package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import com.android.settings.widget.GearPreference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SubscriptionsPreferenceController$$ExternalSyntheticLambda1 implements GearPreference.OnGearClickListener {
    public final /* synthetic */ SubscriptionsPreferenceController f$0;
    public final /* synthetic */ SubscriptionInfo f$1;

    public /* synthetic */ SubscriptionsPreferenceController$$ExternalSyntheticLambda1(SubscriptionsPreferenceController subscriptionsPreferenceController, SubscriptionInfo subscriptionInfo) {
        this.f$0 = subscriptionsPreferenceController;
        this.f$1 = subscriptionInfo;
    }

    public final void onGearClick(GearPreference gearPreference) {
        this.f$0.lambda$update$1(this.f$1, gearPreference);
    }
}
