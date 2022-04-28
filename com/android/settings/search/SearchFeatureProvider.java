package com.android.settings.search;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.window.C0444R;
import com.android.settings.Utils;
import com.android.settings.activityembedding.ActivityEmbeddingRulesController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.search.SearchIndexableResources;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.util.List;

public interface SearchFeatureProvider {
    Intent buildSearchIntent(Context context, int i);

    SearchIndexableResources getSearchIndexableResources();

    void verifyLaunchSearchResultPageCaller(Context context, ComponentName componentName) throws SecurityException, IllegalArgumentException;

    String getSettingsIntelligencePkgName(Context context) {
        return context.getString(C0444R.string.config_settingsintelligence_package_name);
    }

    void initSearchToolbar(FragmentActivity fragmentActivity, Toolbar toolbar, int i) {
        if (fragmentActivity != null && toolbar != null) {
            if (!WizardManagerHelper.isDeviceProvisioned(fragmentActivity) || !Utils.isPackageEnabled(fragmentActivity, getSettingsIntelligencePkgName(fragmentActivity)) || WizardManagerHelper.isAnySetupWizard(fragmentActivity.getIntent())) {
                ViewGroup viewGroup = (ViewGroup) toolbar.getParent();
                if (viewGroup != null) {
                    viewGroup.setVisibility(8);
                    return;
                }
                return;
            }
            View navigationView = toolbar.getNavigationView();
            navigationView.setClickable(false);
            navigationView.setImportantForAccessibility(2);
            navigationView.setBackground((Drawable) null);
            Context applicationContext = fragmentActivity.getApplicationContext();
            Intent addFlags = buildSearchIntent(applicationContext, i).addFlags(67108864);
            List<ResolveInfo> queryIntentActivities = fragmentActivity.getPackageManager().queryIntentActivities(addFlags, 65536);
            if (!queryIntentActivities.isEmpty()) {
                ComponentName componentName = queryIntentActivities.get(0).getComponentInfo().getComponentName();
                addFlags.setComponent(componentName);
                ActivityEmbeddingRulesController.registerTwoPanePairRuleForSettingsHome(applicationContext, componentName, addFlags.getAction(), false, true, false);
                toolbar.setOnClickListener(new SearchFeatureProvider$$ExternalSyntheticLambda0(applicationContext, i, fragmentActivity, addFlags));
            }
        }
    }

    /* access modifiers changed from: private */
    static /* synthetic */ void lambda$initSearchToolbar$0(Context context, int i, FragmentActivity fragmentActivity, Intent intent, View view) {
        FeatureFactory.getFactory(context).getSlicesFeatureProvider().indexSliceDataAsync(context);
        FeatureFactory.getFactory(context).getMetricsFeatureProvider().logSettingsTileClick("homepage_search_bar", i);
        fragmentActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(fragmentActivity, new Pair[0]).toBundle());
    }
}
