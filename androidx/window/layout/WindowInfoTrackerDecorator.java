package androidx.window.layout;

import org.jetbrains.annotations.NotNull;

/* compiled from: WindowInfoTracker.kt */
public interface WindowInfoTrackerDecorator {
    @NotNull
    WindowInfoTracker decorate(@NotNull WindowInfoTracker windowInfoTracker);
}
