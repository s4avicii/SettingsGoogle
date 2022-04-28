package com.android.settings.notification.history;

import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationHistoryAdapter$$ExternalSyntheticLambda1 implements View.OnLongClickListener {
    public final /* synthetic */ View.OnClickListener f$0;

    public /* synthetic */ NotificationHistoryAdapter$$ExternalSyntheticLambda1(View.OnClickListener onClickListener) {
        this.f$0 = onClickListener;
    }

    public final boolean onLongClick(View view) {
        return this.f$0.onClick(view);
    }
}
