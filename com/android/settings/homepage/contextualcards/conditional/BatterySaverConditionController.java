package com.android.settings.homepage.contextualcards.conditional;

import android.content.Context;
import android.os.PowerManager;
import androidx.window.C0444R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.fuelgauge.BatterySaverReceiver;
import com.android.settings.fuelgauge.batterysaver.BatterySaverSettings;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.conditional.ConditionalContextualCard;
import com.android.settingslib.fuelgauge.BatterySaverUtils;
import java.util.Objects;

public class BatterySaverConditionController implements ConditionalCardController, BatterySaverReceiver.BatterySaverListener {

    /* renamed from: ID */
    static final int f150ID = Objects.hash(new Object[]{"BatterySaverConditionController"});
    private final Context mAppContext;
    private final ConditionManager mConditionManager;
    private final PowerManager mPowerManager;
    private final BatterySaverReceiver mReceiver;

    public void onBatteryChanged(boolean z) {
    }

    public BatterySaverConditionController(Context context, ConditionManager conditionManager) {
        this.mAppContext = context;
        this.mConditionManager = conditionManager;
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        BatterySaverReceiver batterySaverReceiver = new BatterySaverReceiver(context);
        this.mReceiver = batterySaverReceiver;
        batterySaverReceiver.setBatterySaverListener(this);
    }

    public long getId() {
        return (long) f150ID;
    }

    public boolean isDisplayable() {
        return this.mPowerManager.isPowerSaveMode();
    }

    public void onPrimaryClick(Context context) {
        new SubSettingLauncher(context).setDestination(BatterySaverSettings.class.getName()).setSourceMetricsCategory(35).setTitleRes(C0444R.string.battery_saver).launch();
    }

    public void onActionClick() {
        BatterySaverUtils.setPowerSaveMode(this.mAppContext, false, false);
    }

    public ContextualCard buildContextualCard() {
        ConditionalContextualCard.Builder actionText = new ConditionalContextualCard.Builder().setConditionId((long) f150ID).setMetricsConstant(379).setActionText(this.mAppContext.getText(C0444R.string.condition_turn_off));
        return actionText.setName(this.mAppContext.getPackageName() + "/" + this.mAppContext.getText(C0444R.string.condition_battery_title)).setTitleText(this.mAppContext.getText(C0444R.string.condition_battery_title).toString()).setSummaryText(this.mAppContext.getText(C0444R.string.condition_battery_summary).toString()).setIconDrawable(this.mAppContext.getDrawable(C0444R.C0447drawable.ic_battery_saver_accent_24dp)).setViewType(C0444R.C0450layout.conditional_card_half_tile).build();
    }

    public void startMonitoringStateChange() {
        this.mReceiver.setListening(true);
    }

    public void stopMonitoringStateChange() {
        this.mReceiver.setListening(false);
    }

    public void onPowerSaveModeChanged() {
        this.mConditionManager.onConditionChanged();
    }
}
