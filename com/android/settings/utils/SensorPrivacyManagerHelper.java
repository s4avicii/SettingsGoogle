package com.android.settings.utils;

import android.content.Context;
import android.hardware.SensorPrivacyManager;
import android.util.ArraySet;
import android.util.SparseArray;
import java.util.Iterator;
import java.util.concurrent.Executor;

public class SensorPrivacyManagerHelper {
    private static SensorPrivacyManagerHelper sInstance;
    private final SparseArray<SparseArray<Boolean>> mCachedState = new SparseArray<>();
    private final ArraySet<CallbackInfo> mCallbacks = new ArraySet<>();
    private final SparseArray<Boolean> mCurrentUserCachedState = new SparseArray<>();
    private final SparseArray<SensorPrivacyManager.OnSensorPrivacyChangedListener> mCurrentUserServiceListeners = new SparseArray<>();
    private final Object mLock = new Object();
    private final SensorPrivacyManager mSensorPrivacyManager;
    private final SparseArray<SparseArray<SensorPrivacyManager.OnSensorPrivacyChangedListener>> mServiceListeners = new SparseArray<>();

    public interface Callback {
        void onSensorPrivacyChanged(int i, boolean z);
    }

    private static class CallbackInfo {
        Callback mCallback;
        Executor mExecutor;
        int mSensor;
        int mUserId;

        CallbackInfo(Callback callback, Executor executor, int i, int i2) {
            this.mCallback = callback;
            this.mExecutor = executor;
            this.mSensor = i;
            this.mUserId = i2;
        }
    }

    public static SensorPrivacyManagerHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SensorPrivacyManagerHelper(context);
        }
        return sInstance;
    }

    private SensorPrivacyManagerHelper(Context context) {
        this.mSensorPrivacyManager = (SensorPrivacyManager) context.getSystemService(SensorPrivacyManager.class);
    }

    public boolean supportsSensorToggle(int i) {
        return this.mSensorPrivacyManager.supportsSensorToggle(i);
    }

    public boolean isSensorBlocked(int i) {
        boolean booleanValue;
        synchronized (this.mLock) {
            Boolean bool = this.mCurrentUserCachedState.get(i);
            if (bool == null) {
                registerCurrentUserListenerIfNeeded(i);
                bool = Boolean.valueOf(this.mSensorPrivacyManager.isSensorPrivacyEnabled(i));
                this.mCurrentUserCachedState.put(i, bool);
            }
            booleanValue = bool.booleanValue();
        }
        return booleanValue;
    }

    public boolean isSensorBlocked(int i, int i2) {
        boolean booleanValue;
        synchronized (this.mLock) {
            SparseArray<Boolean> createUserCachedStateIfNeededLocked = createUserCachedStateIfNeededLocked(i2);
            Boolean bool = createUserCachedStateIfNeededLocked.get(i);
            if (bool == null) {
                registerListenerIfNeeded(i, i2);
                bool = Boolean.valueOf(this.mSensorPrivacyManager.isSensorPrivacyEnabled(i));
                createUserCachedStateIfNeededLocked.put(i, bool);
            }
            booleanValue = bool.booleanValue();
        }
        return booleanValue;
    }

    public void setSensorBlockedForProfileGroup(int i, int i2, boolean z) {
        this.mSensorPrivacyManager.setSensorPrivacyForProfileGroup(i, i2, z);
    }

    public void addSensorBlockedListener(int i, Callback callback, Executor executor) {
        synchronized (this.mLock) {
            this.mCallbacks.add(new CallbackInfo(callback, executor, i, -1));
        }
    }

    private void registerCurrentUserListenerIfNeeded(int i) {
        synchronized (this.mLock) {
            if (!this.mCurrentUserServiceListeners.contains(i)) {
                SensorPrivacyManagerHelper$$ExternalSyntheticLambda0 sensorPrivacyManagerHelper$$ExternalSyntheticLambda0 = new SensorPrivacyManagerHelper$$ExternalSyntheticLambda0(this, i);
                this.mCurrentUserServiceListeners.put(i, sensorPrivacyManagerHelper$$ExternalSyntheticLambda0);
                this.mSensorPrivacyManager.addSensorPrivacyListener(i, sensorPrivacyManagerHelper$$ExternalSyntheticLambda0);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerCurrentUserListenerIfNeeded$1(int i, int i2, boolean z) {
        this.mCurrentUserCachedState.put(i, Boolean.valueOf(z));
        dispatchStateChangedLocked(i, z, -1);
    }

    private void registerListenerIfNeeded(int i, int i2) {
        synchronized (this.mLock) {
            if (!createUserServiceListenersIfNeededLocked(i2).contains(i)) {
                SensorPrivacyManagerHelper$$ExternalSyntheticLambda1 sensorPrivacyManagerHelper$$ExternalSyntheticLambda1 = new SensorPrivacyManagerHelper$$ExternalSyntheticLambda1(this, i2, i);
                this.mCurrentUserServiceListeners.put(i, sensorPrivacyManagerHelper$$ExternalSyntheticLambda1);
                this.mSensorPrivacyManager.addSensorPrivacyListener(i, sensorPrivacyManagerHelper$$ExternalSyntheticLambda1);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerListenerIfNeeded$2(int i, int i2, int i3, boolean z) {
        createUserCachedStateIfNeededLocked(i).put(i2, Boolean.valueOf(z));
        dispatchStateChangedLocked(i2, z, i);
    }

    private void dispatchStateChangedLocked(int i, boolean z, int i2) {
        Iterator<CallbackInfo> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            CallbackInfo next = it.next();
            if (next.mUserId == i2 && next.mSensor == i) {
                next.mExecutor.execute(new SensorPrivacyManagerHelper$$ExternalSyntheticLambda2(next.mCallback, i, z));
            }
        }
    }

    private SparseArray<Boolean> createUserCachedStateIfNeededLocked(int i) {
        SparseArray<Boolean> sparseArray = this.mCachedState.get(i);
        if (sparseArray != null) {
            return sparseArray;
        }
        SparseArray<Boolean> sparseArray2 = new SparseArray<>();
        this.mCachedState.put(i, sparseArray2);
        return sparseArray2;
    }

    private SparseArray<SensorPrivacyManager.OnSensorPrivacyChangedListener> createUserServiceListenersIfNeededLocked(int i) {
        SparseArray<SensorPrivacyManager.OnSensorPrivacyChangedListener> sparseArray = this.mServiceListeners.get(i);
        if (sparseArray != null) {
            return sparseArray;
        }
        SparseArray<SensorPrivacyManager.OnSensorPrivacyChangedListener> sparseArray2 = new SparseArray<>();
        this.mServiceListeners.put(i, sparseArray2);
        return sparseArray2;
    }
}
