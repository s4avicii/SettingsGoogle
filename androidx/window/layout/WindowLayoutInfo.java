package androidx.window.layout;

import java.util.List;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: WindowLayoutInfo.kt */
public final class WindowLayoutInfo {
    @NotNull
    private final List<DisplayFeature> displayFeatures;

    @NotNull
    public final List<DisplayFeature> getDisplayFeatures() {
        return this.displayFeatures;
    }

    public WindowLayoutInfo(@NotNull List<? extends DisplayFeature> list) {
        Intrinsics.checkNotNullParameter(list, "displayFeatures");
        this.displayFeatures = list;
    }

    @NotNull
    public String toString() {
        return CollectionsKt___CollectionsKt.joinToString$default(this.displayFeatures, ", ", "WindowLayoutInfo{ DisplayFeatures[", "] }", 0, (CharSequence) null, (Function1) null, 56, (Object) null);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !Intrinsics.areEqual(WindowLayoutInfo.class, obj.getClass())) {
            return false;
        }
        return Intrinsics.areEqual(this.displayFeatures, ((WindowLayoutInfo) obj).displayFeatures);
    }

    public int hashCode() {
        return this.displayFeatures.hashCode();
    }
}
