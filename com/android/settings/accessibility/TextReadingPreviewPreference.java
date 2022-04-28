package com.android.settings.accessibility;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.viewpager.widget.ViewPager;
import androidx.window.C0444R;
import com.android.internal.util.Preconditions;
import com.android.settings.display.PreviewPagerAdapter;
import com.android.settings.widget.DotsPageIndicator;

public class TextReadingPreviewPreference extends Preference {
    private int mCurrentItem;
    private int mLastLayerIndex;
    private PreviewPagerAdapter mPreviewAdapter;

    TextReadingPreviewPreference(Context context) {
        super(context);
        init();
    }

    public TextReadingPreviewPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    TextReadingPreviewPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    TextReadingPreviewPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ViewPager viewPager = (ViewPager) preferenceViewHolder.findViewById(C0444R.C0448id.preview_pager);
        DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) preferenceViewHolder.findViewById(C0444R.C0448id.page_indicator);
        updateAdapterIfNeeded(viewPager, dotsPageIndicator, this.mPreviewAdapter);
        updatePagerAndIndicator(viewPager, dotsPageIndicator);
    }

    /* access modifiers changed from: package-private */
    public void setPreviewAdapter(PreviewPagerAdapter previewPagerAdapter) {
        if (previewPagerAdapter != this.mPreviewAdapter) {
            this.mPreviewAdapter = previewPagerAdapter;
            notifyChanged();
        }
    }

    /* access modifiers changed from: package-private */
    public int getCurrentItem() {
        return this.mCurrentItem;
    }

    private void updateAdapterIfNeeded(ViewPager viewPager, DotsPageIndicator dotsPageIndicator, PreviewPagerAdapter previewPagerAdapter) {
        if (viewPager.getAdapter() != previewPagerAdapter) {
            viewPager.setAdapter(previewPagerAdapter);
            if (previewPagerAdapter != null) {
                dotsPageIndicator.setViewPager(viewPager);
            } else {
                this.mCurrentItem = 0;
            }
        }
    }

    private void updatePagerAndIndicator(ViewPager viewPager, DotsPageIndicator dotsPageIndicator) {
        if (viewPager.getAdapter() != null) {
            int currentItem = viewPager.getCurrentItem();
            int i = this.mCurrentItem;
            if (currentItem != i) {
                viewPager.setCurrentItem(i);
            }
            dotsPageIndicator.setVisibility(viewPager.getAdapter().getCount() > 1 ? 0 : 8);
        }
    }

    private void init() {
        setLayoutResource(C0444R.C0450layout.accessibility_text_reading_preview);
    }

    /* access modifiers changed from: package-private */
    public void notifyPreviewPagerChanged(int i) {
        Preconditions.checkNotNull(this.mPreviewAdapter, "Preview adapter is null, you should init the preview adapter first");
        int i2 = this.mLastLayerIndex;
        if (i != i2) {
            this.mPreviewAdapter.setPreviewLayer(i, i2, getCurrentItem(), false);
        }
        this.mLastLayerIndex = i;
    }
}
