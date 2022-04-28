package com.android.settings.security.trustagent;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.widget.LockPatternUtils;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import java.util.ArrayList;
import java.util.List;

public class TrustAgentManager {
    static final String PERMISSION_PROVIDE_AGENT = "android.permission.PROVIDE_TRUST_AGENT";
    private static final Intent TRUST_AGENT_INTENT = new Intent("android.service.trust.TrustAgentService");

    public static class TrustAgentComponentInfo {
        public RestrictedLockUtils.EnforcedAdmin admin = null;
        public ComponentName componentName;
        public String summary;
        public String title;
    }

    public boolean shouldProvideTrust(ResolveInfo resolveInfo, PackageManager packageManager) {
        String str = resolveInfo.serviceInfo.packageName;
        if (packageManager.checkPermission(PERMISSION_PROVIDE_AGENT, str) == 0) {
            return true;
        }
        Log.w("TrustAgentManager", "Skipping agent because package " + str + " does not have permission " + PERMISSION_PROVIDE_AGENT + ".");
        return false;
    }

    public CharSequence getActiveTrustAgentLabel(Context context, LockPatternUtils lockPatternUtils) {
        List<TrustAgentComponentInfo> activeTrustAgents = getActiveTrustAgents(context, lockPatternUtils);
        if (activeTrustAgents.isEmpty()) {
            return null;
        }
        return activeTrustAgents.get(0).title;
    }

    public List<TrustAgentComponentInfo> getActiveTrustAgents(Context context, LockPatternUtils lockPatternUtils) {
        int myUserId = UserHandle.myUserId();
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        PackageManager packageManager = context.getPackageManager();
        ArrayList arrayList = new ArrayList();
        List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(TRUST_AGENT_INTENT, 128);
        List enabledTrustAgents = lockPatternUtils.getEnabledTrustAgents(myUserId);
        RestrictedLockUtils.EnforcedAdmin checkIfKeyguardFeaturesDisabled = RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(context, 16, myUserId);
        if (enabledTrustAgents != null && !enabledTrustAgents.isEmpty()) {
            for (ResolveInfo next : queryIntentServices) {
                if (next.serviceInfo != null && shouldProvideTrust(next, packageManager)) {
                    TrustAgentComponentInfo settingsComponent = getSettingsComponent(packageManager, next);
                    if (settingsComponent.componentName != null && enabledTrustAgents.contains(getComponentName(next)) && !TextUtils.isEmpty(settingsComponent.title)) {
                        if (checkIfKeyguardFeaturesDisabled != null && devicePolicyManager.getTrustAgentConfiguration((ComponentName) null, getComponentName(next)) == null) {
                            settingsComponent.admin = checkIfKeyguardFeaturesDisabled;
                        }
                        arrayList.add(settingsComponent);
                    }
                }
            }
        }
        return arrayList;
    }

