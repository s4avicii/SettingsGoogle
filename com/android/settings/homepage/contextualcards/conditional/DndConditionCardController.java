package com.android.settings.homepage.contextualcards.conditional;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.service.notification.ZenModeConfig;
import androidx.window.C0444R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.conditional.ConditionalContextualCard;
import com.android.settings.notification.zen.ZenModeSettings;
import java.util.Objects;

public class DndConditionCardController implements ConditionalCardController {
    static final IntentFilter DND_FILTER = new IntentFilter("android.app.action.INTERRUPTION_FILTER_CHANGED_INTERNAL");

    /* renamed from: ID */
    static final int f152ID = Objects.hash(new Object[]{"DndConditionCardController"});
    private final Context mAppContext;
    /* access modifiers changed from: private */
    public final ConditionManager mConditionManager;
    private final NotificationManager mNotificationManager;
    private final Receiver mReceiver = new Receiver();

    public DndConditionCardController(Context context, ConditionManager conditionManager) {
        this.mAppContext = context;
        this.mConditionManager = conditionManager;
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
    }

    public long getId() {
        return (long) f152ID;
    }

    public boolean isDisplayable() {
        return this.mNotificationManager.getZenMode() != 0;
    }

    public void startMonitoringStateChange() {
        this.mAppContext.registerReceiver(this.mReceiver, DND_FILTER);
    }

    public void stopMonitoringStateChange() {
        this.mAppContext.unregisterReceiver(this.mReceiver);
    }

    public void onPrimaryClick(Context context) {
        new SubSettingLauncher(context).setDestination(ZenModeSettings.class.getName()).setSourceMetricsCategory(1502).setTitleRes(C0444R.string.zen_mode_settings_title).launch();
    }

    public void onActionClick() {
        this.mNotificationManager.setZenMode(0, (Uri) null, "DndCondition");
    }

    public ContextualCard buildContextualCard() {
        ConditionalContextualCard.Builder actionText = new ConditionalContextualCard.Builder().setConditionId((long) f152ID).setMetricsConstant(381).setActionText(this.mAppContext.getText(C0444R.string.condition_turn_off));
        return actionText.setName(this.mAppContext.getPackageName() + "/" + this.mAppContext.getText(C0444R.string.condition_zen_title)).setTitleText(this.mAppContext.getText(C0444R.string.condition_zen_title).toString()).setSummaryText(getSummary()).setIconDrawable(this.mAppContext.getDrawable(C0444R.C0447drawable.ic_do_not_disturb_on_24dp)).setViewType(C0444R.C0450layout.conditional_card_half_tile).build();
    }

    public class Receiver extends BroadcastReceiver {
        public Receiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.app.action.INTERRUPTION_FILTER_CHANGED_INTERNAL".equals(intent.getAction())) {
                DndConditionCardController.this.mConditionManager.onConditionChanged();
            }
        }
    }

    private String getSummary() {
        if (ZenModeConfig.areAllZenBehaviorSoundsMuted(this.mNotificationManager.getZenModeConfig())) {
            return this.mAppContext.getText(C0444R.string.condition_zen_summary_phone_muted).toString();
        }
        return this.mAppContext.getText(C0444R.string.condition_zen_summary_with_exceptions).toString();
    }
}
