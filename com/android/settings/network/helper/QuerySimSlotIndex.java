package com.android.settings.network.helper;

import android.telephony.TelephonyManager;
import android.telephony.UiccPortInfo;
import android.telephony.UiccSlotInfo;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.IntStream;

public class QuerySimSlotIndex implements Callable<AtomicIntegerArray> {
    private boolean mDisabledSlotsIncluded;
    private boolean mOnlySlotWithSim;
    private TelephonyManager mTelephonyManager;

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$call$1(int i, int i2) {
        return i2 >= i;
    }

    public QuerySimSlotIndex(TelephonyManager telephonyManager, boolean z, boolean z2) {
        this.mTelephonyManager = telephonyManager;
        this.mDisabledSlotsIncluded = z;
        this.mOnlySlotWithSim = z2;
    }

    public AtomicIntegerArray call() {
        UiccSlotInfo[] uiccSlotsInfo = this.mTelephonyManager.getUiccSlotsInfo();
        int i = 0;
        if (uiccSlotsInfo == null) {
            return new AtomicIntegerArray(0);
        }
        if (!this.mOnlySlotWithSim) {
            i = -1;
        }
        return new AtomicIntegerArray(Arrays.stream(uiccSlotsInfo).flatMapToInt(new QuerySimSlotIndex$$ExternalSyntheticLambda0(this)).filter(new QuerySimSlotIndex$$ExternalSyntheticLambda1(i)).toArray());
    }

    /* access modifiers changed from: protected */
    /* renamed from: mapToLogicalSlotIndex */
    public IntStream lambda$call$0(UiccSlotInfo uiccSlotInfo) {
        if (uiccSlotInfo == null) {
            return IntStream.of(-1);
        }
        if (uiccSlotInfo.getCardStateInfo() == 1) {
            return IntStream.of(-1);
        }
        return uiccSlotInfo.getPorts().stream().filter(new QuerySimSlotIndex$$ExternalSyntheticLambda2(this)).mapToInt(QuerySimSlotIndex$$ExternalSyntheticLambda3.INSTANCE);
    }

    /* access modifiers changed from: protected */
    /* renamed from: filterPort */
    public boolean lambda$mapToLogicalSlotIndex$2(UiccPortInfo uiccPortInfo) {
        if (this.mDisabledSlotsIncluded) {
            return true;
        }
        if (uiccPortInfo == null) {
            return false;
        }
        return uiccPortInfo.isActive();
    }
}
