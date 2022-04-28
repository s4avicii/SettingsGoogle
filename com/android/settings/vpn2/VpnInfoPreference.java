package com.android.settings.vpn2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import androidx.window.C0444R;
import com.android.settingslib.HelpUtils;
import com.android.settingslib.RestrictedPreference;

public class VpnInfoPreference extends RestrictedPreference implements View.OnClickListener {
    private String mHelpUrl;
    private boolean mIsInsecureVpn = false;

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return C0444R.C0450layout.preference_widget_warning;
    }

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        return false;
    }

    public VpnInfoPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHelpUrl = context.getString(C0444R.string.help_url_insecure_vpn);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(C0444R.C0448id.warning_button);
        if (!this.mIsInsecureVpn || TextUtils.isEmpty(this.mHelpUrl)) {
            findViewById.setVisibility(8);
            findViewById.setOnClickListener(this);
            findViewById.setEnabled(false);
        } else {
            findViewById.setVisibility(0);
            findViewById.setOnClickListener(this);
            findViewById.setEnabled(true);
        }
        preferenceViewHolder.findViewById(C0444R.C0448id.two_target_divider).setVisibility(8);
    }

    public void onClick(View view) {
        Intent helpIntent;
        if (view.getId() == C0444R.C0448id.warning_button && (helpIntent = HelpUtils.getHelpIntent(getContext(), this.mHelpUrl, getClass().getName())) != null) {
            ((Activity) getContext()).startActivityForResult(helpIntent, 0);
        }
    }

    public void setInsecureVpn(boolean z) {
        if (this.mIsInsecureVpn != z) {
            this.mIsInsecureVpn = z;
            notifyChanged();
        }
    }
}
