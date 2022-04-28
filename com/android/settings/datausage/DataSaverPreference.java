package com.android.settings.datausage;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.datausage.DataSaverBackend;

public class DataSaverPreference extends Preference implements DataSaverBackend.Listener {
    private final DataSaverBackend mDataSaverBackend;

    public void onAllowlistStatusChanged(int i, boolean z) {
    }

    public void onDenylistStatusChanged(int i, boolean z) {
    }

    public DataSaverPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDataSaverBackend = new DataSaverBackend(context);
    }

    public void onAttached() {
        super.onAttached();
        this.mDataSaverBackend.addListener(this);
    }

    public void onDetached() {
        super.onDetached();
        this.mDataSaverBackend.remListener(this);
    }

    public void onDataSaverChanged(boolean z) {
        setSummary(z ? C0444R.string.data_saver_on : C0444R.string.data_saver_off);
    }
}
