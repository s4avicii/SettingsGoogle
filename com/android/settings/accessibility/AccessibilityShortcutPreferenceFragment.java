package com.android.settings.accessibility;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.BreakIterator;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.CheckBox;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceCategory;
import androidx.window.C0444R;
import com.android.settings.accessibility.ShortcutPreference;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.utils.LocaleUtils;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.util.ArrayList;
import java.util.Locale;

public abstract class AccessibilityShortcutPreferenceFragment extends DashboardFragment implements ShortcutPreference.OnClickCallback {
    private CheckBox mHardwareTypeCheckBox;
    private boolean mNeedsQSTooltipReshow = false;
    private int mNeedsQSTooltipType = 0;
    protected int mSavedCheckBoxValue = -1;
    private AccessibilitySettingsContentObserver mSettingsContentObserver;
    protected ShortcutPreference mShortcutPreference;
    private CheckBox mSoftwareTypeCheckBox;
    private AccessibilityQuickSettingsTooltipWindow mTooltipWindow;
    private AccessibilityManager.TouchExplorationStateChangeListener mTouchExplorationStateChangeListener;

    private boolean hasShortcutType(int i, int i2) {
        return (i & i2) == i2;
    }

    /* access modifiers changed from: protected */
    public abstract ComponentName getComponentName();

    public int getDialogMetricsCategory(int i) {
        if (i != 1) {
            return i != 1008 ? 0 : 1810;
        }
        return 1812;
    }

    /* access modifiers changed from: protected */
    public abstract CharSequence getLabelName();

    /* access modifiers changed from: protected */
    public String getShortcutPreferenceKey() {
        return "shortcut_preference";
    }

    /* access modifiers changed from: protected */
    public abstract ComponentName getTileComponentName();

    /* access modifiers changed from: protected */
    public abstract CharSequence getTileName();

