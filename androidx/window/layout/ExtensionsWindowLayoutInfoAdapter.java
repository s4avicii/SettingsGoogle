package androidx.window.layout;

import android.app.Activity;
import android.graphics.Rect;
import androidx.window.core.Bounds;
import androidx.window.extensions.layout.FoldingFeature;
import androidx.window.extensions.layout.WindowLayoutInfo;
import androidx.window.layout.FoldingFeature;
import androidx.window.layout.HardwareFoldingFeature;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ExtensionsWindowLayoutInfoAdapter.kt */
public final class ExtensionsWindowLayoutInfoAdapter {
    @NotNull
    public static final ExtensionsWindowLayoutInfoAdapter INSTANCE = new ExtensionsWindowLayoutInfoAdapter();

    private ExtensionsWindowLayoutInfoAdapter() {
    }

    @Nullable
    public final FoldingFeature translate$window_release(@NotNull Activity activity, @NotNull FoldingFeature foldingFeature) {
        HardwareFoldingFeature.Type type;
        FoldingFeature.State state;
        Intrinsics.checkNotNullParameter(activity, "activity");
        Intrinsics.checkNotNullParameter(foldingFeature, "oemFeature");
        int type2 = foldingFeature.getType();
        if (type2 == 1) {
            type = HardwareFoldingFeature.Type.Companion.getFOLD();
        } else if (type2 != 2) {
            return null;
        } else {
            type = HardwareFoldingFeature.Type.Companion.getHINGE();
        }
        int state2 = foldingFeature.getState();
        if (state2 == 1) {
            state = FoldingFeature.State.FLAT;
        } else if (state2 != 2) {
            return null;
        } else {
            state = FoldingFeature.State.HALF_OPENED;
        }
        Rect bounds = foldingFeature.getBounds();
        Intrinsics.checkNotNullExpressionValue(bounds, "oemFeature.bounds");
        if (!validBounds(activity, new Bounds(bounds))) {
            return null;
        }
        Rect bounds2 = foldingFeature.getBounds();
        Intrinsics.checkNotNullExpressionValue(bounds2, "oemFeature.bounds");
        return new HardwareFoldingFeature(new Bounds(bounds2), type, state);
    }

    @NotNull
    public final WindowLayoutInfo translate$window_release(@NotNull Activity activity, @NotNull WindowLayoutInfo windowLayoutInfo) {
        FoldingFeature foldingFeature;
        Intrinsics.checkNotNullParameter(activity, "activity");
        Intrinsics.checkNotNullParameter(windowLayoutInfo, "info");
        List<androidx.window.extensions.layout.FoldingFeature> displayFeatures = windowLayoutInfo.getDisplayFeatures();
        Intrinsics.checkNotNullExpressionValue(displayFeatures, "info.displayFeatures");
        ArrayList arrayList = new ArrayList();
        for (androidx.window.extensions.layout.FoldingFeature foldingFeature2 : displayFeatures) {
            if (foldingFeature2 instanceof androidx.window.extensions.layout.FoldingFeature) {
                ExtensionsWindowLayoutInfoAdapter extensionsWindowLayoutInfoAdapter = INSTANCE;
                Intrinsics.checkNotNullExpressionValue(foldingFeature2, "feature");
                foldingFeature = extensionsWindowLayoutInfoAdapter.translate$window_release(activity, foldingFeature2);
            } else {
                foldingFeature = null;
            }
            if (foldingFeature != null) {
                arrayList.add(foldingFeature);
            }
        }
        return new WindowLayoutInfo(arrayList);
    }

    private final boolean validBounds(Activity activity, Bounds bounds) {
        Rect bounds2 = WindowMetricsCalculatorCompat.INSTANCE.computeCurrentWindowMetrics(activity).getBounds();
        if (bounds.isZero()) {
            return false;
        }
        if (bounds.getWidth() != bounds2.width() && bounds.getHeight() != bounds2.height()) {
            return false;
        }
        if (bounds.getWidth() < bounds2.width() && bounds.getHeight() < bounds2.height()) {
            return false;
        }
        if (bounds.getWidth() == bounds2.width() && bounds.getHeight() == bounds2.height()) {
            return false;
        }
        return true;
    }
}
