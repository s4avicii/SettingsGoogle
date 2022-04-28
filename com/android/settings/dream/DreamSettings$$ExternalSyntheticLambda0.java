package com.android.settings.dream;

import android.view.View;
import com.android.settingslib.dream.DreamBackend;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamSettings$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ DreamBackend f$0;

    public /* synthetic */ DreamSettings$$ExternalSyntheticLambda0(DreamBackend dreamBackend) {
        this.f$0 = dreamBackend;
    }

    public final void onClick(View view) {
        this.f$0.preview(this.f$0.getActiveDream());
    }
}
