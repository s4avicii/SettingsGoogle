package com.android.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.window.C0444R;
import com.android.settingslib.RestrictedTopLevelPreference;

public class RestrictedHomepagePreference extends RestrictedTopLevelPreference {
    public RestrictedHomepagePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(C0444R.C0450layout.homepage_preference);
    }

    public RestrictedHomepagePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(C0444R.C0450layout.homepage_preference);
    }

    public RestrictedHomepagePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(C0444R.C0450layout.homepage_preference);
    }

    public RestrictedHomepagePreference(Context context) {
        super(context);
        setLayoutResource(C0444R.C0450layout.homepage_preference);
    }
}
