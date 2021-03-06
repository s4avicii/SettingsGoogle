package com.android.settings.applications.appinfo;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.fragment.app.FragmentActivity;
import androidx.window.C0444R;
import com.android.settings.SettingsActivity;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.applications.appinfo.ButtonActionDialogFragment;
import com.android.settings.applications.specialaccess.interactacrossprofiles.InteractAcrossProfilesDetailsPreferenceController;
import com.android.settings.applications.specialaccess.pictureinpicture.PictureInPictureDetailPreferenceController;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppInfoDashboardFragment extends DashboardFragment implements ApplicationsState.Callbacks, ButtonActionDialogFragment.AppButtonsDialogListener {
    static final int REQUEST_UNINSTALL = 0;
    static final int UNINSTALL_ALL_USERS_MENU = 1;
    static final int UNINSTALL_UPDATES = 2;
    private AppButtonsPreferenceController mAppButtonsPreferenceController;
    /* access modifiers changed from: private */
    public ApplicationsState.AppEntry mAppEntry;
    private RestrictedLockUtils.EnforcedAdmin mAppsControlDisallowedAdmin;
    private boolean mAppsControlDisallowedBySystem;
    private List<Callback> mCallbacks = new ArrayList();
    private DevicePolicyManager mDpm;
    boolean mFinishing;
    private boolean mInitialized;
    private InstantAppButtonsPreferenceController mInstantAppButtonPreferenceController;
    private boolean mListeningToPackageRemove;
    /* access modifiers changed from: private */
    public PackageInfo mPackageInfo;
    private String mPackageName;
    final BroadcastReceiver mPackageRemovedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!AppInfoDashboardFragment.this.mFinishing) {
                String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
                if (AppInfoDashboardFragment.this.mAppEntry == null || AppInfoDashboardFragment.this.mAppEntry.info == null || TextUtils.equals(AppInfoDashboardFragment.this.mAppEntry.info.packageName, schemeSpecificPart)) {
                    AppInfoDashboardFragment.this.onPackageRemoved();
                } else if (AppInfoDashboardFragment.this.mAppEntry.info.isResourceOverlay() && TextUtils.equals(AppInfoDashboardFragment.this.mPackageInfo.overlayTarget, schemeSpecificPart)) {
                    AppInfoDashboardFragment.this.refreshUi();
                }
            }
        }
    };
    private PackageManager mPm;
    private ApplicationsState.Session mSession;
    private boolean mShowUninstalled;
    private ApplicationsState mState;
    private int mUid;
    private boolean mUpdatedSysApp = false;
    private int mUserId;
    private UserManager mUserManager;

    public interface Callback {
        void refreshUi();
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AppInfoDashboard";
    }

    public int getMetricsCategory() {
        return 20;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.app_info_settings;
    }

    public void onAllSizesComputed() {
    }

    public void onLauncherInfoChanged() {
    }

    public void onLoadEntriesCompleted() {
    }

    public void onPackageIconChanged() {
    }

    public void onRebuildComplete(ArrayList<ApplicationsState.AppEntry> arrayList) {
    }

    public void onRunningStateChanged(boolean z) {
    }

    /* access modifiers changed from: protected */
    public boolean shouldSkipForInitialSUW() {
        return true;
    }

    public void onAttach(Context context) {
        Class cls = AppAllServicesPreferenceController.class;
        Class cls2 = AppPermissionPreferenceController.class;
        super.onAttach(context);
        String packageName = getPackageName();
        TimeSpentInAppPreferenceController timeSpentInAppPreferenceController = (TimeSpentInAppPreferenceController) use(TimeSpentInAppPreferenceController.class);
        timeSpentInAppPreferenceController.setPackageName(packageName);
        timeSpentInAppPreferenceController.initLifeCycleOwner(this);
        ((AppDataUsagePreferenceController) use(AppDataUsagePreferenceController.class)).setParentFragment(this);
        AppInstallerInfoPreferenceController appInstallerInfoPreferenceController = (AppInstallerInfoPreferenceController) use(AppInstallerInfoPreferenceController.class);
        appInstallerInfoPreferenceController.setPackageName(packageName);
        appInstallerInfoPreferenceController.setParentFragment(this);
        ((AppInstallerPreferenceCategoryController) use(AppInstallerPreferenceCategoryController.class)).setChildren(Arrays.asList(new AbstractPreferenceController[]{appInstallerInfoPreferenceController}));
        ((AppNotificationPreferenceController) use(AppNotificationPreferenceController.class)).setParentFragment(this);
        ((AppOpenByDefaultPreferenceController) use(AppOpenByDefaultPreferenceController.class)).setPackageName(packageName).setParentFragment(this);
        ((AppPermissionPreferenceController) use(cls2)).setParentFragment(this);
        ((AppPermissionPreferenceController) use(cls2)).setPackageName(packageName);
        ((AppSettingPreferenceController) use(AppSettingPreferenceController.class)).setPackageName(packageName).setParentFragment(this);
        ((AppAllServicesPreferenceController) use(cls)).setParentFragment(this);
        ((AppAllServicesPreferenceController) use(cls)).setPackageName(packageName);
        ((AppStoragePreferenceController) use(AppStoragePreferenceController.class)).setParentFragment(this);
        ((AppVersionPreferenceController) use(AppVersionPreferenceController.class)).setParentFragment(this);
        ((InstantAppDomainsPreferenceController) use(InstantAppDomainsPreferenceController.class)).setParentFragment(this);
        ((ExtraAppInfoPreferenceController) use(ExtraAppInfoPreferenceController.class)).setPackageName(packageName);
        HibernationSwitchPreferenceController hibernationSwitchPreferenceController = (HibernationSwitchPreferenceController) use(HibernationSwitchPreferenceController.class);
        hibernationSwitchPreferenceController.setParentFragment(this);
        hibernationSwitchPreferenceController.setPackage(packageName);
        ((AppHibernationPreferenceCategoryController) use(AppHibernationPreferenceCategoryController.class)).setChildren(Arrays.asList(new AbstractPreferenceController[]{hibernationSwitchPreferenceController}));
        WriteSystemSettingsPreferenceController writeSystemSettingsPreferenceController = (WriteSystemSettingsPreferenceController) use(WriteSystemSettingsPreferenceController.class);
        writeSystemSettingsPreferenceController.setParentFragment(this);
        DrawOverlayDetailPreferenceController drawOverlayDetailPreferenceController = (DrawOverlayDetailPreferenceController) use(DrawOverlayDetailPreferenceController.class);
        drawOverlayDetailPreferenceController.setParentFragment(this);
        PictureInPictureDetailPreferenceController pictureInPictureDetailPreferenceController = (PictureInPictureDetailPreferenceController) use(PictureInPictureDetailPreferenceController.class);
        pictureInPictureDetailPreferenceController.setPackageName(packageName);
        pictureInPictureDetailPreferenceController.setParentFragment(this);
        ExternalSourceDetailPreferenceController externalSourceDetailPreferenceController = (ExternalSourceDetailPreferenceController) use(ExternalSourceDetailPreferenceController.class);
        externalSourceDetailPreferenceController.setPackageName(packageName);
        externalSourceDetailPreferenceController.setParentFragment(this);
        InteractAcrossProfilesDetailsPreferenceController interactAcrossProfilesDetailsPreferenceController = (InteractAcrossProfilesDetailsPreferenceController) use(InteractAcrossProfilesDetailsPreferenceController.class);
        interactAcrossProfilesDetailsPreferenceController.setPackageName(packageName);
        interactAcrossProfilesDetailsPreferenceController.setParentFragment(this);
        AlarmsAndRemindersDetailPreferenceController alarmsAndRemindersDetailPreferenceController = (AlarmsAndRemindersDetailPreferenceController) use(AlarmsAndRemindersDetailPreferenceController.class);
        alarmsAndRemindersDetailPreferenceController.setPackageName(packageName);
        alarmsAndRemindersDetailPreferenceController.setParentFragment(this);
        ((AdvancedAppInfoPreferenceCategoryController) use(AdvancedAppInfoPreferenceCategoryController.class)).setChildren(Arrays.asList(new AbstractPreferenceController[]{writeSystemSettingsPreferenceController, drawOverlayDetailPreferenceController, pictureInPictureDetailPreferenceController, externalSourceDetailPreferenceController, interactAcrossProfilesDetailsPreferenceController, alarmsAndRemindersDetailPreferenceController}));
        ((AppLocalePreferenceController) use(AppLocalePreferenceController.class)).setParentFragment(this);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mFinishing = false;
        FragmentActivity activity = getActivity();
        this.mDpm = (DevicePolicyManager) activity.getSystemService("device_policy");
        this.mUserManager = (UserManager) activity.getSystemService("user");
        this.mPm = activity.getPackageManager();
        if (ensurePackageInfoAvailable(activity) && ensureDisplayableModule(activity)) {
            startListeningToPackageRemove();
            setHasOptionsMenu(true);
            replaceEnterpriseStringTitle("interact_across_profiles", "Settings.CONNECTED_WORK_AND_PERSONAL_APPS_TITLE", C0444R.string.interact_across_profiles_title);
        }
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        if (ensurePackageInfoAvailable(getActivity())) {
            super.onCreatePreferences(bundle, str);
        }
    }

    public void onDestroy() {
        stopListeningToPackageRemove();
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        this.mAppsControlDisallowedAdmin = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(activity, "no_control_apps", this.mUserId);
        this.mAppsControlDisallowedBySystem = RestrictedLockUtilsInternal.hasBaseUserRestriction(activity, "no_control_apps", this.mUserId);
        if (!refreshUi()) {
            setIntentAndFinish(true, true);
        }
        getActivity().invalidateOptionsMenu();
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        retrieveAppEntry();
        if (this.mPackageInfo == null) {
            return null;
        }
        String packageName = getPackageName();
        ArrayList<AbstractPreferenceController> arrayList = new ArrayList<>();
        Lifecycle settingsLifecycle = getSettingsLifecycle();
        arrayList.add(new AppHeaderViewPreferenceController(context, this, packageName, settingsLifecycle));
        for (AbstractPreferenceController abstractPreferenceController : arrayList) {
            this.mCallbacks.add((Callback) abstractPreferenceController);
        }
        InstantAppButtonsPreferenceController instantAppButtonsPreferenceController = new InstantAppButtonsPreferenceController(context, this, packageName, settingsLifecycle);
        this.mInstantAppButtonPreferenceController = instantAppButtonsPreferenceController;
        arrayList.add(instantAppButtonsPreferenceController);
        AppButtonsPreferenceController appButtonsPreferenceController = new AppButtonsPreferenceController((SettingsActivity) getActivity(), this, settingsLifecycle, packageName, this.mState, 0, 5);
        this.mAppButtonsPreferenceController = appButtonsPreferenceController;
        arrayList.add(appButtonsPreferenceController);
        arrayList.add(new AppBatteryPreferenceController(context, this, packageName, getUid(), settingsLifecycle));
        arrayList.add(new AppMemoryPreferenceController(context, this, settingsLifecycle));
        arrayList.add(new DefaultHomeShortcutPreferenceController(context, packageName));
        arrayList.add(new DefaultBrowserShortcutPreferenceController(context, packageName));
        arrayList.add(new DefaultPhoneShortcutPreferenceController(context, packageName));
        arrayList.add(new DefaultEmergencyShortcutPreferenceController(context, packageName));
        arrayList.add(new DefaultSmsShortcutPreferenceController(context, packageName));
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public void addToCallbackList(Callback callback) {
        if (callback != null) {
            this.mCallbacks.add(callback);
        }
    }

    /* access modifiers changed from: package-private */
    public ApplicationsState.AppEntry getAppEntry() {
        return this.mAppEntry;
    }

    public PackageInfo getPackageInfo() {
        return this.mPackageInfo;
    }

    public void onPackageSizeChanged(String str) {
        if (!TextUtils.equals(str, this.mPackageName)) {
            Log.d("AppInfoDashboard", "Package change irrelevant, skipping");
        } else {
            refreshUi();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean ensurePackageInfoAvailable(Activity activity) {
        if (this.mPackageInfo != null) {
            return true;
        }
        this.mFinishing = true;
        Log.w("AppInfoDashboard", "Package info not available. Is this package already uninstalled?");
        activity.finishAndRemoveTask();
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean ensureDisplayableModule(Activity activity) {
        if (!AppUtils.isHiddenSystemModule(activity.getApplicationContext(), this.mPackageName)) {
            return true;
        }
        this.mFinishing = true;
        Log.w("AppInfoDashboard", "Package is hidden module, exiting: " + this.mPackageName);
        activity.finishAndRemoveTask();
        return false;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menu.add(0, 2, 0, C0444R.string.app_factory_reset).setShowAsAction(0);
        menu.add(0, 1, 1, C0444R.string.uninstall_all_users_text).setShowAsAction(0);
        menu.add(0, 4, 0, C0444R.string.app_restricted_settings_lockscreen_title).setShowAsAction(0);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        if (!this.mFinishing) {
            super.onPrepareOptionsMenu(menu);
            boolean z = true;
            menu.findItem(1).setVisible(shouldShowUninstallForAll(this.mAppEntry));
            menu.findItem(4).setVisible(shouldShowAccessRestrictedSettings());
            this.mUpdatedSysApp = (this.mAppEntry.info.flags & 128) != 0;
            MenuItem findItem = menu.findItem(2);
            boolean z2 = getContext().getResources().getBoolean(C0444R.bool.config_disable_uninstall_update);
            if (!this.mUserManager.isAdminUser() || !this.mUpdatedSysApp || this.mAppsControlDisallowedBySystem || z2) {
                z = false;
            }
            findItem.setVisible(z);
            if (findItem.isVisible()) {
                RestrictedLockUtilsInternal.setMenuItemAsDisabledByAdmin(getActivity(), findItem, this.mAppsControlDisallowedAdmin);
            }
        }
    }

    private static void showLockScreen(Context context, final Runnable runnable) {
        if (((KeyguardManager) context.getSystemService(KeyguardManager.class)).isKeyguardSecure()) {
            C06751 r0 = new BiometricPrompt.AuthenticationCallback() {
                public void onAuthenticationError(int i, CharSequence charSequence) {
                }

                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult authenticationResult) {
                    runnable.run();
                }
            };
            BiometricPrompt.Builder title = new BiometricPrompt.Builder(context).setTitle(context.getText(C0444R.string.app_restricted_settings_lockscreen_title));
            if (((BiometricManager) context.getSystemService(BiometricManager.class)).canAuthenticate(33023) == 0) {
                title.setAllowedAuthenticators(33023);
            }
            title.build().authenticate(new CancellationSignal(), new AppInfoDashboardFragment$$ExternalSyntheticLambda1(new Handler(Looper.getMainLooper())), r0);
            return;
        }
        runnable.run();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 1) {
            uninstallPkg(this.mAppEntry.info.packageName, true, false);
            return true;
        } else if (itemId == 2) {
            uninstallPkg(this.mAppEntry.info.packageName, false, false);
            return true;
        } else if (itemId != 4) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            showLockScreen(getContext(), new AppInfoDashboardFragment$$ExternalSyntheticLambda0(this));
            return true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onOptionsItemSelected$1() {
        ((AppOpsManager) getContext().getSystemService(AppOpsManager.class)).setMode(119, getUid(), getPackageName(), 0);
        getActivity().invalidateOptionsMenu();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 0) {
            getActivity().invalidateOptionsMenu();
        }
        AppButtonsPreferenceController appButtonsPreferenceController = this.mAppButtonsPreferenceController;
        if (appButtonsPreferenceController != null) {
            appButtonsPreferenceController.handleActivityResult(i, i2, intent);
        }
    }

    public void handleDialogClick(int i) {
        AppButtonsPreferenceController appButtonsPreferenceController = this.mAppButtonsPreferenceController;
        if (appButtonsPreferenceController != null) {
            appButtonsPreferenceController.handleDialogClick(i);
        }
    }

    private boolean shouldShowAccessRestrictedSettings() {
        try {
            return ((AppOpsManager) getSystemService(AppOpsManager.class)).noteOpNoThrow(119, getUid(), getPackageName()) == 1;
        } catch (Exception unused) {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0013, code lost:
        r0 = r4.mPackageInfo;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean shouldShowUninstallForAll(com.android.settingslib.applications.ApplicationsState.AppEntry r5) {
        /*
            r4 = this;
            boolean r0 = r4.mUpdatedSysApp
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0008
        L_0x0006:
            r1 = r2
            goto L_0x0052
        L_0x0008:
            if (r5 != 0) goto L_0x000b
            goto L_0x0006
        L_0x000b:
            android.content.pm.ApplicationInfo r0 = r5.info
            int r0 = r0.flags
            r0 = r0 & r1
            if (r0 == 0) goto L_0x0013
            goto L_0x0006
        L_0x0013:
            android.content.pm.PackageInfo r0 = r4.mPackageInfo
            if (r0 == 0) goto L_0x0006
            android.app.admin.DevicePolicyManager r3 = r4.mDpm
            java.lang.String r0 = r0.packageName
            boolean r0 = r3.packageHasActiveAdmins(r0)
            if (r0 == 0) goto L_0x0022
            goto L_0x0006
        L_0x0022:
            int r0 = android.os.UserHandle.myUserId()
            if (r0 == 0) goto L_0x0029
            goto L_0x0006
        L_0x0029:
            android.os.UserManager r0 = r4.mUserManager
            java.util.List r0 = r0.getUsers()
            int r0 = r0.size()
            r3 = 2
            if (r0 >= r3) goto L_0x0037
            goto L_0x0006
        L_0x0037:
            java.lang.String r0 = r4.mPackageName
            int r4 = r4.getNumberOfUserWithPackageInstalled(r0)
            if (r4 >= r3) goto L_0x0049
            android.content.pm.ApplicationInfo r4 = r5.info
            int r4 = r4.flags
            r0 = 8388608(0x800000, float:1.17549435E-38)
            r4 = r4 & r0
            if (r4 == 0) goto L_0x0049
            goto L_0x0006
        L_0x0049:
            android.content.pm.ApplicationInfo r4 = r5.info
            boolean r4 = com.android.settingslib.applications.AppUtils.isInstant(r4)
            if (r4 == 0) goto L_0x0052
            goto L_0x0006
        L_0x0052:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.appinfo.AppInfoDashboardFragment.shouldShowUninstallForAll(com.android.settingslib.applications.ApplicationsState$AppEntry):boolean");
    }

    /* access modifiers changed from: package-private */
    public boolean refreshUi() {
        retrieveAppEntry();
        ApplicationsState.AppEntry appEntry = this.mAppEntry;
        boolean z = false;
        if (appEntry == null || this.mPackageInfo == null) {
            return false;
        }
        this.mState.ensureIcon(appEntry);
        for (Callback refreshUi : this.mCallbacks) {
            refreshUi.refreshUi();
        }
        if (this.mAppButtonsPreferenceController.isAvailable()) {
            this.mAppButtonsPreferenceController.refreshUi();
        }
        if (!this.mInitialized) {
            this.mInitialized = true;
            if ((this.mAppEntry.info.flags & 8388608) == 0) {
                z = true;
            }
            this.mShowUninstalled = z;
        } else {
            try {
                ApplicationInfo applicationInfo = getActivity().getPackageManager().getApplicationInfo(this.mAppEntry.info.packageName, 4194816);
                if (!this.mShowUninstalled) {
                    if ((applicationInfo.flags & 8388608) != 0) {
                        return true;
                    }
                    return false;
                }
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }
        return true;
    }

    private void uninstallPkg(String str, boolean z, boolean z2) {
        stopListeningToPackageRemove();
        Intent intent = new Intent("android.intent.action.UNINSTALL_PACKAGE", Uri.parse("package:" + str));
        intent.putExtra("android.intent.extra.UNINSTALL_ALL_USERS", z);
        this.mMetricsFeatureProvider.action(getContext(), 872, (Pair<Integer, Object>[]) new Pair[0]);
        startActivityForResult(intent, 0);
    }

    public static void startAppInfoFragment(Class<?> cls, int i, Bundle bundle, SettingsPreferenceFragment settingsPreferenceFragment, ApplicationsState.AppEntry appEntry) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString("package", appEntry.info.packageName);
        bundle.putInt("uid", appEntry.info.uid);
        new SubSettingLauncher(settingsPreferenceFragment.getContext()).setDestination(cls.getName()).setArguments(bundle).setTitleRes(i).setResultListener(settingsPreferenceFragment, 1).setSourceMetricsCategory(settingsPreferenceFragment.getMetricsCategory()).launch();
    }

    /* access modifiers changed from: private */
    public void onPackageRemoved() {
        getActivity().finishActivity(1);
        getActivity().finishAndRemoveTask();
    }

    /* access modifiers changed from: package-private */
    public int getNumberOfUserWithPackageInstalled(String str) {
        int i = 0;
        for (UserInfo userInfo : this.mUserManager.getAliveUsers()) {
            try {
                if ((this.mPm.getApplicationInfoAsUser(str, 128, userInfo.id).flags & 8388608) != 0) {
                    i++;
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Log.e("AppInfoDashboard", "Package: " + str + " not found for user: " + userInfo.id);
            }
        }
        return i;
    }

    private String getPackageName() {
        String str = this.mPackageName;
        if (str != null) {
            return str;
        }
        Bundle arguments = getArguments();
        String string = arguments != null ? arguments.getString("package") : null;
        this.mPackageName = string;
        if (string == null) {
            Intent intent = arguments == null ? getActivity().getIntent() : (Intent) arguments.getParcelable("intent");
            if (intent != null) {
                this.mPackageName = intent.getData().getSchemeSpecificPart();
            }
        }
        return this.mPackageName;
    }

    private int getUid() {
        int i = this.mUid;
        if (i > 0) {
            return i;
        }
        Bundle arguments = getArguments();
        int i2 = -1;
        int i3 = arguments != null ? arguments.getInt("uid") : -1;
        this.mUid = i3;
        if (i3 <= 0) {
            Intent intent = arguments == null ? getActivity().getIntent() : (Intent) arguments.getParcelable("intent");
            if (!(intent == null || intent.getExtras() == null)) {
                i2 = intent.getIntExtra("uId", -1);
                this.mUid = i2;
            }
            this.mUid = i2;
        }
        return this.mUid;
    }

    /* access modifiers changed from: package-private */
    public void retrieveAppEntry() {
        FragmentActivity activity = getActivity();
        if (activity != null && !this.mFinishing) {
            if (this.mState == null) {
                ApplicationsState instance = ApplicationsState.getInstance(activity.getApplication());
                this.mState = instance;
                this.mSession = instance.newSession(this, getSettingsLifecycle());
            }
            this.mUserId = UserHandle.myUserId();
            ApplicationsState.AppEntry entry = this.mState.getEntry(getPackageName(), UserHandle.myUserId());
            this.mAppEntry = entry;
            if (entry != null) {
                try {
                    this.mPackageInfo = activity.getPackageManager().getPackageInfo(this.mAppEntry.info.packageName, 4198976);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e("AppInfoDashboard", "Exception when retrieving package:" + this.mAppEntry.info.packageName, e);
                }
            } else {
                Log.w("AppInfoDashboard", "Missing AppEntry; maybe reinstalling?");
                this.mPackageInfo = null;
            }
        }
    }

    private void setIntentAndFinish(boolean z, boolean z2) {
        Intent intent = new Intent();
        intent.putExtra(AppButtonsPreferenceController.APP_CHG, z2);
        ((SettingsActivity) getActivity()).finishPreferencePanel(-1, intent);
        this.mFinishing = true;
    }

    public void onPackageListChanged() {
        if (!refreshUi()) {
            setIntentAndFinish(true, true);
        }
    }

    /* access modifiers changed from: package-private */
    public void startListeningToPackageRemove() {
        if (!this.mListeningToPackageRemove) {
            this.mListeningToPackageRemove = true;
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            getContext().registerReceiver(this.mPackageRemovedReceiver, intentFilter);
        }
    }

    private void stopListeningToPackageRemove() {
        if (this.mListeningToPackageRemove) {
            this.mListeningToPackageRemove = false;
            getContext().unregisterReceiver(this.mPackageRemovedReceiver);
        }
    }
}
