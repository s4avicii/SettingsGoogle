package com.google.android.settings.security;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.safetycenter.SafetyCenterStatusHolder;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.google.android.settings.security.SecurityContentManager;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SecurityHubDashboard extends DashboardFragment implements SecurityContentManager.UiDataSubscriber {
    public static final String KEY_SECURITY_PRIMARY_WARNING_GROUP = "security_primary_warning_group";
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.security_hub_dashboard) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return SecurityHubDashboard.buildPreferenceControllers(context, (SecurityHubDashboard) null);
        }

        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return FeatureFactory.getFactory(context).getSecuritySettingsFeatureProvider().hasAlternativeSecuritySettingsFragment() && !SafetyCenterStatusHolder.get().isEnabled(context);
        }
    };
    private Map<String, Preference> mContentProvidedPreferences = new HashMap();
    private boolean mInitialDataFetched = false;
    private final Object mLoadingAnimationLock = new Object();
    private final Object mPreferenceUpdateLock = new Object();
    private SecurityContentManager mSecurityContentManager;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SecurityHubDashboard";
    }

    public int getMetricsCategory() {
        return 1884;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.security_hub_dashboard;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, this);
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, SecurityHubDashboard securityHubDashboard) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ChangeScreenLockGooglePreferenceController(context, securityHubDashboard));
        arrayList.add(new ShowMoreWarningsPreferenceController(context));
        return arrayList;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSecurityContentManager = SecurityContentManager.getInstance(getContext()).subscribe(this);
        getPreferenceManager().setPreferenceComparisonCallback((PreferenceManager.PreferenceComparisonCallback) null);
    }

    public void onStart() {
        super.onStart();
        synchronized (this.mLoadingAnimationLock) {
            if (!this.mInitialDataFetched) {
                setLoading(true, false);
            }
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((PrimarySecurityWarningPreferenceController) use(PrimarySecurityWarningPreferenceController.class)).init(this);
    }

    public void onSecurityHubUiDataChange() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new SecurityHubDashboard$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSecurityHubUiDataChange$0() {
        if (getContext() != null) {
            synchronized (this.mPreferenceUpdateLock) {
                updateSecurityEntries();
                updateSecurityWarnings();
                updatePreferenceStates();
            }
            synchronized (this.mLoadingAnimationLock) {
                if (getView() != null && !this.mInitialDataFetched) {
                    this.mInitialDataFetched = true;
                    setLoading(false, true);
                }
            }
        }
    }

    private void updateSecurityEntries() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        List<SecurityContentManager.Entry> entries = this.mSecurityContentManager.getEntries();
        Map map = (Map) entries.stream().collect(Collectors.toMap(SecurityHubDashboard$$ExternalSyntheticLambda6.INSTANCE, Function.identity()));
        Map<String, Preference> map2 = (Map) entries.stream().map(new SecurityHubDashboard$$ExternalSyntheticLambda4(this)).collect(Collectors.toMap(SecurityHubDashboard$$ExternalSyntheticLambda5.INSTANCE, Function.identity()));
        MapDifference<String, Preference> difference = Maps.difference(map2, this.mContentProvidedPreferences);
        Sets.SetView<String> intersection = Sets.intersection(map2.keySet(), this.mContentProvidedPreferences.keySet());
        Collection<Preference> values = difference.entriesOnlyOnRight().values();
        Collection<Preference> values2 = difference.entriesOnlyOnLeft().values();
        for (String next : intersection) {
            Preference findPreference = preferenceScreen.findPreference(next);
            if (findPreference != null) {
                updateSecurityPreference(findPreference, (SecurityContentManager.Entry) map.get(next));
            }
        }
        Objects.requireNonNull(preferenceScreen);
        values.forEach(new SecurityHubDashboard$$ExternalSyntheticLambda3(preferenceScreen));
        values2.forEach(new SecurityHubDashboard$$ExternalSyntheticLambda2(preferenceScreen));
        this.mContentProvidedPreferences = map2;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Preference lambda$updateSecurityEntries$1(SecurityContentManager.Entry entry) {
        return updateSecurityPreference(new Preference(getContext()), entry);
    }

    private void updateSecurityWarnings() {
        SecurityWarning primarySecurityWarning = this.mSecurityContentManager.getPrimarySecurityWarning();
        LogicalPreferenceGroup primarySecurityWarningGroup = getPrimarySecurityWarningGroup();
        if (primarySecurityWarning != null) {
            primarySecurityWarningGroup.setVisible(true);
        } else {
            primarySecurityWarningGroup.setVisible(false);
        }
    }

    private LogicalPreferenceGroup getPrimarySecurityWarningGroup() {
        return (LogicalPreferenceGroup) getPreferenceScreen().findPreference(KEY_SECURITY_PRIMARY_WARNING_GROUP);
    }

    private Preference updateSecurityPreference(Preference preference, SecurityContentManager.Entry entry) {
        preference.setTitle((CharSequence) entry.getTitle());
        preference.setKey(entry.getSecuritySourceId());
        preference.setSummary((CharSequence) entry.getSummary());
        preference.setIcon(entry.getSecurityLevel().getEntryIconResId());
        preference.setOrder(entry.getOrder());
        Bundle onClickBundle = entry.getOnClickBundle();
        if (onClickBundle != null) {
            preference.setOnPreferenceClickListener(new SecurityHubDashboard$$ExternalSyntheticLambda0(this, onClickBundle));
        }
        return preference;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateSecurityPreference$2(Bundle bundle, Preference preference) {
        return this.mSecurityContentManager.handleClick(bundle, getActivity());
    }
}