    public ComponentName getComponentName(ResolveInfo resolveInfo) {
        if (resolveInfo == null || resolveInfo.serviceInfo == null) {
            return null;
        }
        ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        return new ComponentName(serviceInfo.packageName, serviceInfo.name);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: android.content.ComponentName} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: android.content.res.XmlResourceParser} */
    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0075, code lost:
        r9 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0077, code lost:
        r9 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0079, code lost:
        r9 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x007b, code lost:
        r8 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x007c, code lost:
        r0 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x008a, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0091, code lost:
        if (r2 == null) goto L_0x00a3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0093, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x009a, code lost:
        if (r2 == null) goto L_0x00a3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00a0, code lost:
        if (r2 == null) goto L_0x00a3;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:11:0x0020, B:29:0x006d] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x007b A[ExcHandler: all (th java.lang.Throwable), Splitter:B:11:0x0020] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x008a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.android.settings.security.trustagent.TrustAgentManager.TrustAgentComponentInfo getSettingsComponent(android.content.pm.PackageManager r9, android.content.pm.ResolveInfo r10) {
        /*
            r8 = this;
            java.lang.String r8 = "TrustAgentManager"
            r0 = 0
            if (r10 == 0) goto L_0x00ea
            android.content.pm.ServiceInfo r1 = r10.serviceInfo
            if (r1 == 0) goto L_0x00ea
            android.os.Bundle r1 = r1.metaData
            if (r1 != 0) goto L_0x000f
            goto L_0x00ea
        L_0x000f:
            com.android.settings.security.trustagent.TrustAgentManager$TrustAgentComponentInfo r1 = new com.android.settings.security.trustagent.TrustAgentManager$TrustAgentComponentInfo
            r1.<init>()
            android.content.pm.ServiceInfo r2 = r10.serviceInfo     // Catch:{ NameNotFoundException -> 0x009d, IOException -> 0x0097, XmlPullParserException -> 0x008e, all -> 0x0087 }
            java.lang.String r3 = "android.service.trust.trustagent"
            android.content.res.XmlResourceParser r2 = r2.loadXmlMetaData(r9, r3)     // Catch:{ NameNotFoundException -> 0x009d, IOException -> 0x0097, XmlPullParserException -> 0x008e, all -> 0x0087 }
            if (r2 != 0) goto L_0x0029
            java.lang.String r9 = "Can't find android.service.trust.trustagent meta-data"
            android.util.Slog.w(r8, r9)     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            if (r2 == 0) goto L_0x0028
            r2.close()
        L_0x0028:
            return r0
        L_0x0029:
            android.content.pm.ServiceInfo r3 = r10.serviceInfo     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            android.content.pm.ApplicationInfo r3 = r3.applicationInfo     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            android.content.res.Resources r9 = r9.getResourcesForApplication(r3)     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            android.util.AttributeSet r3 = android.util.Xml.asAttributeSet(r2)     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
        L_0x0035:
            int r4 = r2.next()     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            r5 = 2
            r6 = 1
            if (r4 == r6) goto L_0x0040
            if (r4 == r5) goto L_0x0040
            goto L_0x0035
        L_0x0040:
            java.lang.String r4 = r2.getName()     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            java.lang.String r7 = "trust-agent"
            boolean r4 = r7.equals(r4)     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            if (r4 != 0) goto L_0x0056
            java.lang.String r9 = "Meta-data does not start with trust-agent tag"
            android.util.Slog.w(r8, r9)     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            r2.close()
            return r0
        L_0x0056:
            int[] r4 = com.android.internal.R.styleable.TrustAgent     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            android.content.res.TypedArray r9 = r9.obtainAttributes(r3, r4)     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            java.lang.String r3 = r9.getString(r6)     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            r1.summary = r3     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            r3 = 0
            java.lang.String r3 = r9.getString(r3)     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            r1.title = r3     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            java.lang.String r3 = r9.getString(r5)     // Catch:{ NameNotFoundException -> 0x0084, IOException -> 0x0081, XmlPullParserException -> 0x007e, all -> 0x007b }
            r9.recycle()     // Catch:{ NameNotFoundException -> 0x0079, IOException -> 0x0077, XmlPullParserException -> 0x0075, all -> 0x007b }
            r2.close()
            r9 = r0
            goto L_0x00a3
        L_0x0075:
            r9 = move-exception
            goto L_0x0091
        L_0x0077:
            r9 = move-exception
            goto L_0x009a
        L_0x0079:
            r9 = move-exception
            goto L_0x00a0
        L_0x007b:
            r8 = move-exception
            r0 = r2
            goto L_0x0088
        L_0x007e:
            r9 = move-exception
            r3 = r0
            goto L_0x0091
        L_0x0081:
            r9 = move-exception
            r3 = r0
            goto L_0x009a
        L_0x0084:
            r9 = move-exception
            r3 = r0
            goto L_0x00a0
        L_0x0087:
            r8 = move-exception
        L_0x0088:
            if (r0 == 0) goto L_0x008d
            r0.close()
        L_0x008d:
            throw r8
        L_0x008e:
            r9 = move-exception
            r2 = r0
            r3 = r2
        L_0x0091:
            if (r2 == 0) goto L_0x00a3
        L_0x0093:
            r2.close()
            goto L_0x00a3
        L_0x0097:
            r9 = move-exception
            r2 = r0
            r3 = r2
        L_0x009a:
            if (r2 == 0) goto L_0x00a3
            goto L_0x0093
        L_0x009d:
            r9 = move-exception
            r2 = r0
            r3 = r2
        L_0x00a0:
            if (r2 == 0) goto L_0x00a3
            goto L_0x0093
        L_0x00a3:
            if (r9 == 0) goto L_0x00be
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Error parsing : "
            r1.append(r2)
            android.content.pm.ServiceInfo r10 = r10.serviceInfo
            java.lang.String r10 = r10.packageName
            r1.append(r10)
            java.lang.String r10 = r1.toString()
            android.util.Slog.w(r8, r10, r9)
            return r0
        L_0x00be:
            if (r3 == 0) goto L_0x00e0
            r8 = 47
            int r8 = r3.indexOf(r8)
            if (r8 >= 0) goto L_0x00e0
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            android.content.pm.ServiceInfo r9 = r10.serviceInfo
            java.lang.String r9 = r9.packageName
            r8.append(r9)
            java.lang.String r9 = "/"
            r8.append(r9)
            r8.append(r3)
            java.lang.String r3 = r8.toString()
        L_0x00e0:
            if (r3 != 0) goto L_0x00e3
            goto L_0x00e7
        L_0x00e3:
            android.content.ComponentName r0 = android.content.ComponentName.unflattenFromString(r3)
        L_0x00e7:
            r1.componentName = r0
            return r1
        L_0x00ea:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.security.trustagent.TrustAgentManager.getSettingsComponent(android.content.pm.PackageManager, android.content.pm.ResolveInfo):com.android.settings.security.trustagent.TrustAgentManager$TrustAgentComponentInfo");
    }
}
