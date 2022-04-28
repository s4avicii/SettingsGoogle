package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.os.AsyncTask;
import android.service.notification.ZenPolicy;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.SelectorWithWidgetPreference;

public class ZenRulePrioritySendersPreferenceController extends AbstractZenCustomRulePreferenceController {
    /* access modifiers changed from: private */
    public ZenPrioritySendersHelper mHelper;
    /* access modifiers changed from: private */
    public final boolean mIsMessages;
    /* access modifiers changed from: private */
    public PreferenceCategory mPreferenceCategory;
    @VisibleForTesting
    SelectorWithWidgetPreference.OnClickListener mSelectorClickListener = new SelectorWithWidgetPreference.OnClickListener() {
        public void onRadioButtonClicked(SelectorWithWidgetPreference selectorWithWidgetPreference) {
            AutomaticZenRule automaticZenRule = ZenRulePrioritySendersPreferenceController.this.mRule;
            if (automaticZenRule != null && automaticZenRule.getZenPolicy() != null) {
                int[] iArr = ZenRulePrioritySendersPreferenceController.this.mHelper.settingsToSaveOnClick(selectorWithWidgetPreference, ZenRulePrioritySendersPreferenceController.this.getPrioritySenders(), ZenRulePrioritySendersPreferenceController.this.getPriorityConversationSenders());
                int i = iArr[0];
                int i2 = iArr[1];
                if (i != -10 || i2 != -10) {
                    if (i != -10) {
                        if (ZenRulePrioritySendersPreferenceController.this.mIsMessages) {
                            ZenRulePrioritySendersPreferenceController.this.mRule.setZenPolicy(new ZenPolicy.Builder(ZenRulePrioritySendersPreferenceController.this.mRule.getZenPolicy()).allowMessages(ZenRulePrioritySendersPreferenceController.zenPolicySettingFromSender(i)).build());
                        } else {
                            ZenRulePrioritySendersPreferenceController.this.mRule.setZenPolicy(new ZenPolicy.Builder(ZenRulePrioritySendersPreferenceController.this.mRule.getZenPolicy()).allowCalls(ZenRulePrioritySendersPreferenceController.zenPolicySettingFromSender(i)).build());
                        }
                    }
                    if (ZenRulePrioritySendersPreferenceController.this.mIsMessages && i2 != -10) {
                        ZenRulePrioritySendersPreferenceController.this.mRule.setZenPolicy(new ZenPolicy.Builder(ZenRulePrioritySendersPreferenceController.this.mRule.getZenPolicy()).allowConversations(i2).build());
                    }
                    ZenRulePrioritySendersPreferenceController zenRulePrioritySendersPreferenceController = ZenRulePrioritySendersPreferenceController.this;
                    zenRulePrioritySendersPreferenceController.mBackend.updateZenRule(zenRulePrioritySendersPreferenceController.mId, zenRulePrioritySendersPreferenceController.mRule);
                }
            }
        }
    };

    public /* bridge */ /* synthetic */ boolean isAvailable() {
        return super.isAvailable();
    }

    public ZenRulePrioritySendersPreferenceController(Context context, String str, Lifecycle lifecycle, boolean z, NotificationBackend notificationBackend) {
        super(context, str, lifecycle);
        this.mIsMessages = z;
        this.mHelper = new ZenPrioritySendersHelper(context, z, this.mBackend, notificationBackend, this.mSelectorClickListener);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreferenceCategory = preferenceCategory;
        this.mHelper.displayPreference(preferenceCategory);
        super.displayPreference(preferenceScreen);
    }

    public String getPreferenceKey() {
        return this.KEY;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        AutomaticZenRule automaticZenRule = this.mRule;
        if (automaticZenRule != null && automaticZenRule.getZenPolicy() != null) {
            this.mHelper.updateState(getPrioritySenders(), getPriorityConversationSenders());
        }
    }

    public void onResume(AutomaticZenRule automaticZenRule, String str) {
        super.onResume(automaticZenRule, str);
        if (this.mIsMessages) {
            updateChannelCounts();
        }
        this.mHelper.updateSummaries();
    }

    private void updateChannelCounts() {
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                ZenRulePrioritySendersPreferenceController.this.mHelper.updateChannelCounts();
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                if (ZenRulePrioritySendersPreferenceController.this.mContext != null) {
                    ZenRulePrioritySendersPreferenceController zenRulePrioritySendersPreferenceController = ZenRulePrioritySendersPreferenceController.this;
                    zenRulePrioritySendersPreferenceController.updateState(zenRulePrioritySendersPreferenceController.mPreferenceCategory);
                }
            }
        }.execute(new Void[0]);
    }

    /* access modifiers changed from: private */
    public int getPrioritySenders() {
        AutomaticZenRule automaticZenRule = this.mRule;
        if (automaticZenRule == null || automaticZenRule.getZenPolicy() == null) {
            return -10;
        }
        if (this.mIsMessages) {
            return ZenModeBackend.getContactSettingFromZenPolicySetting(this.mRule.getZenPolicy().getPriorityMessageSenders());
        }
        return ZenModeBackend.getContactSettingFromZenPolicySetting(this.mRule.getZenPolicy().getPriorityCallSenders());
    }

    /* access modifiers changed from: private */
    public int getPriorityConversationSenders() {
        AutomaticZenRule automaticZenRule = this.mRule;
        if (automaticZenRule == null || automaticZenRule.getZenPolicy() == null) {
            return -10;
        }
        return this.mRule.getZenPolicy().getPriorityConversationSenders();
    }

    static int zenPolicySettingFromSender(int i) {
        return ZenModeBackend.getZenPolicySettingFromPrefKey(ZenModeBackend.getKeyFromSetting(i));
    }
}
