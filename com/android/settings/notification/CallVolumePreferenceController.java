package com.android.settings.notification;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;
import androidx.window.C0444R;

public class CallVolumePreferenceController extends VolumeSeekBarPreferenceController {
    private AudioManager mAudioManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public int getMuteIcon() {
        return C0444R.C0447drawable.ic_local_phone_24_lib;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public boolean useDynamicSliceSummary() {
        return true;
    }

    public CallVolumePreferenceController(Context context, String str) {
        super(context, str);
        this.mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
    }

    public int getAvailabilityStatus() {
        return (!this.mContext.getResources().getBoolean(C0444R.bool.config_show_call_volume) || this.mHelper.isSingleVolume()) ? 3 : 0;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "call_volume");
    }

    public int getAudioStream() {
        return this.mAudioManager.isBluetoothScoOn() ? 6 : 0;
    }
}
