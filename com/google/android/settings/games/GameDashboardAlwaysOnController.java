package com.google.android.settings.games;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.core.TogglePreferenceController;

public class GameDashboardAlwaysOnController extends TogglePreferenceController implements LifecycleObserver {
    @VisibleForTesting
    static final int OFF = 0;
    @VisibleForTesting

    /* renamed from: ON */
    static final int f200ON = 1;
    private static final String TAG = "GDAlwaysOnController";
    private final ContentObserver mAlwaysOnObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean z) {
            super.onChange(z);
            GameDashboardAlwaysOnController gameDashboardAlwaysOnController = GameDashboardAlwaysOnController.this;
            boolean z2 = false;
            if (Settings.Secure.getIntForUser(gameDashboardAlwaysOnController.mContext.getContentResolver(), "game_dashboard_always_on", 0, GameDashboardAlwaysOnController.this.mContext.getUserId()) == 1) {
                z2 = true;
            }
            gameDashboardAlwaysOnController.setChecked(z2);
        }
    };

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
        return C0444R.string.menu_key_apps;
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

    public GameDashboardAlwaysOnController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "game_dashboard_always_on", 0, this.mContext.getUserId()) == 1) {
            return true;
        }
        return false;
    }

    public boolean setChecked(boolean z) {
        Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "game_dashboard_always_on", z ? 1 : 0, this.mContext.getUserId());
        return true;
    }

    public void init(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onLifeCycleStartEvent() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("game_dashboard_always_on"), false, this.mAlwaysOnObserver, this.mContext.getUserId());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onLifeCycleStopEvent() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mAlwaysOnObserver);
    }
}
