package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.RestrictedLockUtils;

public class RestrictedPreferenceHelper {
    private String mAttrUserRestriction;
    private final Context mContext;
    private boolean mDisabledByAdmin;
    private boolean mDisabledByAppOps;
    private boolean mDisabledSummary;
    private RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    private final Preference mPreference;
    final String packageName;
    final int uid;

    public RestrictedPreferenceHelper(Context context, Preference preference, AttributeSet attributeSet, String str, int i) {
        CharSequence charSequence;
        String str2;
        this.mAttrUserRestriction = null;
        boolean z = false;
        this.mDisabledSummary = false;
        this.mContext = context;
        this.mPreference = preference;
        this.packageName = str;
        this.uid = i;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.RestrictedPreference);
            TypedValue peekValue = obtainStyledAttributes.peekValue(R$styleable.RestrictedPreference_userRestriction);
            if (peekValue == null || peekValue.type != 3) {
                charSequence = null;
            } else {
                int i2 = peekValue.resourceId;
                charSequence = i2 != 0 ? context.getText(i2) : peekValue.string;
            }
            if (charSequence == null) {
                str2 = null;
            } else {
                str2 = charSequence.toString();
            }
            this.mAttrUserRestriction = str2;
            if (RestrictedLockUtilsInternal.hasBaseUserRestriction(context, str2, UserHandle.myUserId())) {
                this.mAttrUserRestriction = null;
                return;
            }
            TypedValue peekValue2 = obtainStyledAttributes.peekValue(R$styleable.RestrictedPreference_useAdminDisabledSummary);
            if (peekValue2 != null) {
                if (peekValue2.type == 18 && peekValue2.data != 0) {
                    z = true;
                }
                this.mDisabledSummary = z;
            }
        }
    }

    public RestrictedPreferenceHelper(Context context, Preference preference, AttributeSet attributeSet) {
        this(context, preference, attributeSet, (String) null, -1);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        TextView textView;
        if (this.mDisabledByAdmin || this.mDisabledByAppOps) {
            preferenceViewHolder.itemView.setEnabled(true);
        }
        if (this.mDisabledSummary && (textView = (TextView) preferenceViewHolder.findViewById(16908304)) != null) {
            String string = ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)).getString("Settings.CONTROLLED_BY_ADMIN_SUMMARY", new RestrictedPreferenceHelper$$ExternalSyntheticLambda0(textView));
            if (this.mDisabledByAdmin) {
                textView.setText(string);
            } else if (this.mDisabledByAppOps) {
                textView.setText(R$string.disabled_by_app_ops_text);
            } else if (TextUtils.equals(string, textView.getText())) {
                textView.setText((CharSequence) null);
            }
        }
    }

    public void useAdminDisabledSummary(boolean z) {
        this.mDisabledSummary = z;
    }

    public boolean performClick() {
        if (this.mDisabledByAdmin) {
            RestrictedLockUtils.sendShowAdminSupportDetailsIntent(this.mContext, this.mEnforcedAdmin);
            return true;
        } else if (!this.mDisabledByAppOps) {
            return false;
        } else {
            RestrictedLockUtilsInternal.sendShowRestrictedSettingDialogIntent(this.mContext, this.packageName, this.uid);
            return true;
        }
    }

    public void onAttachedToHierarchy() {
        String str = this.mAttrUserRestriction;
        if (str != null) {
            checkRestrictionAndSetDisabled(str, UserHandle.myUserId());
        }
    }

    public void checkRestrictionAndSetDisabled(String str, int i) {
        setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, str, i));
    }

    public RestrictedLockUtils.EnforcedAdmin checkRestrictionEnforced() {
        String str = this.mAttrUserRestriction;
        if (str == null) {
            return null;
        }
        return RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, str, UserHandle.myUserId());
    }

    public boolean setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        boolean z = true;
        boolean z2 = enforcedAdmin != null;
        this.mEnforcedAdmin = enforcedAdmin;
        if (this.mDisabledByAdmin != z2) {
            this.mDisabledByAdmin = z2;
        } else {
            z = false;
        }
        updateDisabledState();
        return z;
    }

    public boolean setDisabledByAppOps(boolean z) {
        boolean z2;
        if (this.mDisabledByAppOps != z) {
            this.mDisabledByAppOps = z;
            z2 = true;
        } else {
            z2 = false;
        }
        updateDisabledState();
        return z2;
    }

    public boolean isDisabledByAdmin() {
        return this.mDisabledByAdmin;
    }

    private void updateDisabledState() {
        Preference preference = this.mPreference;
        if (!(preference instanceof RestrictedTopLevelPreference)) {
            preference.setEnabled(!this.mDisabledByAdmin && !this.mDisabledByAppOps);
        }
    }
}
