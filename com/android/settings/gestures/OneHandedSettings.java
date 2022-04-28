package com.android.settings.gestures;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.widget.Switch;
import androidx.fragment.app.FragmentActivity;
import androidx.window.C0444R;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.settings.accessibility.AccessibilityShortcutPreferenceFragment;
import com.android.settings.accessibility.ShortcutPreference;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.widget.IllustrationPreference;
import com.android.settingslib.widget.MainSwitchPreference;

public class OneHandedSettings extends AccessibilityShortcutPreferenceFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.one_handed_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return OneHandedSettingsUtils.isSupportOneHandedMode();
        }
    };
    private String mFeatureName;
    private OneHandedSettingsUtils mUtils;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return null;
    }

    public int getMetricsCategory() {
        return 1841;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.one_handed_settings;
    }

    /* access modifiers changed from: protected */
    public String getShortcutPreferenceKey() {
        return "one_handed_shortcuts_preference";
    }

    /* access modifiers changed from: protected */
    public boolean showGeneralCategory() {
        return true;
    }

    /* access modifiers changed from: protected */
    /* renamed from: updatePreferenceStates */
    public void lambda$onStart$1() {
        OneHandedSettingsUtils.setUserId(UserHandle.myUserId());
        super.updatePreferenceStates();
        ((IllustrationPreference) getPreferenceScreen().findPreference("one_handed_header")).setLottieAnimationResId(OneHandedSettingsUtils.isSwipeDownNotificationEnabled(getContext()) ? C0444R.raw.lottie_swipe_for_notifications : C0444R.raw.lottie_one_hand_mode);
        ((MainSwitchPreference) getPreferenceScreen().findPreference("gesture_one_handed_mode_enabled_main_switch")).addOnSwitchChangeListener(new OneHandedSettings$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePreferenceStates$0(Switch switchR, boolean z) {
        switchR.setChecked(z);
        if (z) {
            showQuickSettingsTooltipIfNeeded(1);
        }
    }

    public int getDialogMetricsCategory(int i) {
        int dialogMetricsCategory = super.getDialogMetricsCategory(i);
        if (dialogMetricsCategory == 0) {
            return 1841;
        }
        return dialogMetricsCategory;
    }

    /* access modifiers changed from: protected */
    public void updateShortcutTitle(ShortcutPreference shortcutPreference) {
        shortcutPreference.setTitle((int) C0444R.string.one_handed_mode_shortcut_title);
    }

    public void onStart() {
        super.onStart();
        OneHandedSettingsUtils oneHandedSettingsUtils = new OneHandedSettingsUtils(getContext());
        this.mUtils = oneHandedSettingsUtils;
        oneHandedSettingsUtils.registerToggleAwareObserver(new OneHandedSettings$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$2(Uri uri) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new OneHandedSettings$$ExternalSyntheticLambda2(this));
        }
    }

    public void onStop() {
        super.onStop();
        this.mUtils.unregisterToggleAwareObserver();
    }

    /* access modifiers changed from: protected */
    public ComponentName getComponentName() {
        return AccessibilityShortcutController.ONE_HANDED_COMPONENT_NAME;
    }

    /* access modifiers changed from: protected */
    public CharSequence getLabelName() {
        return this.mFeatureName;
    }

    /* access modifiers changed from: protected */
    public ComponentName getTileComponentName() {
        return AccessibilityShortcutController.ONE_HANDED_TILE_COMPONENT_NAME;
    }

    /* access modifiers changed from: protected */
    public CharSequence getTileName() {
        return this.mFeatureName;
    }

    public void onCreate(Bundle bundle) {
        this.mFeatureName = getContext().getString(C0444R.string.one_handed_title);
        super.onCreate(bundle);
    }
}
