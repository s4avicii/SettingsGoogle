package com.android.settings.dream;

import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamSettings$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ RecyclerView f$0;
    public final /* synthetic */ Button f$1;

    public /* synthetic */ DreamSettings$$ExternalSyntheticLambda1(RecyclerView recyclerView, Button button) {
        this.f$0 = recyclerView;
        this.f$1 = button;
    }

    public final void run() {
        this.f$0.setPadding(0, 0, 0, this.f$1.getMeasuredHeight());
    }
}
