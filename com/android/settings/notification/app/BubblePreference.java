package com.android.settings.notification.app;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.window.C0444R;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedPreferenceHelper;
import com.android.settingslib.Utils;

public class BubblePreference extends Preference implements View.OnClickListener {
    private ButtonViewHolder mBubbleAllButton;
    private ButtonViewHolder mBubbleNoneButton;
    private ButtonViewHolder mBubbleSelectedButton;
    /* access modifiers changed from: private */
    public Context mContext;
    RestrictedPreferenceHelper mHelper;
    private int mSelectedPreference;
    private boolean mSelectedVisible;

    public BubblePreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public BubblePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BubblePreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public BubblePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        RestrictedPreferenceHelper restrictedPreferenceHelper = new RestrictedPreferenceHelper(context, this, attributeSet);
        this.mHelper = restrictedPreferenceHelper;
        restrictedPreferenceHelper.useAdminDisabledSummary(true);
        this.mContext = context;
        setLayoutResource(C0444R.C0450layout.bubble_preference);
    }

    public void setSelectedPreference(int i) {
        this.mSelectedPreference = i;
    }

    public int getSelectedPreference() {
        return this.mSelectedPreference;
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        if (this.mHelper.setDisabledByAdmin(enforcedAdmin)) {
            notifyChanged();
        }
    }

    public void setSelectedVisibility(boolean z) {
        this.mSelectedVisible = z;
        notifyChanged();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        boolean isDisabledByAdmin = this.mHelper.isDisabledByAdmin();
        View findViewById = preferenceViewHolder.findViewById(16908304);
        int i = 8;
        if (isDisabledByAdmin) {
            this.mHelper.onBindViewHolder(preferenceViewHolder);
            findViewById.setVisibility(0);
        } else {
            findViewById.setVisibility(8);
        }
        preferenceViewHolder.itemView.setClickable(false);
        View findViewById2 = preferenceViewHolder.findViewById(C0444R.C0448id.bubble_all);
        ButtonViewHolder buttonViewHolder = new ButtonViewHolder(findViewById2, (ImageView) preferenceViewHolder.findViewById(C0444R.C0448id.bubble_all_icon), (TextView) preferenceViewHolder.findViewById(C0444R.C0448id.bubble_all_label), 1);
        this.mBubbleAllButton = buttonViewHolder;
        boolean z = true;
        buttonViewHolder.setSelected(this.mContext, this.mSelectedPreference == 1);
        findViewById2.setTag(1);
        findViewById2.setOnClickListener(this);
        findViewById2.setVisibility(isDisabledByAdmin ? 8 : 0);
        View findViewById3 = preferenceViewHolder.findViewById(C0444R.C0448id.bubble_selected);
        ButtonViewHolder buttonViewHolder2 = new ButtonViewHolder(findViewById3, (ImageView) preferenceViewHolder.findViewById(C0444R.C0448id.bubble_selected_icon), (TextView) preferenceViewHolder.findViewById(C0444R.C0448id.bubble_selected_label), 2);
        this.mBubbleSelectedButton = buttonViewHolder2;
        buttonViewHolder2.setSelected(this.mContext, this.mSelectedPreference == 2);
        findViewById3.setTag(2);
        findViewById3.setOnClickListener(this);
        findViewById3.setVisibility((!this.mSelectedVisible || isDisabledByAdmin) ? 8 : 0);
        View findViewById4 = preferenceViewHolder.findViewById(C0444R.C0448id.bubble_none);
        ButtonViewHolder buttonViewHolder3 = new ButtonViewHolder(findViewById4, (ImageView) preferenceViewHolder.findViewById(C0444R.C0448id.bubble_none_icon), (TextView) preferenceViewHolder.findViewById(C0444R.C0448id.bubble_none_label), 0);
        this.mBubbleNoneButton = buttonViewHolder3;
        Context context = this.mContext;
        if (this.mSelectedPreference != 0) {
            z = false;
        }
        buttonViewHolder3.setSelected(context, z);
        findViewById4.setTag(0);
        findViewById4.setOnClickListener(this);
        if (!isDisabledByAdmin) {
            i = 0;
        }
        findViewById4.setVisibility(i);
    }

    public void onClick(View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        callChangeListener(Integer.valueOf(intValue));
        boolean z = false;
        this.mBubbleAllButton.setSelected(this.mContext, intValue == 1);
        this.mBubbleSelectedButton.setSelected(this.mContext, intValue == 2);
        ButtonViewHolder buttonViewHolder = this.mBubbleNoneButton;
        Context context = this.mContext;
        if (intValue == 0) {
            z = true;
        }
        buttonViewHolder.setSelected(context, z);
    }

    private class ButtonViewHolder {
        private int mId;
        private ImageView mImageView;
        private TextView mTextView;
        private View mView;

        ButtonViewHolder(View view, ImageView imageView, TextView textView, int i) {
            this.mView = view;
            this.mImageView = imageView;
            this.mTextView = textView;
            this.mId = i;
        }

        /* access modifiers changed from: package-private */
        public void setSelected(Context context, boolean z) {
            ColorStateList colorStateList;
            this.mView.setBackground(BubblePreference.this.mContext.getDrawable(z ? C0444R.C0447drawable.button_border_selected : C0444R.C0447drawable.button_border_unselected));
            this.mView.setSelected(z);
            if (z) {
                colorStateList = Utils.getColorAccent(context);
            } else {
                colorStateList = Utils.getColorAttr(context, 16842806);
            }
            this.mImageView.setImageTintList(colorStateList);
            this.mTextView.setTextColor(colorStateList);
        }
    }
}
