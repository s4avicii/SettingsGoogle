package com.android.settings.development.tare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.window.C0444R;

public class TareHomePage extends Activity {
    private TextView mAlarmManagerView;
    private TextView mJobSchedulerView;
    private Switch mOnSwitch;
    private Button mRevButton;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0444R.C0450layout.tare_homepage);
        this.mOnSwitch = (Switch) findViewById(C0444R.C0448id.on_switch);
        this.mRevButton = (Button) findViewById(C0444R.C0448id.revert_button);
        this.mAlarmManagerView = (TextView) findViewById(C0444R.C0448id.alarmmanager);
        this.mJobSchedulerView = (TextView) findViewById(C0444R.C0448id.jobscheduler);
        boolean z = false;
        if (Settings.Global.getInt(getContentResolver(), "enable_tare", 0) == 1) {
            z = true;
        }
        setEnabled(z);
        this.mOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                TareHomePage.this.setEnabled(z);
                Settings.Global.putInt(TareHomePage.this.getContentResolver(), "enable_tare", z ? 1 : 0);
            }
        });
    }

    public void revertSettings(View view) {
        Toast.makeText(this, C0444R.string.tare_settings_reverted_toast, 1).show();
        Settings.Global.putString(getApplicationContext().getContentResolver(), "enable_tare", (String) null);
        setEnabled(false);
    }

    public void launchAlarmManagerPage(View view) {
        Intent intent = new Intent(getApplicationContext(), DropdownActivity.class);
        intent.putExtra("policy", 0);
        startActivity(intent);
    }

    public void launchJobSchedulerPage(View view) {
        Intent intent = new Intent(getApplicationContext(), DropdownActivity.class);
        intent.putExtra("policy", 1);
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void setEnabled(boolean z) {
        this.mRevButton.setEnabled(z);
        this.mAlarmManagerView.setEnabled(z);
        this.mJobSchedulerView.setEnabled(z);
        this.mOnSwitch.setChecked(z);
    }
}
