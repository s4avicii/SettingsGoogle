package com.android.settings.users;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settingslib.users.AppCopyHelper;
import com.android.settingslib.widget.AppSwitchPreference;

public class AppCopyFragment extends SettingsPreferenceFragment {
    private static final String TAG = AppCopyFragment.class.getSimpleName();
    private PreferenceGroup mAppList;
    /* access modifiers changed from: private */
    public boolean mAppListChanged;
    private AsyncTask mAppLoadingTask;
    /* access modifiers changed from: private */
    public AppCopyHelper mHelper;
    private final BroadcastReceiver mPackageObserver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            AppCopyFragment.this.onPackageChanged(intent);
        }
    };
    protected UserHandle mUser;
    private final BroadcastReceiver mUserBackgrounding = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (AppCopyFragment.this.mAppListChanged) {
                AppCopyFragment.this.mHelper.installSelectedApps();
            }
        }
    };
    protected UserManager mUserManager;

    public int getMetricsCategory() {
        return 1897;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        init(bundle);
    }

    /* access modifiers changed from: protected */
    public void init(Bundle bundle) {
        if (bundle != null) {
            this.mUser = new UserHandle(bundle.getInt("user_id"));
        } else {
            Bundle arguments = getArguments();
            if (arguments != null && arguments.containsKey("user_id")) {
                this.mUser = new UserHandle(arguments.getInt("user_id"));
            }
        }
        if (this.mUser != null) {
            this.mHelper = new AppCopyHelper(getContext(), this.mUser);
            this.mUserManager = (UserManager) getActivity().getSystemService("user");
            addPreferencesFromResource(C0444R.xml.app_copier);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            this.mAppList = preferenceScreen;
            preferenceScreen.setOrderingAsAdded(false);
            return;
        }
        throw new IllegalStateException("No user specified.");
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("user_id", this.mUser.getIdentifier());
    }

    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(this.mUserBackgrounding, new IntentFilter("android.intent.action.USER_BACKGROUND"));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        getActivity().registerReceiver(this.mPackageObserver, intentFilter);
        this.mAppListChanged = false;
        AsyncTask asyncTask = this.mAppLoadingTask;
        if (asyncTask == null || asyncTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mAppLoadingTask = new AppLoadingTask().execute(new Void[0]);
        }
    }

    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(this.mUserBackgrounding);
        getActivity().unregisterReceiver(this.mPackageObserver);
        if (this.mAppListChanged) {
            new AsyncTask<Void, Void, Void>() {
                /* access modifiers changed from: protected */
                public Void doInBackground(Void... voidArr) {
                    AppCopyFragment.this.mHelper.installSelectedApps();
                    return null;
                }
            }.execute(new Void[0]);
        }
    }

    /* access modifiers changed from: private */
    public void onPackageChanged(Intent intent) {
        String action = intent.getAction();
        String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
        AppSwitchPreference appSwitchPreference = (AppSwitchPreference) findPreference(getKeyForPackage(schemeSpecificPart));
        if (appSwitchPreference != null) {
            if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                appSwitchPreference.setEnabled(false);
                appSwitchPreference.setChecked(false);
                this.mHelper.setPackageSelected(schemeSpecificPart, false);
            } else if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                appSwitchPreference.setEnabled(true);
            }
        }
    }

    private class AppLoadingTask extends AsyncTask<Void, Void, Void> {
        private AppLoadingTask() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voidArr) {
            AppCopyFragment.this.mHelper.fetchAndMergeApps();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void voidR) {
            AppCopyFragment.this.populateApps();
        }
    }

    /* access modifiers changed from: private */
    public void populateApps() {
        if (Utils.getExistingUser(this.mUserManager, this.mUser) != null) {
            this.mHelper.resetSelectedPackages();
            this.mAppList.removeAll();
            for (AppCopyHelper.SelectableAppInfo next : this.mHelper.getVisibleApps()) {
                if (next.packageName != null) {
                    AppSwitchPreference appSwitchPreference = new AppSwitchPreference(getPrefContext());
                    Drawable drawable = next.icon;
                    appSwitchPreference.setIcon(drawable != null ? drawable.mutate() : null);
                    appSwitchPreference.setChecked(false);
                    appSwitchPreference.setTitle(next.appName);
                    appSwitchPreference.setKey(getKeyForPackage(next.packageName));
                    appSwitchPreference.setPersistent(false);
                    appSwitchPreference.setOnPreferenceChangeListener(new AppCopyFragment$$ExternalSyntheticLambda0(this));
                    this.mAppList.addPreference(appSwitchPreference);
                }
            }
            this.mAppListChanged = true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$populateApps$0(Preference preference, Object obj) {
        if (!preference.isEnabled()) {
            return false;
        }
        boolean booleanValue = ((Boolean) obj).booleanValue();
        this.mHelper.setPackageSelected(preference.getKey().substring(4), booleanValue);
        this.mAppListChanged = true;
        return true;
    }

    private String getKeyForPackage(String str) {
        return "pkg_" + str;
    }
}
