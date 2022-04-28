package com.android.settings.dashboard.profileselector;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.window.C0444R;
import com.android.settings.location.RecentLocationRequestSeeAllFragment;

public class ProfileSelectRecentLocationRequestFragment extends ProfileSelectFragment {
    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.location_recent_requests_header;
    }

    public Fragment[] getFragments() {
        Bundle bundle = new Bundle();
        bundle.putInt("profile", 2);
        RecentLocationRequestSeeAllFragment recentLocationRequestSeeAllFragment = new RecentLocationRequestSeeAllFragment();
        recentLocationRequestSeeAllFragment.setArguments(bundle);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("profile", 1);
        RecentLocationRequestSeeAllFragment recentLocationRequestSeeAllFragment2 = new RecentLocationRequestSeeAllFragment();
        recentLocationRequestSeeAllFragment2.setArguments(bundle2);
        return new Fragment[]{recentLocationRequestSeeAllFragment2, recentLocationRequestSeeAllFragment};
    }
}
