package com.android.settings.fuelgauge.batterysaver;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.fuelgauge.BatterySaverUtils;
import com.android.settingslib.widget.CandidateInfo;
import com.android.settingslib.widget.SelectorWithWidgetPreference;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class BatterySaverScheduleSettings extends RadioButtonPickerFragment {
    Context mContext;
    public BatterySaverScheduleRadioButtonsController mRadioButtonController;
    private int mSaverPercentage;
    private String mSaverScheduleKey;
    private BatterySaverScheduleSeekBarController mSeekBarController;
    final ContentObserver mSettingsObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z, Uri uri) {
            BatterySaverScheduleSettings.this.getPreferenceScreen().removeAll();
            BatterySaverScheduleSettings.this.updateCandidates();
        }
    };

    public int getMetricsCategory() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.battery_saver_schedule_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        BatterySaverScheduleSeekBarController batterySaverScheduleSeekBarController = new BatterySaverScheduleSeekBarController(context);
        this.mSeekBarController = batterySaverScheduleSeekBarController;
        this.mRadioButtonController = new BatterySaverScheduleRadioButtonsController(context, batterySaverScheduleSeekBarController);
        this.mContext = context;
    }

    public void onResume() {
        super.onResume();
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("low_power_warning_acknowledged"), false, this.mSettingsObserver);
        this.mSaverScheduleKey = this.mRadioButtonController.getDefaultKey();
        this.mSaverPercentage = getSaverPercentage();
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setDivider(new ColorDrawable(0));
        setDividerHeight(0);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onPause() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
        AsyncTask.execute(new BatterySaverScheduleSettings$$ExternalSyntheticLambda0(this));
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        Context context = getContext();
        ArrayList newArrayList = Lists.newArrayList();
        String string = getContext().getResources().getString(17039873);
        newArrayList.add(new BatterySaverScheduleCandidateInfo(context.getText(C0444R.string.battery_saver_auto_no_schedule), (CharSequence) null, "key_battery_saver_no_schedule", true));
        if (!TextUtils.isEmpty(string)) {
            newArrayList.add(new BatterySaverScheduleCandidateInfo(context.getText(C0444R.string.battery_saver_auto_routine), context.getText(C0444R.string.battery_saver_auto_routine_summary), "key_battery_saver_routine", true));
        } else {
            BatterySaverUtils.revertScheduleToNoneIfNeeded(context);
        }
        newArrayList.add(new BatterySaverScheduleCandidateInfo(context.getText(C0444R.string.battery_saver_auto_percentage), (CharSequence) null, "key_battery_saver_percentage", true));
        return newArrayList;
    }

    public void bindPreferenceExtra(SelectorWithWidgetPreference selectorWithWidgetPreference, String str, CandidateInfo candidateInfo, String str2, String str3) {
        CharSequence summary = ((BatterySaverScheduleCandidateInfo) candidateInfo).getSummary();
        if (summary != null) {
            selectorWithWidgetPreference.setSummary(summary);
            selectorWithWidgetPreference.setAppendixVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public void addStaticPreferences(PreferenceScreen preferenceScreen) {
        this.mSeekBarController.updateSeekBar();
        this.mSeekBarController.addToScreen(preferenceScreen);
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        return this.mRadioButtonController.getDefaultKey();
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        return this.mRadioButtonController.setDefaultKey(str);
    }

    /* access modifiers changed from: private */
    /* renamed from: logPowerSaver */
    public void lambda$onPause$0() {
        int saverPercentage = getSaverPercentage();
        String defaultKey = this.mRadioButtonController.getDefaultKey();
        if (!this.mSaverScheduleKey.equals(defaultKey) || this.mSaverPercentage != saverPercentage) {
            FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider().action(52, 1784, 1785, defaultKey, saverPercentage);
        }
    }

    private int getSaverPercentage() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "low_power_trigger_level", -1);
    }

    static class BatterySaverScheduleCandidateInfo extends CandidateInfo {
        private final String mKey;
        private final CharSequence mLabel;
        private final CharSequence mSummary;

        public Drawable loadIcon() {
            return null;
        }

        BatterySaverScheduleCandidateInfo(CharSequence charSequence, CharSequence charSequence2, String str, boolean z) {
            super(z);
            this.mLabel = charSequence;
            this.mKey = str;
            this.mSummary = charSequence2;
        }

        public CharSequence loadLabel() {
            return this.mLabel;
        }

        public String getKey() {
            return this.mKey;
        }

        public CharSequence getSummary() {
            return this.mSummary;
        }
    }
}
