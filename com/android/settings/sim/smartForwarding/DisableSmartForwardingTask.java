package com.android.settings.sim.smartForwarding;

import android.telephony.CallForwardingInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class DisableSmartForwardingTask implements Runnable {
    private final CallForwardingInfo[] callForwardingInfo;
    private final boolean[] callWaitingStatus;

    /* renamed from: tm */
    private final TelephonyManager f174tm;

    public DisableSmartForwardingTask(TelephonyManager telephonyManager, boolean[] zArr, CallForwardingInfo[] callForwardingInfoArr) {
        this.f174tm = telephonyManager;
        this.callWaitingStatus = zArr;
        this.callForwardingInfo = callForwardingInfoArr;
    }

    public void run() {
        for (int i = 0; i < this.f174tm.getActiveModemCount(); i++) {
            int subId = getSubId(i);
            if (!(this.callWaitingStatus == null || subId == -1)) {
                Log.d("SmartForwarding", "Restore call waiting to " + this.callWaitingStatus[i]);
                this.f174tm.createForSubscriptionId(subId).setCallWaitingEnabled(this.callWaitingStatus[i], (Executor) null, (Consumer) null);
            }
            CallForwardingInfo[] callForwardingInfoArr = this.callForwardingInfo;
            if (!(callForwardingInfoArr == null || callForwardingInfoArr[i] == null || subId == -1)) {
                Log.d("SmartForwarding", "Restore call forwarding to " + this.callForwardingInfo[i]);
                this.f174tm.createForSubscriptionId(subId).setCallForwarding(this.callForwardingInfo[i], (Executor) null, (Consumer) null);
            }
        }
    }

    private int getSubId(int i) {
        int[] subId = SubscriptionManager.getSubId(i);
        if (subId == null || subId.length <= 0) {
            return -1;
        }
        return subId[0];
    }
}
