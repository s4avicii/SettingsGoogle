package com.android.settings.widget;

import android.content.Context;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.RestrictedPreferenceHelper;
import com.android.settingslib.widget.AppPreference;

public class RestrictedAppPreference extends AppPreference {
    private RestrictedPreferenceHelper mHelper;
    private String userRestriction;

    public RestrictedAppPreference(Context context, String str) {
        super(context);
        initialize((AttributeSet) null, str);
    }

    private void initialize(AttributeSet attributeSet, String str) {
        this.mHelper = new RestrictedPreferenceHelper(getContext(), this, attributeSet);
        this.userRestriction = str;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mHelper.onBindViewHolder(preferenceViewHolder);
    }

    public void performClick() {
        if (!this.mHelper.performClick()) {
            super.performClick();
        }
    }

    public void setEnabled(boolean z) {
        if (!isDisabledByAdmin() || !z) {
            super.setEnabled(z);
        }
    }

    public boolean isDisabledByAdmin() {
        return this.mHelper.isDisabledByAdmin();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        this.mHelper.onAttachedToHierarchy();
        super.onAttachedToHierarchy(preferenceManager);
    }

    public void checkRestrictionAndSetDisabled() {
        if (!TextUtils.isEmpty(this.userRestriction)) {
            this.mHelper.checkRestrictionAndSetDisabled(this.userRestriction, UserHandle.myUserId());
        }
    }
}
