package com.android.settings.notification.history;

import android.app.NotificationHistory;
import java.util.Comparator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationHistoryAdapter$$ExternalSyntheticLambda2 implements Comparator {
    public static final /* synthetic */ NotificationHistoryAdapter$$ExternalSyntheticLambda2 INSTANCE = new NotificationHistoryAdapter$$ExternalSyntheticLambda2();

    private /* synthetic */ NotificationHistoryAdapter$$ExternalSyntheticLambda2() {
    }

    public final int compare(Object obj, Object obj2) {
        return Long.compare(((NotificationHistory.HistoricalNotification) obj2).getPostedTimeMs(), ((NotificationHistory.HistoricalNotification) obj).getPostedTimeMs());
    }
}
