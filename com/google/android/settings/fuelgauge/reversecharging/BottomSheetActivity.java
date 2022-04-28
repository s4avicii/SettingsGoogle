package com.google.android.settings.fuelgauge.reversecharging;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.window.C0444R;

public class BottomSheetActivity extends FragmentActivity {
    static final String REVERSE_CHARGING_SETTINGS = "android.settings.REVERSE_CHARGING_SETTINGS";
    ReverseChargingManager mReverseChargingManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mReverseChargingManager == null) {
            this.mReverseChargingManager = ReverseChargingManager.getInstance(this);
        }
        if (!this.mReverseChargingManager.isSupportedReverseCharging()) {
            finish();
            return;
        }
        setContentView((int) C0444R.C0450layout.reverse_charging_bottom_sheet);
        getSupportFragmentManager().beginTransaction().replace(C0444R.C0448id.content_frame, new VideoPreferenceFragment()).commit();
        ((Button) findViewById(C0444R.C0448id.ok_btn)).setOnClickListener(new BottomSheetActivity$$ExternalSyntheticLambda1(this));
        ((Button) findViewById(C0444R.C0448id.learn_more_btn)).setOnClickListener(new BottomSheetActivity$$ExternalSyntheticLambda0(this));
        setTitle(getString(C0444R.string.reverse_charging_title));
        ((TextView) findViewById(C0444R.C0448id.toolbar_title)).setText(getString(C0444R.string.reverse_charging_title));
        ((TextView) findViewById(C0444R.C0448id.header_subtitle)).setText(getString(C0444R.string.reverse_charging_instructions_title));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        onOkClick();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        onLearnMoreClick();
    }

    private void onLearnMoreClick() {
        Intent intent = new Intent(REVERSE_CHARGING_SETTINGS);
        intent.setPackage("com.android.settings");
        startActivity(intent);
        finish();
    }

    private void onOkClick() {
        finish();
    }
}
