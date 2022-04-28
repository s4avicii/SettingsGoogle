package com.android.settings.display;

import androidx.window.C0444R;

public class ScreenZoomPreferenceFragmentForSetupWizard extends ScreenZoomSettings {
    /* access modifiers changed from: protected */
    public int getActivityLayoutResId() {
        return C0444R.C0450layout.suw_screen_zoom_fragment;
    }

    public int getMetricsCategory() {
        return 370;
    }

    public void onStop() {
        if (this.mCurrentIndex != this.mInitialIndex) {
            this.mMetricsFeatureProvider.action(getContext(), 370, this.mCurrentIndex);
        }
        super.onStop();
    }
}
