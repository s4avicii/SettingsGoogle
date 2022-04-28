package com.google.android.settings.gestures.columbus;

import android.content.Context;
import android.widget.ImageView;
import androidx.preference.PreferenceViewHolder;
import androidx.window.C0444R;
import com.android.settingslib.widget.SelectorWithWidgetPreference;

public class ColumbusRadioButtonPreference extends SelectorWithWidgetPreference {
    private ContextualSummaryProvider mContextualSummaryProvider;
    private ImageView mExtraWidgetView;
    private int mMetric;

    public interface ContextualSummaryProvider {
        CharSequence getSummary(Context context);
    }

    public ColumbusRadioButtonPreference(Context context) {
        super(context);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mExtraWidgetView = (ImageView) preferenceViewHolder.findViewById(C0444R.C0448id.selector_extra_widget);
        updateAccessibilityDescription();
    }

    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        updateAccessibilityDescription();
    }

    /* access modifiers changed from: package-private */
    public void setMetric(int i) {
        this.mMetric = i;
    }

    /* access modifiers changed from: package-private */
    public int getMetric() {
        return this.mMetric;
    }

    /* access modifiers changed from: package-private */
    public void setContextualSummaryProvider(ContextualSummaryProvider contextualSummaryProvider) {
        this.mContextualSummaryProvider = contextualSummaryProvider;
    }

    /* access modifiers changed from: package-private */
    public void updateSummary(Context context) {
        ContextualSummaryProvider contextualSummaryProvider = this.mContextualSummaryProvider;
        if (contextualSummaryProvider == null) {
            setSummary((CharSequence) null);
        } else {
            setSummary(contextualSummaryProvider.getSummary(context));
        }
    }

    private void updateAccessibilityDescription() {
        ImageView imageView = this.mExtraWidgetView;
        if (imageView != null) {
            imageView.setContentDescription(getContext().getString(C0444R.string.columbus_radio_button_extra_widget_a11y_label, new Object[]{getTitle()}));
        }
    }
}
