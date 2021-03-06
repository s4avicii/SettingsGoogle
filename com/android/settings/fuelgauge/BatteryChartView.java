package com.android.settings.fuelgauge;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.window.C0444R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.Utils;
import java.time.Clock;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class BatteryChartView extends AppCompatImageView implements View.OnClickListener, AccessibilityManager.AccessibilityStateChangeListener {
    private static final List<String> ACCESSIBILITY_SERVICE_NAMES = Arrays.asList(new String[]{"SwitchAccessService", "TalkBackService", "JustSpeakService"});
    private static final int DIVIDER_COLOR = Color.parseColor("#CDCCC5");
    private int mDividerHeight;
    private Paint mDividerPaint;
    private int mDividerWidth;
    Handler mHandler = new Handler();
    int mHoveredIndex = -2;
    private final Rect mIndent = new Rect();
    private boolean mIsSlotsClickabled;
    private int[] mLevels;
    private OnSelectListener mOnSelectListener;
    private final Rect[] mPercentageBounds = {new Rect(), new Rect(), new Rect()};
    private String[] mPercentages = getPercentages();
    int mSelectedIndex = -2;
    private int mTextPadding;
    private Paint mTextPaint;
    String[] mTimestamps;
    private final Rect[] mTimestampsBounds = {new Rect(), new Rect(), new Rect(), new Rect()};
    private float mTouchUpEventX = Float.MIN_VALUE;
    private int mTrapezoidColor;
    private int mTrapezoidCount;
    Paint mTrapezoidCurvePaint = null;
    private float mTrapezoidHOffset;
    private int mTrapezoidHoverColor;
    private Paint mTrapezoidPaint;
    private TrapezoidSlot[] mTrapezoidSlots;
    private int mTrapezoidSolidColor;
    private float mTrapezoidVOffset;
    final Runnable mUpdateClickableStateRun = new BatteryChartView$$ExternalSyntheticLambda0(this);

    public interface OnSelectListener {
        void onSelect(int i);
    }

    public BatteryChartView(Context context) {
        super(context, (AttributeSet) null);
    }

    public BatteryChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initializeColors(context);
        setOnClickListener(this);
        setSelectedIndex(-1);
        setTrapezoidCount(12);
        setClickable(false);
        setLatestTimestamp(0);
    }

    public void setTrapezoidCount(int i) {
        Log.i("BatteryChartView", "trapezoidCount:" + i);
        this.mTrapezoidCount = i;
        this.mTrapezoidSlots = new TrapezoidSlot[i];
        for (int i2 = 0; i2 < i; i2++) {
            this.mTrapezoidSlots[i2] = new TrapezoidSlot();
        }
        invalidate();
    }

    public void setLevels(int[] iArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("setLevels() ");
        sb.append(iArr == null ? "null" : Integer.valueOf(iArr.length));
        Log.d("BatteryChartView", sb.toString());
        if (iArr == null) {
            this.mLevels = null;
            return;
        }
        if (iArr.length != this.mTrapezoidCount + 1) {
            iArr = null;
        }
        this.mLevels = iArr;
        int i = 0;
        setClickable(false);
        invalidate();
        if (this.mLevels != null) {
            while (true) {
                int[] iArr2 = this.mLevels;
                if (i >= iArr2.length - 1) {
                    return;
                }
                if (iArr2[i] == 0 || iArr2[i + 1] == 0) {
                    i++;
                } else {
                    setClickable(true);
                    return;
                }
            }
        }
    }

    public void setSelectedIndex(int i) {
        if (this.mSelectedIndex != i) {
            this.mSelectedIndex = i;
            invalidate();
            OnSelectListener onSelectListener = this.mOnSelectListener;
            if (onSelectListener != null) {
                onSelectListener.onSelect(this.mSelectedIndex);
            }
        }
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.mOnSelectListener = onSelectListener;
    }

    public void setCompanionTextView(TextView textView) {
        if (textView != null) {
            textView.draw(new Canvas());
            this.mTextPaint = textView.getPaint();
        } else {
            this.mTextPaint = null;
        }
        setVisibility(0);
        requestLayout();
    }

    public void setLatestTimestamp(long j) {
        if (j == 0) {
            j = Clock.systemUTC().millis();
        }
        if (this.mTimestamps == null) {
            this.mTimestamps = new String[4];
        }
        boolean is24HourFormat = DateFormat.is24HourFormat(getContext());
        for (int i = 0; i < 4; i++) {
            this.mTimestamps[i] = ConvertUtils.utcToLocalTimeHour(getContext(), j - (((long) (3 - i)) * 28800000), is24HourFormat);
        }
        requestLayout();
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mTextPaint != null) {
            int i3 = 0;
            while (true) {
                String[] strArr = this.mPercentages;
                if (i3 >= strArr.length) {
                    break;
                }
                this.mTextPaint.getTextBounds(strArr[i3], 0, strArr[i3].length(), this.mPercentageBounds[i3]);
                i3++;
            }
            this.mIndent.top = this.mPercentageBounds[0].height();
            this.mIndent.right = this.mPercentageBounds[0].width() + this.mTextPadding;
            if (this.mTimestamps != null) {
                int i4 = 0;
                for (int i5 = 0; i5 < 4; i5++) {
                    Paint paint = this.mTextPaint;
                    String[] strArr2 = this.mTimestamps;
                    paint.getTextBounds(strArr2[i5], 0, strArr2[i5].length(), this.mTimestampsBounds[i5]);
                    i4 = Math.max(i4, this.mTimestampsBounds[i5].height());
                }
                this.mIndent.bottom = i4 + Math.round(((float) this.mTextPadding) * 1.5f);
            }
            Log.d("BatteryChartView", "setIndent:" + this.mPercentageBounds[0]);
            return;
        }
        this.mIndent.set(0, 0, 0, 0);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawHorizontalDividers(canvas);
        drawVerticalDividers(canvas);
        drawTrapezoids(canvas);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 1) {
            this.mTouchUpEventX = motionEvent.getX();
        } else if (action == 3) {
            this.mTouchUpEventX = Float.MIN_VALUE;
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        int trapezoidIndex;
        int action = motionEvent.getAction();
        if ((action == 7 || action == 9) && this.mHoveredIndex != (trapezoidIndex = getTrapezoidIndex(motionEvent.getX()))) {
            this.mHoveredIndex = trapezoidIndex;
            invalidate();
        }
        return super.onHoverEvent(motionEvent);
    }

    public void onHoverChanged(boolean z) {
        super.onHoverChanged(z);
        if (!z) {
            this.mHoveredIndex = -2;
            invalidate();
        }
    }

    public void onClick(View view) {
        float f = this.mTouchUpEventX;
        if (f == Float.MIN_VALUE) {
            Log.w("BatteryChartView", "invalid motion event for onClick() callback");
            return;
        }
        int trapezoidIndex = getTrapezoidIndex(f);
        if (trapezoidIndex != -2 && isValidToDraw(trapezoidIndex)) {
            if (trapezoidIndex == this.mSelectedIndex) {
                setSelectedIndex(-1);
            } else {
                setSelectedIndex(trapezoidIndex);
            }
            view.performHapticFeedback(6);
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        lambda$new$0();
        ((AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class)).addAccessibilityStateChangeListener(this);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class)).removeAccessibilityStateChangeListener(this);
        this.mHandler.removeCallbacks(this.mUpdateClickableStateRun);
    }

    public void onAccessibilityStateChanged(boolean z) {
        Log.d("BatteryChartView", "onAccessibilityStateChanged:" + z);
        this.mHandler.removeCallbacks(this.mUpdateClickableStateRun);
        this.mHandler.postDelayed(this.mUpdateClickableStateRun, 500);
    }

    /* access modifiers changed from: private */
    /* renamed from: updateClickableState */
    public void lambda$new$0() {
        Context context = this.mContext;
        this.mIsSlotsClickabled = FeatureFactory.getFactory(context).getPowerUsageFeatureProvider(context).isChartGraphSlotsEnabled(context) && !isAccessibilityEnabled(context);
        Log.d("BatteryChartView", "isChartGraphSlotsEnabled:" + this.mIsSlotsClickabled);
        setClickable(isClickable());
        boolean z = this.mIsSlotsClickabled;
        if (!z && this.mTrapezoidCurvePaint == null) {
            Paint paint = new Paint();
            this.mTrapezoidCurvePaint = paint;
            paint.setAntiAlias(true);
            this.mTrapezoidCurvePaint.setColor(this.mTrapezoidSolidColor);
            this.mTrapezoidCurvePaint.setStyle(Paint.Style.STROKE);
            this.mTrapezoidCurvePaint.setStrokeWidth((float) (this.mDividerWidth * 2));
        } else if (z) {
            this.mTrapezoidCurvePaint = null;
            setLevels(this.mLevels);
        }
        invalidate();
    }

    public void setClickable(boolean z) {
        super.setClickable(this.mIsSlotsClickabled && z);
    }

    /* access modifiers changed from: package-private */
    public void setClickableForce(boolean z) {
        super.setClickable(z);
    }

    private void initializeColors(Context context) {
        setBackgroundColor(0);
        int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(context);
        this.mTrapezoidSolidColor = colorAccentDefaultColor;
        this.mTrapezoidColor = Utils.getDisabled(context, colorAccentDefaultColor);
        this.mTrapezoidHoverColor = Utils.getColorAttrDefaultColor(context, 17956903);
        Resources resources = getContext().getResources();
        this.mDividerWidth = resources.getDimensionPixelSize(C0444R.dimen.chartview_divider_width);
        this.mDividerHeight = resources.getDimensionPixelSize(C0444R.dimen.chartview_divider_height);
        Paint paint = new Paint();
        this.mDividerPaint = paint;
        paint.setAntiAlias(true);
        this.mDividerPaint.setColor(DIVIDER_COLOR);
        this.mDividerPaint.setStyle(Paint.Style.STROKE);
        this.mDividerPaint.setStrokeWidth((float) this.mDividerWidth);
        Log.i("BatteryChartView", "mDividerWidth:" + this.mDividerWidth);
        Log.i("BatteryChartView", "mDividerHeight:" + this.mDividerHeight);
        this.mTrapezoidHOffset = resources.getDimension(C0444R.dimen.chartview_trapezoid_margin_start);
        this.mTrapezoidVOffset = resources.getDimension(C0444R.dimen.chartview_trapezoid_margin_bottom);
        Paint paint2 = new Paint();
        this.mTrapezoidPaint = paint2;
        paint2.setAntiAlias(true);
        this.mTrapezoidPaint.setColor(this.mTrapezoidSolidColor);
        this.mTrapezoidPaint.setStyle(Paint.Style.FILL);
        this.mTrapezoidPaint.setPathEffect(new CornerPathEffect((float) resources.getDimensionPixelSize(C0444R.dimen.chartview_trapezoid_radius)));
        this.mTextPadding = resources.getDimensionPixelSize(C0444R.dimen.chartview_text_padding);
    }

    private void drawHorizontalDividers(Canvas canvas) {
        int width = getWidth() - this.mIndent.right;
        int height = getHeight();
        Rect rect = this.mIndent;
        int i = rect.top;
        int i2 = (height - i) - rect.bottom;
        float f = ((float) i) + (((float) this.mDividerWidth) * 0.5f);
        float f2 = (float) width;
        canvas.drawLine(0.0f, f, f2, f, this.mDividerPaint);
        drawPercentage(canvas, 0, f);
        int i3 = this.mDividerWidth;
        float f3 = ((float) (this.mIndent.top + i3)) + (((((float) (i2 - (i3 * 2))) - this.mTrapezoidVOffset) - ((float) this.mDividerHeight)) * 0.5f);
        Canvas canvas2 = canvas;
        canvas2.drawLine(0.0f, f3, f2, f3, this.mDividerPaint);
        drawPercentage(canvas, 1, f3);
        float f4 = ((float) this.mIndent.top) + (((float) (i2 - this.mDividerHeight)) - (((float) this.mDividerWidth) * 0.5f));
        canvas2.drawLine(0.0f, f4, f2, f4, this.mDividerPaint);
        drawPercentage(canvas, 2, f4);
    }

    private void drawPercentage(Canvas canvas, int i, float f) {
        if (this.mTextPaint != null) {
            String str = this.mPercentages[i];
            int width = getWidth() - this.mPercentageBounds[i].width();
            Rect[] rectArr = this.mPercentageBounds;
            canvas.drawText(str, (float) (width - rectArr[i].left), f + (((float) rectArr[i].height()) * 0.5f), this.mTextPaint);
        }
    }

    private void drawVerticalDividers(Canvas canvas) {
        int width = getWidth() - this.mIndent.right;
        int i = this.mTrapezoidCount;
        int i2 = i + 1;
        float f = (((float) width) - ((float) (this.mDividerWidth * i2))) / ((float) i);
        float height = (float) (getHeight() - this.mIndent.bottom);
        float f2 = height - ((float) this.mDividerHeight);
        float f3 = this.mTrapezoidHOffset;
        int i3 = this.mDividerWidth;
        float f4 = f3 + (((float) i3) * 0.5f);
        float f5 = ((float) i3) * 0.5f;
        int i4 = 0;
        while (i4 < i2) {
            canvas.drawLine(f5, f2, f5, height, this.mDividerPaint);
            float f6 = ((float) this.mDividerWidth) + f5 + f;
            TrapezoidSlot[] trapezoidSlotArr = this.mTrapezoidSlots;
            if (i4 < trapezoidSlotArr.length) {
                trapezoidSlotArr[i4].mLeft = (float) Math.round(f5 + f4);
                this.mTrapezoidSlots[i4].mRight = (float) Math.round(f6 - f4);
            }
            i4++;
            f5 = f6;
        }
        if (this.mTimestamps != null) {
            float[] fArr = new float[4];
            int i5 = this.mDividerWidth;
            float f7 = ((float) i5) * 0.5f;
            float f8 = ((float) i5) + f;
            for (int i6 = 0; i6 < 4; i6++) {
                fArr[i6] = (((float) i6) * f8 * 4.0f) + f7;
            }
            drawTimestamp(canvas, fArr);
        }
    }

    private void drawTimestamp(Canvas canvas, float[] fArr) {
        canvas.drawText(this.mTimestamps[0], fArr[0] - ((float) this.mTimestampsBounds[0].left), (float) getTimestampY(0), this.mTextPaint);
        canvas.drawText(this.mTimestamps[3], (fArr[3] - ((float) this.mTimestampsBounds[3].width())) - ((float) this.mTimestampsBounds[3].left), (float) getTimestampY(3), this.mTextPaint);
        for (int i = 1; i <= 2; i++) {
            canvas.drawText(this.mTimestamps[i], fArr[i] - (((float) (this.mTimestampsBounds[i].width() - this.mTimestampsBounds[i].left)) * 0.5f), (float) getTimestampY(i), this.mTextPaint);
        }
    }

    private int getTimestampY(int i) {
        return (getHeight() - this.mTimestampsBounds[i].height()) + this.mTimestampsBounds[i].height() + this.mTimestampsBounds[i].top + Math.round(((float) this.mTextPadding) * 1.5f);
    }

    private void drawTrapezoids(Canvas canvas) {
        int i;
        int i2;
        if (this.mLevels != null) {
            int height = getHeight();
            Rect rect = this.mIndent;
            int i3 = (height - rect.bottom) - this.mDividerHeight;
            int i4 = this.mDividerWidth;
            float f = ((float) (i3 - i4)) - this.mTrapezoidVOffset;
            float f2 = ((f - (((float) i4) * 0.5f)) - ((float) rect.top)) / 100.0f;
            Path path = new Path();
            int i5 = 0;
            Path path2 = null;
            while (i5 < this.mTrapezoidCount) {
                if (!isValidToDraw(i5)) {
                    Paint paint = this.mTrapezoidCurvePaint;
                    if (!(paint == null || path2 == null)) {
                        canvas.drawPath(path2, paint);
                        path2 = null;
                    }
                } else {
                    boolean z = this.mIsSlotsClickabled;
                    if (!z) {
                        i = this.mTrapezoidColor;
                    } else {
                        int i6 = this.mSelectedIndex;
                        i = (i6 == i5 || i6 == -1) ? this.mTrapezoidSolidColor : this.mTrapezoidColor;
                    }
                    boolean z2 = z && (i2 = this.mHoveredIndex) == i5 && isValidToDraw(i2);
                    Paint paint2 = this.mTrapezoidPaint;
                    if (z2) {
                        i = this.mTrapezoidHoverColor;
                    }
                    paint2.setColor(i);
                    float round = (float) Math.round(f - (((float) this.mLevels[i5]) * f2));
                    float round2 = (float) Math.round(f - (((float) this.mLevels[i5 + 1]) * f2));
                    path.reset();
                    path.moveTo(this.mTrapezoidSlots[i5].mLeft, f);
                    path.lineTo(this.mTrapezoidSlots[i5].mLeft, round);
                    path.lineTo(this.mTrapezoidSlots[i5].mRight, round2);
                    path.lineTo(this.mTrapezoidSlots[i5].mRight, f);
                    path.lineTo(this.mTrapezoidSlots[i5].mLeft, f);
                    path.lineTo(this.mTrapezoidSlots[i5].mLeft, round);
                    canvas.drawPath(path, this.mTrapezoidPaint);
                    if (this.mTrapezoidCurvePaint != null) {
                        if (path2 == null) {
                            path2 = new Path();
                            path2.moveTo(this.mTrapezoidSlots[i5].mLeft, round);
                        } else {
                            path2.lineTo(this.mTrapezoidSlots[i5].mLeft, round);
                        }
                        path2.lineTo(this.mTrapezoidSlots[i5].mRight, round2);
                    }
                }
                i5++;
            }
            Paint paint3 = this.mTrapezoidCurvePaint;
            if (paint3 != null && path2 != null) {
                canvas.drawPath(path2, paint3);
            }
        }
    }

    private int getTrapezoidIndex(float f) {
        int i = 0;
        while (true) {
            TrapezoidSlot[] trapezoidSlotArr = this.mTrapezoidSlots;
            if (i >= trapezoidSlotArr.length) {
                return -2;
            }
            TrapezoidSlot trapezoidSlot = trapezoidSlotArr[i];
            float f2 = trapezoidSlot.mLeft;
            float f3 = this.mTrapezoidHOffset;
            if (f >= f2 - f3 && f <= trapezoidSlot.mRight + f3) {
                return i;
            }
            i++;
        }
    }

    private boolean isValidToDraw(int i) {
        int[] iArr = this.mLevels;
        return (iArr == null || i < 0 || i >= iArr.length - 1 || iArr[i] == 0 || iArr[i + 1] == 0) ? false : true;
    }

    private static String[] getPercentages() {
        return new String[]{Utils.formatPercentage(100.0d, true), Utils.formatPercentage(50.0d, true), Utils.formatPercentage(0.0d, true)};
    }

    static boolean isAccessibilityEnabled(Context context) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        if (!accessibilityManager.isEnabled()) {
            return false;
        }
        for (AccessibilityServiceInfo next : accessibilityManager.getEnabledAccessibilityServiceList(17)) {
            Iterator<String> it = ACCESSIBILITY_SERVICE_NAMES.iterator();
            while (true) {
                if (it.hasNext()) {
                    String next2 = it.next();
                    String id = next.getId();
                    if (id != null && id.contains(next2)) {
                        Log.d("BatteryChartView", "acccessibilityEnabled:" + id);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static final class TrapezoidSlot {
        public float mLeft;
        public float mRight;

        private TrapezoidSlot() {
        }

        public String toString() {
            return String.format(Locale.US, "TrapezoidSlot[%f,%f]", new Object[]{Float.valueOf(this.mLeft), Float.valueOf(this.mRight)});
        }
    }
}
