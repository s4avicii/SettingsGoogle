package com.google.android.settings.fuelgauge;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.BatteryStatsManager;
import android.os.BatteryUsageStats;
import android.os.BatteryUsageStatsQuery;
import android.util.Log;
import androidx.core.app.JobIntentService;
import com.android.settings.SettingsActivity;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.fuelgauge.BatteryAppListPreferenceController;
import com.android.settings.fuelgauge.BatteryEntry;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.List;

public class BatteryUsageLoaderService extends JobIntentService {
    private static final Intent JOB_INTENT = new Intent("action.LOAD_BATTERY_USAGE_DATA");
    static BatteryAppListPreferenceController mController;

    static void enqueueWork(Context context) {
        AsyncTask.execute(new BatteryUsageLoaderService$$ExternalSyntheticLambda0(context));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$enqueueWork$0(Context context) {
        Log.d("BatteryUsageLoaderService", "loadUsageDataSafely() in the AsyncTask");
        loadUsageDataSafely(context);
    }

    /* access modifiers changed from: protected */
    public void onHandleWork(Intent intent) {
        Log.d("BatteryUsageLoaderService", "onHandleWork: load usage data");
        loadUsageDataSafely(this);
    }

    private static void loadUsageDataSafely(Context context) {
        try {
            loadUsageData(context);
        } catch (RuntimeException e) {
            Log.e("BatteryUsageLoaderService", "loadUsageData:" + e);
        }
    }

    static void loadUsageData(Context context) {
        if (!DatabaseUtils.isContentProviderEnabled(context)) {
            Log.w("BatteryUsageLoaderService", "battery usage content provider is disabled!");
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        BatteryUsageStats batteryUsageStats = ((BatteryStatsManager) context.getSystemService(BatteryStatsManager.class)).getBatteryUsageStats(new BatteryUsageStatsQuery.Builder().includeBatteryHistory().build());
        if (batteryUsageStats == null) {
            Log.w("BatteryUsageLoaderService", "getBatteryUsageStats() returns null content");
        }
        BatteryAppListPreferenceController batteryAppListPreferenceController = mController;
        if (batteryAppListPreferenceController == null) {
            batteryAppListPreferenceController = new BatteryAppListPreferenceController(context, (String) null, (Lifecycle) null, (SettingsActivity) null, (InstrumentedPreferenceFragment) null);
        }
        List<BatteryEntry> batteryEntryList = batteryUsageStats != null ? batteryAppListPreferenceController.getBatteryEntryList(batteryUsageStats, true) : null;
        if (batteryEntryList == null || batteryEntryList.isEmpty()) {
            Log.w("BatteryUsageLoaderService", "getBatteryEntryList() returns null or empty content");
        }
        Log.d("BatteryUsageLoaderService", String.format("getBatteryUsageStats() in %d/ms", new Object[]{Long.valueOf(System.currentTimeMillis() - currentTimeMillis)}));
        DatabaseUtils.sendBatteryEntryData(context, batteryEntryList, batteryUsageStats);
        if (batteryUsageStats != null) {
            try {
                batteryUsageStats.close();
            } catch (Exception e) {
                Log.e("BatteryUsageLoaderService", "BatteryUsageStats.close() failed", e);
            }
        }
    }
}
