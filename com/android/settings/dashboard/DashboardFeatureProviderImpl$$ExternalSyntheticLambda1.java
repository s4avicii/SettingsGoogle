package com.android.settings.dashboard;

import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import com.android.settingslib.drawer.Tile;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda1 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ DashboardFeatureProviderImpl f$0;
    public final /* synthetic */ DashboardFragment f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ FragmentActivity f$3;
    public final /* synthetic */ Tile f$4;
    public final /* synthetic */ Intent f$5;
    public final /* synthetic */ int f$6;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda1(DashboardFeatureProviderImpl dashboardFeatureProviderImpl, DashboardFragment dashboardFragment, String str, FragmentActivity fragmentActivity, Tile tile, Intent intent, int i) {
        this.f$0 = dashboardFeatureProviderImpl;
        this.f$1 = dashboardFragment;
        this.f$2 = str;
        this.f$3 = fragmentActivity;
        this.f$4 = tile;
        this.f$5 = intent;
        this.f$6 = i;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$bindPreferenceToTileAndGetObservers$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, preference);
    }
}
