package com.android.settings.search;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import androidx.fragment.app.FragmentActivity;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SearchFeatureProvider$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ FragmentActivity f$2;
    public final /* synthetic */ Intent f$3;

    public /* synthetic */ SearchFeatureProvider$$ExternalSyntheticLambda0(Context context, int i, FragmentActivity fragmentActivity, Intent intent) {
        this.f$0 = context;
        this.f$1 = i;
        this.f$2 = fragmentActivity;
        this.f$3 = intent;
    }

    public final void onClick(View view) {
        SearchFeatureProvider.lambda$initSearchToolbar$0(this.f$0, this.f$1, this.f$2, this.f$3, view);
    }
}
