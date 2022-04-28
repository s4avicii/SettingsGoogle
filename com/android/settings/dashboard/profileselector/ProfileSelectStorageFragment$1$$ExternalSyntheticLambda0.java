package com.android.settings.dashboard.profileselector;

import android.os.storage.VolumeRecord;
import com.android.settings.dashboard.profileselector.ProfileSelectStorageFragment;
import com.android.settings.deviceinfo.storage.StorageEntry;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ProfileSelectStorageFragment$1$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ VolumeRecord f$0;

    public /* synthetic */ ProfileSelectStorageFragment$1$$ExternalSyntheticLambda0(VolumeRecord volumeRecord) {
        this.f$0 = volumeRecord;
    }

    public final boolean test(Object obj) {
        return ProfileSelectStorageFragment.C07931.lambda$onVolumeRecordChanged$1(this.f$0, (StorageEntry) obj);
    }
}
