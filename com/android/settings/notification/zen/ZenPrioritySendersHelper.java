package com.android.settings.notification.zen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.icu.text.MessageFormat;
import android.service.notification.ConversationChannelWrapper;
import android.view.View;
import androidx.preference.PreferenceCategory;
import androidx.window.C0444R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.notification.app.ConversationListSettings;
import com.android.settingslib.widget.SelectorWithWidgetPreference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ZenPrioritySendersHelper {
    /* access modifiers changed from: private */
    public static final Intent ALL_CONTACTS_INTENT = new Intent("com.android.contacts.action.LIST_DEFAULT").setFlags(268468224);
    /* access modifiers changed from: private */
    public static final Intent FALLBACK_INTENT = new Intent("android.intent.action.MAIN").setFlags(268468224);
    /* access modifiers changed from: private */
    public static final Intent STARRED_CONTACTS_INTENT = new Intent("com.android.contacts.action.LIST_STARRED").setFlags(268468224);
    /* access modifiers changed from: private */
    public final Context mContext;
    private final boolean mIsMessages;
    private final NotificationBackend mNotificationBackend;
    private int mNumImportantConversations = -10;
    /* access modifiers changed from: private */
    public final PackageManager mPackageManager;
    private PreferenceCategory mPreferenceCategory;
    private final SelectorWithWidgetPreference.OnClickListener mSelectorClickListener;
    private List<SelectorWithWidgetPreference> mSelectorPreferences = new ArrayList();
    private final ZenModeBackend mZenModeBackend;

    public ZenPrioritySendersHelper(Context context, boolean z, ZenModeBackend zenModeBackend, NotificationBackend notificationBackend, SelectorWithWidgetPreference.OnClickListener onClickListener) {
        this.mContext = context;
        this.mIsMessages = z;
        this.mZenModeBackend = zenModeBackend;
        this.mNotificationBackend = notificationBackend;
        this.mSelectorClickListener = onClickListener;
        this.mPackageManager = context.getPackageManager();
        Intent intent = FALLBACK_INTENT;
        if (!intent.hasCategory("android.intent.category.APP_CONTACTS")) {
            intent.addCategory("android.intent.category.APP_CONTACTS");
        }
    }

    /* access modifiers changed from: package-private */
    public void displayPreference(PreferenceCategory preferenceCategory) {
        this.mPreferenceCategory = preferenceCategory;
        if (preferenceCategory.getPreferenceCount() == 0) {
            makeSelectorPreference("senders_starred_contacts", C0444R.string.zen_mode_from_starred, this.mIsMessages);
            makeSelectorPreference("senders_contacts", C0444R.string.zen_mode_from_contacts, this.mIsMessages);
            if (this.mIsMessages) {
                makeSelectorPreference("conversations_important", C0444R.string.zen_mode_from_important_conversations, true);
                updateChannelCounts();
            }
            makeSelectorPreference("senders_anyone", C0444R.string.zen_mode_from_anyone, this.mIsMessages);
            makeSelectorPreference("senders_none", C0444R.string.zen_mode_none_messages, this.mIsMessages);
            updateSummaries();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateState(int i, int i2) {
        for (SelectorWithWidgetPreference next : this.mSelectorPreferences) {
            boolean z = true;
            int[] keyToSettingEndState = keyToSettingEndState(next.getKey(), true);
            int i3 = keyToSettingEndState[0];
            int i4 = keyToSettingEndState[1];
            boolean z2 = i3 == i;
            if (this.mIsMessages && i4 != -10) {
                if ((!z2 && i3 != -10) || i4 != i2) {
                    z = false;
                }
                z2 = z;
            }
            next.setChecked(z2);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateSummaries() {
        for (SelectorWithWidgetPreference next : this.mSelectorPreferences) {
            next.setSummary((CharSequence) getSummary(next.getKey()));
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0060, code lost:
        if (r12.equals("senders_none") == false) goto L_0x005a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int[] keyToSettingEndState(java.lang.String r12, boolean r13) {
        /*
            r11 = this;
            r0 = 2
            int[] r1 = new int[r0]
            r1 = {-10, -10} // fill-array
            java.lang.String r2 = "conversations_important"
            java.lang.String r3 = "senders_contacts"
            java.lang.String r4 = "senders_starred_contacts"
            r5 = 3
            java.lang.String r6 = "senders_none"
            java.lang.String r7 = "senders_anyone"
            r8 = -1
            r9 = 1
            r10 = 0
            if (r13 != 0) goto L_0x007d
            r12.hashCode()
            int r13 = r12.hashCode()
            switch(r13) {
                case -1145842476: goto L_0x0041;
                case -133103980: goto L_0x0038;
                case 1725241211: goto L_0x002f;
                case 1767544313: goto L_0x0026;
                default: goto L_0x0024;
            }
        L_0x0024:
            r13 = r8
            goto L_0x0049
        L_0x0026:
            boolean r13 = r12.equals(r6)
            if (r13 != 0) goto L_0x002d
            goto L_0x0024
        L_0x002d:
            r13 = r5
            goto L_0x0049
        L_0x002f:
            boolean r13 = r12.equals(r7)
            if (r13 != 0) goto L_0x0036
            goto L_0x0024
        L_0x0036:
            r13 = r0
            goto L_0x0049
        L_0x0038:
            boolean r13 = r12.equals(r3)
            if (r13 != 0) goto L_0x003f
            goto L_0x0024
        L_0x003f:
            r13 = r9
            goto L_0x0049
        L_0x0041:
            boolean r13 = r12.equals(r4)
            if (r13 != 0) goto L_0x0048
            goto L_0x0024
        L_0x0048:
            r13 = r10
        L_0x0049:
            switch(r13) {
                case 0: goto L_0x004d;
                case 1: goto L_0x004d;
                case 2: goto L_0x004d;
                case 3: goto L_0x004d;
                default: goto L_0x004c;
            }
        L_0x004c:
            goto L_0x004f
        L_0x004d:
            r1[r10] = r8
        L_0x004f:
            boolean r11 = r11.mIsMessages
            if (r11 == 0) goto L_0x00ed
            int r11 = r12.hashCode()
            switch(r11) {
                case 660058867: goto L_0x006c;
                case 1725241211: goto L_0x0063;
                case 1767544313: goto L_0x005c;
                default: goto L_0x005a;
            }
        L_0x005a:
            r0 = r8
            goto L_0x0074
        L_0x005c:
            boolean r11 = r12.equals(r6)
            if (r11 != 0) goto L_0x0074
            goto L_0x005a
        L_0x0063:
            boolean r11 = r12.equals(r7)
            if (r11 != 0) goto L_0x006a
            goto L_0x005a
        L_0x006a:
            r0 = r9
            goto L_0x0074
        L_0x006c:
            boolean r11 = r12.equals(r2)
            if (r11 != 0) goto L_0x0073
            goto L_0x005a
        L_0x0073:
            r0 = r10
        L_0x0074:
            switch(r0) {
                case 0: goto L_0x0079;
                case 1: goto L_0x0079;
                case 2: goto L_0x0079;
                default: goto L_0x0077;
            }
        L_0x0077:
            goto L_0x00ed
        L_0x0079:
            r1[r9] = r5
            goto L_0x00ed
        L_0x007d:
            r12.hashCode()
            int r13 = r12.hashCode()
            switch(r13) {
                case -1145842476: goto L_0x00a4;
                case -133103980: goto L_0x009b;
                case 1725241211: goto L_0x0092;
                case 1767544313: goto L_0x0089;
                default: goto L_0x0087;
            }
        L_0x0087:
            r13 = r8
            goto L_0x00ac
        L_0x0089:
            boolean r13 = r12.equals(r6)
            if (r13 != 0) goto L_0x0090
            goto L_0x0087
        L_0x0090:
            r13 = r5
            goto L_0x00ac
        L_0x0092:
            boolean r13 = r12.equals(r7)
            if (r13 != 0) goto L_0x0099
            goto L_0x0087
        L_0x0099:
            r13 = r0
            goto L_0x00ac
        L_0x009b:
            boolean r13 = r12.equals(r3)
            if (r13 != 0) goto L_0x00a2
            goto L_0x0087
        L_0x00a2:
            r13 = r9
            goto L_0x00ac
        L_0x00a4:
            boolean r13 = r12.equals(r4)
            if (r13 != 0) goto L_0x00ab
            goto L_0x0087
        L_0x00ab:
            r13 = r10
        L_0x00ac:
            switch(r13) {
                case 0: goto L_0x00b9;
                case 1: goto L_0x00b6;
                case 2: goto L_0x00b3;
                case 3: goto L_0x00b0;
                default: goto L_0x00af;
            }
        L_0x00af:
            goto L_0x00bb
        L_0x00b0:
            r1[r10] = r8
            goto L_0x00bb
        L_0x00b3:
            r1[r10] = r10
            goto L_0x00bb
        L_0x00b6:
            r1[r10] = r9
            goto L_0x00bb
        L_0x00b9:
            r1[r10] = r0
        L_0x00bb:
            boolean r11 = r11.mIsMessages
            if (r11 == 0) goto L_0x00ed
            int r11 = r12.hashCode()
            switch(r11) {
                case 660058867: goto L_0x00d9;
                case 1725241211: goto L_0x00d0;
                case 1767544313: goto L_0x00c7;
                default: goto L_0x00c6;
            }
        L_0x00c6:
            goto L_0x00e1
        L_0x00c7:
            boolean r11 = r12.equals(r6)
            if (r11 != 0) goto L_0x00ce
            goto L_0x00e1
        L_0x00ce:
            r8 = r0
            goto L_0x00e1
        L_0x00d0:
            boolean r11 = r12.equals(r7)
            if (r11 != 0) goto L_0x00d7
            goto L_0x00e1
        L_0x00d7:
            r8 = r9
            goto L_0x00e1
        L_0x00d9:
            boolean r11 = r12.equals(r2)
            if (r11 != 0) goto L_0x00e0
            goto L_0x00e1
        L_0x00e0:
            r8 = r10
        L_0x00e1:
            switch(r8) {
                case 0: goto L_0x00eb;
                case 1: goto L_0x00e8;
                case 2: goto L_0x00e5;
                default: goto L_0x00e4;
            }
        L_0x00e4:
            goto L_0x00ed
        L_0x00e5:
            r1[r9] = r5
            goto L_0x00ed
        L_0x00e8:
            r1[r9] = r9
            goto L_0x00ed
        L_0x00eb:
            r1[r9] = r0
        L_0x00ed:
            r11 = r1[r10]
            r13 = -10
            if (r11 != r13) goto L_0x010f
            r11 = r1[r9]
            if (r11 == r13) goto L_0x00f8
            goto L_0x010f
        L_0x00f8:
            java.lang.IllegalArgumentException r11 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r0 = "invalid key "
            r13.append(r0)
            r13.append(r12)
            java.lang.String r12 = r13.toString()
            r11.<init>(r12)
            throw r11
        L_0x010f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.notification.zen.ZenPrioritySendersHelper.keyToSettingEndState(java.lang.String, boolean):int[]");
    }

    /* access modifiers changed from: package-private */
    public int[] settingsToSaveOnClick(SelectorWithWidgetPreference selectorWithWidgetPreference, int i, int i2) {
        int[] iArr = {-10, -10};
        int[] keyToSettingEndState = keyToSettingEndState(selectorWithWidgetPreference.getKey(), !selectorWithWidgetPreference.isCheckBox() || !selectorWithWidgetPreference.isChecked());
        int i3 = keyToSettingEndState[0];
        int i4 = keyToSettingEndState[1];
        if (!(i3 == -10 || i3 == i)) {
            iArr[0] = i3;
        }
        if (this.mIsMessages) {
            if (!(i4 == -10 || i4 == i2)) {
                iArr[1] = i4;
            }
            if (selectorWithWidgetPreference.getKey() == "conversations_important" && i == 0) {
                iArr[0] = -1;
            }
            if ((selectorWithWidgetPreference.getKey() == "senders_starred_contacts" || selectorWithWidgetPreference.getKey() == "senders_contacts") && i2 == 1) {
                iArr[1] = 3;
            }
        }
        return iArr;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getSummary(java.lang.String r5) {
        /*
            r4 = this;
            int r0 = r5.hashCode()
            r1 = 3
            r2 = 2
            r3 = 1
            switch(r0) {
                case -1145842476: goto L_0x0036;
                case -133103980: goto L_0x002b;
                case 660058867: goto L_0x0021;
                case 1725241211: goto L_0x0016;
                case 1767544313: goto L_0x000b;
                default: goto L_0x000a;
            }
        L_0x000a:
            goto L_0x0041
        L_0x000b:
            java.lang.String r0 = "senders_none"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0041
            r5 = 4
            goto L_0x0042
        L_0x0016:
            java.lang.String r0 = "senders_anyone"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0041
            r5 = r1
            goto L_0x0042
        L_0x0021:
            java.lang.String r0 = "conversations_important"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0041
            r5 = r2
            goto L_0x0042
        L_0x002b:
            java.lang.String r0 = "senders_contacts"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0041
            r5 = r3
            goto L_0x0042
        L_0x0036:
            java.lang.String r0 = "senders_starred_contacts"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0041
            r5 = 0
            goto L_0x0042
        L_0x0041:
            r5 = -1
        L_0x0042:
            if (r5 == 0) goto L_0x0070
            if (r5 == r3) goto L_0x0067
            if (r5 == r2) goto L_0x0062
            if (r5 == r1) goto L_0x004c
            r4 = 0
            return r4
        L_0x004c:
            android.content.Context r5 = r4.mContext
            android.content.res.Resources r5 = r5.getResources()
            boolean r4 = r4.mIsMessages
            if (r4 == 0) goto L_0x005a
            r4 = 2130974840(0x7f041878, float:1.7558514E38)
            goto L_0x005d
        L_0x005a:
            r4 = 2130974839(0x7f041877, float:1.7558512E38)
        L_0x005d:
            java.lang.String r4 = r5.getString(r4)
            return r4
        L_0x0062:
            java.lang.String r4 = r4.getConversationSummary()
            return r4
        L_0x0067:
            com.android.settings.notification.zen.ZenModeBackend r5 = r4.mZenModeBackend
            android.content.Context r4 = r4.mContext
            java.lang.String r4 = r5.getContactsNumberSummary(r4)
            return r4
        L_0x0070:
            com.android.settings.notification.zen.ZenModeBackend r5 = r4.mZenModeBackend
            android.content.Context r4 = r4.mContext
            java.lang.String r4 = r5.getStarredContactsSummary(r4)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.notification.zen.ZenPrioritySendersHelper.getSummary(java.lang.String):java.lang.String");
    }

    private String getConversationSummary() {
        int i = this.mNumImportantConversations;
        if (i == -10) {
            return null;
        }
        MessageFormat messageFormat = new MessageFormat(this.mContext.getString(C0444R.string.zen_mode_conversations_count), Locale.getDefault());
        HashMap hashMap = new HashMap();
        hashMap.put("count", Integer.valueOf(i));
        return messageFormat.format(hashMap);
    }

    /* access modifiers changed from: package-private */
    public void updateChannelCounts() {
        ParceledListSlice<ConversationChannelWrapper> conversations = this.mNotificationBackend.getConversations(true);
        int i = 0;
        if (conversations != null) {
            for (ConversationChannelWrapper notificationChannel : conversations.getList()) {
                if (!notificationChannel.getNotificationChannel().isDemoted()) {
                    i++;
                }
            }
        }
        this.mNumImportantConversations = i;
    }

    private SelectorWithWidgetPreference makeSelectorPreference(String str, int i, boolean z) {
        SelectorWithWidgetPreference selectorWithWidgetPreference = new SelectorWithWidgetPreference(this.mPreferenceCategory.getContext(), z);
        selectorWithWidgetPreference.setKey(str);
        selectorWithWidgetPreference.setTitle(i);
        selectorWithWidgetPreference.setOnClickListener(this.mSelectorClickListener);
        View.OnClickListener widgetClickListener = getWidgetClickListener(str);
        if (widgetClickListener != null) {
            selectorWithWidgetPreference.setExtraWidgetOnClickListener(widgetClickListener);
        }
        this.mPreferenceCategory.addPreference(selectorWithWidgetPreference);
        this.mSelectorPreferences.add(selectorWithWidgetPreference);
        return selectorWithWidgetPreference;
    }

    private View.OnClickListener getWidgetClickListener(final String str) {
        if (!"senders_contacts".equals(str) && !"senders_starred_contacts".equals(str) && !"conversations_important".equals(str)) {
            return null;
        }
        if ("senders_starred_contacts".equals(str) && !isStarredIntentValid()) {
            return null;
        }
        if (!"senders_contacts".equals(str) || isContactsIntentValid()) {
            return new View.OnClickListener() {
                public void onClick(View view) {
                    if ("senders_starred_contacts".equals(str) && ZenPrioritySendersHelper.STARRED_CONTACTS_INTENT.resolveActivity(ZenPrioritySendersHelper.this.mPackageManager) != null) {
                        ZenPrioritySendersHelper.this.mContext.startActivity(ZenPrioritySendersHelper.STARRED_CONTACTS_INTENT);
                    } else if ("senders_contacts".equals(str) && ZenPrioritySendersHelper.ALL_CONTACTS_INTENT.resolveActivity(ZenPrioritySendersHelper.this.mPackageManager) != null) {
                        ZenPrioritySendersHelper.this.mContext.startActivity(ZenPrioritySendersHelper.ALL_CONTACTS_INTENT);
                    } else if ("conversations_important".equals(str)) {
                        new SubSettingLauncher(ZenPrioritySendersHelper.this.mContext).setDestination(ConversationListSettings.class.getName()).setSourceMetricsCategory(1837).launch();
                    } else {
                        ZenPrioritySendersHelper.this.mContext.startActivity(ZenPrioritySendersHelper.FALLBACK_INTENT);
                    }
                }
            };
        }
        return null;
    }

    private boolean isStarredIntentValid() {
        return (STARRED_CONTACTS_INTENT.resolveActivity(this.mPackageManager) == null && FALLBACK_INTENT.resolveActivity(this.mPackageManager) == null) ? false : true;
    }

    private boolean isContactsIntentValid() {
        return (ALL_CONTACTS_INTENT.resolveActivity(this.mPackageManager) == null && FALLBACK_INTENT.resolveActivity(this.mPackageManager) == null) ? false : true;
    }
}
