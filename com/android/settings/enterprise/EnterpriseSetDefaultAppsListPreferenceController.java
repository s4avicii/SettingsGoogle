package com.android.settings.enterprise;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.applications.ApplicationFeatureProvider;
import com.android.settings.applications.EnterpriseDefaultApps;
import com.android.settings.applications.UserAppInfo;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.users.UserFeatureProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

public class EnterpriseSetDefaultAppsListPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final ApplicationFeatureProvider mApplicationFeatureProvider;
    private List<EnumMap<EnterpriseDefaultApps, List<ApplicationInfo>>> mApps = Collections.emptyList();
    private final EnterprisePrivacyFeatureProvider mEnterprisePrivacyFeatureProvider;
    private final SettingsPreferenceFragment mParent;
    private final PackageManager mPm;
    private final UserFeatureProvider mUserFeatureProvider;
    private List<UserInfo> mUsers = Collections.emptyList();

    public String getPreferenceKey() {
        return null;
    }

    public boolean isAvailable() {
        return true;
    }

    public EnterpriseSetDefaultAppsListPreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, PackageManager packageManager) {
        super(context);
        this.mPm = packageManager;
        this.mParent = settingsPreferenceFragment;
        FeatureFactory factory = FeatureFactory.getFactory(context);
        this.mApplicationFeatureProvider = factory.getApplicationFeatureProvider(context);
        this.mEnterprisePrivacyFeatureProvider = factory.getEnterprisePrivacyFeatureProvider(context);
        this.mUserFeatureProvider = factory.getUserFeatureProvider(context);
        buildAppList();
    }

    private void buildAppList() {
        this.mUsers = new ArrayList();
        this.mApps = new ArrayList();
        for (UserHandle next : this.mUserFeatureProvider.getUserProfiles()) {
            EnumMap enumMap = null;
            boolean z = false;
            for (EnterpriseDefaultApps enterpriseDefaultApps : EnterpriseDefaultApps.values()) {
                List<UserAppInfo> findPersistentPreferredActivities = this.mApplicationFeatureProvider.findPersistentPreferredActivities(next.getIdentifier(), enterpriseDefaultApps.getIntents());
                if (!findPersistentPreferredActivities.isEmpty()) {
                    if (!z) {
                        this.mUsers.add(findPersistentPreferredActivities.get(0).userInfo);
                        enumMap = new EnumMap(EnterpriseDefaultApps.class);
                        this.mApps.add(enumMap);
                        z = true;
                    }
                    ArrayList arrayList = new ArrayList();
                    for (UserAppInfo userAppInfo : findPersistentPreferredActivities) {
                        arrayList.add(userAppInfo.appInfo);
                    }
                    enumMap.put(enterpriseDefaultApps, arrayList);
                }
            }
        }
        ThreadUtils.postOnMainThread(new C0917xb9e1e136(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateUi */
    public void lambda$buildAppList$0() {
        Context context = this.mParent.getPreferenceManager().getContext();
        PreferenceScreen preferenceScreen = this.mParent.getPreferenceScreen();
        if (preferenceScreen != null) {
            if (this.mEnterprisePrivacyFeatureProvider.isInCompMode() || this.mUsers.size() != 1) {
                DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class);
                for (int i = 0; i < this.mUsers.size(); i++) {
                    PreferenceCategory preferenceCategory = new PreferenceCategory(context);
                    preferenceScreen.addPreference(preferenceCategory);
                    if (this.mUsers.get(i).isManagedProfile()) {
                        preferenceCategory.setTitle((CharSequence) devicePolicyManager.getString("Settings.WORK_CATEGORY_HEADER", new C0918xb9e1e137(this)));
                    } else {
                        preferenceCategory.setTitle((CharSequence) devicePolicyManager.getString("Settings.category_personal", new C0919xb9e1e138(this)));
                    }
                    preferenceCategory.setOrder(i);
                    createPreferences(context, preferenceCategory, this.mApps.get(i));
                }
                return;
            }
            createPreferences(context, preferenceScreen, this.mApps.get(0));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$updateUi$1() throws Exception {
        return this.mContext.getString(C0444R.string.category_work);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$updateUi$2() throws Exception {
        return this.mContext.getString(C0444R.string.category_personal);
    }

    private void createPreferences(Context context, PreferenceGroup preferenceGroup, EnumMap<EnterpriseDefaultApps, List<ApplicationInfo>> enumMap) {
        if (preferenceGroup != null) {
            for (EnterpriseDefaultApps enterpriseDefaultApps : EnterpriseDefaultApps.values()) {
                List list = enumMap.get(enterpriseDefaultApps);
                if (list != null && !list.isEmpty()) {
                    Preference preference = new Preference(context);
                    preference.setTitle((CharSequence) getTitle(context, enterpriseDefaultApps, list.size()));
                    preference.setSummary(buildSummaryString(context, list));
                    preference.setOrder(enterpriseDefaultApps.ordinal());
                    preference.setSelectable(false);
                    preferenceGroup.addPreference(preference);
                }
            }
        }
    }

    private CharSequence buildSummaryString(Context context, List<ApplicationInfo> list) {
        CharSequence[] charSequenceArr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            charSequenceArr[i] = list.get(i).loadLabel(this.mPm);
        }
        if (list.size() == 1) {
            return charSequenceArr[0];
        }
        if (list.size() == 2) {
            return context.getString(C0444R.string.app_names_concatenation_template_2, new Object[]{charSequenceArr[0], charSequenceArr[1]});
        }
        return context.getString(C0444R.string.app_names_concatenation_template_3, new Object[]{charSequenceArr[0], charSequenceArr[1], charSequenceArr[2]});
    }

    /* renamed from: com.android.settings.enterprise.EnterpriseSetDefaultAppsListPreferenceController$1 */
    static /* synthetic */ class C09161 {

        /* renamed from: $SwitchMap$com$android$settings$applications$EnterpriseDefaultApps */
        static final /* synthetic */ int[] f139x4783f43a;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.settings.applications.EnterpriseDefaultApps[] r0 = com.android.settings.applications.EnterpriseDefaultApps.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f139x4783f43a = r0
                com.android.settings.applications.EnterpriseDefaultApps r1 = com.android.settings.applications.EnterpriseDefaultApps.BROWSER     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f139x4783f43a     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.settings.applications.EnterpriseDefaultApps r1 = com.android.settings.applications.EnterpriseDefaultApps.CALENDAR     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f139x4783f43a     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.settings.applications.EnterpriseDefaultApps r1 = com.android.settings.applications.EnterpriseDefaultApps.CONTACTS     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f139x4783f43a     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.settings.applications.EnterpriseDefaultApps r1 = com.android.settings.applications.EnterpriseDefaultApps.PHONE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f139x4783f43a     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.settings.applications.EnterpriseDefaultApps r1 = com.android.settings.applications.EnterpriseDefaultApps.MAP     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = f139x4783f43a     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.settings.applications.EnterpriseDefaultApps r1 = com.android.settings.applications.EnterpriseDefaultApps.EMAIL     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = f139x4783f43a     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.android.settings.applications.EnterpriseDefaultApps r1 = com.android.settings.applications.EnterpriseDefaultApps.CAMERA     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.enterprise.EnterpriseSetDefaultAppsListPreferenceController.C09161.<clinit>():void");
        }
    }

    private String getTitle(Context context, EnterpriseDefaultApps enterpriseDefaultApps, int i) {
        switch (C09161.f139x4783f43a[enterpriseDefaultApps.ordinal()]) {
            case 1:
                return context.getString(C0444R.string.default_browser_title);
            case 2:
                return context.getString(C0444R.string.default_calendar_app_title);
            case 3:
                return context.getString(C0444R.string.default_contacts_app_title);
            case 4:
                return context.getResources().getQuantityString(C0444R.plurals.default_phone_app_title, i);
            case 5:
                return context.getString(C0444R.string.default_map_app_title);
            case 6:
                return context.getResources().getQuantityString(C0444R.plurals.default_email_app_title, i);
            case 7:
                return context.getResources().getQuantityString(C0444R.plurals.default_camera_app_title, i);
            default:
                throw new IllegalStateException("Unknown type of default " + enterpriseDefaultApps);
        }
    }
}
