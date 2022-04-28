package com.android.settings.inputmethod;

import android.content.Context;
import android.hardware.input.InputDeviceIdentifier;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;

public class KeyboardLayoutPickerFragment extends DashboardFragment {
    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "KeyboardLayoutPicker";
    }

    public int getMetricsCategory() {
        return 58;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.keyboard_layout_picker_fragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        InputDeviceIdentifier parcelableExtra = getActivity().getIntent().getParcelableExtra("input_device_identifier");
        if (parcelableExtra == null) {
            getActivity().finish();
        }
        ((KeyboardLayoutPickerController) use(KeyboardLayoutPickerController.class)).initialize(this, parcelableExtra);
    }
}
