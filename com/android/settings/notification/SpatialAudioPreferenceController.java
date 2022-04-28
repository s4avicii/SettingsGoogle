package com.android.settings.notification;

import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.Spatializer;
import androidx.window.C0444R;
import com.android.settings.core.TogglePreferenceController;

public class SpatialAudioPreferenceController extends TogglePreferenceController {
    private static final String KEY_SPATIAL_AUDIO = "spatial_audio";
    private final Spatializer mSpatializer;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceHighlightMenuRes() {
        return C0444R.string.menu_key_notifications;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SpatialAudioPreferenceController(Context context) {
        super(context, KEY_SPATIAL_AUDIO);
        this.mSpatializer = ((AudioManager) context.getSystemService(AudioManager.class)).getSpatializer();
    }

    public int getAvailabilityStatus() {
        return this.mSpatializer.getImmersiveAudioLevel() == 0 ? 3 : 0;
    }

    public boolean isChecked() {
        return this.mSpatializer.isEnabled();
    }

    public boolean setChecked(boolean z) {
        this.mSpatializer.setEnabled(z);
        return z == isChecked();
    }
}
