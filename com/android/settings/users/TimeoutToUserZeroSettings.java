package com.android.settings.users;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.window.C0444R;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.widget.CandidateInfo;
import java.util.ArrayList;
import java.util.List;

public class TimeoutToUserZeroSettings extends RadioButtonPickerFragment {
    private String[] mEntries;
    private String[] mValues;

    public int getMetricsCategory() {
        return 1916;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.user_timeout_to_user_zero_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mEntries = getContext().getResources().getStringArray(C0444R.array.switch_to_user_zero_when_docked_timeout_entries);
        this.mValues = getContext().getResources().getStringArray(C0444R.array.switch_to_user_zero_when_docked_timeout_values);
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        ArrayList arrayList = new ArrayList();
        if (this.mEntries != null && this.mValues != null) {
            int i = 0;
            while (true) {
                String[] strArr = this.mValues;
                if (i >= strArr.length) {
                    break;
                }
                arrayList.add(new TimeoutCandidateInfo(this.mEntries[i], strArr[i], true));
                i++;
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        String stringForUser = Settings.Secure.getStringForUser(getContext().getContentResolver(), "timeout_to_user_zero", UserHandle.myUserId());
        return stringForUser != null ? stringForUser : this.mValues[0];
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        Settings.Secure.putStringForUser(getContext().getContentResolver(), "timeout_to_user_zero", str, UserHandle.myUserId());
        return true;
    }

    private static class TimeoutCandidateInfo extends CandidateInfo {
        private final String mKey;
        private final CharSequence mLabel;

        public Drawable loadIcon() {
            return null;
        }

        TimeoutCandidateInfo(CharSequence charSequence, String str, boolean z) {
            super(z);
            this.mLabel = charSequence;
            this.mKey = str;
        }

        public CharSequence loadLabel() {
            return this.mLabel;
        }

        public String getKey() {
            return this.mKey;
        }
    }
}
