package com.android.settings.applications.specialaccess.notificationaccess;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.core.BasePreferenceController;

public class NotificationAccessController extends BasePreferenceController {
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

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NotificationAccessController(Context context, String str) {
        super(context, str);
    }

    public static boolean hasAccess(Context context, ComponentName componentName) {
        return ((NotificationManager) context.getSystemService(NotificationManager.class)).isNotificationListenerAccessGranted(componentName);
    }
}
