package com.android.settings.accessibility;

import android.content.Context;
import java.lang.Number;
import java.util.List;

abstract class PreviewSizeData<T extends Number> {
    private final Context mContext;
    private T mDefaultValue;
    private int mInitialIndex;
    private List<T> mValues;

    PreviewSizeData(Context context) {
        this.mContext = context;
    }

    /* access modifiers changed from: package-private */
    public Context getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: package-private */
    public List<T> getValues() {
        return this.mValues;
    }

    /* access modifiers changed from: package-private */
    public void setValues(List<T> list) {
        this.mValues = list;
    }

    /* access modifiers changed from: package-private */
    public T getDefaultValue() {
        return this.mDefaultValue;
    }

    /* access modifiers changed from: package-private */
    public void setDefaultValue(T t) {
        this.mDefaultValue = t;
    }

    /* access modifiers changed from: package-private */
    public int getInitialIndex() {
        return this.mInitialIndex;
    }

    /* access modifiers changed from: package-private */
    public void setInitialIndex(int i) {
        this.mInitialIndex = i;
    }
}
