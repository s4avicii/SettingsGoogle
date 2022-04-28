package com.android.settings;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class ActionDisabledByAppOpsDialog extends Activity implements DialogInterface.OnDismissListener {
    private ActionDisabledByAppOpsHelper mDialogHelper;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActionDisabledByAppOpsHelper actionDisabledByAppOpsHelper = new ActionDisabledByAppOpsHelper(this);
        this.mDialogHelper = actionDisabledByAppOpsHelper;
        actionDisabledByAppOpsHelper.prepareDialogBuilder().setOnDismissListener(this).show();
        updateAppOps();
    }

    private void updateAppOps() {
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("android.intent.extra.PACKAGE_NAME");
        ((AppOpsManager) getSystemService(AppOpsManager.class)).setMode(119, intent.getIntExtra("android.intent.extra.UID", -1), stringExtra, 1);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mDialogHelper.updateDialog();
        updateAppOps();
    }

    public void onDismiss(DialogInterface dialogInterface) {
        finish();
    }
}
