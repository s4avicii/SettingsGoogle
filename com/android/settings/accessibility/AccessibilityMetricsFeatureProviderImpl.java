package com.android.settings.accessibility;

import android.content.ComponentName;

public class AccessibilityMetricsFeatureProviderImpl implements AccessibilityMetricsFeatureProvider {
    public int getDownloadedFeatureMetricsCategory(ComponentName componentName) {
        return 4;
    }
}
