package com.android.settings.datetime.timezone;

import android.app.timezonedetector.TimeZoneDetector;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.preference.PreferenceCategory;
import androidx.window.C0444R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.datetime.timezone.TimeZoneInfo;
import com.android.settings.datetime.timezone.model.FilteredCountryTimeZones;
import com.android.settings.datetime.timezone.model.TimeZoneData;
import com.android.settings.datetime.timezone.model.TimeZoneDataLoader;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class TimeZoneSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.time_zone_prefs) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return Settings.Global.getInt(context.getContentResolver(), "auto_time_zone", 1) != 1;
        }
    };
    private Locale mLocale;
    private Intent mPendingZonePickerRequestResult;
    private boolean mSelectByRegion;
    private String mSelectedTimeZoneId;
    private TimeZoneData mTimeZoneData;
    private TimeZoneInfo.Formatter mTimeZoneInfoFormatter;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "TimeZoneSettings";
    }

    public int getMetricsCategory() {
        return 515;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.time_zone_prefs;
    }

    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        Locale locale = context.getResources().getConfiguration().getLocales().get(0);
        this.mLocale = locale;
        this.mTimeZoneInfoFormatter = new TimeZoneInfo.Formatter(locale, new Date());
        ArrayList arrayList = new ArrayList();
        RegionPreferenceController regionPreferenceController = new RegionPreferenceController(context);
        regionPreferenceController.setOnClickListener(new TimeZoneSettings$$ExternalSyntheticLambda1(this));
        RegionZonePreferenceController regionZonePreferenceController = new RegionZonePreferenceController(context);
        regionZonePreferenceController.setOnClickListener(new TimeZoneSettings$$ExternalSyntheticLambda2(this));
        FixedOffsetPreferenceController fixedOffsetPreferenceController = new FixedOffsetPreferenceController(context);
        fixedOffsetPreferenceController.setOnClickListener(new TimeZoneSettings$$ExternalSyntheticLambda0(this));
        arrayList.add(regionPreferenceController);
        arrayList.add(regionZonePreferenceController);
        arrayList.add(fixedOffsetPreferenceController);
        return arrayList;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setPreferenceCategoryVisible((PreferenceCategory) findPreference("time_zone_region_preference_category"), false);
        setPreferenceCategoryVisible((PreferenceCategory) findPreference("time_zone_fixed_offset_preference_category"), false);
        getLoaderManager().initLoader(0, (Bundle) null, new TimeZoneDataLoader.LoaderCreator(getContext(), new TimeZoneSettings$$ExternalSyntheticLambda3(this)));
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        String stringExtra;
        if (i2 == -1 && intent != null) {
            if (i == 1 || i == 2) {
                TimeZoneData timeZoneData = this.mTimeZoneData;
                if (timeZoneData == null) {
                    this.mPendingZonePickerRequestResult = intent;
                } else {
                    onZonePickerRequestResult(timeZoneData, intent);
                }
            } else if (i == 3 && (stringExtra = intent.getStringExtra("com.android.settings.datetime.timezone.result_time_zone_id")) != null && !stringExtra.equals(this.mSelectedTimeZoneId)) {
                onFixedOffsetZoneChanged(stringExtra);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setTimeZoneData(TimeZoneData timeZoneData) {
        this.mTimeZoneData = timeZoneData;
    }

    /* access modifiers changed from: private */
    public void onTimeZoneDataReady(TimeZoneData timeZoneData) {
        if (this.mTimeZoneData == null && timeZoneData != null) {
            this.mTimeZoneData = timeZoneData;
            setupForCurrentTimeZone();
            getActivity().invalidateOptionsMenu();
            Intent intent = this.mPendingZonePickerRequestResult;
            if (intent != null) {
                onZonePickerRequestResult(timeZoneData, intent);
                this.mPendingZonePickerRequestResult = null;
            }
        }
    }

    /* access modifiers changed from: private */
    public void startRegionPicker() {
        startPickerFragment(RegionSearchPicker.class, new Bundle(), 1);
    }

    /* access modifiers changed from: private */
    public void onRegionZonePreferenceClicked() {
        Bundle bundle = new Bundle();
        bundle.putString("com.android.settings.datetime.timezone.region_id", ((RegionPreferenceController) use(RegionPreferenceController.class)).getRegionId());
        startPickerFragment(RegionZonePicker.class, bundle, 2);
    }

    /* access modifiers changed from: private */
    public void startFixedOffsetPicker() {
        startPickerFragment(FixedOffsetPicker.class, new Bundle(), 3);
    }

    private void startPickerFragment(Class<? extends BaseTimeZonePicker> cls, Bundle bundle, int i) {
        new SubSettingLauncher(getContext()).setDestination(cls.getCanonicalName()).setArguments(bundle).setSourceMetricsCategory(getMetricsCategory()).setResultListener(this, i).launch();
    }

    private void setDisplayedRegion(String str) {
        ((RegionPreferenceController) use(RegionPreferenceController.class)).setRegionId(str);
        updatePreferenceStates();
    }

    private void setDisplayedTimeZoneInfo(String str, String str2) {
        Class cls = RegionZonePreferenceController.class;
        TimeZoneInfo format = str2 == null ? null : this.mTimeZoneInfoFormatter.format(str2);
        FilteredCountryTimeZones lookupCountryTimeZones = this.mTimeZoneData.lookupCountryTimeZones(str);
        ((RegionZonePreferenceController) use(cls)).setTimeZoneInfo(format);
        RegionZonePreferenceController regionZonePreferenceController = (RegionZonePreferenceController) use(cls);
        boolean z = true;
        if (format != null && (lookupCountryTimeZones == null || lookupCountryTimeZones.getPreferredTimeZoneIds().size() <= 1)) {
            z = false;
        }
        regionZonePreferenceController.setClickable(z);
        ((TimeZoneInfoPreferenceController) use(TimeZoneInfoPreferenceController.class)).setTimeZoneInfo(format);
        updatePreferenceStates();
    }

    private void setDisplayedFixedOffsetTimeZoneInfo(String str) {
        Class cls = FixedOffsetPreferenceController.class;
        if (isFixedOffset(str)) {
            ((FixedOffsetPreferenceController) use(cls)).setTimeZoneInfo(this.mTimeZoneInfoFormatter.format(str));
        } else {
            ((FixedOffsetPreferenceController) use(cls)).setTimeZoneInfo((TimeZoneInfo) null);
        }
        updatePreferenceStates();
    }

    private void onZonePickerRequestResult(TimeZoneData timeZoneData, Intent intent) {
        String stringExtra = intent.getStringExtra("com.android.settings.datetime.timezone.result_region_id");
        String stringExtra2 = intent.getStringExtra("com.android.settings.datetime.timezone.result_time_zone_id");
        if (!Objects.equals(stringExtra, ((RegionPreferenceController) use(RegionPreferenceController.class)).getRegionId()) || !Objects.equals(stringExtra2, this.mSelectedTimeZoneId)) {
            FilteredCountryTimeZones lookupCountryTimeZones = timeZoneData.lookupCountryTimeZones(stringExtra);
            if (lookupCountryTimeZones == null || !lookupCountryTimeZones.getPreferredTimeZoneIds().contains(stringExtra2)) {
                Log.e("TimeZoneSettings", "Unknown time zone id is selected: " + stringExtra2);
                return;
            }
            this.mSelectedTimeZoneId = stringExtra2;
            setDisplayedRegion(stringExtra);
            setDisplayedTimeZoneInfo(stringExtra, this.mSelectedTimeZoneId);
            saveTimeZone(stringExtra, this.mSelectedTimeZoneId);
            setSelectByRegion(true);
        }
    }

    private void onFixedOffsetZoneChanged(String str) {
        this.mSelectedTimeZoneId = str;
        setDisplayedFixedOffsetTimeZoneInfo(str);
        saveTimeZone((String) null, this.mSelectedTimeZoneId);
        setSelectByRegion(false);
    }

    private void saveTimeZone(String str, String str2) {
        SharedPreferences.Editor edit = getPreferenceManager().getSharedPreferences().edit();
        if (str == null) {
            edit.remove("time_zone_region");
        } else {
            edit.putString("time_zone_region", str);
        }
        edit.apply();
        ((TimeZoneDetector) getActivity().getSystemService(TimeZoneDetector.class)).suggestManualTimeZone(TimeZoneDetector.createManualTimeZoneSuggestion(str2, "Settings: Set time zone"));
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.add(0, 1, 0, C0444R.string.zone_menu_by_region);
        menu.add(0, 2, 0, C0444R.string.zone_menu_by_offset);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        boolean z = true;
        menu.findItem(1).setVisible(this.mTimeZoneData != null && !this.mSelectByRegion);
        MenuItem findItem = menu.findItem(2);
        if (this.mTimeZoneData == null || !this.mSelectByRegion) {
            z = false;
        }
        findItem.setVisible(z);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 1) {
            startRegionPicker();
            return true;
        } else if (itemId != 2) {
            return false;
        } else {
            startFixedOffsetPicker();
            return true;
        }
    }

    private void setupForCurrentTimeZone() {
        String id = TimeZone.getDefault().getID();
        this.mSelectedTimeZoneId = id;
        setSelectByRegion(!isFixedOffset(id));
    }

    private static boolean isFixedOffset(String str) {
        return str.startsWith("Etc/GMT") || str.equals("Etc/UTC");
    }

    private void setSelectByRegion(boolean z) {
        this.mSelectByRegion = z;
        setPreferenceCategoryVisible((PreferenceCategory) findPreference("time_zone_region_preference_category"), z);
        setPreferenceCategoryVisible((PreferenceCategory) findPreference("time_zone_fixed_offset_preference_category"), !z);
        String localeRegionId = getLocaleRegionId();
        if (!this.mTimeZoneData.getRegionIds().contains(localeRegionId)) {
            localeRegionId = null;
        }
        setDisplayedRegion(localeRegionId);
        setDisplayedTimeZoneInfo(localeRegionId, (String) null);
        if (!this.mSelectByRegion) {
            setDisplayedFixedOffsetTimeZoneInfo(this.mSelectedTimeZoneId);
            return;
        }
        String findRegionIdForTzId = findRegionIdForTzId(this.mSelectedTimeZoneId);
        if (findRegionIdForTzId != null) {
            setDisplayedRegion(findRegionIdForTzId);
            setDisplayedTimeZoneInfo(findRegionIdForTzId, this.mSelectedTimeZoneId);
        }
    }

    private String findRegionIdForTzId(String str) {
        return findRegionIdForTzId(str, getPreferenceManager().getSharedPreferences().getString("time_zone_region", (String) null), getLocaleRegionId());
    }

    /* access modifiers changed from: package-private */
    public String findRegionIdForTzId(String str, String str2, String str3) {
        Set<String> lookupCountryCodesForZoneId = this.mTimeZoneData.lookupCountryCodesForZoneId(str);
        if (lookupCountryCodesForZoneId.size() == 0) {
            return null;
        }
        if (str2 != null && lookupCountryCodesForZoneId.contains(str2)) {
            return str2;
        }
        if (str3 == null || !lookupCountryCodesForZoneId.contains(str3)) {
            return ((String[]) lookupCountryCodesForZoneId.toArray(new String[lookupCountryCodesForZoneId.size()]))[0];
        }
        return str3;
    }

    private void setPreferenceCategoryVisible(PreferenceCategory preferenceCategory, boolean z) {
        preferenceCategory.setVisible(z);
        for (int i = 0; i < preferenceCategory.getPreferenceCount(); i++) {
            preferenceCategory.getPreference(i).setVisible(z);
        }
    }

    private String getLocaleRegionId() {
        return this.mLocale.getCountry().toUpperCase(Locale.US);
    }
}
