package com.android.settings.users;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.AttributeSet;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.RestrictedPreference;
import java.util.Comparator;

public class UserPreference extends RestrictedPreference {
    public static final Comparator<UserPreference> SERIAL_NUMBER_COMPARATOR = UserPreference$$ExternalSyntheticLambda0.INSTANCE;
    private int mSerialNumber;
    private int mUserId;

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        return true;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ int lambda$static$0(UserPreference userPreference, UserPreference userPreference2) {
        if (userPreference == null) {
            return -1;
        }
        if (userPreference2 == null) {
            return 1;
        }
        int serialNumber = userPreference.getSerialNumber();
        int serialNumber2 = userPreference2.getSerialNumber();
        if (serialNumber < serialNumber2) {
            return -1;
        }
        if (serialNumber > serialNumber2) {
            return 1;
        }
        return 0;
    }

    public UserPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -10);
    }

    UserPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mSerialNumber = -1;
        this.mUserId = i;
        useAdminDisabledSummary(true);
    }

    private void dimIcon(boolean z) {
        Drawable icon = getIcon();
        if (icon != null) {
            icon.mutate().setAlpha(z ? 102 : 255);
            setIcon(icon);
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        dimIcon(isDisabledByAdmin());
    }

    private int getSerialNumber() {
        if (this.mUserId == UserHandle.myUserId()) {
            return Integer.MIN_VALUE;
        }
        if (this.mSerialNumber < 0) {
            if (this.mUserId == -10) {
                return Integer.MAX_VALUE;
            }
            int userSerialNumber = ((UserManager) getContext().getSystemService("user")).getUserSerialNumber(this.mUserId);
            this.mSerialNumber = userSerialNumber;
            if (userSerialNumber < 0) {
                return this.mUserId;
            }
        }
        return this.mSerialNumber;
    }

    public int getUserId() {
        return this.mUserId;
    }
}
