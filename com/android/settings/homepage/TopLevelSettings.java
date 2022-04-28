package com.android.settings.homepage;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.C0444R;
import androidx.window.embedding.SplitController;
import com.android.settings.Utils;
import com.android.settings.activityembedding.ActivityEmbeddingRulesController;
import com.android.settings.activityembedding.ActivityEmbeddingUtils;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.support.SupportPreferenceController;
import com.android.settings.widget.HomepagePreference;
import com.android.settingslib.core.instrumentation.Instrumentable;
import com.android.settingslib.drawer.Tile;

public class TopLevelSettings extends DashboardFragment implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.top_level_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return false;
        }
    };
    private boolean mFirstStarted = true;
    private TopLevelHighlightMixin mHighlightMixin;
    private boolean mIsEmbeddingActivityEnabled;

    public Fragment getCallbackFragment() {
        return this;
    }

    public int getHelpResource() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "TopLevelSettings";
    }

    public int getMetricsCategory() {
        return 35;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.top_level_settings;
    }

    public TopLevelSettings() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("need_search_icon_in_action_bar", false);
        setArguments(bundle);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        HighlightableMenu.fromXml(context, getPreferenceScreenResId());
        ((SupportPreferenceController) use(SupportPreferenceController.class)).setActivity(getActivity());
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        ActivityEmbeddingRulesController.registerSubSettingsPairRule(getContext(), true);
        setHighlightPreferenceKey(preference.getKey());
        return super.onPreferenceTreeClick(preference);
    }

    public boolean onPreferenceStartFragment(PreferenceFragmentCompat preferenceFragmentCompat, Preference preference) {
        new SubSettingLauncher(getActivity()).setDestination(preference.getFragment()).setArguments(preference.getExtras()).setSourceMetricsCategory(preferenceFragmentCompat instanceof Instrumentable ? ((Instrumentable) preferenceFragmentCompat).getMetricsCategory() : 0).setTitleRes(-1).setIsSecondaryLayerPage(true).launch();
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        boolean isEmbeddingActivityEnabled = ActivityEmbeddingUtils.isEmbeddingActivityEnabled(getContext());
        this.mIsEmbeddingActivityEnabled = isEmbeddingActivityEnabled;
        if (isEmbeddingActivityEnabled) {
            if (bundle != null) {
                this.mHighlightMixin = (TopLevelHighlightMixin) bundle.getParcelable("highlight_mixin");
            }
            if (this.mHighlightMixin == null) {
                this.mHighlightMixin = new TopLevelHighlightMixin();
            }
        }
    }

    public void onStart() {
        if (this.mFirstStarted) {
            this.mFirstStarted = false;
        } else if (this.mIsEmbeddingActivityEnabled && isOnlyOneActivityInTask() && !SplitController.getInstance().isActivityEmbedded(getActivity())) {
            Log.i("TopLevelSettings", "Set default menu key");
            setHighlightMenuKey(getString(C0444R.string.menu_key_network), false);
        }
        super.onStart();
    }

    private boolean isOnlyOneActivityInTask() {
        if (((ActivityManager) getSystemService(ActivityManager.class)).getRunningTasks(1).get(0).numActivities == 1) {
            return true;
        }
        return false;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        TopLevelHighlightMixin topLevelHighlightMixin = this.mHighlightMixin;
        if (topLevelHighlightMixin != null) {
            bundle.putParcelable("highlight_mixin", topLevelHighlightMixin);
        }
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            int homepageIconColor = Utils.getHomepageIconColor(getContext());
            int preferenceCount = preferenceScreen.getPreferenceCount();
            int i = 0;
            while (i < preferenceCount) {
                Preference preference = preferenceScreen.getPreference(i);
                if (preference != null) {
                    Drawable icon = preference.getIcon();
                    if (icon != null) {
                        icon.setTint(homepageIconColor);
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        highlightPreferenceIfNeeded();
    }

    public void highlightPreferenceIfNeeded() {
        TopLevelHighlightMixin topLevelHighlightMixin = this.mHighlightMixin;
        if (topLevelHighlightMixin != null) {
            topLevelHighlightMixin.highlightPreferenceIfNeeded(getActivity());
        }
    }

    public TopLevelHighlightMixin getHighlightMixin() {
        return this.mHighlightMixin;
    }

    public void setHighlightPreferenceKey(String str) {
        if (this.mHighlightMixin != null && !TextUtils.equals(str, "top_level_support")) {
            this.mHighlightMixin.setHighlightPreferenceKey(str);
        }
    }

    public void setMenuHighlightShowed(boolean z) {
        TopLevelHighlightMixin topLevelHighlightMixin = this.mHighlightMixin;
        if (topLevelHighlightMixin != null) {
            topLevelHighlightMixin.setMenuHighlightShowed(z);
        }
    }

    public void setHighlightMenuKey(String str, boolean z) {
        TopLevelHighlightMixin topLevelHighlightMixin = this.mHighlightMixin;
        if (topLevelHighlightMixin != null) {
            topLevelHighlightMixin.setHighlightMenuKey(str, z);
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldForceRoundedIcon() {
        return getContext().getResources().getBoolean(C0444R.bool.config_force_rounded_icon_TopLevelSettings);
    }

    /* access modifiers changed from: protected */
    public RecyclerView.Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        if (!this.mIsEmbeddingActivityEnabled || !(getActivity() instanceof SettingsHomepageActivity)) {
            return super.onCreateAdapter(preferenceScreen);
        }
        return this.mHighlightMixin.onCreateAdapter(this, preferenceScreen);
    }

    /* access modifiers changed from: protected */
    public Preference createPreference(Tile tile) {
        return new HomepagePreference(getPrefContext());
    }

    /* access modifiers changed from: package-private */
    public void reloadHighlightMenuKey() {
        TopLevelHighlightMixin topLevelHighlightMixin = this.mHighlightMixin;
        if (topLevelHighlightMixin != null) {
            topLevelHighlightMixin.reloadHighlightMenuKey(getArguments());
        }
    }
}
