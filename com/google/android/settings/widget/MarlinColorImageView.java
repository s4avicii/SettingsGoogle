package com.google.android.settings.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.window.C0444R;

public class MarlinColorImageView extends ImageView {
    public MarlinColorImageView(Context context) {
        super(context);
    }

    public MarlinColorImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setImageDrawable(Drawable drawable) {
        Drawable mutate = drawable.mutate();
        mutate.applyTheme(getDeviceColorTheme());
        super.setImageDrawable(mutate);
    }

    private Resources.Theme getDeviceColorTheme() {
        Resources.Theme newTheme = getResources().newTheme();
        String str = SystemProperties.get("ro.boot.hardware.color");
        if ("BLU00".equals(str)) {
            newTheme.applyStyle(C0444R.style.MarlinColors_Blue, true);
        } else if ("SLV00".equals(str)) {
            newTheme.applyStyle(C0444R.style.MarlinColors_Silver, true);
        } else if ("GRA00".equals(str)) {
            newTheme.applyStyle(C0444R.style.MarlinColors_Graphite, true);
        } else {
            newTheme.applyStyle(C0444R.style.MarlinColors, true);
        }
        return newTheme;
    }
}
