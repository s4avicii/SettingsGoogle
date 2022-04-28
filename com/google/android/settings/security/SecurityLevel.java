package com.google.android.settings.security;

import androidx.window.C0444R;
import com.android.settingslib.widget.BannerMessagePreference;

enum SecurityLevel {
    SECURITY_LEVEL_UNKNOWN(C0444R.C0447drawable.ic_security_empty, C0444R.C0447drawable.ic_security_empty, r5),
    NONE(C0444R.C0447drawable.ic_security_null_state, C0444R.C0447drawable.ic_security_null_state, r5),
    INFORMATION(C0444R.C0447drawable.ic_security_info, C0444R.C0447drawable.ic_security_info_outline, r5),
    RECOMMENDATION(C0444R.C0447drawable.ic_security_recommendation, C0444R.C0447drawable.ic_security_recommendation_outline, BannerMessagePreference.AttentionLevel.MEDIUM),
    CRITICAL_WARNING(C0444R.C0447drawable.ic_security_warn, C0444R.C0447drawable.ic_security_warn_outline, BannerMessagePreference.AttentionLevel.HIGH);
    
    private final BannerMessagePreference.AttentionLevel mAttentionLevel;
    private final int mEntryIconResId;
    private final int mWarningCardIconResId;

    private SecurityLevel(int i, int i2, BannerMessagePreference.AttentionLevel attentionLevel) {
        this.mEntryIconResId = i;
        this.mWarningCardIconResId = i2;
        this.mAttentionLevel = attentionLevel;
    }

    public int getEntryIconResId() {
        return this.mEntryIconResId;
    }

    public int getWarningCardIconResId() {
        return this.mWarningCardIconResId;
    }

    public BannerMessagePreference.AttentionLevel getAttentionLevel() {
        return this.mAttentionLevel;
    }
}
