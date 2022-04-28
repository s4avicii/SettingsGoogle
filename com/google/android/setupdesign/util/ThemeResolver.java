package com.google.android.setupdesign.util;

import android.app.Activity;
import android.content.Intent;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.R$style;

public class ThemeResolver {
    private static ThemeResolver defaultResolver;
    private final int defaultTheme;
    private final ThemeSupplier defaultThemeSupplier;
    private final String oldestSupportedTheme;
    private final boolean useDayNight;

    public interface ThemeSupplier {
        String getTheme();
    }

    public static ThemeResolver getDefault() {
        if (defaultResolver == null) {
            defaultResolver = new Builder().setDefaultTheme(R$style.SudThemeGlif_DayNight).setUseDayNight(true).build();
        }
        return defaultResolver;
    }

    private ThemeResolver(int i, String str, ThemeSupplier themeSupplier, boolean z) {
        this.defaultTheme = i;
        this.oldestSupportedTheme = str;
        this.defaultThemeSupplier = themeSupplier;
        this.useDayNight = z;
    }

    public int resolve(Intent intent) {
        return resolve(intent.getStringExtra("theme"), WizardManagerHelper.isAnySetupWizard(intent));
    }

    public int resolve(Intent intent, boolean z) {
        return resolve(intent.getStringExtra("theme"), z);
    }

    public int resolve(String str, boolean z) {
        int themeRes = (!this.useDayNight || z) ? getThemeRes(str) : getDayNightThemeRes(str);
        if (themeRes == 0) {
            ThemeSupplier themeSupplier = this.defaultThemeSupplier;
            if (themeSupplier != null) {
                str = themeSupplier.getTheme();
                themeRes = (!this.useDayNight || z) ? getThemeRes(str) : getDayNightThemeRes(str);
            }
            if (themeRes == 0) {
                return this.defaultTheme;
            }
        }
        String str2 = this.oldestSupportedTheme;
        return (str2 == null || compareThemes(str, str2) >= 0) ? themeRes : this.defaultTheme;
    }

    public void applyTheme(Activity activity) {
        activity.setTheme(resolve(activity.getIntent(), WizardManagerHelper.isAnySetupWizard(activity.getIntent()) && !ThemeHelper.isSetupWizardDayNightEnabled(activity)));
    }

