package com.android.settings.dream;

import android.graphics.drawable.Drawable;

public interface IDreamItem {
    boolean allowCustomization() {
        return false;
    }

    Drawable getIcon();

    Drawable getPreviewImage();

    CharSequence getTitle();

    boolean isActive();

    void onCustomizeClicked() {
    }

    void onItemClicked();
}
