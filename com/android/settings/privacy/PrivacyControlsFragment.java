package com.android.settings.privacy;

import android.content.Context;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class PrivacyControlsFragment extends DashboardFragment {
    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "PrivacyDashboardFrag";
    }

    public int getMetricsCategory() {
        return 1587;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.privacy_controls_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new CameraToggleController(context, "privacy_camera_toggle"));
        arrayList.add(new MicToggleController(context, "privacy_mic_toggle"));
        arrayList.add(new LocationToggleController(context, "privacy_location_toggle", getSettingsLifecycle()));
        arrayList.add(new ShowClipAccessNotificationPreferenceController(context));
        return arrayList;
    }
}
