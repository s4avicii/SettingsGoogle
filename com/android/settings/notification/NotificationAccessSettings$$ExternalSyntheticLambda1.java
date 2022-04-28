package com.android.settings.notification;

import com.android.settingslib.applications.ServiceListing;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationAccessSettings$$ExternalSyntheticLambda1 implements ServiceListing.Callback {
    public final /* synthetic */ NotificationAccessSettings f$0;

    public /* synthetic */ NotificationAccessSettings$$ExternalSyntheticLambda1(NotificationAccessSettings notificationAccessSettings) {
        this.f$0 = notificationAccessSettings;
    }

    public final void onServicesReloaded(List list) {
        this.f$0.updateList(list);
    }
}
