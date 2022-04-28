package com.android.settings.nfc;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import androidx.window.C0444R;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.nfc.PaymentBackend;

public final class PaymentDefaultDialog extends AlertActivity implements DialogInterface.OnClickListener {
    private PaymentBackend mBackend;
    private PaymentBackend.PaymentInfo mNewDefault;

    /* JADX WARNING: type inference failed for: r3v0, types: [android.content.Context, com.android.internal.app.AlertActivity, com.android.settings.nfc.PaymentDefaultDialog] */
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        PaymentDefaultDialog.super.onCreate(bundle);
        getWindow().addPrivateFlags(524288);
        try {
            this.mBackend = new PaymentBackend(this);
        } catch (NullPointerException unused) {
            finish();
        }
        Intent intent = getIntent();
        ComponentName componentName = (ComponentName) intent.getParcelableExtra("component");
        String stringExtra = intent.getStringExtra(DashboardFragment.CATEGORY);
        UserHandle userHandle = (UserHandle) intent.getParcelableExtra("android.intent.extra.USER");
        if (userHandle == null) {
            userHandle = UserHandle.CURRENT;
        }
        int identifier = userHandle.getIdentifier();
        setResult(0);
        if (!buildDialog(componentName, stringExtra, identifier)) {
            finish();
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            PaymentBackend paymentBackend = this.mBackend;
            PaymentBackend.PaymentInfo paymentInfo = this.mNewDefault;
            paymentBackend.setDefaultPaymentApp(paymentInfo.componentName, paymentInfo.userId);
            setResult(-1);
        }
    }

    private boolean buildDialog(ComponentName componentName, String str, int i) {
        if (componentName == null || str == null) {
            Log.e("PaymentDefaultDialog", "Component or category are null");
            return false;
        } else if (!"payment".equals(str)) {
            Log.e("PaymentDefaultDialog", "Don't support defaults for category " + str);
            return false;
        } else {
            PaymentBackend.PaymentAppInfo paymentAppInfo = null;
            PaymentBackend.PaymentAppInfo paymentAppInfo2 = null;
            for (PaymentBackend.PaymentAppInfo next : this.mBackend.getPaymentAppInfos()) {
                if (componentName.equals(next.componentName) && next.userHandle.getIdentifier() == i) {
                    paymentAppInfo = next;
                }
                if (next.isDefault && next.userHandle.getIdentifier() == i) {
                    paymentAppInfo2 = next;
                }
            }
            if (paymentAppInfo == null) {
                Log.e("PaymentDefaultDialog", "Component " + componentName + " is not a registered payment service.");
                return false;
            }
            PaymentBackend.PaymentInfo defaultPaymentApp = this.mBackend.getDefaultPaymentApp();
            if (defaultPaymentApp == null || !defaultPaymentApp.componentName.equals(componentName) || defaultPaymentApp.userId != i) {
                PaymentBackend.PaymentInfo paymentInfo = new PaymentBackend.PaymentInfo();
                this.mNewDefault = paymentInfo;
                paymentInfo.componentName = componentName;
                paymentInfo.userId = i;
                AlertController.AlertParams alertParams = this.mAlertParams;
                if (paymentAppInfo2 == null) {
                    alertParams.mTitle = getString(C0444R.string.nfc_payment_set_default_label);
                    alertParams.mMessage = String.format(getString(C0444R.string.nfc_payment_set_default), new Object[]{sanitizePaymentAppCaption(paymentAppInfo.label.toString())});
                    alertParams.mPositiveButtonText = getString(C0444R.string.nfc_payment_btn_text_set_deault);
                } else {
                    alertParams.mTitle = getString(C0444R.string.nfc_payment_update_default_label);
                    alertParams.mMessage = String.format(getString(C0444R.string.nfc_payment_set_default_instead_of), new Object[]{sanitizePaymentAppCaption(paymentAppInfo.label.toString()), sanitizePaymentAppCaption(paymentAppInfo2.label.toString())});
                    alertParams.mPositiveButtonText = getString(C0444R.string.nfc_payment_btn_text_update);
                }
                alertParams.mNegativeButtonText = getString(C0444R.string.cancel);
                alertParams.mPositiveButtonListener = this;
                alertParams.mNegativeButtonListener = this;
                setupAlert();
                return true;
            }
            Log.e("PaymentDefaultDialog", "Component " + componentName + " is already default.");
            return false;
        }
    }

    private String sanitizePaymentAppCaption(String str) {
        String trim = str.replace(10, ' ').replace(13, ' ').trim();
        return trim.length() > 40 ? trim.substring(0, 40) : trim;
    }
}
