package com.android.settings.display.darkmode;

import android.app.UiModeManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.AttributeSet;
import androidx.window.C0444R;
import com.android.settingslib.PrimarySwitchPreference;
import java.time.LocalTime;

public class DarkModePreference extends PrimarySwitchPreference {
    private Runnable mCallback;
    private DarkModeObserver mDarkModeObserver;
    private TimeFormatter mFormat;
    private PowerManager mPowerManager;
    private UiModeManager mUiModeManager;

    public DarkModePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDarkModeObserver = new DarkModeObserver(context);
        this.mUiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        this.mFormat = new TimeFormatter(context);
        DarkModePreference$$ExternalSyntheticLambda0 darkModePreference$$ExternalSyntheticLambda0 = new DarkModePreference$$ExternalSyntheticLambda0(this);
        this.mCallback = darkModePreference$$ExternalSyntheticLambda0;
        this.mDarkModeObserver.subscribe(darkModePreference$$ExternalSyntheticLambda0);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        boolean isPowerSaveMode = this.mPowerManager.isPowerSaveMode();
        boolean z = (getContext().getResources().getConfiguration().uiMode & 32) != 0;
        setSwitchEnabled(!isPowerSaveMode);
        updateSummary(isPowerSaveMode, z);
    }

    public void onAttached() {
        super.onAttached();
        this.mDarkModeObserver.subscribe(this.mCallback);
    }

    public void onDetached() {
        super.onDetached();
        this.mDarkModeObserver.unsubscribe();
    }

    private void updateSummary(boolean z, boolean z2) {
        String str;
        LocalTime localTime;
        if (z) {
            setSummary((CharSequence) getContext().getString(z2 ? C0444R.string.dark_ui_mode_disabled_summary_dark_theme_on : C0444R.string.dark_ui_mode_disabled_summary_dark_theme_off));
            return;
        }
        int nightMode = this.mUiModeManager.getNightMode();
        if (nightMode == 0) {
            str = getContext().getString(z2 ? C0444R.string.dark_ui_summary_on_auto_mode_auto : C0444R.string.dark_ui_summary_off_auto_mode_auto);
        } else if (nightMode != 3) {
            str = getContext().getString(z2 ? C0444R.string.dark_ui_summary_on_auto_mode_never : C0444R.string.dark_ui_summary_off_auto_mode_never);
        } else if (this.mUiModeManager.getNightModeCustomType() == 1) {
            str = getContext().getString(z2 ? C0444R.string.dark_ui_summary_on_auto_mode_custom_bedtime : C0444R.string.dark_ui_summary_off_auto_mode_custom_bedtime);
        } else {
            if (z2) {
                localTime = this.mUiModeManager.getCustomNightModeEnd();
            } else {
                localTime = this.mUiModeManager.getCustomNightModeStart();
            }
            str = getContext().getString(z2 ? C0444R.string.dark_ui_summary_on_auto_mode_custom : C0444R.string.dark_ui_summary_off_auto_mode_custom, new Object[]{this.mFormat.mo12554of(localTime)});
        }
        setSummary((CharSequence) str);
    }
}
