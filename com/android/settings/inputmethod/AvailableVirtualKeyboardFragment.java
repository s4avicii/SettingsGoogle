package com.android.settings.inputmethod;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.SearchIndexableResource;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.Utils;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.inputmethod.InputMethodAndSubtypeUtilCompat;
import com.android.settingslib.inputmethod.InputMethodPreference;
import com.android.settingslib.inputmethod.InputMethodSettingValuesWrapper;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AvailableVirtualKeyboardFragment extends DashboardFragment implements InputMethodPreference.OnSavePreferenceListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            ArrayList arrayList = new ArrayList();
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            searchIndexableResource.xmlResId = C0444R.xml.available_virtual_keyboard;
            arrayList.add(searchIndexableResource);
            return arrayList;
        }
    };
    @VisibleForTesting
    final ArrayList<InputMethodPreference> mInputMethodPreferenceList = new ArrayList<>();
    @VisibleForTesting
    InputMethodSettingValuesWrapper mInputMethodSettingValues;
    private Context mUserAwareContext;
    private int mUserId;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AvailableVirtualKeyboardFragment";
    }

    public int getMetricsCategory() {
        return 347;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.available_virtual_keyboard;
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(C0444R.xml.available_virtual_keyboard);
        this.mInputMethodSettingValues = InputMethodSettingValuesWrapper.getInstance(this.mUserAwareContext);
    }

    public void onAttach(Context context) {
        UserHandle userHandle;
        super.onAttach(context);
        int i = getArguments().getInt("profile");
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        int myUserId = UserHandle.myUserId();
        if (i == 1) {
            UserHandle userHandle2 = userManager.getPrimaryUser().getUserHandle();
            myUserId = userHandle2.getIdentifier();
            context = context.createContextAsUser(userHandle2, 0);
        } else if (i == 2) {
            if (myUserId == 10) {
                userHandle = UserHandle.of(myUserId);
            } else {
                myUserId = Utils.getManagedProfileId(userManager, myUserId);
                userHandle = Utils.getManagedProfile(userManager);
            }
            context = context.createContextAsUser(userHandle, 0);
        }
        this.mUserId = myUserId;
        this.mUserAwareContext = context;
    }

    public void onResume() {
        super.onResume();
        this.mInputMethodSettingValues.refreshAllInputMethodAndSubtypes();
        updateInputMethodPreferenceViews();
    }

    public void onSaveInputMethodPreference(InputMethodPreference inputMethodPreference) {
        InputMethodAndSubtypeUtilCompat.saveInputMethodSubtypeListForUser(this, this.mUserAwareContext.getContentResolver(), ((InputMethodManager) getContext().getSystemService(InputMethodManager.class)).getInputMethodListAsUser(this.mUserId), getResources().getConfiguration().keyboard == 2, this.mUserId);
        this.mInputMethodSettingValues.refreshAllInputMethodAndSubtypes();
        Iterator<InputMethodPreference> it = this.mInputMethodPreferenceList.iterator();
        while (it.hasNext()) {
            it.next().updatePreferenceViews();
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void updateInputMethodPreferenceViews() {
        int i;
        this.mInputMethodSettingValues.refreshAllInputMethodAndSubtypes();
        this.mInputMethodPreferenceList.clear();
        List permittedInputMethods = ((DevicePolicyManager) this.mUserAwareContext.getSystemService(DevicePolicyManager.class)).getPermittedInputMethods();
        Context prefContext = getPrefContext();
        List<InputMethodInfo> inputMethodList = this.mInputMethodSettingValues.getInputMethodList();
        List enabledInputMethodListAsUser = ((InputMethodManager) getContext().getSystemService(InputMethodManager.class)).getEnabledInputMethodListAsUser(this.mUserId);
        if (inputMethodList == null) {
            i = 0;
        } else {
            i = inputMethodList.size();
        }
        for (int i2 = 0; i2 < i; i2++) {
            InputMethodInfo inputMethodInfo = inputMethodList.get(i2);
            InputMethodPreference inputMethodPreference = new InputMethodPreference(prefContext, inputMethodInfo, permittedInputMethods == null || permittedInputMethods.contains(inputMethodInfo.getPackageName()) || enabledInputMethodListAsUser.contains(inputMethodInfo), this, this.mUserId);
            inputMethodPreference.setIcon(inputMethodInfo.loadIcon(this.mUserAwareContext.getPackageManager()));
            this.mInputMethodPreferenceList.add(inputMethodPreference);
        }
        this.mInputMethodPreferenceList.sort(new AvailableVirtualKeyboardFragment$$ExternalSyntheticLambda0(Collator.getInstance()));
        getPreferenceScreen().removeAll();
        for (int i3 = 0; i3 < i; i3++) {
            InputMethodPreference inputMethodPreference2 = this.mInputMethodPreferenceList.get(i3);
            inputMethodPreference2.setOrder(i3);
            getPreferenceScreen().addPreference(inputMethodPreference2);
            InputMethodAndSubtypeUtilCompat.removeUnnecessaryNonPersistentPreference(inputMethodPreference2);
            inputMethodPreference2.updatePreferenceViews();
        }
    }
}
