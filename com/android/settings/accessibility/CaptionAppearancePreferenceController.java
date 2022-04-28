package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.view.accessibility.CaptioningManager;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;

public class CaptionAppearancePreferenceController extends BasePreferenceController {
    private final CaptioningManager mCaptioningManager;

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

    public CaptionAppearancePreferenceController(Context context, String str) {
        super(context, str);
        this.mCaptioningManager = (CaptioningManager) context.getSystemService(CaptioningManager.class);
    }

    public CharSequence getSummary() {
        return this.mContext.getString(C0444R.string.preference_summary_default_combination, new Object[]{geFontScaleSummary(), getPresetSummary()});
    }

    private float[] getFontScaleValuesArray() {
        String[] stringArray = this.mContext.getResources().getStringArray(C0444R.array.captioning_font_size_selector_values);
        int length = stringArray.length;
        float[] fArr = new float[length];
        for (int i = 0; i < length; i++) {
            fArr[i] = Float.parseFloat(stringArray[i]);
        }
        return fArr;
    }

    private CharSequence geFontScaleSummary() {
        float[] fontScaleValuesArray = getFontScaleValuesArray();
        String[] stringArray = this.mContext.getResources().getStringArray(C0444R.array.captioning_font_size_selector_titles);
        int indexOf = Floats.indexOf(fontScaleValuesArray, this.mCaptioningManager.getFontScale());
        if (indexOf == -1) {
            indexOf = 0;
        }
        return stringArray[indexOf];
    }

    private CharSequence getPresetSummary() {
        return this.mContext.getResources().getStringArray(C0444R.array.captioning_preset_selector_titles)[Ints.indexOf(this.mContext.getResources().getIntArray(C0444R.array.captioning_preset_selector_values), this.mCaptioningManager.getRawUserStyle())];
    }
}
