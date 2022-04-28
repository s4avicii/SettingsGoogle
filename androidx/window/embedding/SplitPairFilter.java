package androidx.window.embedding;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import androidx.window.core.ExperimentalWindowApi;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExperimentalWindowApi
/* compiled from: SplitPairFilter.kt */
public final class SplitPairFilter {
    @NotNull
    private final ComponentName primaryActivityName;
    @Nullable
    private final String secondaryActivityIntentAction;
    @NotNull
    private final ComponentName secondaryActivityName;

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0119  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0125  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SplitPairFilter(@org.jetbrains.annotations.NotNull android.content.ComponentName r13, @org.jetbrains.annotations.NotNull android.content.ComponentName r14, @org.jetbrains.annotations.Nullable java.lang.String r15) {
        /*
            r12 = this;
            java.lang.String r0 = "primaryActivityName"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r13, r0)
            java.lang.String r0 = "secondaryActivityName"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            r12.<init>()
            r12.primaryActivityName = r13
            r12.secondaryActivityName = r14
            r12.secondaryActivityIntentAction = r15
            java.lang.String r12 = r13.getPackageName()
            java.lang.String r15 = "primaryActivityName.packageName"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r12, r15)
            java.lang.String r13 = r13.getClassName()
            java.lang.String r15 = "primaryActivityName.className"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r13, r15)
            java.lang.String r15 = r14.getPackageName()
            java.lang.String r0 = "secondaryActivityName.packageName"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r15, r0)
            java.lang.String r14 = r14.getClassName()
            java.lang.String r0 = "secondaryActivityName.className"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r14, r0)
            int r0 = r12.length()
            r7 = 1
            r8 = 0
            if (r0 != 0) goto L_0x0044
            r0 = r7
            goto L_0x0045
        L_0x0044:
            r0 = r8
        L_0x0045:
            if (r0 != 0) goto L_0x0054
            int r0 = r15.length()
            if (r0 != 0) goto L_0x004f
            r0 = r7
            goto L_0x0050
        L_0x004f:
            r0 = r8
        L_0x0050:
            if (r0 != 0) goto L_0x0054
            r0 = r7
            goto L_0x0055
        L_0x0054:
            r0 = r8
        L_0x0055:
            if (r0 == 0) goto L_0x0125
            int r0 = r13.length()
            if (r0 != 0) goto L_0x005f
            r0 = r7
            goto L_0x0060
        L_0x005f:
            r0 = r8
        L_0x0060:
            if (r0 != 0) goto L_0x006f
            int r0 = r14.length()
            if (r0 != 0) goto L_0x006a
            r0 = r7
            goto L_0x006b
        L_0x006a:
            r0 = r8
        L_0x006b:
            if (r0 != 0) goto L_0x006f
            r0 = r7
            goto L_0x0070
        L_0x006f:
            r0 = r8
        L_0x0070:
            if (r0 == 0) goto L_0x0119
            java.lang.String r9 = "*"
            r10 = 2
            r11 = 0
            boolean r0 = kotlin.text.StringsKt__StringsKt.contains$default(r12, r9, r8, r10, r11)
            if (r0 == 0) goto L_0x0091
            r3 = 0
            r4 = 0
            r5 = 6
            r6 = 0
            java.lang.String r2 = "*"
            r1 = r12
            int r0 = kotlin.text.StringsKt__StringsKt.indexOf$default((java.lang.CharSequence) r1, (java.lang.String) r2, (int) r3, (boolean) r4, (int) r5, (java.lang.Object) r6)
            int r12 = r12.length()
            int r12 = r12 - r7
            if (r0 != r12) goto L_0x008f
            goto L_0x0091
        L_0x008f:
            r12 = r8
            goto L_0x0092
        L_0x0091:
            r12 = r7
        L_0x0092:
            java.lang.String r6 = "Wildcard in package name is only allowed at the end."
            if (r12 == 0) goto L_0x010f
            boolean r12 = kotlin.text.StringsKt__StringsKt.contains$default(r13, r9, r8, r10, r11)
            if (r12 == 0) goto L_0x00b1
            r2 = 0
            r3 = 0
            r4 = 6
            r5 = 0
            java.lang.String r1 = "*"
            r0 = r13
            int r12 = kotlin.text.StringsKt__StringsKt.indexOf$default((java.lang.CharSequence) r0, (java.lang.String) r1, (int) r2, (boolean) r3, (int) r4, (java.lang.Object) r5)
            int r13 = r13.length()
            int r13 = r13 - r7
            if (r12 != r13) goto L_0x00af
            goto L_0x00b1
        L_0x00af:
            r12 = r8
            goto L_0x00b2
        L_0x00b1:
            r12 = r7
        L_0x00b2:
            java.lang.String r13 = "Wildcard in class name is only allowed at the end."
            if (r12 == 0) goto L_0x0105
            boolean r12 = kotlin.text.StringsKt__StringsKt.contains$default(r15, r9, r8, r10, r11)
            if (r12 == 0) goto L_0x00d1
            r2 = 0
            r3 = 0
            r4 = 6
            r5 = 0
            java.lang.String r1 = "*"
            r0 = r15
            int r12 = kotlin.text.StringsKt__StringsKt.indexOf$default((java.lang.CharSequence) r0, (java.lang.String) r1, (int) r2, (boolean) r3, (int) r4, (java.lang.Object) r5)
            int r15 = r15.length()
            int r15 = r15 - r7
            if (r12 != r15) goto L_0x00cf
            goto L_0x00d1
        L_0x00cf:
            r12 = r8
            goto L_0x00d2
        L_0x00d1:
            r12 = r7
        L_0x00d2:
            if (r12 == 0) goto L_0x00fb
            boolean r12 = kotlin.text.StringsKt__StringsKt.contains$default(r14, r9, r8, r10, r11)
            if (r12 == 0) goto L_0x00ee
            r3 = 0
            r4 = 0
            r5 = 6
            r6 = 0
            java.lang.String r2 = "*"
            r1 = r14
            int r12 = kotlin.text.StringsKt__StringsKt.indexOf$default((java.lang.CharSequence) r1, (java.lang.String) r2, (int) r3, (boolean) r4, (int) r5, (java.lang.Object) r6)
            int r14 = r14.length()
            int r14 = r14 - r7
            if (r12 != r14) goto L_0x00ed
            goto L_0x00ee
        L_0x00ed:
            r7 = r8
        L_0x00ee:
            if (r7 == 0) goto L_0x00f1
            return
        L_0x00f1:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        L_0x00fb:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = r6.toString()
            r12.<init>(r13)
            throw r12
        L_0x0105:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        L_0x010f:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = r6.toString()
            r12.<init>(r13)
            throw r12
        L_0x0119:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = "Activity class name must not be empty."
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        L_0x0125:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = "Package name must not be empty"
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.window.embedding.SplitPairFilter.<init>(android.content.ComponentName, android.content.ComponentName, java.lang.String):void");
    }

    @NotNull
    public final ComponentName getPrimaryActivityName() {
        return this.primaryActivityName;
    }

    @NotNull
    public final ComponentName getSecondaryActivityName() {
        return this.secondaryActivityName;
    }

    @Nullable
    public final String getSecondaryActivityIntentAction() {
        return this.secondaryActivityIntentAction;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0040, code lost:
        if (matchesActivityIntentPair(r6, r7) != false) goto L_0x0044;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean matchesActivityPair(@org.jetbrains.annotations.NotNull android.app.Activity r6, @org.jetbrains.annotations.NotNull android.app.Activity r7) {
        /*
            r5 = this;
            java.lang.String r0 = "primaryActivity"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            java.lang.String r0 = "secondaryActivity"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
            androidx.window.embedding.MatcherUtils r0 = androidx.window.embedding.MatcherUtils.INSTANCE
            android.content.ComponentName r1 = r6.getComponentName()
            android.content.ComponentName r2 = r5.primaryActivityName
            boolean r1 = r0.areComponentsMatching$window_release(r1, r2)
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0029
            android.content.ComponentName r1 = r7.getComponentName()
            android.content.ComponentName r4 = r5.secondaryActivityName
            boolean r0 = r0.areComponentsMatching$window_release(r1, r4)
            if (r0 == 0) goto L_0x0029
            r0 = r2
            goto L_0x002a
        L_0x0029:
            r0 = r3
        L_0x002a:
            android.content.Intent r1 = r7.getIntent()
            if (r1 == 0) goto L_0x0045
            if (r0 == 0) goto L_0x0043
            android.content.Intent r7 = r7.getIntent()
            java.lang.String r0 = "secondaryActivity.intent"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r7, r0)
            boolean r5 = r5.matchesActivityIntentPair(r6, r7)
            if (r5 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r2 = r3
        L_0x0044:
            r0 = r2
        L_0x0045:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.window.embedding.SplitPairFilter.matchesActivityPair(android.app.Activity, android.app.Activity):boolean");
    }

    public final boolean matchesActivityIntentPair(@NotNull Activity activity, @NotNull Intent intent) {
        Intrinsics.checkNotNullParameter(activity, "primaryActivity");
        Intrinsics.checkNotNullParameter(intent, "secondaryActivityIntent");
        ComponentName componentName = activity.getComponentName();
        MatcherUtils matcherUtils = MatcherUtils.INSTANCE;
        if (!matcherUtils.areComponentsMatching$window_release(componentName, this.primaryActivityName) || !matcherUtils.areComponentsMatching$window_release(intent.getComponent(), this.secondaryActivityName)) {
            return false;
        }
        String str = this.secondaryActivityIntentAction;
        if (str == null || Intrinsics.areEqual(str, intent.getAction())) {
            return true;
        }
        return false;
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SplitPairFilter)) {
            return false;
        }
        SplitPairFilter splitPairFilter = (SplitPairFilter) obj;
        return Intrinsics.areEqual(this.primaryActivityName, splitPairFilter.primaryActivityName) && Intrinsics.areEqual(this.secondaryActivityName, splitPairFilter.secondaryActivityName) && Intrinsics.areEqual(this.secondaryActivityIntentAction, splitPairFilter.secondaryActivityIntentAction);
    }

    public int hashCode() {
        int hashCode = ((this.primaryActivityName.hashCode() * 31) + this.secondaryActivityName.hashCode()) * 31;
        String str = this.secondaryActivityIntentAction;
        return hashCode + (str == null ? 0 : str.hashCode());
    }

    @NotNull
    public String toString() {
        return "SplitPairFilter{primaryActivityName=" + this.primaryActivityName + ", secondaryActivityName=" + this.secondaryActivityName + ", secondaryActivityAction=" + this.secondaryActivityIntentAction + '}';
    }
}
