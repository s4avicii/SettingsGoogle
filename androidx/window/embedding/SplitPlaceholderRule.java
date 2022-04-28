package androidx.window.embedding;

import android.content.Intent;
import androidx.window.core.ExperimentalWindowApi;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExperimentalWindowApi
/* compiled from: SplitPlaceholderRule.kt */
public final class SplitPlaceholderRule extends SplitRule {
    @NotNull
    private final Set<ActivityFilter> filters;
    private final int finishPrimaryWithSecondary;
    private final boolean isSticky;
    @NotNull
    private final Intent placeholderIntent;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ SplitPlaceholderRule(java.util.Set r12, android.content.Intent r13, boolean r14, int r15, int r16, int r17, float r18, int r19, int r20, kotlin.jvm.internal.DefaultConstructorMarker r21) {
        /*
            r11 = this;
            r0 = r20
            r1 = r0 & 8
            if (r1 == 0) goto L_0x0009
            r1 = 1
            r6 = r1
            goto L_0x000a
        L_0x0009:
            r6 = r15
        L_0x000a:
            r1 = r0 & 16
            r2 = 0
            if (r1 == 0) goto L_0x0011
            r7 = r2
            goto L_0x0013
        L_0x0011:
            r7 = r16
        L_0x0013:
            r1 = r0 & 32
            if (r1 == 0) goto L_0x0019
            r8 = r2
            goto L_0x001b
        L_0x0019:
            r8 = r17
        L_0x001b:
            r1 = r0 & 64
            if (r1 == 0) goto L_0x0023
            r1 = 1056964608(0x3f000000, float:0.5)
            r9 = r1
            goto L_0x0025
        L_0x0023:
            r9 = r18
        L_0x0025:
            r0 = r0 & 128(0x80, float:1.794E-43)
            if (r0 == 0) goto L_0x002c
            r0 = 3
            r10 = r0
            goto L_0x002e
        L_0x002c:
            r10 = r19
        L_0x002e:
            r2 = r11
            r3 = r12
            r4 = r13
            r5 = r14
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.window.embedding.SplitPlaceholderRule.<init>(java.util.Set, android.content.Intent, boolean, int, int, int, float, int, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    @NotNull
    public final Intent getPlaceholderIntent() {
        return this.placeholderIntent;
    }

    public final boolean isSticky() {
        return this.isSticky;
    }

    public final int getFinishPrimaryWithSecondary() {
        return this.finishPrimaryWithSecondary;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SplitPlaceholderRule(@NotNull Set<ActivityFilter> set, @NotNull Intent intent, boolean z, int i, int i2, int i3, float f, int i4) {
        super(i2, i3, f, i4);
        Intrinsics.checkNotNullParameter(set, "filters");
        Intrinsics.checkNotNullParameter(intent, "placeholderIntent");
        this.placeholderIntent = intent;
        this.isSticky = z;
        this.finishPrimaryWithSecondary = i;
        this.filters = CollectionsKt___CollectionsKt.toSet(set);
    }

    @NotNull
    public final Set<ActivityFilter> getFilters() {
        return this.filters;
    }

    @NotNull
    public final SplitPlaceholderRule plus$window_release(@NotNull ActivityFilter activityFilter) {
        Intrinsics.checkNotNullParameter(activityFilter, "filter");
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.addAll(this.filters);
        linkedHashSet.add(activityFilter);
        return new SplitPlaceholderRule(CollectionsKt___CollectionsKt.toSet(linkedHashSet), this.placeholderIntent, this.isSticky, this.finishPrimaryWithSecondary, getMinWidth(), getMinSmallestWidth(), getSplitRatio(), getLayoutDirection());
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SplitPlaceholderRule) || !super.equals(obj)) {
            return false;
        }
        SplitPlaceholderRule splitPlaceholderRule = (SplitPlaceholderRule) obj;
        return Intrinsics.areEqual(this.placeholderIntent, splitPlaceholderRule.placeholderIntent) && this.isSticky == splitPlaceholderRule.isSticky && this.finishPrimaryWithSecondary == splitPlaceholderRule.finishPrimaryWithSecondary && Intrinsics.areEqual(this.filters, splitPlaceholderRule.filters);
    }

    public int hashCode() {
        return (((((((super.hashCode() * 31) + this.placeholderIntent.hashCode()) * 31) + Boolean.hashCode(this.isSticky)) * 31) + Integer.hashCode(this.finishPrimaryWithSecondary)) * 31) + this.filters.hashCode();
    }
}
