package com.android.settings.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.EthernetManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.internal.annotations.VisibleForTesting;

public final class EthernetTetherPreferenceController extends TetherBasePreferenceController {
    @VisibleForTesting
    EthernetManager.Listener mEthernetListener;
    private final EthernetManager mEthernetManager;
    private final String mEthernetRegex;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getTetherType() {
        return 5;
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

    public EthernetTetherPreferenceController(Context context, String str) {
        super(context, str);
        this.mEthernetRegex = context.getString(17039958);
        this.mEthernetManager = (EthernetManager) context.getSystemService("ethernet");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0(String str, boolean z) {
        updateState(this.mPreference);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mEthernetListener = new EthernetTetherPreferenceController$$ExternalSyntheticLambda0(this);
        this.mEthernetManager.addListener(this.mEthernetListener, new EthernetTetherPreferenceController$$ExternalSyntheticLambda1(new Handler(Looper.getMainLooper())));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mEthernetManager.removeListener(this.mEthernetListener);
        this.mEthernetListener = null;
    }

    public boolean shouldEnable() {
        for (String matches : this.mTm.getTetherableIfaces()) {
            if (matches.matches(this.mEthernetRegex)) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldShow() {
        return !TextUtils.isEmpty(this.mEthernetRegex);
    }
}
