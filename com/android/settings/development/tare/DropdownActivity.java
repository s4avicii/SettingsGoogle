package com.android.settings.development.tare;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import androidx.window.C0444R;
import com.android.settingslib.widget.SettingsSpinnerAdapter;

public class DropdownActivity extends Activity {
    private Fragment mAlarmManagerFragment;
    private Fragment mJobSchedulerFragment;
    private Spinner mSpinner;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0444R.C0450layout.tare_dropdown_page);
        int intExtra = getIntent().getIntExtra("policy", 0);
        this.mSpinner = (Spinner) findViewById(C0444R.C0448id.spinner);
        this.mAlarmManagerFragment = new AlarmManagerFragment();
        this.mJobSchedulerFragment = new JobSchedulerFragment();
        String[] stringArray = getResources().getStringArray(C0444R.array.tare_policies);
        SettingsSpinnerAdapter settingsSpinnerAdapter = new SettingsSpinnerAdapter(this);
        settingsSpinnerAdapter.addAll(stringArray);
        this.mSpinner.setAdapter(settingsSpinnerAdapter);
        this.mSpinner.setSelection(intExtra);
        selectFragment(intExtra);
        this.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                DropdownActivity.this.selectFragment(i);
            }
        });
    }

    /* access modifiers changed from: private */
    public void selectFragment(int i) {
        if (i == 0) {
            openFragment(this.mAlarmManagerFragment);
        } else if (i != 1) {
            openFragment(this.mAlarmManagerFragment);
        } else {
            openFragment(this.mJobSchedulerFragment);
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        beginTransaction.replace(C0444R.C0448id.frame_layout, fragment);
        beginTransaction.commit();
    }
}
