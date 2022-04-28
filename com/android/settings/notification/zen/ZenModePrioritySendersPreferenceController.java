package com.android.settings.notification.zen;

import android.content.Context;
import android.os.AsyncTask;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.SelectorWithWidgetPreference;

public class ZenModePrioritySendersPreferenceController extends AbstractZenModePreferenceController {
    /* access modifiers changed from: private */
    public ZenPrioritySendersHelper mHelper;
    /* access modifiers changed from: private */
    public final boolean mIsMessages;
    /* access modifiers changed from: private */
    public PreferenceCategory mPreferenceCategory;
    SelectorWithWidgetPreference.OnClickListener mSelectorClickListener = new SelectorWithWidgetPreference.OnClickListener() {
        public void onRadioButtonClicked(SelectorWithWidgetPreference selectorWithWidgetPreference) {
            int[] iArr = ZenModePrioritySendersPreferenceController.this.mHelper.settingsToSaveOnClick(selectorWithWidgetPreference, ZenModePrioritySendersPreferenceController.this.getPrioritySenders(), ZenModePrioritySendersPreferenceController.this.getPriorityConversationSenders());
            int i = iArr[0];
            int i2 = iArr[1];
            if (i != -10) {
                ZenModePrioritySendersPreferenceController zenModePrioritySendersPreferenceController = ZenModePrioritySendersPreferenceController.this;
                zenModePrioritySendersPreferenceController.mBackend.saveSenders(zenModePrioritySendersPreferenceController.mIsMessages ? 4 : 8, i);
            }
            if (ZenModePrioritySendersPreferenceController.this.mIsMessages && i2 != -10) {
                ZenModePrioritySendersPreferenceController.this.mBackend.saveConversationSenders(i2);
            }
        }
    };

    public boolean isAvailable() {
        return true;
    }

    public ZenModePrioritySendersPreferenceController(Context context, String str, Lifecycle lifecycle, boolean z, NotificationBackend notificationBackend) {
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
        this.mHelper.updateState(getPrioritySenders(), getPriorityConversationSenders());
    }

    public void onResume() {
        super.onResume();
        if (this.mIsMessages) {
            updateChannelCounts();
        }
        this.mHelper.updateSummaries();
    }

    private void updateChannelCounts() {
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                ZenModePrioritySendersPreferenceController.this.mHelper.updateChannelCounts();
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                if (ZenModePrioritySendersPreferenceController.this.mContext != null) {
                    ZenModePrioritySendersPreferenceController zenModePrioritySendersPreferenceController = ZenModePrioritySendersPreferenceController.this;
                    zenModePrioritySendersPreferenceController.updateState(zenModePrioritySendersPreferenceController.mPreferenceCategory);
                }
            }
        }.execute(new Void[0]);
    }

    /* access modifiers changed from: private */
    public int getPrioritySenders() {
        if (this.mIsMessages) {
            return this.mBackend.getPriorityMessageSenders();
        }
        return this.mBackend.getPriorityCallSenders();
    }

    /* access modifiers changed from: private */
    public int getPriorityConversationSenders() {
        if (this.mIsMessages) {
            return this.mBackend.getPriorityConversationSenders();
        }
        return -10;
    }
}
