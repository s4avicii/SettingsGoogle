package androidx.window.layout;

import androidx.window.core.ExperimentalWindowApi;
import org.jetbrains.annotations.NotNull;

@ExperimentalWindowApi
/* compiled from: WindowMetricsCalculator.kt */
public interface WindowMetricsCalculatorDecorator {
    @NotNull
    @ExperimentalWindowApi
    WindowMetricsCalculator decorate(@NotNull WindowMetricsCalculator windowMetricsCalculator);
}
