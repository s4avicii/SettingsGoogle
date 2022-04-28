package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.window.C0444R;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.security.SecurityFeatureProvider;
import com.android.settings.security.trustagent.TrustAgentManager;

public class AwareLockPreferenceController extends AwareTogglePreferenceController {
    private final LockPatternUtils mLockPatternUtils;
    private final TrustAgentManager mTrustAgentManager;

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

    public AwareLockPreferenceController(Context context, String str) {
        super(context, str);
        SecurityFeatureProvider securityFeatureProvider = FeatureFactory.getFactory(context).getSecurityFeatureProvider();
        this.mLockPatternUtils = securityFeatureProvider.getLockPatternUtils(context);
        this.mTrustAgentManager = securityFeatureProvider.getTrustAgentManager();
    }

    public int getAvailabilityStatus() {
        return (!this.mLockPatternUtils.isSecure(UserHandle.myUserId()) || !this.mHelper.isGestureConfigurable()) ? 5 : 0;
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "aware_lock_enabled", 1) == 1;
    }

    public boolean setChecked(boolean z) {
        this.mHelper.writeFeatureEnabled("aware_lock_enabled", z);
        Settings.Secure.putInt(this.mContext.getContentResolver(), "aware_lock_enabled", z ? 1 : 0);
        return true;
    }

    public CharSequence getSummary() {
        CharSequence activeTrustAgentLabel = this.mTrustAgentManager.getActiveTrustAgentLabel(this.mContext, this.mLockPatternUtils);
        if (TextUtils.isEmpty(activeTrustAgentLabel)) {
            return this.mContext.getString(C0444R.string.summary_placeholder);
        }
        return this.mContext.getString(C0444R.string.lockpattern_settings_power_button_instantly_locks_summary, new Object[]{activeTrustAgentLabel});
    }
}
