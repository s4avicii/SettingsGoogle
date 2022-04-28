package com.android.settings.accessibility;

import android.content.ComponentName;
import com.android.settings.core.instrumentation.SettingsStatsLog;

public final class AccessibilityStatsLogUtils {
    private static int convertToLoggingServiceEnabled(boolean z) {
        return z ? 1 : 2;
    }

    static void logAccessibilityServiceEnabled(ComponentName componentName, boolean z) {
        SettingsStatsLog.write(267, componentName.flattenToString(), convertToLoggingServiceEnabled(z));
    }

    static void logDisableNonA11yCategoryService(String str, long j) {
        com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logNonA11yToolServiceWarningReported(str, com.android.internal.accessibility.util.AccessibilityStatsLogUtils.ACCESSIBILITY_PRIVACY_WARNING_STATUS_SERVICE_DISABLED, j);
    }
}
