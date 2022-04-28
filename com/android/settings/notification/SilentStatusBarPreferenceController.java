package com.android.settings.notification;

import android.content.Context;
import android.content.IntentFilter;
import androidx.window.C0444R;
import com.android.settings.core.TogglePreferenceController;

public class SilentStatusBarPreferenceController extends TogglePreferenceController {
    private static final String KEY = "silent_icons";
    private NotificationBackend mBackend = new NotificationBackend();

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceHighlightMenuRes() {
        return C0444R.string.menu_key_notifications;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SilentStatusBarPreferenceController(Context context) {
        super(context, KEY);
    }

    /* access modifiers changed from: package-private */
    public void setBackend(NotificationBackend notificationBackend) {
        this.mBackend = notificationBackend;
    }

    public boolean isChecked() {
        return this.mBackend.shouldHideSilentStatusBarIcons(this.mContext);
    }

    public boolean setChecked(boolean z) {
        this.mBackend.setHideSilentStatusIcons(z);
        return true;
    }
}
