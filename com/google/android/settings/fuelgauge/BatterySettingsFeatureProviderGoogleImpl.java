package com.google.android.settings.fuelgauge;

import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.android.settings.fuelgauge.BatterySettingsFeatureProviderImpl;

public class BatterySettingsFeatureProviderGoogleImpl extends BatterySettingsFeatureProviderImpl {
    public BatterySettingsFeatureProviderGoogleImpl(Context context) {
        super(context);
    }

    public ComponentName getReplacingActivity(ComponentName componentName) {
        if (!DatabaseUtils.isContentProviderEnabled(this.mContext)) {
            return componentName;
        }
        Log.v("BatterySettingsFeatureProvider", "enabled");
        if (componentName == null) {
            return null;
        }
        Cursor query = this.mContext.getContentResolver().query(DatabaseUtils.SI_BATTERY_SETTINGS_URI, (String[]) null, componentName.flattenToString(), (String[]) null, (String) null);
        if (query != null) {
            try {
                if (query.getCount() != 0) {
                    query.moveToFirst();
                    int columnIndex = query.getColumnIndex("package");
                    int columnIndex2 = query.getColumnIndex("className");
                    if (columnIndex == -1 || columnIndex2 == -1) {
                        query.close();
                        return componentName;
                    }
                    ComponentName componentName2 = new ComponentName(query.getString(columnIndex), query.getString(columnIndex2));
                    query.close();
                    return componentName2;
                }
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        }
        if (query != null) {
            query.close();
        }
        return componentName;
        throw th;
    }
}