    /* access modifiers changed from: protected */
    public boolean showGeneralCategory() {
        return false;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            if (bundle.containsKey("shortcut_type")) {
                this.mSavedCheckBoxValue = bundle.getInt("shortcut_type", -1);
            }
            if (bundle.containsKey("qs_tooltip_reshow")) {
                this.mNeedsQSTooltipReshow = bundle.getBoolean("qs_tooltip_reshow");
            }
            if (bundle.containsKey("qs_tooltip_type")) {
                this.mNeedsQSTooltipType = bundle.getInt("qs_tooltip_type");
            }
        }
        if (getPreferenceScreenResId() <= 0) {
            setPreferenceScreen(getPreferenceManager().createPreferenceScreen(getPrefContext()));
        }
        if (showGeneralCategory()) {
            initGeneralCategory();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add("accessibility_button_targets");
        arrayList.add("accessibility_shortcut_target_service");
        AccessibilitySettingsContentObserver accessibilitySettingsContentObserver = new AccessibilitySettingsContentObserver(new Handler());
        this.mSettingsContentObserver = accessibilitySettingsContentObserver;
        accessibilitySettingsContentObserver.registerKeysToObserverCallback(arrayList, new C0585x835f3312(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(String str) {
        updateShortcutPreferenceData();
        updateShortcutPreference();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ShortcutPreference shortcutPreference = new ShortcutPreference(getPrefContext(), (AttributeSet) null);
        this.mShortcutPreference = shortcutPreference;
        shortcutPreference.setPersistent(false);
        this.mShortcutPreference.setKey(getShortcutPreferenceKey());
        this.mShortcutPreference.setOnClickCallback(this);
        updateShortcutTitle(this.mShortcutPreference);
        getPreferenceScreen().addPreference(this.mShortcutPreference);
        this.mTouchExplorationStateChangeListener = new C0584x835f3311(this);
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$1(boolean z) {
        removeDialog(1);
        this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.mNeedsQSTooltipReshow) {
            getView().post(new C0586x835f3313(this));
        }
    }

    public void onResume() {
        super.onResume();
        ((AccessibilityManager) getPrefContext().getSystemService(AccessibilityManager.class)).addTouchExplorationStateChangeListener(this.mTouchExplorationStateChangeListener);
        this.mSettingsContentObserver.register(getContentResolver());
        updateShortcutPreferenceData();
        updateShortcutPreference();
    }

    public void onPause() {
        ((AccessibilityManager) getPrefContext().getSystemService(AccessibilityManager.class)).removeTouchExplorationStateChangeListener(this.mTouchExplorationStateChangeListener);
        this.mSettingsContentObserver.unregister(getContentResolver());
        super.onPause();
    }

    public void onSaveInstanceState(Bundle bundle) {
        int shortcutTypeCheckBoxValue = getShortcutTypeCheckBoxValue();
        if (shortcutTypeCheckBoxValue != -1) {
            bundle.putInt("shortcut_type", shortcutTypeCheckBoxValue);
        }
        AccessibilityQuickSettingsTooltipWindow accessibilityQuickSettingsTooltipWindow = this.mTooltipWindow;
        if (accessibilityQuickSettingsTooltipWindow != null) {
            bundle.putBoolean("qs_tooltip_reshow", accessibilityQuickSettingsTooltipWindow.isShowing());
            bundle.putInt("qs_tooltip_type", this.mNeedsQSTooltipType);
        }
        super.onSaveInstanceState(bundle);
    }

    public Dialog onCreateDialog(int i) {
        if (i == 1) {
            String string = getPrefContext().getString(C0444R.string.accessibility_shortcut_title, new Object[]{getLabelName()});
            boolean isAnySetupWizard = WizardManagerHelper.isAnySetupWizard(getIntent());
            AlertDialog showEditShortcutDialog = AccessibilityDialogUtils.showEditShortcutDialog(getPrefContext(), isAnySetupWizard ? 1 : 0, string, new C0581x835f330e(this));
            setupEditShortcutDialog(showEditShortcutDialog);
            return showEditShortcutDialog;
        } else if (i == 1008) {
            AlertDialog createAccessibilityTutorialDialog = AccessibilityGestureNavigationTutorial.createAccessibilityTutorialDialog(getPrefContext(), getUserShortcutTypes(), new C0582x835f330f(this));
            createAccessibilityTutorialDialog.setCanceledOnTouchOutside(false);
            return createAccessibilityTutorialDialog;
        } else {
            throw new IllegalArgumentException("Unsupported dialogId " + i);
        }
    }

    /* access modifiers changed from: protected */
    public void updateShortcutTitle(ShortcutPreference shortcutPreference) {
        shortcutPreference.setTitle((CharSequence) getString(C0444R.string.accessibility_shortcut_title, getLabelName()));
    }

    public void onSettingsClicked(ShortcutPreference shortcutPreference) {
        showDialog(1);
    }

    public void onToggleClicked(ShortcutPreference shortcutPreference) {
        if (getComponentName() != null) {
            int retrieveUserShortcutType = PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), getComponentName().flattenToString(), 1);
            if (shortcutPreference.isChecked()) {
                AccessibilityUtil.optInAllValuesToSettings(getPrefContext(), retrieveUserShortcutType, getComponentName());
                showDialog(1008);
            } else {
                AccessibilityUtil.optOutAllValuesFromSettings(getPrefContext(), retrieveUserShortcutType, getComponentName());
            }
            this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
        }
    }

    /* access modifiers changed from: package-private */
    public void setupEditShortcutDialog(Dialog dialog) {
        View findViewById = dialog.findViewById(C0444R.C0448id.software_shortcut);
        CheckBox checkBox = (CheckBox) findViewById.findViewById(C0444R.C0448id.checkbox);
        this.mSoftwareTypeCheckBox = checkBox;
        setDialogTextAreaClickListener(findViewById, checkBox);
        View findViewById2 = dialog.findViewById(C0444R.C0448id.hardware_shortcut);
        CheckBox checkBox2 = (CheckBox) findViewById2.findViewById(C0444R.C0448id.checkbox);
        this.mHardwareTypeCheckBox = checkBox2;
        setDialogTextAreaClickListener(findViewById2, checkBox2);
        updateEditShortcutDialogCheckBox();
    }

    /* access modifiers changed from: protected */
    public int getShortcutTypeCheckBoxValue() {
        CheckBox checkBox = this.mSoftwareTypeCheckBox;
        if (checkBox == null || this.mHardwareTypeCheckBox == null) {
            return -1;
        }
        boolean isChecked = checkBox.isChecked();
        return this.mHardwareTypeCheckBox.isChecked() ? isChecked | true ? 1 : 0 : isChecked ? 1 : 0;
    }

    /* access modifiers changed from: protected */
    public int getUserShortcutTypes() {
        return AccessibilityUtil.getUserShortcutTypesFromSettings(getPrefContext(), getComponentName());
    }

    /* access modifiers changed from: private */
    public void callOnTutorialDialogButtonClicked(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        showQuickSettingsTooltipIfNeeded();
    }

    /* access modifiers changed from: protected */
    public void callOnAlertDialogCheckboxClicked(DialogInterface dialogInterface, int i) {
        if (getComponentName() != null) {
            int shortcutTypeCheckBoxValue = getShortcutTypeCheckBoxValue();
            saveNonEmptyUserShortcutType(shortcutTypeCheckBoxValue);
            AccessibilityUtil.optInAllValuesToSettings(getPrefContext(), shortcutTypeCheckBoxValue, getComponentName());
            AccessibilityUtil.optOutAllValuesFromSettings(getPrefContext(), ~shortcutTypeCheckBoxValue, getComponentName());
            boolean z = shortcutTypeCheckBoxValue != 0;
            this.mShortcutPreference.setChecked(z);
            this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
            if (z) {
                showQuickSettingsTooltipIfNeeded();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void initGeneralCategory() {
        PreferenceCategory preferenceCategory = new PreferenceCategory(getPrefContext());
        preferenceCategory.setKey("general_categories");
        preferenceCategory.setTitle(getGeneralCategoryDescription((CharSequence) null));
        getPreferenceScreen().addPreference(preferenceCategory);
    }

    /* access modifiers changed from: package-private */
    public void saveNonEmptyUserShortcutType(int i) {
        if (i != 0) {
            PreferredShortcuts.saveUserShortcutType(getPrefContext(), new PreferredShortcut(getComponentName().flattenToString(), i));
        }
    }

    /* access modifiers changed from: protected */
    public CharSequence getGeneralCategoryDescription(CharSequence charSequence) {
        if (charSequence == null || charSequence.toString().isEmpty()) {
            return getContext().getString(C0444R.string.accessibility_screen_option);
        }
        return charSequence;
    }

    private void setDialogTextAreaClickListener(View view, CheckBox checkBox) {
        view.findViewById(C0444R.C0448id.container).setOnClickListener(new C0583x835f3310(checkBox));
    }

    /* access modifiers changed from: protected */
    public CharSequence getShortcutTypeSummary(Context context) {
        if (!this.mShortcutPreference.isSettingsEditable()) {
            return context.getText(C0444R.string.accessibility_shortcut_edit_dialog_title_hardware);
        }
        if (!this.mShortcutPreference.isChecked()) {
            return context.getText(C0444R.string.switch_off_text);
        }
        int retrieveUserShortcutType = PreferredShortcuts.retrieveUserShortcutType(context, getComponentName().flattenToString(), 1);
        ArrayList arrayList = new ArrayList();
        CharSequence text = context.getText(C0444R.string.accessibility_shortcut_edit_summary_software);
        if (hasShortcutType(retrieveUserShortcutType, 1)) {
            arrayList.add(text);
        }
        if (hasShortcutType(retrieveUserShortcutType, 2)) {
            arrayList.add(context.getText(C0444R.string.accessibility_shortcut_hardware_keyword));
        }
        if (arrayList.isEmpty()) {
            arrayList.add(text);
        }
        return CaseMap.toTitle().wholeString().noLowercase().apply(Locale.getDefault(), (BreakIterator) null, LocaleUtils.getConcatenatedString(arrayList));
    }

    private void updateEditShortcutDialogCheckBox() {
        int restoreOnConfigChangedValue = restoreOnConfigChangedValue();
        if (restoreOnConfigChangedValue == -1) {
            restoreOnConfigChangedValue = PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), getComponentName().flattenToString(), 1);
            if (!this.mShortcutPreference.isChecked()) {
                restoreOnConfigChangedValue = 0;
            }
        }
        this.mSoftwareTypeCheckBox.setChecked(hasShortcutType(restoreOnConfigChangedValue, 1));
        this.mHardwareTypeCheckBox.setChecked(hasShortcutType(restoreOnConfigChangedValue, 2));
    }

    private int restoreOnConfigChangedValue() {
        int i = this.mSavedCheckBoxValue;
        this.mSavedCheckBoxValue = -1;
        return i;
    }

    /* access modifiers changed from: protected */
    public void updateShortcutPreferenceData() {
        int userShortcutTypesFromSettings;
        if (getComponentName() != null && (userShortcutTypesFromSettings = AccessibilityUtil.getUserShortcutTypesFromSettings(getPrefContext(), getComponentName())) != 0) {
            PreferredShortcuts.saveUserShortcutType(getPrefContext(), new PreferredShortcut(getComponentName().flattenToString(), userShortcutTypesFromSettings));
        }
    }

    /* access modifiers changed from: protected */
    public void updateShortcutPreference() {
        if (getComponentName() != null) {
            this.mShortcutPreference.setChecked(AccessibilityUtil.hasValuesInSettings(getPrefContext(), PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), getComponentName().flattenToString(), 1), getComponentName()));
            this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
        }
    }

