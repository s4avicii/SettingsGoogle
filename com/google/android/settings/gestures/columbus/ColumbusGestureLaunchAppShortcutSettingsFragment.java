package com.google.android.settings.gestures.columbus;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.google.android.settings.gestures.columbus.ColumbusGestureHelper;
import java.util.ArrayList;

public class ColumbusGestureLaunchAppShortcutSettingsFragment extends DashboardFragment implements ColumbusGestureHelper.GestureListener {
    private ColumbusGestureHelper mColumbusGestureHelper;
    private Context mContext;
    private Handler mHandler;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ColumbusAppShortcutSettings";
    }

    public int getMetricsCategory() {
        return 1872;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.columbus_launch_app_shortcut_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mColumbusGestureHelper = new ColumbusGestureHelper(context);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ArrayList parcelableArrayListExtra = getIntent().getParcelableArrayListExtra("columbus_app_shortcuts");
        ((ColumbusAppShortcutListPreferenceController) use(ColumbusAppShortcutListPreferenceController.class)).setApplicationPackageAndShortcuts((ComponentName) getIntent().getParcelableExtra("columbus_launch_app"), parcelableArrayListExtra);
        this.mHandler = new Handler(Looper.myLooper());
    }

    public void onResume() {
        super.onResume();
        this.mColumbusGestureHelper.bindToColumbusServiceProxy();
        this.mColumbusGestureHelper.setListener(this);
    }

    public void onPause() {
        super.onPause();
        this.mColumbusGestureHelper.setListener((ColumbusGestureHelper.GestureListener) null);
        this.mColumbusGestureHelper.unbindFromColumbusServiceProxy();
        finish();
    }

    public void onTrigger() {
        this.mHandler.post(new C1811x6d0c9835(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTrigger$0() {
        Toast.makeText(this.mContext, C0444R.string.columbus_gesture_detected, 0).show();
    }
}
