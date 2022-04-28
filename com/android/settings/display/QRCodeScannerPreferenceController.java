package com.android.settings.display;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.core.TogglePreferenceController;

public class QRCodeScannerPreferenceController extends TogglePreferenceController {
    private static final String SETTING_KEY = "lock_screen_show_qr_code_scanner";
    private final ContentResolver mContentResolver;
    /* access modifiers changed from: private */
    public Preference mPreference;
    private final ContentObserver mSettingsObserver = new ContentObserver((Handler) null) {
        public void onChange(boolean z, Uri uri) {
            QRCodeScannerPreferenceController qRCodeScannerPreferenceController = QRCodeScannerPreferenceController.this;
            qRCodeScannerPreferenceController.updateState(qRCodeScannerPreferenceController.mPreference);
        }
    };

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
        return C0444R.string.menu_key_display;
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

    public QRCodeScannerPreferenceController(Context context, String str) {
        super(context, str);
        this.mContentResolver = context.getContentResolver();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("show_qr_code_scanner_setting"), false, this.mSettingsObserver);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mContentResolver.unregisterContentObserver(this.mSettingsObserver);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), SETTING_KEY, 0) != 0;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), SETTING_KEY, z ? 1 : 0);
    }

    public int getAvailabilityStatus() {
        return isScannerActivityAvailable() ? 0 : 3;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        refreshSummary(preference);
    }

    private boolean isScannerActivityAvailable() {
        return Settings.Secure.getString(this.mContext.getContentResolver(), "show_qr_code_scanner_setting") != null;
    }
}
