package com.android.settings.deviceinfo;

import android.util.SparseArray;
import com.android.settings.deviceinfo.storage.UserIconLoader;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.function.Consumer;

/* renamed from: com.android.settings.deviceinfo.StorageCategoryFragment$IconLoaderCallbacks$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0849xfb8aaf7 implements Consumer {
    public final /* synthetic */ SparseArray f$0;

    public /* synthetic */ C0849xfb8aaf7(SparseArray sparseArray) {
        this.f$0 = sparseArray;
    }

    public final void accept(Object obj) {
        ((UserIconLoader.UserIconHandler) ((AbstractPreferenceController) obj)).handleUserIcons(this.f$0);
    }
}
