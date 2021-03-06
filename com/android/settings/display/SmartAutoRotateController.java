package com.android.settings.display;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.SensorPrivacyManager;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.view.RotationPolicy;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class SmartAutoRotateController extends TogglePreferenceController implements LifecycleObserver {
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final PowerManager mPowerManager;
    protected Preference mPreference;
    private final SensorPrivacyManager mPrivacyManager;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            SmartAutoRotateController smartAutoRotateController = SmartAutoRotateController.this;
            smartAutoRotateController.updateState(smartAutoRotateController.mPreference);
        }
    };
    private RotationPolicy.RotationPolicyListener mRotationPolicyListener;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceHighlightMenuRes() {
        return C0444R.string.menu_key_display;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SmartAutoRotateController(Context context, String str) {
        super(context, str);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        SensorPrivacyManager instance = SensorPrivacyManager.getInstance(context);
        this.mPrivacyManager = instance;
        instance.addSensorPrivacyListener(2, new SmartAutoRotateController$$ExternalSyntheticLambda0(this));
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, boolean z) {
        updateState(this.mPreference);
    }

    public void init(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    public int getAvailabilityStatus() {
        if (!isRotationResolverServiceAvailable(this.mContext)) {
            return 3;
        }
        return (isRotationLocked() || !hasSufficientPermission(this.mContext) || isCameraLocked() || isPowerSaveMode()) ? 5 : 0;
    }

    /* access modifiers changed from: protected */
    public boolean isRotationLocked() {
        return RotationPolicy.isRotationLocked(this.mContext);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null) {
            preference.setEnabled(getAvailabilityStatus() == 0);
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isCameraLocked() {
        return this.mPrivacyManager.isSensorPrivacyEnabled(2);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isPowerSaveMode() {
        return this.mPowerManager.isPowerSaveMode();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED"));
        if (this.mRotationPolicyListener == null) {
            this.mRotationPolicyListener = new RotationPolicy.RotationPolicyListener() {
                public void onChange() {
                    SmartAutoRotateController smartAutoRotateController = SmartAutoRotateController.this;
                    smartAutoRotateController.updateState(smartAutoRotateController.mPreference);
                }
            };
        }
        RotationPolicy.registerRotationPolicyListener(this.mContext, this.mRotationPolicyListener);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mContext.unregisterReceiver(this.mReceiver);
        RotationPolicy.RotationPolicyListener rotationPolicyListener = this.mRotationPolicyListener;
        if (rotationPolicyListener != null) {
            RotationPolicy.unregisterRotationPolicyListener(this.mContext, rotationPolicyListener);
            this.mRotationPolicyListener = null;
        }
    }

    public boolean isChecked() {
        if (isRotationLocked() || !hasSufficientPermission(this.mContext) || isCameraLocked() || isPowerSaveMode() || Settings.Secure.getInt(this.mContext.getContentResolver(), "camera_autorotate", 0) != 1) {
            return false;
        }
        return true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public boolean setChecked(boolean z) {
        this.mMetricsFeatureProvider.action(this.mContext, 1751, z);
        Settings.Secure.putInt(this.mContext.getContentResolver(), "camera_autorotate", z ? 1 : 0);
        return true;
    }

    public static boolean isRotationResolverServiceAvailable(Context context) {
        ResolveInfo resolveService;
        PackageManager packageManager = context.getPackageManager();
        String rotationResolverPackageName = packageManager.getRotationResolverPackageName();
        if (TextUtils.isEmpty(rotationResolverPackageName) || (resolveService = packageManager.resolveService(new Intent("android.service.rotationresolver.RotationResolverService").setPackage(rotationResolverPackageName), 1048576)) == null || resolveService.serviceInfo == null) {
            return false;
        }
        return true;
    }

    static boolean hasSufficientPermission(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String rotationResolverPackageName = packageManager.getRotationResolverPackageName();
        return rotationResolverPackageName != null && packageManager.checkPermission("android.permission.CAMERA", rotationResolverPackageName) == 0;
    }
}