    /* access modifiers changed from: protected */
    public void showQuickSettingsTooltipIfNeeded(int i) {
        this.mNeedsQSTooltipType = i;
        showQuickSettingsTooltipIfNeeded();
    }

    /* access modifiers changed from: private */
    public void showQuickSettingsTooltipIfNeeded() {
        ComponentName tileComponentName = getTileComponentName();
        if (tileComponentName != null) {
            if (this.mNeedsQSTooltipReshow || !AccessibilityQuickSettingUtils.hasValueInSharedPreferences(getContext(), tileComponentName)) {
                CharSequence tileName = getTileName();
                if (!TextUtils.isEmpty(tileName)) {
                    String string = getString(this.mNeedsQSTooltipType == 0 ? C0444R.string.accessibility_service_qs_tooltips_content : C0444R.string.accessibility_service_auto_added_qs_tooltips_content, tileName);
                    AccessibilityQuickSettingsTooltipWindow accessibilityQuickSettingsTooltipWindow = new AccessibilityQuickSettingsTooltipWindow(getContext());
                    this.mTooltipWindow = accessibilityQuickSettingsTooltipWindow;
                    accessibilityQuickSettingsTooltipWindow.setup(string, C0444R.C0447drawable.accessibility_qs_tooltips_illustration);
                    this.mTooltipWindow.showAtTopCenter(getView());
                    AccessibilityQuickSettingUtils.optInValueToSharedPreferences(getContext(), tileComponentName);
                    this.mNeedsQSTooltipReshow = false;
                }
            }
        }
    }
}
