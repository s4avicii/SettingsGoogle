package com.android.settings.deviceinfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.storage.DiskInfo;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.window.C0444R;
import com.android.settings.core.InstrumentedFragment;

public class PrivateVolumeFormat extends InstrumentedFragment {
    private final View.OnClickListener mConfirmListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(PrivateVolumeFormat.this.getActivity(), StorageWizardFormatProgress.class);
            intent.putExtra("android.os.storage.extra.DISK_ID", PrivateVolumeFormat.this.mDisk.getId());
            intent.putExtra("format_private", false);
            intent.putExtra("format_forget_uuid", PrivateVolumeFormat.this.mVolume.getFsUuid());
            PrivateVolumeFormat.this.startActivity(intent);
            PrivateVolumeFormat.this.getActivity().finish();
        }
    };
    /* access modifiers changed from: private */
    public DiskInfo mDisk;
    /* access modifiers changed from: private */
    public VolumeInfo mVolume;

    public int getMetricsCategory() {
        return 42;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        StorageManager storageManager = (StorageManager) getActivity().getSystemService(StorageManager.class);
        VolumeInfo findVolumeById = storageManager.findVolumeById(getArguments().getString("android.os.storage.extra.VOLUME_ID"));
        this.mVolume = findVolumeById;
        this.mDisk = storageManager.findDiskById(findVolumeById.getDiskId());
        View inflate = layoutInflater.inflate(C0444R.C0450layout.storage_internal_format, viewGroup, false);
        ((TextView) inflate.findViewById(C0444R.C0448id.body)).setText(TextUtils.expandTemplate(getText(C0444R.string.storage_internal_format_details), new CharSequence[]{this.mDisk.getDescription()}));
        ((Button) inflate.findViewById(C0444R.C0448id.confirm)).setOnClickListener(this.mConfirmListener);
        return inflate;
    }
}
