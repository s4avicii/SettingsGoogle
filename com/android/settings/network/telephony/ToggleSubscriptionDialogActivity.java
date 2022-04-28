package com.android.settings.network.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccSlotInfo;
import android.text.TextUtils;
import android.util.Log;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.SidecarFragment;
import com.android.settings.network.EnableMultiSimSidecar;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SwitchToEuiccSubscriptionSidecar;
import com.android.settings.network.SwitchToRemovableSlotSidecar;
import com.android.settings.network.SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0;
import com.android.settings.network.UiccSlotUtil;
import com.android.settings.network.telephony.ConfirmDialogFragment;
import com.android.settings.sim.SimActivationNotifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ToggleSubscriptionDialogActivity extends SubscriptionActionDialogActivity implements SidecarFragment.Listener, ConfirmDialogFragment.OnConfirmListener {
    @VisibleForTesting
    public static final String ARG_enable = "enable";
    private boolean isRtlMode;
    private List<SubscriptionInfo> mActiveSubInfos;
    private boolean mEnable;
    private EnableMultiSimSidecar mEnableMultiSimSidecar;
    private boolean mIsEsimOperation;
    private SubscriptionInfo mSubInfo;
    private SwitchToEuiccSubscriptionSidecar mSwitchToEuiccSubscriptionSidecar;
    private SwitchToRemovableSlotSidecar mSwitchToRemovableSlotSidecar;
    private TelephonyManager mTelMgr;

    public static Intent getIntent(Context context, int i, boolean z) {
        Intent intent = new Intent(context, ToggleSubscriptionDialogActivity.class);
        intent.putExtra("sub_id", i);
        intent.putExtra(ARG_enable, z);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        int intExtra = intent.getIntExtra("sub_id", -1);
        this.mTelMgr = (TelephonyManager) getSystemService(TelephonyManager.class);
        if (!((UserManager) getSystemService(UserManager.class)).isAdminUser()) {
            Log.e("ToggleSubscriptionDialogActivity", "It is not the admin user. Unable to toggle subscription.");
            finish();
        } else if (!SubscriptionManager.isUsableSubscriptionId(intExtra)) {
            Log.e("ToggleSubscriptionDialogActivity", "The subscription id is not usable.");
            finish();
        } else {
            this.mActiveSubInfos = SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager);
            SubscriptionInfo subById = SubscriptionUtil.getSubById(this.mSubscriptionManager, intExtra);
            this.mSubInfo = subById;
            boolean z = false;
            this.mIsEsimOperation = subById != null && subById.isEmbedded();
            this.mSwitchToEuiccSubscriptionSidecar = SwitchToEuiccSubscriptionSidecar.get(getFragmentManager());
            this.mSwitchToRemovableSlotSidecar = SwitchToRemovableSlotSidecar.get(getFragmentManager());
            this.mEnableMultiSimSidecar = EnableMultiSimSidecar.get(getFragmentManager());
            this.mEnable = intent.getBooleanExtra(ARG_enable, true);
            if (getResources().getConfiguration().getLayoutDirection() == 1) {
                z = true;
            }
            this.isRtlMode = z;
            Log.i("ToggleSubscriptionDialogActivity", "isMultipleEnabledProfilesSupported():" + isMultipleEnabledProfilesSupported());
            if (bundle != null) {
                return;
            }
            if (this.mEnable) {
                showEnableSubDialog();
            } else {
                showDisableSimConfirmDialog();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mSwitchToEuiccSubscriptionSidecar.addListener(this);
        this.mSwitchToRemovableSlotSidecar.addListener(this);
        this.mEnableMultiSimSidecar.addListener(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.mEnableMultiSimSidecar.removeListener(this);
        this.mSwitchToRemovableSlotSidecar.removeListener(this);
        this.mSwitchToEuiccSubscriptionSidecar.removeListener(this);
        super.onPause();
    }

    public void onStateChange(SidecarFragment sidecarFragment) {
        if (sidecarFragment == this.mSwitchToEuiccSubscriptionSidecar) {
            handleSwitchToEuiccSubscriptionSidecarStateChange();
        } else if (sidecarFragment == this.mSwitchToRemovableSlotSidecar) {
            handleSwitchToRemovableSlotSidecarStateChange();
        } else if (sidecarFragment == this.mEnableMultiSimSidecar) {
            handleEnableMultiSimSidecarStateChange();
        }
    }

    public void onConfirm(int i, boolean z, int i2) {
        List<SubscriptionInfo> list;
        if (z || i == 3 || i == 4) {
            int i3 = 0;
            SubscriptionInfo subscriptionInfo = null;
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i != 4) {
                            if (i != 5) {
                                Log.e("ToggleSubscriptionDialogActivity", "Unrecognized confirmation dialog tag: " + i);
                                return;
                            } else if (!(i2 == -1 || (list = this.mActiveSubInfos) == null)) {
                                subscriptionInfo = list.get(i2);
                            }
                        } else if (!z) {
                            Log.i("ToggleSubscriptionDialogActivity", "User cancel the dialog to reboot to enable DSDS.");
                            showEnableSimConfirmDialog();
                            return;
                        } else {
                            Log.i("ToggleSubscriptionDialogActivity", "User confirmed reboot to enable DSDS.");
                            SimActivationNotifier.setShowSimSettingsNotification(this, true);
                            this.mTelMgr.switchMultiSimConfig(2);
                            return;
                        }
                    } else if (!z) {
                        Log.i("ToggleSubscriptionDialogActivity", "User cancel the dialog to enable DSDS.");
                        showEnableSimConfirmDialog();
                        return;
                    } else if (this.mTelMgr.doesSwitchMultiSimConfigTriggerReboot()) {
                        Log.i("ToggleSubscriptionDialogActivity", "Device does not support reboot free DSDS.");
                        showRebootConfirmDialog();
                        return;
                    } else {
                        Log.i("ToggleSubscriptionDialogActivity", "Enabling DSDS without rebooting.");
                        showProgressDialog(getString(C0444R.string.sim_action_enabling_sim_without_carrier_name));
                        this.mEnableMultiSimSidecar.run(2);
                        return;
                    }
                }
                Log.i("ToggleSubscriptionDialogActivity", "User confirmed to enable the subscription.");
                if (this.mIsEsimOperation) {
                    showProgressDialog(getString(C0444R.string.sim_action_switch_sub_dialog_progress, new Object[]{SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this)}));
                    this.mSwitchToEuiccSubscriptionSidecar.run(this.mSubInfo.getSubscriptionId(), -1, subscriptionInfo);
                    return;
                }
                showProgressDialog(getString(C0444R.string.sim_action_enabling_sim_without_carrier_name));
                this.mSwitchToRemovableSlotSidecar.run(-1, subscriptionInfo);
            } else if (this.mIsEsimOperation) {
                Log.i("ToggleSubscriptionDialogActivity", "Disabling the eSIM profile.");
                showProgressDialog(getString(C0444R.string.privileged_action_disable_sub_dialog_progress));
                SubscriptionInfo subscriptionInfo2 = this.mSubInfo;
                if (subscriptionInfo2 != null) {
                    i3 = subscriptionInfo2.getPortIndex();
                }
                this.mSwitchToEuiccSubscriptionSidecar.run(-1, i3, (SubscriptionInfo) null);
            } else {
                Log.i("ToggleSubscriptionDialogActivity", "Disabling the pSIM profile.");
                handleTogglePsimAction();
            }
        } else {
            finish();
        }
    }

    private void handleSwitchToEuiccSubscriptionSidecarStateChange() {
        int state = this.mSwitchToEuiccSubscriptionSidecar.getState();
        String str = ARG_enable;
        if (state == 2) {
            Object[] objArr = new Object[1];
            if (!this.mEnable) {
                str = "disable";
            }
            objArr[0] = str;
            Log.i("ToggleSubscriptionDialogActivity", String.format("Successfully %s the eSIM profile.", objArr));
            this.mSwitchToEuiccSubscriptionSidecar.reset();
            dismissProgressDialog();
            finish();
        } else if (state == 3) {
            Object[] objArr2 = new Object[1];
            if (!this.mEnable) {
                str = "disable";
            }
            objArr2[0] = str;
            Log.i("ToggleSubscriptionDialogActivity", String.format("Failed to %s the eSIM profile.", objArr2));
            this.mSwitchToEuiccSubscriptionSidecar.reset();
            dismissProgressDialog();
            showErrorDialog(getString(C0444R.string.privileged_action_disable_fail_title), getString(C0444R.string.privileged_action_disable_fail_text));
        }
    }

    private void handleSwitchToRemovableSlotSidecarStateChange() {
        int state = this.mSwitchToRemovableSlotSidecar.getState();
        if (state == 2) {
            Log.i("ToggleSubscriptionDialogActivity", "Successfully switched to removable slot.");
            this.mSwitchToRemovableSlotSidecar.reset();
            handleTogglePsimAction();
            dismissProgressDialog();
            finish();
        } else if (state == 3) {
            Log.e("ToggleSubscriptionDialogActivity", "Failed switching to removable slot.");
            this.mSwitchToRemovableSlotSidecar.reset();
            dismissProgressDialog();
            showErrorDialog(getString(C0444R.string.sim_action_enable_sim_fail_title), getString(C0444R.string.sim_action_enable_sim_fail_text));
        }
    }

    private void handleEnableMultiSimSidecarStateChange() {
        int state = this.mEnableMultiSimSidecar.getState();
        if (state == 2) {
            this.mEnableMultiSimSidecar.reset();
            Log.i("ToggleSubscriptionDialogActivity", "Successfully switched to DSDS without reboot.");
            handleEnableSubscriptionAfterEnablingDsds();
        } else if (state == 3) {
            this.mEnableMultiSimSidecar.reset();
            Log.i("ToggleSubscriptionDialogActivity", "Failed to switch to DSDS without rebooting.");
            dismissProgressDialog();
            showErrorDialog(getString(C0444R.string.dsds_activation_failure_title), getString(C0444R.string.dsds_activation_failure_body_msg2));
        }
    }

    private void handleEnableSubscriptionAfterEnablingDsds() {
        if (this.mIsEsimOperation) {
            Log.i("ToggleSubscriptionDialogActivity", "DSDS enabled, start to enable profile: " + this.mSubInfo.getSubscriptionId());
            this.mSwitchToEuiccSubscriptionSidecar.run(this.mSubInfo.getSubscriptionId(), -1, (SubscriptionInfo) null);
            return;
        }
        Log.i("ToggleSubscriptionDialogActivity", "DSDS enabled, start to enable pSIM profile.");
        handleTogglePsimAction();
        dismissProgressDialog();
        finish();
    }

    private void handleTogglePsimAction() {
        SubscriptionInfo subscriptionInfo;
        if (!this.mSubscriptionManager.canDisablePhysicalSubscription() || (subscriptionInfo = this.mSubInfo) == null) {
            Log.i("ToggleSubscriptionDialogActivity", "The device does not support toggling pSIM. It is enough to just enable the removable slot.");
            return;
        }
        this.mSubscriptionManager.setUiccApplicationsEnabled(subscriptionInfo.getSubscriptionId(), this.mEnable);
        finish();
    }

    private void showEnableSubDialog() {
        Log.i("ToggleSubscriptionDialogActivity", "Handle subscription enabling.");
        if (isDsdsConditionSatisfied()) {
            showEnableDsdsConfirmDialog();
        } else if (this.mIsEsimOperation || !this.mTelMgr.isMultiSimEnabled() || !isRemovableSimEnabled()) {
            showEnableSimConfirmDialog();
        } else {
            Log.i("ToggleSubscriptionDialogActivity", "Toggle on pSIM, no dialog displayed.");
            handleTogglePsimAction();
            finish();
        }
    }

    private void showEnableDsdsConfirmDialog() {
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 3, getString(C0444R.string.sim_action_enable_dsds_title), getString(C0444R.string.sim_action_enable_dsds_text), getString(C0444R.string.sim_action_yes), getString(C0444R.string.sim_action_no_thanks));
    }

    private void showRebootConfirmDialog() {
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 4, getString(C0444R.string.sim_action_restart_title), getString(C0444R.string.sim_action_enable_dsds_text), getString(C0444R.string.sim_action_reboot), getString(C0444R.string.sim_action_cancel));
    }

    private void showDisableSimConfirmDialog() {
        String str;
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this);
        if (this.mSubInfo == null || TextUtils.isEmpty(uniqueSubscriptionDisplayName)) {
            str = getString(C0444R.string.privileged_action_disable_sub_dialog_title_without_carrier);
        } else {
            str = getString(C0444R.string.privileged_action_disable_sub_dialog_title, new Object[]{uniqueSubscriptionDisplayName});
        }
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 1, str, (String) null, getString(C0444R.string.yes), getString(C0444R.string.sim_action_cancel));
    }

    private void showEnableSimConfirmDialog() {
        SubscriptionInfo subscriptionInfo;
        List<SubscriptionInfo> list = this.mActiveSubInfos;
        if (list == null || list.isEmpty()) {
            Log.i("ToggleSubscriptionDialogActivity", "No active subscriptions available.");
            showNonSwitchSimConfirmDialog();
            return;
        }
        Log.i("ToggleSubscriptionDialogActivity", "mActiveSubInfos:" + this.mActiveSubInfos);
        boolean z = this.mIsEsimOperation && this.mActiveSubInfos.stream().anyMatch(ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda1.INSTANCE);
        boolean isMultiSimEnabled = this.mTelMgr.isMultiSimEnabled();
        if (isMultiSimEnabled && !isMultipleEnabledProfilesSupported() && !z) {
            showNonSwitchSimConfirmDialog();
        } else if (!isMultiSimEnabled || !isMultipleEnabledProfilesSupported()) {
            if (!isMultiSimEnabled || !z) {
                subscriptionInfo = this.mActiveSubInfos.get(0);
            } else {
                subscriptionInfo = (SubscriptionInfo) this.mActiveSubInfos.stream().filter(ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda2.INSTANCE).findFirst().get();
            }
            ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 2, getSwitchSubscriptionTitle(), getSwitchDialogBodyMsg(subscriptionInfo, z), getSwitchDialogPosBtnText(), getString(C0444R.string.sim_action_cancel));
        } else if (this.mActiveSubInfos.size() < 2) {
            showNonSwitchSimConfirmDialog();
        } else {
            showMepSwitchSimConfirmDialog();
        }
    }

    private void showNonSwitchSimConfirmDialog() {
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 2, getEnableSubscriptionTitle(), (String) null, getString(C0444R.string.yes), getString(C0444R.string.sim_action_cancel));
    }

    private void showMepSwitchSimConfirmDialog() {
        Log.i("ToggleSubscriptionDialogActivity", "showMepSwitchSimConfirmDialog");
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this);
        String string = getString(C0444R.string.sim_action_switch_sub_dialog_mep_title, new Object[]{uniqueSubscriptionDisplayName});
        StringBuilder sb = new StringBuilder();
        sb.append(getString(C0444R.string.sim_action_switch_sub_dialog_mep_text, new Object[]{uniqueSubscriptionDisplayName}));
        if (this.isRtlMode) {
            sb.insert(0, "‏").insert(sb.indexOf("\n") - 1, "‏").insert(sb.indexOf("\n") + 2, "‏").insert(sb.length(), "‏");
        }
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 5, string, sb.toString(), (String) null, (String) null, getSwitchDialogBodyList());
    }

    private String getSwitchDialogPosBtnText() {
        if (!this.mIsEsimOperation) {
            return getString(C0444R.string.sim_switch_button);
        }
        return getString(C0444R.string.sim_action_switch_sub_dialog_confirm, new Object[]{SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this)});
    }

    private String getEnableSubscriptionTitle() {
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this);
        if (this.mSubInfo == null || TextUtils.isEmpty(uniqueSubscriptionDisplayName)) {
            return getString(C0444R.string.sim_action_enable_sub_dialog_title_without_carrier_name);
        }
        return getString(C0444R.string.sim_action_enable_sub_dialog_title, new Object[]{uniqueSubscriptionDisplayName});
    }

    private String getSwitchSubscriptionTitle() {
        if (!this.mIsEsimOperation) {
            return getString(C0444R.string.sim_action_switch_psim_dialog_title);
        }
        return getString(C0444R.string.sim_action_switch_sub_dialog_title, new Object[]{SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this)});
    }

    private String getSwitchDialogBodyMsg(SubscriptionInfo subscriptionInfo, boolean z) {
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this);
        CharSequence uniqueSubscriptionDisplayName2 = SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, (Context) this);
        StringBuilder sb = new StringBuilder();
        if (z && this.mIsEsimOperation) {
            sb.append(getString(C0444R.string.sim_action_switch_sub_dialog_text_downloaded, new Object[]{uniqueSubscriptionDisplayName, uniqueSubscriptionDisplayName2}));
        } else if (this.mIsEsimOperation) {
            sb.append(getString(C0444R.string.sim_action_switch_sub_dialog_text, new Object[]{uniqueSubscriptionDisplayName, uniqueSubscriptionDisplayName2}));
        } else {
            sb.append(getString(C0444R.string.sim_action_switch_sub_dialog_text_single_sim, new Object[]{uniqueSubscriptionDisplayName2}));
        }
        if (this.isRtlMode) {
            sb.insert(0, "‏").insert(sb.indexOf("\n") - 1, "‏").insert(sb.indexOf("\n") + 2, "‏").insert(sb.length(), "‏");
        }
        return sb.toString();
    }

    private ArrayList<String> getSwitchDialogBodyList() {
        ArrayList<String> arrayList = new ArrayList<>((Collection) this.mActiveSubInfos.stream().map(new ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda0(this)).collect(Collectors.toList()));
        arrayList.add(getString(C0444R.string.sim_action_cancel));
        return arrayList;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getSwitchDialogBodyList$2(SubscriptionInfo subscriptionInfo) {
        return getString(C0444R.string.sim_action_switch_sub_dialog_carrier_list_item_for_turning_off, new Object[]{SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, (Context) this)});
    }

    private boolean isDsdsConditionSatisfied() {
        if (this.mTelMgr.isMultiSimEnabled()) {
            Log.i("ToggleSubscriptionDialogActivity", "DSDS is already enabled. Condition not satisfied.");
            return false;
        } else if (this.mTelMgr.isMultiSimSupported() != 0) {
            Log.i("ToggleSubscriptionDialogActivity", "Hardware does not support DSDS.");
            return false;
        } else {
            boolean isRemovableSimEnabled = isRemovableSimEnabled();
            if (!this.mIsEsimOperation || !isRemovableSimEnabled) {
                boolean anyMatch = SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager).stream().anyMatch(SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0.INSTANCE);
                if (this.mIsEsimOperation || !anyMatch) {
                    Log.i("ToggleSubscriptionDialogActivity", "DSDS condition not satisfied.");
                    return false;
                }
                Log.i("ToggleSubscriptionDialogActivity", "Removable SIM operation and eSIM profile is enabled. DSDS condition satisfied.");
                return true;
            }
            Log.i("ToggleSubscriptionDialogActivity", "eSIM operation and removable SIM is enabled. DSDS condition satisfied.");
            return true;
        }
    }

    private boolean isRemovableSimEnabled() {
        boolean anyMatch = UiccSlotUtil.getSlotInfos(this.mTelMgr).stream().anyMatch(ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda5.INSTANCE);
        Log.i("ToggleSubscriptionDialogActivity", "isRemovableSimEnabled: " + anyMatch);
        return anyMatch;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$isRemovableSimEnabled$4(UiccSlotInfo uiccSlotInfo) {
        return uiccSlotInfo != null && uiccSlotInfo.isRemovable() && uiccSlotInfo.getPorts().stream().anyMatch(ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda4.INSTANCE) && uiccSlotInfo.getCardStateInfo() == 2;
    }

    private boolean isMultipleEnabledProfilesSupported() {
        List<UiccCardInfo> uiccCardsInfo = this.mTelMgr.getUiccCardsInfo();
        if (uiccCardsInfo != null) {
            return uiccCardsInfo.stream().anyMatch(ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda3.INSTANCE);
        }
        Log.w("ToggleSubscriptionDialogActivity", "UICC cards info list is empty.");
        return false;
    }
}
