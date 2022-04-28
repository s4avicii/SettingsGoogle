package com.android.settings.display;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.window.C0444R;

public class FontSizePreferenceFragmentForSetupWizard extends ToggleFontSizePreferenceFragment {
    /* access modifiers changed from: protected */
    public int getActivityLayoutResId() {
        return C0444R.C0450layout.suw_font_size_fragment;
    }

    public int getMetricsCategory() {
        return 369;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        if (getResources().getBoolean(C0444R.bool.config_supported_large_screen)) {
            ViewPager viewPager = (ViewPager) onCreateView.findViewById(C0444R.C0448id.preview_pager);
            LinearLayout linearLayout = (LinearLayout) ((View) viewPager.getAdapter().instantiateItem((ViewGroup) viewPager, viewPager.getCurrentItem())).findViewById(C0444R.C0448id.font_size_preview_text_group);
            linearLayout.setPaddingRelative(getResources().getDimensionPixelSize(C0444R.dimen.font_size_preview_padding_start), linearLayout.getPaddingTop(), linearLayout.getPaddingEnd(), linearLayout.getPaddingBottom());
        }
        return onCreateView;
    }

    public void onStop() {
        if (this.mCurrentIndex != this.mInitialIndex) {
            this.mMetricsFeatureProvider.action(getContext(), 369, this.mCurrentIndex);
        }
        super.onStop();
    }
}
