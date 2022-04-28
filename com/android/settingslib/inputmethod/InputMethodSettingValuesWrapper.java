package com.android.settingslib.inputmethod;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class InputMethodSettingValuesWrapper {
    private static final String TAG = "InputMethodSettingValuesWrapper";
    @GuardedBy({"sInstanceMapLock"})
    private static SparseArray<InputMethodSettingValuesWrapper> sInstanceMap = new SparseArray<>();
    private static final Object sInstanceMapLock = new Object();
    private final ContentResolver mContentResolver;
    private final InputMethodManager mImm;
    private final ArrayList<InputMethodInfo> mMethodList = new ArrayList<>();

    public static InputMethodSettingValuesWrapper getInstance(Context context) {
        int userId = context.getUserId();
        synchronized (sInstanceMapLock) {
            if (sInstanceMap.size() == 0) {
                InputMethodSettingValuesWrapper inputMethodSettingValuesWrapper = new InputMethodSettingValuesWrapper(context);
                sInstanceMap.put(userId, inputMethodSettingValuesWrapper);
                return inputMethodSettingValuesWrapper;
            } else if (sInstanceMap.indexOfKey(userId) >= 0) {
                InputMethodSettingValuesWrapper inputMethodSettingValuesWrapper2 = sInstanceMap.get(userId);
                return inputMethodSettingValuesWrapper2;
            } else {
                InputMethodSettingValuesWrapper inputMethodSettingValuesWrapper3 = new InputMethodSettingValuesWrapper(context);
                sInstanceMap.put(context.getUserId(), inputMethodSettingValuesWrapper3);
                return inputMethodSettingValuesWrapper3;
            }
        }
    }

    private InputMethodSettingValuesWrapper(Context context) {
        this.mContentResolver = context.getContentResolver();
        this.mImm = (InputMethodManager) context.getSystemService(InputMethodManager.class);
        refreshAllInputMethodAndSubtypes();
    }

    public void refreshAllInputMethodAndSubtypes() {
        this.mMethodList.clear();
        this.mMethodList.addAll(this.mImm.getInputMethodListAsUser(this.mContentResolver.getUserId(), 1));
    }

    public List<InputMethodInfo> getInputMethodList() {
        return new ArrayList(this.mMethodList);
    }

    public boolean isAlwaysCheckedIme(InputMethodInfo inputMethodInfo) {
        boolean isEnabledImi = isEnabledImi(inputMethodInfo);
        if (getEnabledInputMethodList().size() <= 1 && isEnabledImi) {
            return true;
        }
        int enabledValidNonAuxAsciiCapableImeCount = getEnabledValidNonAuxAsciiCapableImeCount();
        if (enabledValidNonAuxAsciiCapableImeCount > 1 || ((enabledValidNonAuxAsciiCapableImeCount == 1 && !isEnabledImi) || !inputMethodInfo.isSystem() || !InputMethodAndSubtypeUtil.isValidNonAuxAsciiCapableIme(inputMethodInfo))) {
            return false;
        }
        return true;
    }

    private int getEnabledValidNonAuxAsciiCapableImeCount() {
        int i = 0;
        for (InputMethodInfo isValidNonAuxAsciiCapableIme : getEnabledInputMethodList()) {
            if (InputMethodAndSubtypeUtil.isValidNonAuxAsciiCapableIme(isValidNonAuxAsciiCapableIme)) {
                i++;
            }
        }
        if (i == 0) {
            Log.w(TAG, "No \"enabledValidNonAuxAsciiCapableIme\"s found.");
        }
        return i;
    }

    public boolean isEnabledImi(InputMethodInfo inputMethodInfo) {
        for (InputMethodInfo id : getEnabledInputMethodList()) {
            if (id.getId().equals(inputMethodInfo.getId())) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<InputMethodInfo> getEnabledInputMethodList() {
        HashMap<String, HashSet<String>> enabledInputMethodsAndSubtypeList = InputMethodAndSubtypeUtil.getEnabledInputMethodsAndSubtypeList(this.mContentResolver);
        ArrayList<InputMethodInfo> arrayList = new ArrayList<>();
        Iterator<InputMethodInfo> it = this.mMethodList.iterator();
        while (it.hasNext()) {
            InputMethodInfo next = it.next();
            if (enabledInputMethodsAndSubtypeList.keySet().contains(next.getId())) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }
}
