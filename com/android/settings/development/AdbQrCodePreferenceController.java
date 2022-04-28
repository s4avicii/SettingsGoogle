package com.android.settings.development;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.debug.IAdbManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;

public class AdbQrCodePreferenceController extends BasePreferenceController {
    private static final String TAG = "AdbQrCodePrefCtrl";
    private IAdbManager mAdbManager = IAdbManager.Stub.asInterface(ServiceManager.getService("adb"));
    private Fragment mParentFragment;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AdbQrCodePreferenceController(Context context, String str) {
        super(context, str);
    }

    public void setParentFragment(Fragment fragment) {
        this.mParentFragment = fragment;
    }

    public int getAvailabilityStatus() {
        try {
            return this.mAdbManager.isAdbWifiQrSupported() ? 0 : 3;
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to check if adb wifi QR code scanning is supported.", e);
            return 3;
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        this.mParentFragment.startActivityForResult(new Intent(this.mContext, AdbQrCodeActivity.class), 1);
        return true;
    }
}
