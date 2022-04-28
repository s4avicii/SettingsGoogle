package com.android.settings.notification.history;

import android.view.View;
import android.widget.DateTimeView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.C0444R;

public class NotificationHistoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView mSummary;
    private final DateTimeView mTime;
    private final TextView mTitle;

    NotificationHistoryViewHolder(View view) {
        super(view);
        this.mTime = view.findViewById(C0444R.C0448id.timestamp);
        this.mTitle = (TextView) view.findViewById(C0444R.C0448id.title);
        this.mSummary = (TextView) view.findViewById(C0444R.C0448id.text);
    }

    /* access modifiers changed from: package-private */
    public void setSummary(CharSequence charSequence) {
        this.mSummary.setText(charSequence);
        this.mSummary.setVisibility(charSequence != null ? 0 : 8);
    }

    /* access modifiers changed from: package-private */
    public void setTitle(CharSequence charSequence) {
        this.mTitle.setText(charSequence);
        this.mTitle.setVisibility(charSequence != null ? 0 : 4);
    }

    /* access modifiers changed from: package-private */
    public void setPostedTime(long j) {
        this.mTime.setTime(j);
    }
}
