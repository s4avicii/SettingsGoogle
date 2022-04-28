package androidx.window.layout;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: WindowInfoTracker.kt */
final class EmptyDecorator implements WindowInfoTrackerDecorator {
    @NotNull
    public static final EmptyDecorator INSTANCE = new EmptyDecorator();

    @NotNull
    public WindowInfoTracker decorate(@NotNull WindowInfoTracker windowInfoTracker) {
        Intrinsics.checkNotNullParameter(windowInfoTracker, "tracker");
        return windowInfoTracker;
    }

    private EmptyDecorator() {
    }
}
