package com.android.settings.development.bluetooth;

import android.content.Context;
import android.util.AttributeSet;
import androidx.window.C0444R;

public class BluetoothSampleRateDialogPreference extends BaseBluetoothDialogPreference {
    /* access modifiers changed from: protected */
    public int getRadioButtonGroupId() {
        return C0444R.C0448id.bluetooth_audio_sample_rate_radio_group;
    }

    public BluetoothSampleRateDialogPreference(Context context) {
        super(context);
        initialize(context);
    }

    public BluetoothSampleRateDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context);
    }

    public BluetoothSampleRateDialogPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context);
    }

    public BluetoothSampleRateDialogPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initialize(context);
    }

    private void initialize(Context context) {
        this.mRadioButtonIds.add(Integer.valueOf(C0444R.C0448id.bluetooth_audio_sample_rate_default));
        this.mRadioButtonIds.add(Integer.valueOf(C0444R.C0448id.bluetooth_audio_sample_rate_441));
        this.mRadioButtonIds.add(Integer.valueOf(C0444R.C0448id.bluetooth_audio_sample_rate_480));
        this.mRadioButtonIds.add(Integer.valueOf(C0444R.C0448id.bluetooth_audio_sample_rate_882));
        this.mRadioButtonIds.add(Integer.valueOf(C0444R.C0448id.bluetooth_audio_sample_rate_960));
        String[] stringArray = context.getResources().getStringArray(C0444R.array.bluetooth_a2dp_codec_sample_rate_titles);
        for (String add : stringArray) {
            this.mRadioButtonStrings.add(add);
        }
        String[] stringArray2 = context.getResources().getStringArray(C0444R.array.bluetooth_a2dp_codec_sample_rate_summaries);
        for (String add2 : stringArray2) {
            this.mSummaryStrings.add(add2);
        }
    }
}
