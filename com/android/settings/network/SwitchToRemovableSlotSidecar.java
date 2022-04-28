package com.android.settings.network;

import android.app.FragmentManager;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.settings.SidecarFragment;
import com.android.settings.network.telephony.EuiccOperationSidecar;

public class SwitchToRemovableSlotSidecar extends EuiccOperationSidecar {
    private int mPhysicalSlotId;
    private SubscriptionInfo mRemovedSubInfo;
    private SwitchToEuiccSubscriptionSidecar mSwitchToSubscriptionSidecar;

    /* access modifiers changed from: protected */
    public String getReceiverAction() {
        return "disable_subscription_and_switch_slot_sidecar";
    }

    public static SwitchToRemovableSlotSidecar get(FragmentManager fragmentManager) {
        return (SwitchToRemovableSlotSidecar) SidecarFragment.get(fragmentManager, "SwitchRemovableSidecar", SwitchToRemovableSlotSidecar.class, (Bundle) null);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSwitchToSubscriptionSidecar = SwitchToEuiccSubscriptionSidecar.get(getChildFragmentManager());
    }

    public void onResume() {
        super.onResume();
        this.mSwitchToSubscriptionSidecar.addListener(this);
    }

    public void onPause() {
        this.mSwitchToSubscriptionSidecar.removeListener(this);
        super.onPause();
    }

    public void onStateChange(SidecarFragment sidecarFragment) {
        if (sidecarFragment == this.mSwitchToSubscriptionSidecar) {
            onSwitchToSubscriptionSidecarStateChange();
        } else if (sidecarFragment == this.mSwitchSlotSidecar) {
            onSwitchSlotSidecarStateChange();
        } else {
            Log.wtf("SwitchRemovableSidecar", "Received state change from a sidecar not expected.");
        }
    }

    public void run(int i, SubscriptionInfo subscriptionInfo) {
        SubscriptionInfo subscriptionInfo2;
        this.mPhysicalSlotId = i;
        this.mRemovedSubInfo = subscriptionInfo;
        SubscriptionManager subscriptionManager = (SubscriptionManager) getContext().getSystemService(SubscriptionManager.class);
        if (!this.mTelephonyManager.isMultiSimEnabled() && SubscriptionUtil.getActiveSubscriptions(subscriptionManager).stream().anyMatch(SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0.INSTANCE)) {
            Log.i("SwitchRemovableSidecar", "There is an active eSIM profile. Disable the profile first.");
            this.mSwitchToSubscriptionSidecar.run(-1, 0, (SubscriptionInfo) null);
        } else if (!this.mTelephonyManager.isMultiSimEnabled() || (subscriptionInfo2 = this.mRemovedSubInfo) == null) {
            Log.i("SwitchRemovableSidecar", "Start to switch to removable slot.");
            this.mSwitchSlotSidecar.runSwitchToRemovableSlot(this.mPhysicalSlotId, this.mRemovedSubInfo);
        } else {
            this.mSwitchToSubscriptionSidecar.run(-1, subscriptionInfo2.getPortIndex(), (SubscriptionInfo) null);
        }
    }

    private void onSwitchToSubscriptionSidecarStateChange() {
        int state = this.mSwitchToSubscriptionSidecar.getState();
        if (state == 2) {
            this.mSwitchToSubscriptionSidecar.reset();
            Log.i("SwitchRemovableSidecar", "Successfully disabled eSIM profile. Start to switch to Removable slot.");
            this.mSwitchSlotSidecar.runSwitchToRemovableSlot(this.mPhysicalSlotId, this.mRemovedSubInfo);
        } else if (state == 3) {
            this.mSwitchToSubscriptionSidecar.reset();
            Log.i("SwitchRemovableSidecar", "Failed to disable the active eSIM profile.");
            setState(3, 0);
        }
    }

    private void onSwitchSlotSidecarStateChange() {
        int state = this.mSwitchSlotSidecar.getState();
        if (state == 2) {
            this.mSwitchSlotSidecar.reset();
            Log.i("SwitchRemovableSidecar", "Successfully switched to removable slot.");
            setState(2, 0);
        } else if (state == 3) {
            this.mSwitchSlotSidecar.reset();
            Log.i("SwitchRemovableSidecar", "Failed to switch to removable slot.");
            setState(3, 0);
        }
    }
}
