package com.android.settings.applications;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceCategory;
import androidx.window.C0444R;
import com.android.settings.Utils;
import com.android.settingslib.widget.FooterPreference;
import com.android.settingslib.widget.SelectorWithWidgetPreference;
import java.util.Iterator;

public class OpenSupportedLinks extends AppInfoWithHeader implements SelectorWithWidgetPreference.OnClickListener {
    SelectorWithWidgetPreference mAllowOpening;
    SelectorWithWidgetPreference mAskEveryTime;
    private int mCurrentIndex;
    SelectorWithWidgetPreference mNotAllowed;
    PackageManager mPackageManager;
    PreferenceCategory mPreferenceCategory;
    private String[] mRadioKeys = {"app_link_open_always", "app_link_open_ask", "app_link_open_never"};

    private int indexToLinkState(int i) {
        if (i != 0) {
            return i != 2 ? 1 : 3;
        }
        return 2;
    }

    private int linkStateToIndex(int i) {
        if (i != 2) {
            return i != 3 ? 1 : 2;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public AlertDialog createDialog(int i, int i2) {
        return null;
    }

    public int getMetricsCategory() {
        return 1824;
    }

    /* access modifiers changed from: protected */
    public boolean refreshUi() {
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPackageManager = getPackageManager();
        addPreferencesFromResource(C0444R.xml.open_supported_links);
        initRadioPreferencesGroup();
        updateFooterPreference();
    }

    /* access modifiers changed from: package-private */
    public void initRadioPreferencesGroup() {
        this.mPreferenceCategory = (PreferenceCategory) findPreference("supported_links_radio_group");
        this.mAllowOpening = makeRadioPreference("app_link_open_always", C0444R.string.app_link_open_always);
        int entriesNo = getEntriesNo();
        this.mAllowOpening.setAppendixVisibility(8);
        this.mAllowOpening.setSummary((CharSequence) getResources().getQuantityString(C0444R.plurals.app_link_open_always_summary, entriesNo, new Object[]{Integer.valueOf(entriesNo)}));
        this.mAskEveryTime = makeRadioPreference("app_link_open_ask", C0444R.string.app_link_open_ask);
        this.mNotAllowed = makeRadioPreference("app_link_open_never", C0444R.string.app_link_open_never);
        int linkStateToIndex = linkStateToIndex(this.mPackageManager.getIntentVerificationStatusAsUser(this.mPackageName, this.mUserId));
        this.mCurrentIndex = linkStateToIndex;
        setRadioStatus(linkStateToIndex);
    }

    public void onRadioButtonClicked(SelectorWithWidgetPreference selectorWithWidgetPreference) {
        int preferenceKeyToIndex = preferenceKeyToIndex(selectorWithWidgetPreference.getKey());
        if (this.mCurrentIndex != preferenceKeyToIndex) {
            this.mCurrentIndex = preferenceKeyToIndex;
            setRadioStatus(preferenceKeyToIndex);
            updateAppLinkState(indexToLinkState(this.mCurrentIndex));
        }
    }

    private SelectorWithWidgetPreference makeRadioPreference(String str, int i) {
        SelectorWithWidgetPreference selectorWithWidgetPreference = new SelectorWithWidgetPreference(this.mPreferenceCategory.getContext());
        selectorWithWidgetPreference.setKey(str);
        selectorWithWidgetPreference.setTitle(i);
        selectorWithWidgetPreference.setOnClickListener(this);
        this.mPreferenceCategory.addPreference(selectorWithWidgetPreference);
        return selectorWithWidgetPreference;
    }

    /* access modifiers changed from: package-private */
    public int getEntriesNo() {
        return Utils.getHandledDomains(this.mPackageManager, this.mPackageName).size();
    }

    private void setRadioStatus(int i) {
        boolean z = false;
        this.mAllowOpening.setChecked(i == 0);
        this.mAskEveryTime.setChecked(i == 1);
        SelectorWithWidgetPreference selectorWithWidgetPreference = this.mNotAllowed;
        if (i == 2) {
            z = true;
        }
        selectorWithWidgetPreference.setChecked(z);
    }

    private int preferenceKeyToIndex(String str) {
        int i = 0;
        while (true) {
            String[] strArr = this.mRadioKeys;
            if (i >= strArr.length) {
                return 1;
            }
            if (TextUtils.equals(str, strArr[i])) {
                return i;
            }
            i++;
        }
    }

    private void updateAppLinkState(int i) {
        if (this.mPackageManager.getIntentVerificationStatusAsUser(this.mPackageName, this.mUserId) != i) {
            if (this.mPackageManager.updateIntentVerificationStatusAsUser(this.mPackageName, i, this.mUserId)) {
                this.mPackageManager.getIntentVerificationStatusAsUser(this.mPackageName, this.mUserId);
            } else {
                Log.e("OpenSupportedLinks", "Couldn't update intent verification status!");
            }
        }
    }

    private void updateFooterPreference() {
        FooterPreference footerPreference = (FooterPreference) findPreference("supported_links_footer");
        if (footerPreference == null) {
            Log.w("OpenSupportedLinks", "Can't find the footer preference.");
        } else {
            addLinksToFooter(footerPreference);
        }
    }

    /* access modifiers changed from: package-private */
    public void addLinksToFooter(FooterPreference footerPreference) {
        ArraySet<String> handledDomains = Utils.getHandledDomains(this.mPackageManager, this.mPackageName);
        if (handledDomains.isEmpty()) {
            Log.w("OpenSupportedLinks", "Can't find any app links.");
            return;
        }
        String str = footerPreference.getTitle() + System.lineSeparator();
        Iterator<String> it = handledDomains.iterator();
        while (it.hasNext()) {
            str = str + System.lineSeparator() + it.next();
        }
        footerPreference.setTitle((CharSequence) str);
    }
}
