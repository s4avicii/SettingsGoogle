package com.google.android.settings.connecteddevice.dock;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.window.C0444R;
import com.android.settingslib.widget.AdaptiveIcon;
import com.google.common.base.Preconditions;

public class DockUtils {
    static Drawable buildRainbowIcon(Context context, String str) {
        Preconditions.checkNotNull(context, "Preference context cannot be null");
        Resources resources = context.getResources();
        Drawable drawable = context.getDrawable(C0444R.C0447drawable.ic_dock_24dp);
        int[] intArray = resources.getIntArray(C0444R.array.wlc_icon_fg_colors);
        int[] intArray2 = resources.getIntArray(C0444R.array.wlc_icon_bg_colors);
        int abs = Math.abs(str.hashCode() % Math.min(intArray.length, intArray2.length));
        drawable.setTint(intArray[abs]);
        AdaptiveIcon adaptiveIcon = new AdaptiveIcon(context, drawable);
        adaptiveIcon.setBackgroundColor(intArray2[abs]);
        return adaptiveIcon;
    }
}
