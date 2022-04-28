package com.android.settings.applications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.window.C0444R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.applications.RecentAppOpsAccess;

public class ConfirmConvertToFbe extends SettingsPreferenceFragment {
    public int getMetricsCategory() {
        return 403;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C0444R.C0450layout.confirm_convert_fbe, (ViewGroup) null);
        ((Button) inflate.findViewById(C0444R.C0448id.button_confirm_convert_fbe)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.FACTORY_RESET");
                intent.addFlags(268435456);
                intent.setPackage(RecentAppOpsAccess.ANDROID_SYSTEM_PACKAGE_NAME);
                intent.putExtra("android.intent.extra.REASON", "convert_fbe");
                ConfirmConvertToFbe.this.getActivity().sendBroadcast(intent);
            }
        });
        return inflate;
    }
}
