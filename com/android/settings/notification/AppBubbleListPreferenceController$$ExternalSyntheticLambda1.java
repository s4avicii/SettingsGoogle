package com.android.settings.notification;

import android.service.notification.ConversationChannelWrapper;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AppBubbleListPreferenceController$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ AppBubbleListPreferenceController f$0;

    public /* synthetic */ AppBubbleListPreferenceController$$ExternalSyntheticLambda1(AppBubbleListPreferenceController appBubbleListPreferenceController) {
        this.f$0 = appBubbleListPreferenceController;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$filterAndSortConversations$0((ConversationChannelWrapper) obj);
    }
}
