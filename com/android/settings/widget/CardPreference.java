package com.android.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import androidx.window.C0444R;

public class CardPreference extends Preference {
    public CardPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public CardPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, C0444R.attr.cardPreferenceStyle);
    }
}
