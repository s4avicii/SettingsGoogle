package com.android.settings.display.darkmode;

import android.app.UiModeManager;
import android.content.Context;
import android.content.IntentFilter;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.widget.FooterPreference;

public class DarkModeCustomBedtimePreferenceController extends BasePreferenceController {
    private BedtimeSettings mBedtimeSettings;
    private FooterPreference mFooterPreference;
    private final UiModeManager mUiModeManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
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

    public DarkModeCustomBedtimePreferenceController(Context context, String str) {
        super(context, str);
        this.mUiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        this.mBedtimeSettings = new BedtimeSettings(context);
    }

    public int getAvailabilityStatus() {
        return this.mBedtimeSettings.getBedtimeSettingsIntent() == null ? 3 : 1;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        FooterPreference footerPreference = (FooterPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mFooterPreference = footerPreference;
        footerPreference.setLearnMoreAction(new C0902x5b122a4a(this));
        this.mFooterPreference.setLearnMoreText(this.mContext.getString(C0444R.string.dark_ui_bedtime_footer_action));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(View view) {
        view.getContext().startActivity(this.mBedtimeSettings.getBedtimeSettingsIntent());
    }

    public void updateState(Preference preference) {
        if (this.mUiModeManager.getNightModeCustomType() != 1) {
            preference.setVisible(false);
        } else {
            preference.setVisible(true);
        }
    }
}
