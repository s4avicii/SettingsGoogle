package com.android.settings.widget;

import androidx.recyclerview.widget.RecyclerView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class HighlightablePreferenceGroupAdapter$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ HighlightablePreferenceGroupAdapter f$0;
    public final /* synthetic */ RecyclerView f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ HighlightablePreferenceGroupAdapter$$ExternalSyntheticLambda4(HighlightablePreferenceGroupAdapter highlightablePreferenceGroupAdapter, RecyclerView recyclerView, int i) {
        this.f$0 = highlightablePreferenceGroupAdapter;
        this.f$1 = recyclerView;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$requestHighlight$1(this.f$1, this.f$2);
    }
}
