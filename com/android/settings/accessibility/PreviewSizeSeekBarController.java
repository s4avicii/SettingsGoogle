package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.widget.SeekBar;
import androidx.preference.PreferenceScreen;
import com.android.settings.accessibility.TextReadingResetController;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.widget.LabeledSeekBarPreference;

class PreviewSizeSeekBarController extends BasePreferenceController implements TextReadingResetController.ResetStateListener {
    /* access modifiers changed from: private */
    public ProgressInteractionListener mInteractionListener;
    private final SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            PreviewSizeSeekBarController.this.mInteractionListener.notifyPreferenceChanged();
            if (!PreviewSizeSeekBarController.this.mSeekByTouch && PreviewSizeSeekBarController.this.mInteractionListener != null) {
                PreviewSizeSeekBarController.this.mInteractionListener.onProgressChanged();
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            PreviewSizeSeekBarController.this.mSeekByTouch = true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            PreviewSizeSeekBarController.this.mSeekByTouch = false;
            if (PreviewSizeSeekBarController.this.mInteractionListener != null) {
                PreviewSizeSeekBarController.this.mInteractionListener.onEndTrackingTouch();
            }
        }
    };
    private LabeledSeekBarPreference mSeekBarPreference;
    /* access modifiers changed from: private */
    public boolean mSeekByTouch;
    private final PreviewSizeData<? extends Number> mSizeData;

    interface ProgressInteractionListener {
        void notifyPreferenceChanged();

        void onEndTrackingTouch();

        void onProgressChanged();
    }

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    PreviewSizeSeekBarController(Context context, String str, PreviewSizeData<? extends Number> previewSizeData) {
        super(context, str);
        this.mSizeData = previewSizeData;
    }

    /* access modifiers changed from: package-private */
    public void setInteractionListener(ProgressInteractionListener progressInteractionListener) {
        this.mInteractionListener = progressInteractionListener;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        int size = this.mSizeData.getValues().size();
        int initialIndex = this.mSizeData.getInitialIndex();
        LabeledSeekBarPreference labeledSeekBarPreference = (LabeledSeekBarPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mSeekBarPreference = labeledSeekBarPreference;
        labeledSeekBarPreference.setMax(size - 1);
        this.mSeekBarPreference.setProgress(initialIndex);
        this.mSeekBarPreference.setContinuousUpdates(true);
        this.mSeekBarPreference.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
    }

    public void resetState() {
        this.mSeekBarPreference.setProgress(this.mSizeData.getValues().indexOf(this.mSizeData.getDefaultValue()));
    }
}
