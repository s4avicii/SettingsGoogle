package com.google.android.settings.security;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.window.C0444R;
import com.google.android.material.card.MaterialCardView;

public class ShowMoreWarningsPreference extends Preference {
    private int mBackgroundColor;

    public ShowMoreWarningsPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public ShowMoreWarningsPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(C0444R.C0450layout.preference_security_show_warnings);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((TextView) preferenceViewHolder.findViewById(C0444R.C0448id.show_more_title)).setText(getTitle());
        ((MaterialCardView) preferenceViewHolder.itemView).setCardBackgroundColor(this.mBackgroundColor);
    }

    public void setCardBackgroundColor(int i) {
        this.mBackgroundColor = i;
        notifyChanged();
    }
}
