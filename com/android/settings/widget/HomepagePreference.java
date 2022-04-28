package com.android.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import androidx.window.C0444R;

public class HomepagePreference extends Preference {
    public HomepagePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(C0444R.C0450layout.homepage_preference);
    }

    public HomepagePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(C0444R.C0450layout.homepage_preference);
    }

    public HomepagePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(C0444R.C0450layout.homepage_preference);
    }

    public HomepagePreference(Context context) {
        super(context);
        setLayoutResource(C0444R.C0450layout.homepage_preference);
    }
}
