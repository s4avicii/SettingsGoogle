package androidx.window.layout;

import android.app.Activity;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: WindowInfoTrackerImpl.kt */
public final class WindowInfoTrackerImpl implements WindowInfoTracker {
    private static final int BUFFER_CAPACITY = 10;
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    @NotNull
    public final WindowBackend windowBackend;
    @NotNull
    private final WindowMetricsCalculator windowMetricsCalculator;

    public WindowInfoTrackerImpl(@NotNull WindowMetricsCalculator windowMetricsCalculator2, @NotNull WindowBackend windowBackend2) {
        Intrinsics.checkNotNullParameter(windowMetricsCalculator2, "windowMetricsCalculator");
        Intrinsics.checkNotNullParameter(windowBackend2, "windowBackend");
        this.windowMetricsCalculator = windowMetricsCalculator2;
        this.windowBackend = windowBackend2;
    }

    @NotNull
    public Flow<WindowLayoutInfo> windowLayoutInfo(@NotNull Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        return FlowKt.flow(new WindowInfoTrackerImpl$windowLayoutInfo$1(this, activity, (Continuation<? super WindowInfoTrackerImpl$windowLayoutInfo$1>) null));
    }

    /* compiled from: WindowInfoTrackerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