    private static int getDayNightThemeRes(String str) {
        if (str != null) {
            char c = 65535;
            switch (str.hashCode()) {
                case -2128555920:
                    if (str.equals("glif_v2_light")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1270463490:
                    if (str.equals("material_light")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1241052239:
                    if (str.equals("glif_v3_light")) {
                        c = 2;
                        break;
                    }
                    break;
                case -353548558:
                    if (str.equals("glif_v4_light")) {
                        c = 3;
                        break;
                    }
                    break;
                case 3175618:
                    if (str.equals("glif")) {
                        c = 4;
                        break;
                    }
                    break;
                case 115650329:
                    if (str.equals("glif_v2")) {
                        c = 5;
                        break;
                    }
                    break;
                case 115650330:
                    if (str.equals("glif_v3")) {
                        c = 6;
                        break;
                    }
                    break;
                case 115650331:
                    if (str.equals("glif_v4")) {
                        c = 7;
                        break;
                    }
                    break;
                case 299066663:
                    if (str.equals("material")) {
                        c = 8;
                        break;
                    }
                    break;
                case 767685465:
                    if (str.equals("glif_light")) {
                        c = 9;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 5:
                    return R$style.SudThemeGlifV2_DayNight;
                case 1:
                case 8:
                    return R$style.SudThemeMaterial_DayNight;
                case 2:
                case 6:
                    return R$style.SudThemeGlifV3_DayNight;
                case 3:
                case 7:
                    return R$style.SudThemeGlifV4_DayNight;
                case 4:
                case 9:
                    return R$style.SudThemeGlif_DayNight;
            }
        }
        return 0;
    }

    private static int getThemeRes(String str) {
        if (str != null) {
            char c = 65535;
            switch (str.hashCode()) {
                case -2128555920:
                    if (str.equals("glif_v2_light")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1270463490:
                    if (str.equals("material_light")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1241052239:
                    if (str.equals("glif_v3_light")) {
                        c = 2;
                        break;
                    }
                    break;
                case -353548558:
                    if (str.equals("glif_v4_light")) {
                        c = 3;
                        break;
                    }
                    break;
                case 3175618:
                    if (str.equals("glif")) {
                        c = 4;
                        break;
                    }
                    break;
                case 115650329:
                    if (str.equals("glif_v2")) {
                        c = 5;
                        break;
                    }
                    break;
                case 115650330:
                    if (str.equals("glif_v3")) {
                        c = 6;
                        break;
                    }
                    break;
                case 115650331:
                    if (str.equals("glif_v4")) {
                        c = 7;
                        break;
                    }
                    break;
                case 299066663:
                    if (str.equals("material")) {
                        c = 8;
                        break;
                    }
                    break;
                case 767685465:
                    if (str.equals("glif_light")) {
                        c = 9;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return R$style.SudThemeGlifV2_Light;
                case 1:
                    return R$style.SudThemeMaterial_Light;
                case 2:
                    return R$style.SudThemeGlifV3_Light;
                case 3:
                    return R$style.SudThemeGlifV4_Light;
                case 4:
                    return R$style.SudThemeGlif;
                case 5:
                    return R$style.SudThemeGlifV2;
                case 6:
                    return R$style.SudThemeGlifV3;
                case 7:
                    return R$style.SudThemeGlifV4;
                case 8:
                    return R$style.SudThemeMaterial;
                case 9:
                    return R$style.SudThemeGlif_Light;
            }
        }
        return 0;
    }

    private static int compareThemes(String str, String str2) {
        return Integer.valueOf(getThemeVersion(str)).compareTo(Integer.valueOf(getThemeVersion(str2)));
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int getThemeVersion(java.lang.String r7) {
        /*
            r0 = -1
            if (r7 == 0) goto L_0x008c
            int r1 = r7.hashCode()
            r2 = 5
            r3 = 4
            r4 = 3
            r5 = 2
            r6 = 1
            switch(r1) {
                case -2128555920: goto L_0x0079;
                case -1270463490: goto L_0x006e;
                case -1241052239: goto L_0x0063;
                case -353548558: goto L_0x0058;
                case 3175618: goto L_0x004d;
                case 115650329: goto L_0x0042;
                case 115650330: goto L_0x0037;
                case 115650331: goto L_0x002c;
                case 299066663: goto L_0x001f;
                case 767685465: goto L_0x0012;
                default: goto L_0x000f;
            }
        L_0x000f:
            r7 = r0
            goto L_0x0083
        L_0x0012:
            java.lang.String r1 = "glif_light"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x001b
            goto L_0x000f
        L_0x001b:
            r7 = 9
            goto L_0x0083
        L_0x001f:
            java.lang.String r1 = "material"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0028
            goto L_0x000f
        L_0x0028:
            r7 = 8
            goto L_0x0083
        L_0x002c:
            java.lang.String r1 = "glif_v4"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0035
            goto L_0x000f
        L_0x0035:
            r7 = 7
            goto L_0x0083
        L_0x0037:
            java.lang.String r1 = "glif_v3"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0040
            goto L_0x000f
        L_0x0040:
            r7 = 6
            goto L_0x0083
        L_0x0042:
            java.lang.String r1 = "glif_v2"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x004b
            goto L_0x000f
        L_0x004b:
            r7 = r2
            goto L_0x0083
        L_0x004d:
            java.lang.String r1 = "glif"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0056
            goto L_0x000f
        L_0x0056:
            r7 = r3
            goto L_0x0083
        L_0x0058:
            java.lang.String r1 = "glif_v4_light"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0061
            goto L_0x000f
        L_0x0061:
            r7 = r4
            goto L_0x0083
        L_0x0063:
            java.lang.String r1 = "glif_v3_light"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x006c
            goto L_0x000f
        L_0x006c:
            r7 = r5
            goto L_0x0083
        L_0x006e:
            java.lang.String r1 = "material_light"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0077
            goto L_0x000f
        L_0x0077:
            r7 = r6
            goto L_0x0083
        L_0x0079:
            java.lang.String r1 = "glif_v2_light"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0082
            goto L_0x000f
        L_0x0082:
            r7 = 0
        L_0x0083:
            switch(r7) {
                case 0: goto L_0x008b;
                case 1: goto L_0x008a;
                case 2: goto L_0x0089;
                case 3: goto L_0x0088;
                case 4: goto L_0x0087;
                case 5: goto L_0x008b;
                case 6: goto L_0x0089;
                case 7: goto L_0x0088;
                case 8: goto L_0x008a;
                case 9: goto L_0x0087;
                default: goto L_0x0086;
            }
        L_0x0086:
            goto L_0x008c
        L_0x0087:
            return r5
        L_0x0088:
            return r2
        L_0x0089:
            return r3
        L_0x008a:
            return r6
        L_0x008b:
            return r4
        L_0x008c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupdesign.util.ThemeResolver.getThemeVersion(java.lang.String):int");
    }

    public static class Builder {
        private int defaultTheme = R$style.SudThemeGlif_DayNight;
        private ThemeSupplier defaultThemeSupplier;
        private String oldestSupportedTheme = null;
        private boolean useDayNight = true;

        public Builder setDefaultTheme(int i) {
            this.defaultTheme = i;
            return this;
        }

        public Builder setUseDayNight(boolean z) {
            this.useDayNight = z;
            return this;
        }

        public ThemeResolver build() {
            return new ThemeResolver(this.defaultTheme, this.oldestSupportedTheme, this.defaultThemeSupplier, this.useDayNight);
        }
    }
}
