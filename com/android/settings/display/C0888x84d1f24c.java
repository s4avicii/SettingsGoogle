package com.android.settings.display;

import com.android.settings.display.PreviewSeekBarPreferenceFragment;

/* renamed from: com.android.settings.display.PreviewSeekBarPreferenceFragment$onPreviewSeekBarChangeListener$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0888x84d1f24c implements Runnable {
    public final /* synthetic */ PreviewSeekBarPreferenceFragment.onPreviewSeekBarChangeListener f$0;

    public /* synthetic */ C0888x84d1f24c(PreviewSeekBarPreferenceFragment.onPreviewSeekBarChangeListener onpreviewseekbarchangelistener) {
        this.f$0 = onpreviewseekbarchangelistener;
    }

    public final void run() {
        this.f$0.commitOnNextFrame();
    }
}
