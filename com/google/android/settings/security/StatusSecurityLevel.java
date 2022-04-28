package com.google.android.settings.security;

import androidx.window.C0444R;

enum StatusSecurityLevel {
    STATUS_SECURITY_LEVEL_UNKNOWN(C0444R.C0447drawable.security_status_info),
    INFORMATION_NO_ISSUES(C0444R.C0447drawable.security_status_info),
    INFORMATION_REVIEW_ISSUES(C0444R.C0447drawable.security_status_info_review),
    RECOMMENDATION(C0444R.C0447drawable.security_status_recommendation),
    CRITICAL_WARNING(C0444R.C0447drawable.security_status_warn);
    
    private final int mImageResId;

    private StatusSecurityLevel(int i) {
        this.mImageResId = i;
    }

    public int getImageResId() {
        return this.mImageResId;
    }
}
