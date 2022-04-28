package com.android.settings.accessibility;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.C0444R;
import com.google.android.setupdesign.GlifPreferenceLayout;

public class ToggleScreenMagnificationPreferenceFragmentForSetupWizard extends ToggleScreenMagnificationPreferenceFragment {
    public int getHelpResource() {
        return 0;
    }

    public int getMetricsCategory() {
        return 368;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        String string = getContext().getString(C0444R.string.accessibility_screen_magnification_title);
        String string2 = getContext().getString(C0444R.string.accessibility_preference_magnification_summary);
        Drawable drawable = getContext().getDrawable(C0444R.C0447drawable.ic_accessibility_visibility);
        AccessibilitySetupWizardUtils.updateGlifPreferenceLayout(getContext(), (GlifPreferenceLayout) view, string, string2, drawable);
        hidePreferenceSettingComponents();
    }

    private void hidePreferenceSettingComponents() {
        this.mSettingsPreference.setVisible(false);
        this.mFollowingTypingSwitchPreference.setVisible(false);
    }

    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return ((GlifPreferenceLayout) viewGroup).onCreateRecyclerView(layoutInflater, viewGroup, bundle);
    }

    public void onStop() {
        Bundle arguments = getArguments();
        if (!(arguments == null || !arguments.containsKey("checked") || this.mToggleServiceSwitchPreference.isChecked() == arguments.getBoolean("checked"))) {
            this.mMetricsFeatureProvider.action(getContext(), 368, this.mToggleServiceSwitchPreference.isChecked());
        }
        super.onStop();
    }
}
