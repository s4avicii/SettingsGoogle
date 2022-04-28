package com.android.settings.dashboard.profileselector;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.window.C0444R;
import com.android.settings.inputmethod.AvailableVirtualKeyboardFragment;

public final class ProfileSelectKeyboardFragment extends ProfileSelectFragment {
    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.available_virtual_keyboard;
    }

    public Fragment[] getFragments() {
        Bundle bundle = new Bundle();
        bundle.putInt("profile", 1);
        AvailableVirtualKeyboardFragment availableVirtualKeyboardFragment = new AvailableVirtualKeyboardFragment();
        availableVirtualKeyboardFragment.setArguments(bundle);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("profile", 2);
        AvailableVirtualKeyboardFragment availableVirtualKeyboardFragment2 = new AvailableVirtualKeyboardFragment();
        availableVirtualKeyboardFragment2.setArguments(bundle2);
        return new Fragment[]{availableVirtualKeyboardFragment, availableVirtualKeyboardFragment2};
    }
}
