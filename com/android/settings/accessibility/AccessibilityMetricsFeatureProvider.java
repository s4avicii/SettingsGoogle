package com.android.settings.accessibility;

import android.content.ComponentName;

public interface AccessibilityMetricsFeatureProvider {
    int getDownloadedFeatureMetricsCategory(ComponentName componentName);
}
