package com.android.settings.notification;

import android.content.Context;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.notification.VolumeSeekBarPreference;

public abstract class VolumeSeekBarPreferenceController extends AdjustVolumeRestrictedPreferenceController implements LifecycleObserver {
    protected AudioHelper mHelper;
    protected VolumeSeekBarPreference mPreference;
    protected VolumeSeekBarPreference.Callback mVolumePreferenceCallback;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public abstract int getAudioStream();

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    /* access modifiers changed from: protected */
    public abstract int getMuteIcon();

    public int getSliceHighlightMenuRes() {
        return C0444R.string.menu_key_sound;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public VolumeSeekBarPreferenceController(Context context, String str) {
        super(context, str);
        setAudioHelper(new AudioHelper(context));
    }

    /* access modifiers changed from: package-private */
    public void setAudioHelper(AudioHelper audioHelper) {
        this.mHelper = audioHelper;
    }

    public void setCallback(VolumeSeekBarPreference.Callback callback) {
        this.mVolumePreferenceCallback = callback;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            VolumeSeekBarPreference volumeSeekBarPreference = (VolumeSeekBarPreference) preferenceScreen.findPreference(getPreferenceKey());
            this.mPreference = volumeSeekBarPreference;
            volumeSeekBarPreference.setCallback(this.mVolumePreferenceCallback);
            this.mPreference.setStream(getAudioStream());
            this.mPreference.setMuteIcon(getMuteIcon());
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        VolumeSeekBarPreference volumeSeekBarPreference = this.mPreference;
        if (volumeSeekBarPreference != null) {
            volumeSeekBarPreference.onActivityResume();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        VolumeSeekBarPreference volumeSeekBarPreference = this.mPreference;
        if (volumeSeekBarPreference != null) {
            volumeSeekBarPreference.onActivityPause();
        }
    }

    public int getSliderPosition() {
        VolumeSeekBarPreference volumeSeekBarPreference = this.mPreference;
        if (volumeSeekBarPreference != null) {
            return volumeSeekBarPreference.getProgress();
        }
        return this.mHelper.getStreamVolume(getAudioStream());
    }

    public boolean setSliderPosition(int i) {
        VolumeSeekBarPreference volumeSeekBarPreference = this.mPreference;
        if (volumeSeekBarPreference != null) {
            volumeSeekBarPreference.setProgress(i);
        }
        return this.mHelper.setStreamVolume(getAudioStream(), i);
    }

    public int getMax() {
        VolumeSeekBarPreference volumeSeekBarPreference = this.mPreference;
        if (volumeSeekBarPreference != null) {
            return volumeSeekBarPreference.getMax();
        }
        return this.mHelper.getMaxVolume(getAudioStream());
    }

    public int getMin() {
        VolumeSeekBarPreference volumeSeekBarPreference = this.mPreference;
        if (volumeSeekBarPreference != null) {
            return volumeSeekBarPreference.getMin();
        }
        return this.mHelper.getMinVolume(getAudioStream());
    }
}
