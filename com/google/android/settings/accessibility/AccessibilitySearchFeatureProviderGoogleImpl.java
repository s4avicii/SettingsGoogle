package com.google.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.AccessibilityShortcutInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.view.accessibility.AccessibilityManager;
import androidx.window.C0444R;
import com.android.settings.accessibility.AccessibilitySearchFeatureProvider;
import com.android.settingslib.search.SearchIndexableRaw;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccessibilitySearchFeatureProviderGoogleImpl implements AccessibilitySearchFeatureProvider {
    public List<SearchIndexableRaw> getSearchIndexableRawData(Context context) {
        AccessibilityManager instance = AccessibilityManager.getInstance(context);
        List installedAccessibilityShortcutListAsUser = instance.getInstalledAccessibilityShortcutListAsUser(context, UserHandle.myUserId());
        ArrayList arrayList = new ArrayList(instance.getInstalledAccessibilityServiceList());
        SearchIndexableRawHelper searchIndexableRawHelper = new SearchIndexableRawHelper(context);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.addAll(searchIndexableRawHelper.buildSupportedServiceSearchIndex(arrayList));
        arrayList2.addAll(searchIndexableRawHelper.buildSupportedActivitySearchIndex(installedAccessibilityShortcutListAsUser));
        return arrayList2;
    }

    static class SearchIndexableRawHelper {
        private final Context mContext;
        private final PackageManager mPm;

        SearchIndexableRawHelper(Context context) {
            this.mContext = context;
            this.mPm = context.getPackageManager();
        }

        public List<SearchIndexableRaw> buildSupportedServiceSearchIndex(List<AccessibilityServiceInfo> list) {
            ImmutableMap of = ImmutableMap.m28of(new ComponentName("com.google.android.marvin.talkback", "com.google.android.marvin.talkback.TalkBackService"), this.mContext.getString(C0444R.string.keywords_talkback), new ComponentName("com.google.android.marvin.talkback", "com.google.android.accessibility.accessibilitymenu.AccessibilityMenuService"), this.mContext.getString(C0444R.string.keywords_accessibility_menu), new ComponentName("com.google.android.marvin.talkback", "com.google.android.accessibility.selecttospeak.SelectToSpeakService"), this.mContext.getString(C0444R.string.keywords_select_to_speak), new ComponentName("com.google.android.marvin.talkback", "com.android.switchaccess.SwitchAccessService"), this.mContext.getString(C0444R.string.keywords_switch_access), new ComponentName("com.google.android.apps.accessibility.voiceaccess", "com.google.android.apps.accessibility.voiceaccess.JustSpeakService"), this.mContext.getString(C0444R.string.keywords_voice_access));
            ArrayList arrayList = new ArrayList();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ResolveInfo resolveInfo = list.get(i).getResolveInfo();
                of.entrySet().stream().filter(new C1747xf66b84bd(new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name))).forEach(new C1746xf66b84bc(this, resolveInfo, arrayList));
            }
            return arrayList;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$buildSupportedServiceSearchIndex$1(ResolveInfo resolveInfo, List list, Map.Entry entry) {
            list.add(getSearchIndexableRaw(((ComponentName) entry.getKey()).flattenToString(), resolveInfo.loadLabel(this.mPm).toString(), (String) entry.getValue()));
        }

        public List<SearchIndexableRaw> buildSupportedActivitySearchIndex(List<AccessibilityShortcutInfo> list) {
            ImmutableMap of = ImmutableMap.m27of(new ComponentName("com.google.android.accessibility.soundamplifier", "com.google.android.accessibility.soundamplifier.ui.SoundAmplifierSettingActivity"), this.mContext.getString(C0444R.string.keywords_sound_amplifier), new ComponentName("com.google.audio.hearing.visualization.accessibility.scribe", "com.google.audio.hearing.visualization.accessibility.scribe.MainActivity"), this.mContext.getString(C0444R.string.keywords_live_transcribe), new ComponentName("com.google.audio.hearing.visualization.accessibility.scribe", "com.google.audio.hearing.visualization.accessibility.dolphin.ui.visualizer.TimelineActivity"), this.mContext.getString(C0444R.string.keywords_sound_notifications));
            ArrayList arrayList = new ArrayList();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                AccessibilityShortcutInfo accessibilityShortcutInfo = list.get(i);
                ActivityInfo activityInfo = accessibilityShortcutInfo.getActivityInfo();
                of.entrySet().stream().filter(new C1748xf66b84be(accessibilityShortcutInfo.getComponentName())).forEach(new C1745xf66b84bb(this, activityInfo, arrayList));
            }
            return arrayList;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$buildSupportedActivitySearchIndex$3(ActivityInfo activityInfo, List list, Map.Entry entry) {
            list.add(getSearchIndexableRaw(((ComponentName) entry.getKey()).flattenToString(), activityInfo.loadLabel(this.mPm).toString(), (String) entry.getValue()));
        }

        private SearchIndexableRaw getSearchIndexableRaw(String str, String str2, String str3) {
            SearchIndexableRaw searchIndexableRaw = new SearchIndexableRaw(this.mContext);
            searchIndexableRaw.key = str;
            searchIndexableRaw.title = str2;
            searchIndexableRaw.keywords = str3;
            return searchIndexableRaw;
        }
    }
}
