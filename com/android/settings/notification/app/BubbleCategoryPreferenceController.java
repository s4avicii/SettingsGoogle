package com.android.settings.notification.app;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.notification.NotificationBackend;

public class BubbleCategoryPreferenceController extends NotificationPreferenceController {

    /* renamed from: ON */
    static final int f167ON = 1;

    public String getPreferenceKey() {
        return "bubbles";
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return false;
    }

    public BubbleCategoryPreferenceController(Context context) {
        super(context, (NotificationBackend) null);
    }

    public boolean isAvailable() {
        if (!super.isAvailable()) {
            return false;
        }
        return areBubblesEnabled();
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (this.mAppRow != null) {
            Intent intent = new Intent("android.settings.APP_NOTIFICATION_BUBBLE_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", this.mAppRow.pkg);
            intent.putExtra("app_uid", this.mAppRow.uid);
            preference.setIntent(intent);
        }
    }

    private boolean areBubblesEnabled() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "notification_bubbles", 1) == 1;
    }
}
