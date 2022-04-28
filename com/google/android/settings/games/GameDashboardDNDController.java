package com.google.android.settings.games;

import android.app.AutomaticZenRule;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.service.notification.ZenPolicy;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.core.TogglePreferenceController;
import java.util.Map;

public class GameDashboardDNDController extends TogglePreferenceController implements LifecycleObserver {
    private static final ComponentName COMPONENT_NAME = new ComponentName(PACKAGE_NAME, "com.google.android.systemui.gamedashboard.GameDndConfigActivity");
    @VisibleForTesting
    static final Uri CONDITION_ID = new Uri.Builder().scheme("android-app").authority(PACKAGE_NAME).appendPath("game-mode-dnd-controller").build();
    private static final String PACKAGE_NAME = "com.android.systemui";
    private static final String TAG = "GDDNDController";
    private final NotificationManager mNotificationManager = ((NotificationManager) this.mContext.getSystemService(NotificationManager.class));
    @VisibleForTesting
    String mRuleId;
    private final String mRuleName = this.mContext.getString(C0444R.string.game_dashboard_dnd_rule_name);

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

    public GameDashboardDNDController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        AutomaticZenRule fetchRule;
        if (this.mRuleId == null || (fetchRule = fetchRule()) == null || fetchRule.getInterruptionFilter() == 1) {
            return false;
        }
        return true;
    }

    public boolean setChecked(boolean z) {
        if (this.mRuleId == null && z) {
            this.mRuleId = this.mNotificationManager.addAutomaticZenRule(new AutomaticZenRule(this.mRuleName, (ComponentName) null, COMPONENT_NAME, CONDITION_ID, (ZenPolicy) null, 1, true));
        }
        if (this.mRuleId == null && !z) {
            return true;
        }
        AutomaticZenRule fetchRule = fetchRule();
        fetchRule.setInterruptionFilter(z ? 2 : 1);
        this.mNotificationManager.updateAutomaticZenRule(this.mRuleId, fetchRule);
        return true;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public String getRuleId() {
        for (Map.Entry next : this.mNotificationManager.getAutomaticZenRules().entrySet()) {
            if (((AutomaticZenRule) next.getValue()).getConditionId().equals(CONDITION_ID)) {
                return (String) next.getKey();
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public AutomaticZenRule fetchRule() {
        AutomaticZenRule automaticZenRule = this.mNotificationManager.getAutomaticZenRule(this.mRuleId);
        if (automaticZenRule != null) {
            return automaticZenRule;
        }
        return null;
    }

    public void init(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onLifeCycleStartEvent() {
        this.mRuleId = getRuleId();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onLifeCycleStopEvent() {
        this.mRuleId = null;
    }
}
