package com.android.settings.homepage.contextualcards.conditional;

import android.content.Context;
import androidx.window.C0444R;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.conditional.ConditionalContextualCard;
import java.util.Objects;

public class RingerVibrateConditionController extends AbnormalRingerConditionController {

    /* renamed from: ID */
    static final int f157ID = Objects.hash(new Object[]{"RingerVibrateConditionController"});
    private final Context mAppContext;

    public RingerVibrateConditionController(Context context, ConditionManager conditionManager) {
        super(context, conditionManager);
        this.mAppContext = context;
    }

    public long getId() {
        return (long) f157ID;
    }

    public boolean isDisplayable() {
        return this.mAudioManager.getRingerModeInternal() == 1;
    }

    public ContextualCard buildContextualCard() {
        ConditionalContextualCard.Builder actionText = new ConditionalContextualCard.Builder().setConditionId((long) f157ID).setMetricsConstant(1369).setActionText(this.mAppContext.getText(C0444R.string.condition_device_muted_action_turn_on_sound));
        return actionText.setName(this.mAppContext.getPackageName() + "/" + this.mAppContext.getText(C0444R.string.condition_device_vibrate_title)).setTitleText(this.mAppContext.getText(C0444R.string.condition_device_vibrate_title).toString()).setSummaryText(this.mAppContext.getText(C0444R.string.condition_device_vibrate_summary).toString()).setIconDrawable(this.mAppContext.getDrawable(C0444R.C0447drawable.ic_volume_ringer_vibrate)).setViewType(C0444R.C0450layout.conditional_card_half_tile).build();
    }
}
