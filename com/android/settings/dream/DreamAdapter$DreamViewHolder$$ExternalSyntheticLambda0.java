package com.android.settings.dream;

import android.view.View;
import com.android.settings.dream.DreamAdapter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamAdapter$DreamViewHolder$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ DreamAdapter.DreamViewHolder f$0;
    public final /* synthetic */ IDreamItem f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ DreamAdapter$DreamViewHolder$$ExternalSyntheticLambda0(DreamAdapter.DreamViewHolder dreamViewHolder, IDreamItem iDreamItem, int i) {
        this.f$0 = dreamViewHolder;
        this.f$1 = iDreamItem;
        this.f$2 = i;
    }

    public final void onClick(View view) {
        this.f$0.lambda$bindView$1(this.f$1, this.f$2, view);
    }
}
