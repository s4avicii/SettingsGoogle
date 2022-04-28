package com.android.settings.deviceinfo;

import android.app.admin.DevicePolicyManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.window.C0444R;

public class StorageWizardMoveProgress extends StorageWizardBase {
    private final PackageManager.MoveCallback mCallback = new PackageManager.MoveCallback() {
        public void onStatusChanged(int i, int i2, long j) {
            if (StorageWizardMoveProgress.this.mMoveId == i) {
                if (PackageManager.isMoveStatusFinished(i2)) {
                    Log.d("StorageWizardMoveProgress", "Finished with status " + i2);
                    if (i2 != -100) {
                        StorageWizardMoveProgress storageWizardMoveProgress = StorageWizardMoveProgress.this;
                        Toast.makeText(storageWizardMoveProgress, storageWizardMoveProgress.moveStatusToMessage(i2), 1).show();
                    }
                    StorageWizardMoveProgress.this.finishAffinity();
                    return;
                }
                StorageWizardMoveProgress.this.setCurrentProgress(i2);
            }
        }
    };
    /* access modifiers changed from: private */
    public int mMoveId;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mVolume == null) {
            finish();
            return;
        }
        setContentView(C0444R.C0450layout.storage_wizard_progress);
        this.mMoveId = getIntent().getIntExtra("android.content.pm.extra.MOVE_ID", -1);
        String stringExtra = getIntent().getStringExtra("android.intent.extra.TITLE");
        String bestVolumeDescription = this.mStorage.getBestVolumeDescription(this.mVolume);
        setIcon(C0444R.C0447drawable.ic_swap_horiz);
        setHeaderText(C0444R.string.storage_wizard_move_progress_title, stringExtra);
        setBodyText(C0444R.string.storage_wizard_move_progress_body, bestVolumeDescription, stringExtra);
        setBackButtonVisibility(4);
        setNextButtonVisibility(4);
        getPackageManager().registerMoveCallback(this.mCallback, new Handler());
        this.mCallback.onStatusChanged(this.mMoveId, getPackageManager().getMoveStatus(this.mMoveId), -1);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        getPackageManager().unregisterMoveCallback(this.mCallback);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$moveStatusToMessage$0() throws Exception {
        return getString(C0444R.string.move_error_device_admin);
    }

    /* access modifiers changed from: private */
    public CharSequence moveStatusToMessage(int i) {
        if (i == -8) {
            return ((DevicePolicyManager) getSystemService(DevicePolicyManager.class)).getString("Settings.ERROR_MOVE_DEVICE_ADMIN", new StorageWizardMoveProgress$$ExternalSyntheticLambda0(this));
        }
        if (i == -5) {
            return getString(C0444R.string.invalid_location);
        }
        if (i == -3) {
            return getString(C0444R.string.system_package);
        }
        if (i == -2) {
            return getString(C0444R.string.does_not_exist);
        }
        if (i != -1) {
            return getString(C0444R.string.insufficient_storage);
        }
        return getString(C0444R.string.insufficient_storage);
    }
}
