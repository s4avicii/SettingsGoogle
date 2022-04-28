package com.android.settings.accessibility;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.C0444R;
import com.android.settingslib.Utils;
import com.google.android.setupdesign.GlifPreferenceLayout;

public class TextReadingPreferenceFragmentForSetupWizard extends TextReadingPreferenceFragment {
    public int getHelpResource() {
        return 0;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        String string = getContext().getString(C0444R.string.accessibility_text_reading_options_title);
        Drawable drawable = getContext().getDrawable(C0444R.C0447drawable.ic_font_download);
        drawable.setTintList(Utils.getColorAttr(getContext(), 16843827));
        AccessibilitySetupWizardUtils.updateGlifPreferenceLayout(getContext(), (GlifPreferenceLayout) view, string, (CharSequence) null, drawable);
    }

    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return ((GlifPreferenceLayout) viewGroup).onCreateRecyclerView(layoutInflater, viewGroup, bundle);
    }

    public int getMetricsCategory() {
        return super.getMetricsCategory();
    }
}
