package androidx.window.layout;

import android.graphics.Rect;
import androidx.window.core.Bounds;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: WindowMetrics.kt */
public final class WindowMetrics {
    @NotNull
    private final Bounds _bounds;

    public WindowMetrics(@NotNull Bounds bounds) {
        Intrinsics.checkNotNullParameter(bounds, "_bounds");
        this._bounds = bounds;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public WindowMetrics(@NotNull Rect rect) {
        this(new Bounds(rect));
        Intrinsics.checkNotNullParameter(rect, "bounds");
    }

    @NotNull
    public final Rect getBounds() {
        return this._bounds.toRect();
    }

    @NotNull
    public String toString() {
        return "WindowMetrics { bounds: " + getBounds() + " }";
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !Intrinsics.areEqual(WindowMetrics.class, obj.getClass())) {
            return false;
        }
        return Intrinsics.areEqual(this._bounds, ((WindowMetrics) obj)._bounds);
    }

    public int hashCode() {
        return this._bounds.hashCode();
    }
}
