package com.android.settings.deviceinfo;

import android.app.ActivityManager;
import android.os.Bundle;
import android.os.UserManager;
import android.os.storage.DiskInfo;
import android.os.storage.StorageEventListener;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.os.storage.VolumeRecord;
import android.provider.DocumentsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.internal.util.Preconditions;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.deviceinfo.storage.StorageUtils;
import com.android.settingslib.widget.UsageProgressBarPreference;
import java.io.File;
import java.util.Objects;

public class PublicVolumeSettings extends SettingsPreferenceFragment {
    private DiskInfo mDisk;
    private Preference mFormatPrivate;
    private Preference mFormatPublic;
    private boolean mIsPermittedToAdopt;
    private Preference mMount;
    private final StorageEventListener mStorageListener = new StorageEventListener() {
        public void onVolumeStateChanged(VolumeInfo volumeInfo, int i, int i2) {
            if (Objects.equals(PublicVolumeSettings.this.mVolume.getId(), volumeInfo.getId())) {
                PublicVolumeSettings.this.mVolume = volumeInfo;
                PublicVolumeSettings.this.update();
            }
        }

        public void onVolumeRecordChanged(VolumeRecord volumeRecord) {
            if (Objects.equals(PublicVolumeSettings.this.mVolume.getFsUuid(), volumeRecord.getFsUuid())) {
                PublicVolumeSettings publicVolumeSettings = PublicVolumeSettings.this;
                publicVolumeSettings.mVolume = publicVolumeSettings.mStorageManager.findVolumeById(PublicVolumeSettings.this.mVolumeId);
                PublicVolumeSettings.this.update();
            }
        }
    };
    /* access modifiers changed from: private */
    public StorageManager mStorageManager;
    private UsageProgressBarPreference mSummary;
    private Button mUnmount;
    private final View.OnClickListener mUnmountListener = new View.OnClickListener() {
        public void onClick(View view) {
            new StorageUtils.UnmountTask(PublicVolumeSettings.this.getActivity(), PublicVolumeSettings.this.mVolume).execute(new Void[0]);
        }
    };
    /* access modifiers changed from: private */
    public VolumeInfo mVolume;
    /* access modifiers changed from: private */
    public String mVolumeId;

    public int getMetricsCategory() {
        return 42;
    }

    private boolean isVolumeValid() {
        VolumeInfo volumeInfo = this.mVolume;
        return volumeInfo != null && volumeInfo.getType() == 0 && this.mVolume.isMountedReadable();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        this.mIsPermittedToAdopt = UserManager.get(activity).isAdminUser() && !ActivityManager.isUserAMonkey();
        this.mStorageManager = (StorageManager) activity.getSystemService(StorageManager.class);
        if ("android.provider.action.DOCUMENT_ROOT_SETTINGS".equals(getActivity().getIntent().getAction())) {
            this.mVolume = this.mStorageManager.findVolumeByUuid(DocumentsContract.getRootId(getActivity().getIntent().getData()));
        } else {
            String string = getArguments().getString("android.os.storage.extra.VOLUME_ID");
            if (string != null) {
                this.mVolume = this.mStorageManager.findVolumeById(string);
            }
        }
        if (!isVolumeValid()) {
            getActivity().finish();
            return;
        }
        DiskInfo findDiskById = this.mStorageManager.findDiskById(this.mVolume.getDiskId());
        this.mDisk = findDiskById;
        Preconditions.checkNotNull(findDiskById);
        this.mVolumeId = this.mVolume.getId();
        addPreferencesFromResource(C0444R.xml.device_info_storage_volume);
        getPreferenceScreen().setOrderingAsAdded(true);
        this.mSummary = new UsageProgressBarPreference(getPrefContext());
        this.mMount = buildAction(C0444R.string.storage_menu_mount);
        Button button = new Button(getActivity());
        this.mUnmount = button;
        button.setText(C0444R.string.storage_menu_unmount);
        this.mUnmount.setOnClickListener(this.mUnmountListener);
        this.mFormatPublic = buildAction(C0444R.string.storage_menu_format);
        if (this.mIsPermittedToAdopt) {
            this.mFormatPrivate = buildAction(C0444R.string.storage_menu_format_private);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (isVolumeValid()) {
            ((ViewGroup) getActivity().findViewById(C0444R.C0448id.container_material)).addView(this.mUnmount, new ViewGroup.LayoutParams(-1, -2));
        }
    }

    public void update() {
        if (!isVolumeValid()) {
            getActivity().finish();
            return;
        }
        getActivity().setTitle(this.mStorageManager.getBestVolumeDescription(this.mVolume));
        FragmentActivity activity = getActivity();
        getPreferenceScreen().removeAll();
        if (this.mVolume.isMountedReadable()) {
            addPreference(this.mSummary);
            File path = this.mVolume.getPath();
            long totalSpace = path.getTotalSpace();
            long freeSpace = totalSpace - path.getFreeSpace();
            this.mSummary.setUsageSummary(StorageUtils.getStorageSummary(activity, C0444R.string.storage_usage_summary, freeSpace));
            this.mSummary.setTotalSummary(StorageUtils.getStorageSummary(activity, C0444R.string.storage_total_summary, totalSpace));
            this.mSummary.setPercent(freeSpace, totalSpace);
        }
        if (this.mVolume.getState() == 0) {
            addPreference(this.mMount);
        }
        if (!this.mVolume.isMountedReadable()) {
            this.mUnmount.setVisibility(8);
        }
        addPreference(this.mFormatPublic);
        if (this.mDisk.isAdoptable() && this.mIsPermittedToAdopt) {
            addPreference(this.mFormatPrivate);
        }
    }

    private void addPreference(Preference preference) {
        preference.setOrder(Integer.MAX_VALUE);
        getPreferenceScreen().addPreference(preference);
    }

    private Preference buildAction(int i) {
        Preference preference = new Preference(getPrefContext());
        preference.setTitle(i);
        return preference;
    }

    public void onResume() {
        super.onResume();
        this.mVolume = this.mStorageManager.findVolumeById(this.mVolumeId);
        if (!isVolumeValid()) {
            getActivity().finish();
            return;
        }
        this.mStorageManager.registerListener(this.mStorageListener);
        update();
    }

    public void onPause() {
        super.onPause();
        this.mStorageManager.unregisterListener(this.mStorageListener);
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == this.mMount) {
            new StorageUtils.MountTask(getActivity(), this.mVolume).execute(new Void[0]);
        } else if (preference == this.mFormatPublic) {
            StorageWizardFormatConfirm.showPublic(getActivity(), this.mDisk.getId());
        } else if (preference == this.mFormatPrivate) {
            StorageWizardFormatConfirm.showPrivate(getActivity(), this.mDisk.getId());
        }
        return super.onPreferenceTreeClick(preference);
    }
}
