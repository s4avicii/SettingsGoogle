package com.android.settings.dashboard.profileselector;

import androidx.fragment.app.Fragment;
import androidx.window.C0444R;
import com.android.settings.accounts.AccountPersonalDashboardFragment;
import com.android.settings.accounts.AccountWorkProfileDashboardFragment;

public class ProfileSelectAccountFragment extends ProfileSelectFragment {
    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.accounts_dashboard_settings_header;
    }

    public Fragment[] getFragments() {
        return new Fragment[]{new AccountPersonalDashboardFragment(), new AccountWorkProfileDashboardFragment()};
    }
}
