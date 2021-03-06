package com.android.settings.accessibility;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.icu.text.BreakIterator;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.SettingsActivity;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.accessibility.ShortcutPreference;
import com.android.settings.utils.LocaleUtils;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.settingslib.widget.IllustrationPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import com.android.settingslib.widget.TopIntroPreference;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class ToggleFeaturePreferenceFragment extends SettingsPreferenceFragment implements ShortcutPreference.OnClickCallback, OnMainSwitchChangeListener {
    protected ComponentName mComponentName;
    private CharSequence mDescription;
    protected Dialog mDialog;
    protected AccessibilityFooterPreferenceController mFooterPreferenceController;
    private CheckBox mHardwareTypeCheckBox;
    protected CharSequence mHtmlDescription;
    private final Html.ImageGetter mImageGetter = new ToggleFeaturePreferenceFragment$$ExternalSyntheticLambda2(this);
    private ImageView mImageGetterCacheView;
    protected Uri mImageUri;
    private boolean mNeedsQSTooltipReshow = false;
    private int mNeedsQSTooltipType = 0;
    protected CharSequence mPackageName;
    protected String mPreferenceKey;
    private boolean mSavedAccessibilityFloatingMenuEnabled;
    protected int mSavedCheckBoxValue = -1;
    private AccessibilitySettingsContentObserver mSettingsContentObserver;
    protected Intent mSettingsIntent;
    protected Preference mSettingsPreference;
    protected CharSequence mSettingsTitle;
    protected ShortcutPreference mShortcutPreference;
    private CheckBox mSoftwareTypeCheckBox;
    protected SettingsMainSwitchPreference mToggleServiceSwitchPreference;
    private AccessibilityQuickSettingsTooltipWindow mTooltipWindow;
    protected CharSequence mTopIntroTitle;
    private AccessibilityManager.TouchExplorationStateChangeListener mTouchExplorationStateChangeListener;

    private boolean hasShortcutType(int i, int i2) {
        return (i & i2) == i2;
    }

    public int getDialogMetricsCategory(int i) {
        if (i != 1) {
            return i != 1008 ? 0 : 1810;
        }
        return 1812;
    }

    public int getHelpResource() {
        return 0;
    }

    public int getMetricsCategory() {
        return 4;
    }

    /* access modifiers changed from: protected */
    public String getShortcutPreferenceKey() {
        return "shortcut_preference";
    }

    /* access modifiers changed from: package-private */
    public abstract ComponentName getTileComponentName();

    /* access modifiers changed from: package-private */
    public abstract CharSequence getTileName();

    /* access modifiers changed from: package-private */
    public abstract int getUserShortcutTypes();

    /* access modifiers changed from: protected */
    public void onRemoveSwitchPreferenceToggleSwitch() {
    }

    /* access modifiers changed from: protected */
    public void updateSwitchBarToggleSwitch() {
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Drawable lambda$new$0(String str) {
        if (str == null || !str.startsWith("R.drawable.")) {
            return null;
        }
        String substring = str.substring(11);
        return getDrawableFromUri(Uri.parse("android.resource://" + this.mComponentName.getPackageName() + "/" + "drawable" + "/" + substring));
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
        setupDefaultShortcutIfNecessary(getPrefContext());
        if (getPreferenceScreenResId() <= 0) {
            setPreferenceScreen(getPreferenceManager().createPreferenceScreen(getPrefContext()));
        }
        AccessibilitySettingsContentObserver accessibilitySettingsContentObserver = new AccessibilitySettingsContentObserver(new Handler());
        this.mSettingsContentObserver = accessibilitySettingsContentObserver;
        registerKeysToObserverCallback(accessibilitySettingsContentObserver);
    }

    /* access modifiers changed from: protected */
    public void registerKeysToObserverCallback(AccessibilitySettingsContentObserver accessibilitySettingsContentObserver) {
        accessibilitySettingsContentObserver.registerKeysToObserverCallback(getShortcutFeatureSettingsKeys(), new ToggleFeaturePreferenceFragment$$ExternalSyntheticLambda5(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerKeysToObserverCallback$1(String str) {
        updateShortcutPreferenceData();
        updateShortcutPreference();
    }

    /* access modifiers changed from: protected */
    public List<String> getShortcutFeatureSettingsKeys() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("accessibility_button_targets");
        arrayList.add("accessibility_shortcut_target_service");
        return arrayList;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        onProcessArguments(getArguments());
        initTopIntroPreference();
        initAnimatedImagePreference();
        initToggleServiceSwitchPreference();
        initGeneralCategory();
        initShortcutPreference();
        initSettingsPreference();
        initHtmlTextPreference();
        initFooterPreference();
        installActionBarToggleSwitch();
        updateToggleServiceTitle(this.mToggleServiceSwitchPreference);
        this.mTouchExplorationStateChangeListener = new ToggleFeaturePreferenceFragment$$ExternalSyntheticLambda4(this);
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$2(boolean z) {
        removeDialog(1);
        this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((SettingsActivity) getActivity()).getSwitchBar().hide();
        updatePreferenceOrder();
        if (this.mNeedsQSTooltipReshow) {
            getView().post(new ToggleFeaturePreferenceFragment$$ExternalSyntheticLambda6(this));
        }
    }

    public void onResume() {
        super.onResume();
        ((AccessibilityManager) getPrefContext().getSystemService(AccessibilityManager.class)).addTouchExplorationStateChangeListener(this.mTouchExplorationStateChangeListener);
        this.mSettingsContentObserver.register(getContentResolver());
        updateShortcutPreferenceData();
        updateShortcutPreference();
        updateEditShortcutDialogIfNeeded();
    }

    public void onPause() {
        ((AccessibilityManager) getPrefContext().getSystemService(AccessibilityManager.class)).removeTouchExplorationStateChangeListener(this.mTouchExplorationStateChangeListener);
        this.mSettingsContentObserver.unregister(getContentResolver());
        this.mSavedAccessibilityFloatingMenuEnabled = AccessibilityUtil.isFloatingMenuEnabled(getContext());
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
            String string = getPrefContext().getString(C0444R.string.accessibility_shortcut_title, new Object[]{this.mPackageName});
            boolean isAnySetupWizard = WizardManagerHelper.isAnySetupWizard(getIntent());
            AlertDialog showEditShortcutDialog = AccessibilityDialogUtils.showEditShortcutDialog(getPrefContext(), isAnySetupWizard ? 1 : 0, string, new ToggleFeaturePreferenceFragment$$ExternalSyntheticLambda0(this));
            this.mDialog = showEditShortcutDialog;
            setupEditShortcutDialog(showEditShortcutDialog);
            return this.mDialog;
        } else if (i == 1008) {
            AlertDialog createAccessibilityTutorialDialog = AccessibilityGestureNavigationTutorial.createAccessibilityTutorialDialog(getPrefContext(), getUserShortcutTypes(), new ToggleFeaturePreferenceFragment$$ExternalSyntheticLambda1(this));
            this.mDialog = createAccessibilityTutorialDialog;
            createAccessibilityTutorialDialog.setCanceledOnTouchOutside(false);
            return this.mDialog;
        } else {
            throw new IllegalArgumentException("Unsupported dialogId " + i);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        removeActionBarToggleSwitch();
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        onPreferenceToggled(this.mPreferenceKey, z);
    }

    /* access modifiers changed from: protected */
    public void updateToggleServiceTitle(SettingsMainSwitchPreference settingsMainSwitchPreference) {
        settingsMainSwitchPreference.setTitle(getString(C0444R.string.accessibility_service_primary_switch_title, this.mPackageName));
    }

    /* access modifiers changed from: protected */
    public void updateShortcutTitle(ShortcutPreference shortcutPreference) {
        shortcutPreference.setTitle((CharSequence) getString(C0444R.string.accessibility_shortcut_title, this.mPackageName));
    }

    /* access modifiers changed from: protected */
    public void onPreferenceToggled(String str, boolean z) {
        if (z) {
            showQuickSettingsTooltipIfNeeded();
        }
    }

    /* access modifiers changed from: protected */
    public void onInstallSwitchPreferenceToggleSwitch() {
        updateSwitchBarToggleSwitch();
        this.mToggleServiceSwitchPreference.addOnSwitchChangeListener(this);
    }

    private void installActionBarToggleSwitch() {
        onInstallSwitchPreferenceToggleSwitch();
    }

    private void removeActionBarToggleSwitch() {
        this.mToggleServiceSwitchPreference.setOnPreferenceClickListener((Preference.OnPreferenceClickListener) null);
        onRemoveSwitchPreferenceToggleSwitch();
    }

    public void setTitle(String str) {
        getActivity().setTitle(str);
    }

    /* access modifiers changed from: protected */
    public void onProcessArguments(Bundle bundle) {
        this.mPreferenceKey = bundle.getString("preference_key");
        if (bundle.containsKey("resolve_info")) {
            getActivity().setTitle(((ResolveInfo) bundle.getParcelable("resolve_info")).loadLabel(getPackageManager()).toString());
        } else if (bundle.containsKey("title")) {
            setTitle(bundle.getString("title"));
        }
        if (bundle.containsKey("summary")) {
            this.mDescription = bundle.getCharSequence("summary");
        }
        if (bundle.containsKey("html_description")) {
            this.mHtmlDescription = bundle.getCharSequence("html_description");
        }
        if (bundle.containsKey("intro")) {
            this.mTopIntroTitle = bundle.getCharSequence("intro");
        }
    }

    /* access modifiers changed from: protected */
    public List<String> getPreferenceOrderList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("top_intro");
        arrayList.add("animated_image");
        arrayList.add("use_service");
        arrayList.add("general_categories");
        arrayList.add("html_description");
        return arrayList;
    }

    private void updatePreferenceOrder() {
        List<String> preferenceOrderList = getPreferenceOrderList();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.setOrderingAsAdded(false);
        int size = preferenceOrderList.size();
        for (int i = 0; i < size; i++) {
            Preference findPreference = preferenceScreen.findPreference(preferenceOrderList.get(i));
            if (findPreference != null) {
                findPreference.setOrder(i);
            }
        }
    }

    private Drawable getDrawableFromUri(Uri uri) {
        if (this.mImageGetterCacheView == null) {
            this.mImageGetterCacheView = new ImageView(getPrefContext());
        }
        this.mImageGetterCacheView.setAdjustViewBounds(true);
        this.mImageGetterCacheView.setImageURI(uri);
        if (this.mImageGetterCacheView.getDrawable() == null) {
            return null;
        }
        Drawable newDrawable = this.mImageGetterCacheView.getDrawable().mutate().getConstantState().newDrawable();
        this.mImageGetterCacheView.setImageURI((Uri) null);
        int intrinsicWidth = newDrawable.getIntrinsicWidth();
        int intrinsicHeight = newDrawable.getIntrinsicHeight();
        int screenHeightPixels = AccessibilityUtil.getScreenHeightPixels(getPrefContext()) / 2;
        if (intrinsicWidth > AccessibilityUtil.getScreenWidthPixels(getPrefContext()) || intrinsicHeight > screenHeightPixels) {
            return null;
        }
        newDrawable.setBounds(0, 0, newDrawable.getIntrinsicWidth(), newDrawable.getIntrinsicHeight());
        return newDrawable;
    }

    private void initAnimatedImagePreference() {
        if (this.mImageUri != null) {
            IllustrationPreference illustrationPreference = new IllustrationPreference(getPrefContext());
            illustrationPreference.setImageUri(this.mImageUri);
            illustrationPreference.setSelectable(false);
            illustrationPreference.setMaxHeight(AccessibilityUtil.getDisplayBounds(getPrefContext()).height() / 2);
            illustrationPreference.setKey("animated_image");
            getPreferenceScreen().addPreference(illustrationPreference);
        }
    }

    /* access modifiers changed from: package-private */
    public void initTopIntroPreference() {
        if (!TextUtils.isEmpty(this.mTopIntroTitle)) {
            TopIntroPreference topIntroPreference = new TopIntroPreference(getPrefContext());
            topIntroPreference.setKey("top_intro");
            topIntroPreference.setTitle(this.mTopIntroTitle);
            getPreferenceScreen().addPreference(topIntroPreference);
        }
    }

    private void initToggleServiceSwitchPreference() {
        SettingsMainSwitchPreference settingsMainSwitchPreference = new SettingsMainSwitchPreference(getPrefContext());
        this.mToggleServiceSwitchPreference = settingsMainSwitchPreference;
        settingsMainSwitchPreference.setKey("use_service");
        if (getArguments().containsKey("checked")) {
            this.mToggleServiceSwitchPreference.setChecked(getArguments().getBoolean("checked"));
        }
        getPreferenceScreen().addPreference(this.mToggleServiceSwitchPreference);
    }

    private void initGeneralCategory() {
        PreferenceCategory preferenceCategory = new PreferenceCategory(getPrefContext());
        preferenceCategory.setKey("general_categories");
        preferenceCategory.setTitle((int) C0444R.string.accessibility_screen_option);
        getPreferenceScreen().addPreference(preferenceCategory);
    }

    /* access modifiers changed from: protected */
    public void initShortcutPreference() {
        ShortcutPreference shortcutPreference = new ShortcutPreference(getPrefContext(), (AttributeSet) null);
        this.mShortcutPreference = shortcutPreference;
        shortcutPreference.setPersistent(false);
        this.mShortcutPreference.setKey(getShortcutPreferenceKey());
        this.mShortcutPreference.setOnClickCallback(this);
        updateShortcutTitle(this.mShortcutPreference);
        ((PreferenceCategory) findPreference("general_categories")).addPreference(this.mShortcutPreference);
    }

    /* access modifiers changed from: protected */
    public void initSettingsPreference() {
        if (this.mSettingsTitle != null && this.mSettingsIntent != null) {
            Preference preference = new Preference(getPrefContext());
            this.mSettingsPreference = preference;
            preference.setTitle(this.mSettingsTitle);
            this.mSettingsPreference.setIconSpaceReserved(false);
            this.mSettingsPreference.setIntent(this.mSettingsIntent);
            ((PreferenceCategory) findPreference("general_categories")).addPreference(this.mSettingsPreference);
        }
    }

    private void initHtmlTextPreference() {
        if (!TextUtils.isEmpty(this.mHtmlDescription)) {
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            Spanned fromHtml = Html.fromHtml(this.mHtmlDescription.toString(), 63, this.mImageGetter, (Html.TagHandler) null);
            AccessibilityFooterPreference accessibilityFooterPreference = new AccessibilityFooterPreference(preferenceScreen.getContext());
            accessibilityFooterPreference.setKey("html_description");
            accessibilityFooterPreference.setSummary((CharSequence) fromHtml);
            preferenceScreen.addPreference(accessibilityFooterPreference);
            String string = getString(C0444R.string.accessibility_introduction_title, this.mPackageName);
            AccessibilityFooterPreferenceController accessibilityFooterPreferenceController = new AccessibilityFooterPreferenceController(preferenceScreen.getContext(), accessibilityFooterPreference.getKey());
            this.mFooterPreferenceController = accessibilityFooterPreferenceController;
            accessibilityFooterPreferenceController.setIntroductionTitle(string);
            this.mFooterPreferenceController.displayPreference(preferenceScreen);
        }
    }

    private void initFooterPreference() {
        if (!TextUtils.isEmpty(this.mDescription)) {
            createFooterPreference(getPreferenceScreen(), this.mDescription, getString(C0444R.string.accessibility_introduction_title, this.mPackageName));
        }
        if (TextUtils.isEmpty(this.mHtmlDescription) && TextUtils.isEmpty(this.mDescription)) {
            CharSequence text = getText(C0444R.string.accessibility_service_default_description);
            createFooterPreference(getPreferenceScreen(), text, getString(C0444R.string.accessibility_introduction_title, this.mPackageName));
        }
    }

    /* access modifiers changed from: package-private */
    public void createFooterPreference(PreferenceScreen preferenceScreen, CharSequence charSequence, String str) {
        AccessibilityFooterPreference accessibilityFooterPreference = new AccessibilityFooterPreference(preferenceScreen.getContext());
        accessibilityFooterPreference.setSummary(charSequence);
        preferenceScreen.addPreference(accessibilityFooterPreference);
        AccessibilityFooterPreferenceController accessibilityFooterPreferenceController = new AccessibilityFooterPreferenceController(preferenceScreen.getContext(), accessibilityFooterPreference.getKey());
        this.mFooterPreferenceController = accessibilityFooterPreferenceController;
        accessibilityFooterPreferenceController.setIntroductionTitle(str);
        this.mFooterPreferenceController.displayPreference(preferenceScreen);
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

    private void setDialogTextAreaClickListener(View view, CheckBox checkBox) {
        view.findViewById(C0444R.C0448id.container).setOnClickListener(new ToggleFeaturePreferenceFragment$$ExternalSyntheticLambda3(checkBox));
    }

    private void updateEditShortcutDialogCheckBox() {
        int restoreOnConfigChangedValue = restoreOnConfigChangedValue();
        if (restoreOnConfigChangedValue == -1) {
            restoreOnConfigChangedValue = PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), this.mComponentName.flattenToString(), 1);
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
    public int getShortcutTypeCheckBoxValue() {
        CheckBox checkBox = this.mSoftwareTypeCheckBox;
        if (checkBox == null || this.mHardwareTypeCheckBox == null) {
            return -1;
        }
        boolean isChecked = checkBox.isChecked();
        return this.mHardwareTypeCheckBox.isChecked() ? isChecked | true ? 1 : 0 : isChecked ? 1 : 0;
    }

    private static CharSequence getSoftwareShortcutTypeSummary(Context context) {
        boolean isFloatingMenuEnabled = AccessibilityUtil.isFloatingMenuEnabled(context);
        int i = C0444R.string.accessibility_shortcut_edit_summary_software;
        if (!isFloatingMenuEnabled && AccessibilityUtil.isGestureNavigateEnabled(context)) {
            i = C0444R.string.accessibility_shortcut_edit_summary_software_gesture;
        }
        return context.getText(i);
    }

    /* access modifiers changed from: protected */
    public CharSequence getShortcutTypeSummary(Context context) {
        if (!this.mShortcutPreference.isSettingsEditable()) {
            return context.getText(C0444R.string.accessibility_shortcut_edit_dialog_title_hardware);
        }
        if (!this.mShortcutPreference.isChecked()) {
            return context.getText(C0444R.string.switch_off_text);
        }
        int retrieveUserShortcutType = PreferredShortcuts.retrieveUserShortcutType(context, this.mComponentName.flattenToString(), 1);
        ArrayList arrayList = new ArrayList();
        if (hasShortcutType(retrieveUserShortcutType, 1)) {
            arrayList.add(getSoftwareShortcutTypeSummary(context));
        }
        if (hasShortcutType(retrieveUserShortcutType, 2)) {
            arrayList.add(context.getText(C0444R.string.accessibility_shortcut_hardware_keyword));
        }
        if (arrayList.isEmpty()) {
            arrayList.add(getSoftwareShortcutTypeSummary(context));
        }
        return CaseMap.toTitle().wholeString().noLowercase().apply(Locale.getDefault(), (BreakIterator) null, LocaleUtils.getConcatenatedString(arrayList));
    }

    /* access modifiers changed from: private */
    public void callOnTutorialDialogButtonClicked(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        showQuickSettingsTooltipIfNeeded();
    }

    /* access modifiers changed from: protected */
    public void callOnAlertDialogCheckboxClicked(DialogInterface dialogInterface, int i) {
        if (this.mComponentName != null) {
            int shortcutTypeCheckBoxValue = getShortcutTypeCheckBoxValue();
            saveNonEmptyUserShortcutType(shortcutTypeCheckBoxValue);
            AccessibilityUtil.optInAllValuesToSettings(getPrefContext(), shortcutTypeCheckBoxValue, this.mComponentName);
            AccessibilityUtil.optOutAllValuesFromSettings(getPrefContext(), ~shortcutTypeCheckBoxValue, this.mComponentName);
            boolean z = shortcutTypeCheckBoxValue != 0;
            this.mShortcutPreference.setChecked(z);
            this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
            if (z) {
                showQuickSettingsTooltipIfNeeded();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateShortcutPreferenceData() {
        int userShortcutTypesFromSettings;
        if (this.mComponentName != null && (userShortcutTypesFromSettings = AccessibilityUtil.getUserShortcutTypesFromSettings(getPrefContext(), this.mComponentName)) != 0) {
            PreferredShortcuts.saveUserShortcutType(getPrefContext(), new PreferredShortcut(this.mComponentName.flattenToString(), userShortcutTypesFromSettings));
        }
    }

    /* access modifiers changed from: protected */
    public void updateShortcutPreference() {
        if (this.mComponentName != null) {
            this.mShortcutPreference.setChecked(AccessibilityUtil.hasValuesInSettings(getPrefContext(), PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), this.mComponentName.flattenToString(), 1), this.mComponentName));
            this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
        }
    }

    public void onToggleClicked(ShortcutPreference shortcutPreference) {
        if (this.mComponentName != null) {
            int retrieveUserShortcutType = PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), this.mComponentName.flattenToString(), 1);
            if (shortcutPreference.isChecked()) {
                AccessibilityUtil.optInAllValuesToSettings(getPrefContext(), retrieveUserShortcutType, this.mComponentName);
                showDialog(1008);
            } else {
                AccessibilityUtil.optOutAllValuesFromSettings(getPrefContext(), retrieveUserShortcutType, this.mComponentName);
            }
            this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
        }
    }

    public void onSettingsClicked(ShortcutPreference shortcutPreference) {
        showDialog(1);
    }

    private static void setupDefaultShortcutIfNecessary(Context context) {
        ComponentName unflattenFromString;
        if (TextUtils.isEmpty(Settings.Secure.getString(context.getContentResolver(), "accessibility_shortcut_target_service"))) {
            String shortcutTargetServiceComponentNameString = AccessibilityUtils.getShortcutTargetServiceComponentNameString(context, UserHandle.myUserId());
            if (!TextUtils.isEmpty(shortcutTargetServiceComponentNameString) && (unflattenFromString = ComponentName.unflattenFromString(shortcutTargetServiceComponentNameString)) != null) {
                Settings.Secure.putString(context.getContentResolver(), "accessibility_shortcut_target_service", unflattenFromString.flattenToString());
            }
        }
    }

    private void updateEditShortcutDialogIfNeeded() {
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            if (this.mSavedAccessibilityFloatingMenuEnabled != AccessibilityUtil.isFloatingMenuEnabled(getContext())) {
                AccessibilityDialogUtils.updateSoftwareShortcutInDialog(getContext(), this.mDialog);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void saveNonEmptyUserShortcutType(int i) {
        if (i != 0) {
            PreferredShortcuts.saveUserShortcutType(getPrefContext(), new PreferredShortcut(this.mComponentName.flattenToString(), i));
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

    /* access modifiers changed from: protected */
    public CharSequence loadTileLabel(Context context, ComponentName componentName) {
        PackageManager packageManager = context.getPackageManager();
        for (ResolveInfo resolveInfo : packageManager.queryIntentServices(new Intent("android.service.quicksettings.action.QS_TILE"), 128)) {
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (TextUtils.equals(componentName.getPackageName(), serviceInfo.packageName) && TextUtils.equals(componentName.getClassName(), serviceInfo.name)) {
                return serviceInfo.loadLabel(packageManager);
            }
        }
        return null;
    }
}
