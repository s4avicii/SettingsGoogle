package com.android.settingslib.display;

import android.os.RemoteException;
import android.view.WindowManagerGlobal;
import com.android.settingslib.R$string;

public class DisplayDensityUtils {
    private static final int[] SUMMARIES_LARGER = {R$string.screen_zoom_summary_large, R$string.screen_zoom_summary_very_large, R$string.screen_zoom_summary_extremely_large};
    private static final int[] SUMMARIES_SMALLER = {R$string.screen_zoom_summary_small};
    private static final int SUMMARY_CUSTOM = R$string.screen_zoom_summary_custom;
    public static final int SUMMARY_DEFAULT = R$string.screen_zoom_summary_default;
    private final int mCurrentIndex;
    private final int mDefaultDensity;
    private final String[] mEntries;
    private final int[] mValues;

    /* JADX WARNING: type inference failed for: r1v3, types: [java.lang.Object[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public DisplayDensityUtils(android.content.Context r19) {
        /*
            r18 = this;
            r0 = r18
            r18.<init>()
            r1 = 0
            int r2 = getDefaultDisplayDensity(r1)
            r3 = -1
            if (r2 > 0) goto L_0x0017
            r2 = 0
            r0.mEntries = r2
            r0.mValues = r2
            r0.mDefaultDensity = r1
            r0.mCurrentIndex = r3
            return
        L_0x0017:
            android.content.res.Resources r4 = r19.getResources()
            android.util.DisplayMetrics r5 = new android.util.DisplayMetrics
            r5.<init>()
            android.view.Display r6 = r19.getDisplayNoVerify()
            r6.getRealMetrics(r5)
            int r6 = r5.densityDpi
            int r7 = r5.widthPixels
            int r5 = r5.heightPixels
            int r5 = java.lang.Math.min(r7, r5)
            int r5 = r5 * 160
            int r5 = r5 / 320
            android.content.res.Resources r7 = r19.getResources()
            int r8 = com.android.settingslib.R$fraction.display_density_max_scale
            r9 = 1
            float r7 = r7.getFraction(r8, r9, r9)
            float r5 = (float) r5
            float r8 = (float) r2
            float r5 = r5 / r8
            float r5 = java.lang.Math.min(r7, r5)
            android.content.res.Resources r7 = r19.getResources()
            int r10 = com.android.settingslib.R$fraction.display_density_min_scale
            float r7 = r7.getFraction(r10, r9, r9)
            android.content.res.Resources r10 = r19.getResources()
            int r11 = com.android.settingslib.R$fraction.display_density_min_scale_interval
            float r10 = r10.getFraction(r11, r9, r9)
            r11 = 1065353216(0x3f800000, float:1.0)
            float r5 = r5 - r11
            float r12 = r5 / r10
            int[] r13 = SUMMARIES_LARGER
            int r13 = r13.length
            float r13 = (float) r13
            r14 = 0
            float r12 = android.util.MathUtils.constrain(r12, r14, r13)
            int r12 = (int) r12
            float r7 = r11 - r7
            float r10 = r7 / r10
            int[] r13 = SUMMARIES_SMALLER
            int r13 = r13.length
            float r13 = (float) r13
            float r10 = android.util.MathUtils.constrain(r10, r14, r13)
            int r10 = (int) r10
            int r13 = r10 + 1
            int r13 = r13 + r12
            java.lang.String[] r14 = new java.lang.String[r13]
            int[] r15 = new int[r13]
            if (r10 <= 0) goto L_0x00aa
            float r3 = (float) r10
            float r7 = r7 / r3
            int r10 = r10 - r9
            r16 = r1
            r3 = -1
        L_0x0086:
            if (r10 < 0) goto L_0x00ad
            int r1 = r10 + 1
            float r1 = (float) r1
            float r1 = r1 * r7
            float r1 = r11 - r1
            float r1 = r1 * r8
            int r1 = (int) r1
            r1 = r1 & -2
            if (r6 != r1) goto L_0x0096
            r3 = r16
        L_0x0096:
            int[] r17 = SUMMARIES_SMALLER
            r11 = r17[r10]
            java.lang.String r11 = r4.getString(r11)
            r14[r16] = r11
            r15[r16] = r1
            int r16 = r16 + 1
            int r10 = r10 + -1
            r1 = 0
            r11 = 1065353216(0x3f800000, float:1.0)
            goto L_0x0086
        L_0x00aa:
            r3 = -1
            r16 = 0
        L_0x00ad:
            if (r6 != r2) goto L_0x00b1
            r3 = r16
        L_0x00b1:
            r15[r16] = r2
            int r1 = SUMMARY_DEFAULT
            java.lang.String r1 = r4.getString(r1)
            r14[r16] = r1
            int r16 = r16 + 1
            if (r12 <= 0) goto L_0x00e3
            float r1 = (float) r12
            float r5 = r5 / r1
            r1 = 0
        L_0x00c2:
            if (r1 >= r12) goto L_0x00e3
            int r7 = r1 + 1
            float r10 = (float) r7
            float r10 = r10 * r5
            r11 = 1065353216(0x3f800000, float:1.0)
            float r10 = r10 + r11
            float r10 = r10 * r8
            int r10 = (int) r10
            r10 = r10 & -2
            if (r6 != r10) goto L_0x00d3
            r3 = r16
        L_0x00d3:
            r15[r16] = r10
            int[] r10 = SUMMARIES_LARGER
            r1 = r10[r1]
            java.lang.String r1 = r4.getString(r1)
            r14[r16] = r1
            int r16 = r16 + 1
            r1 = r7
            goto L_0x00c2
        L_0x00e3:
            if (r3 < 0) goto L_0x00e6
            goto L_0x0107
        L_0x00e6:
            int r13 = r13 + r9
            int[] r15 = java.util.Arrays.copyOf(r15, r13)
            r15[r16] = r6
            java.lang.Object[] r1 = java.util.Arrays.copyOf(r14, r13)
            r14 = r1
            java.lang.String[] r14 = (java.lang.String[]) r14
            int r1 = SUMMARY_CUSTOM
            java.lang.Object[] r3 = new java.lang.Object[r9]
            java.lang.Integer r5 = java.lang.Integer.valueOf(r6)
            r6 = 0
            r3[r6] = r5
            java.lang.String r1 = r4.getString(r1, r3)
            r14[r16] = r1
            r3 = r16
        L_0x0107:
            r0.mDefaultDensity = r2
            r0.mCurrentIndex = r3
            r0.mEntries = r14
            r0.mValues = r15
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.display.DisplayDensityUtils.<init>(android.content.Context):void");
    }

    public String[] getEntries() {
        return this.mEntries;
    }

    public int[] getValues() {
        return this.mValues;
    }

    public int getCurrentIndex() {
        return this.mCurrentIndex;
    }

    public int getDefaultDensity() {
        return this.mDefaultDensity;
    }

    private static int getDefaultDisplayDensity(int i) {
        try {
            return WindowManagerGlobal.getWindowManagerService().getInitialDisplayDensity(i);
        } catch (RemoteException unused) {
            return -1;
        }
    }
}
