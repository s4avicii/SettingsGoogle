package com.android.settings.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.C0444R;
import androidx.window.embedding.SplitController;
import com.android.settings.homepage.SettingsHomepageActivity;
import com.android.settingslib.Utils;

public class HighlightableTopLevelPreferenceAdapter extends PreferenceGroupAdapter implements SettingsHomepageActivity.HomepageLoadedListener {
    private final Context mContext;
    private final int mHighlightBackgroundRes;
    private String mHighlightKey;
    private boolean mHighlightNeeded;
    private int mHighlightPosition = -1;
    private final SettingsHomepageActivity mHomepageActivity;
    private final int mIconColorHighlight;
    private final int mIconColorNormal;
    private final int mNormalBackgroundRes;
    private final RecyclerView mRecyclerView;
    private int mScrollPosition = -1;
    private boolean mScrolled;
    private final int mSummaryColorHighlight;
    private final int mSummaryColorNormal;
    private final int mTitleColorHighlight;
    private final int mTitleColorNormal;
    private SparseArray<PreferenceViewHolder> mViewHolders;

    public HighlightableTopLevelPreferenceAdapter(SettingsHomepageActivity settingsHomepageActivity, PreferenceGroup preferenceGroup, RecyclerView recyclerView, String str) {
        super(preferenceGroup);
        this.mRecyclerView = recyclerView;
        this.mHighlightKey = str;
        this.mViewHolders = new SparseArray<>();
        Context context = preferenceGroup.getContext();
        this.mContext = context;
        this.mHomepageActivity = settingsHomepageActivity;
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843534, typedValue, true);
        this.mNormalBackgroundRes = typedValue.resourceId;
        this.mHighlightBackgroundRes = C0444R.C0447drawable.homepage_highlighted_item_background;
        this.mTitleColorNormal = Utils.getColorAttrDefaultColor(context, 16842806);
        this.mTitleColorHighlight = Utils.getColorAttrDefaultColor(context, 16842809);
        this.mSummaryColorNormal = Utils.getColorAttrDefaultColor(context, 16842808);
        this.mSummaryColorHighlight = Utils.getColorAttrDefaultColor(context, 16842810);
        this.mIconColorNormal = com.android.settings.Utils.getHomepageIconColor(context);
        this.mIconColorHighlight = com.android.settings.Utils.getHomepageIconColorHighlight(context);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder, int i) {
        super.onBindViewHolder(preferenceViewHolder, i);
        this.mViewHolders.put(i, preferenceViewHolder);
        updateBackground(preferenceViewHolder, i);
    }

    /* access modifiers changed from: package-private */
    public void updateBackground(PreferenceViewHolder preferenceViewHolder, int i) {
        String str;
        if (!isHighlightNeeded()) {
            removeHighlightBackground(preferenceViewHolder);
        } else if (i != this.mHighlightPosition || (str = this.mHighlightKey) == null || !TextUtils.equals(str, getItem(i).getKey())) {
            removeHighlightBackground(preferenceViewHolder);
        } else {
            addHighlightBackground(preferenceViewHolder);
        }
    }

    public void requestHighlight() {
        if (this.mRecyclerView != null) {
            int i = this.mHighlightPosition;
            if (TextUtils.isEmpty(this.mHighlightKey)) {
                this.mHighlightPosition = -1;
                this.mScrolled = true;
                if (i >= 0) {
                    notifyItemChanged(i);
                    return;
                }
                return;
            }
            int preferenceAdapterPosition = getPreferenceAdapterPosition(this.mHighlightKey);
            if (preferenceAdapterPosition >= 0) {
                boolean isHighlightNeeded = isHighlightNeeded();
                if (isHighlightNeeded) {
                    this.mScrollPosition = preferenceAdapterPosition;
                    lambda$scroll$0();
                }
                if (isHighlightNeeded != this.mHighlightNeeded) {
                    Log.d("HighlightableTopLevelAdapter", "Highlight needed change: " + isHighlightNeeded);
                    this.mHighlightNeeded = isHighlightNeeded;
                    this.mHighlightPosition = preferenceAdapterPosition;
                    notifyItemChanged(preferenceAdapterPosition);
                    if (!isHighlightNeeded) {
                        removeHighlightAt(i);
                    }
                } else if (preferenceAdapterPosition != this.mHighlightPosition) {
                    this.mHighlightPosition = preferenceAdapterPosition;
                    Log.d("HighlightableTopLevelAdapter", "Request highlight position " + preferenceAdapterPosition);
                    Log.d("HighlightableTopLevelAdapter", "Is highlight needed: " + isHighlightNeeded);
                    if (isHighlightNeeded) {
                        notifyItemChanged(preferenceAdapterPosition);
                        if (i >= 0) {
                            notifyItemChanged(i);
                        }
                    }
                }
            }
        }
    }

    public void highlightPreference(String str, boolean z) {
        this.mHighlightKey = str;
        this.mScrolled = !z;
        requestHighlight();
    }

    public void onHomepageLoaded() {
        lambda$scroll$0();
    }

    /* access modifiers changed from: private */
    /* renamed from: scroll */
    public void lambda$scroll$0() {
        if (!this.mScrolled && this.mScrollPosition >= 0 && !this.mHomepageActivity.addHomepageLoadedListener(this)) {
            View childAt = this.mRecyclerView.getChildAt(this.mScrollPosition);
            if (childAt == null) {
                this.mRecyclerView.postDelayed(new HighlightableTopLevelPreferenceAdapter$$ExternalSyntheticLambda0(this), 100);
                return;
            }
            this.mScrolled = true;
            Log.d("HighlightableTopLevelAdapter", "Scroll to position " + this.mScrollPosition);
            RecyclerView recyclerView = this.mRecyclerView;
            recyclerView.nestedScrollBy(0, -recyclerView.getHeight());
            int top = childAt.getTop();
            if (top > 0) {
                this.mRecyclerView.nestedScrollBy(0, top);
            }
        }
    }

    private void removeHighlightAt(int i) {
        if (i >= 0) {
            PreferenceViewHolder preferenceViewHolder = this.mViewHolders.get(i);
            if (preferenceViewHolder != null) {
                removeHighlightBackground(preferenceViewHolder);
            }
            notifyItemChanged(i);
        }
    }

    private void addHighlightBackground(PreferenceViewHolder preferenceViewHolder) {
        View view = preferenceViewHolder.itemView;
        view.setBackgroundResource(this.mHighlightBackgroundRes);
        ((TextView) view.findViewById(16908310)).setTextColor(this.mTitleColorHighlight);
        ((TextView) view.findViewById(16908304)).setTextColor(this.mSummaryColorHighlight);
        Drawable drawable = ((ImageView) view.findViewById(16908294)).getDrawable();
        if (drawable != null) {
            drawable.setTint(this.mIconColorHighlight);
        }
    }

    private void removeHighlightBackground(PreferenceViewHolder preferenceViewHolder) {
        View view = preferenceViewHolder.itemView;
        view.setBackgroundResource(this.mNormalBackgroundRes);
        ((TextView) view.findViewById(16908310)).setTextColor(this.mTitleColorNormal);
        ((TextView) view.findViewById(16908304)).setTextColor(this.mSummaryColorNormal);
        Drawable drawable = ((ImageView) view.findViewById(16908294)).getDrawable();
        if (drawable != null) {
            drawable.setTint(this.mIconColorNormal);
        }
    }

    private boolean isHighlightNeeded() {
        return SplitController.getInstance().isActivityEmbedded(this.mHomepageActivity);
    }
}
