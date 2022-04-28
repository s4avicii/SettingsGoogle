package com.google.android.settings.gestures.columbus;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import androidx.window.C0444R;
import com.google.android.setupdesign.view.Illustration;

public class ColumbusEnrollingIllustration extends Illustration {
    /* access modifiers changed from: private */
    public Animator mAnimator;
    /* access modifiers changed from: private */
    public float mGestureValue = 0.0f;
    private final int mInset;
    private final Paint mPaint;

    public ColumbusEnrollingIllustration(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int dimensionPixelSize = getResources().getDimensionPixelSize(C0444R.dimen.columbus_enroll_illustration_stroke_width);
        int color = getContext().getColor(C0444R.C0446color.columbus_highlight);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) dimensionPixelSize);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(color);
        this.mInset = dimensionPixelSize / 2;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i = this.mInset;
        canvas.drawArc((float) i, (float) i, (float) (getWidth() - this.mInset), (float) (getHeight() - this.mInset), 270.0f, this.mGestureValue * 180.0f, false, this.mPaint);
    }

    /* access modifiers changed from: package-private */
    public void setGestureCount(final int i, final Runnable runnable) {
        Animator animator = this.mAnimator;
        if (animator != null) {
            animator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mGestureValue, (float) i});
        ofFloat.setDuration(500);
        ofFloat.setInterpolator(new DecelerateInterpolator());
        ofFloat.addUpdateListener(new ColumbusEnrollingIllustration$$ExternalSyntheticLambda0(this));
        ofFloat.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                ColumbusEnrollingIllustration.this.mGestureValue = (float) i;
                if (i == 2) {
                    ColumbusEnrollingIllustration.this.setBackgroundResource(C0444R.C0447drawable.ic_icon_check);
                }
                ColumbusEnrollingIllustration.this.mAnimator = null;
                Runnable runnable = runnable;
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        this.mAnimator = ofFloat;
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setGestureCount$0(ValueAnimator valueAnimator) {
        this.mGestureValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }
}
