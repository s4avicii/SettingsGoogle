package com.android.settings.widget;

import android.animation.ValueAnimator;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class HighlightablePreferenceGroupAdapter$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ View f$0;

    public /* synthetic */ HighlightablePreferenceGroupAdapter$$ExternalSyntheticLambda0(View view) {
        this.f$0 = view;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.setBackgroundColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }
}
