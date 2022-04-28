package com.android.settings.accessibility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.window.C0444R;

public class AccessibilityQuickSettingsTooltipWindow extends PopupWindow {
    private final View.AccessibilityDelegate mAccessibilityDelegate = new View.AccessibilityDelegate() {
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, AccessibilityQuickSettingsTooltipWindow.this.mContext.getString(C0444R.string.accessibility_quick_settings_tooltips_dismiss)));
        }

        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (i != 16) {
                return super.performAccessibilityAction(view, i, bundle);
            }
            AccessibilityQuickSettingsTooltipWindow.this.dismiss();
            return true;
        }
    };
    private long mCloseDelayTimeMillis;
    /* access modifiers changed from: private */
    public final Context mContext;
    private Handler mHandler;

    public AccessibilityQuickSettingsTooltipWindow(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setup(String str, int i) {
        setup(str, i, 0);
    }

    public void setup(String str, int i, long j) {
        this.mCloseDelayTimeMillis = j;
        setBackgroundDrawable(new ColorDrawable(this.mContext.getColor(17170445)));
        View inflate = ((LayoutInflater) this.mContext.getSystemService(LayoutInflater.class)).inflate(C0444R.C0450layout.accessibility_qs_tooltips, (ViewGroup) null);
        inflate.setFocusable(true);
        inflate.setAccessibilityDelegate(this.mAccessibilityDelegate);
        setContentView(inflate);
        ((ImageView) getContentView().findViewById(C0444R.C0448id.qs_illustration)).setImageResource(i);
        TextView textView = (TextView) getContentView().findViewById(C0444R.C0448id.qs_content);
        textView.setText(str);
        setWidth(getWindowWidthWith(textView));
        setHeight(-2);
        setFocusable(true);
        setOutsideTouchable(true);
    }

    public void showAtTopCenter(View view) {
        showAtLocation(view, 49, 0, 0);
    }

    public void dismiss() {
        super.dismiss();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages((Object) null);
        }
    }

    public void showAtLocation(View view, int i, int i2, int i3) {
        super.showAtLocation(view, i, i2, i3);
        scheduleAutoCloseAction();
    }

    private void scheduleAutoCloseAction() {
        if (this.mCloseDelayTimeMillis > 0) {
            if (this.mHandler == null) {
                this.mHandler = new Handler(this.mContext.getMainLooper());
            }
            this.mHandler.removeCallbacksAndMessages((Object) null);
            this.mHandler.postDelayed(new C0574x3a763e0(this), this.mCloseDelayTimeMillis);
        }
    }

    private int getWindowWidthWith(TextView textView) {
        textView.measure(View.MeasureSpec.makeMeasureSpec(getAvailableWindowWidth(), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
        return textView.getMeasuredWidth();
    }

    /* access modifiers changed from: package-private */
    public int getAvailableWindowWidth() {
        Resources resources = this.mContext.getResources();
        return resources.getDisplayMetrics().widthPixels - (resources.getDimensionPixelSize(C0444R.dimen.accessibility_qs_tooltips_margin) * 2);
    }
}
