package com.android.settings.notification.history;

import android.content.Intent;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationSbnAdapter$$ExternalSyntheticLambda0 implements View.OnLongClickListener {
    public final /* synthetic */ StatusBarNotification f$0;
    public final /* synthetic */ NotificationSbnViewHolder f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ NotificationSbnAdapter$$ExternalSyntheticLambda0(StatusBarNotification statusBarNotification, NotificationSbnViewHolder notificationSbnViewHolder, int i) {
        this.f$0 = statusBarNotification;
        this.f$1 = notificationSbnViewHolder;
        this.f$2 = i;
    }

    public final boolean onLongClick(View view) {
        return this.f$1.itemView.getContext().startActivityAsUser(new Intent("android.settings.CHANNEL_NOTIFICATION_SETTINGS").putExtra("android.provider.extra.APP_PACKAGE", this.f$0.getPackageName()).putExtra("android.provider.extra.CHANNEL_ID", this.f$0.getNotification().getChannelId()).putExtra("android.provider.extra.CONVERSATION_ID", this.f$0.getNotification().getShortcutId()), UserHandle.of(this.f$2));
    }
}
