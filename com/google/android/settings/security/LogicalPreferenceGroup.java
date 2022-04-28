package com.google.android.settings.security;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.PreferenceGroup;
import androidx.window.C0444R;

public class LogicalPreferenceGroup extends PreferenceGroup {
    public LogicalPreferenceGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(C0444R.C0450layout.security_logical_preference_group);
    }
}
