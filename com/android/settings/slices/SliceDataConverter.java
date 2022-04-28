package com.android.settings.slices;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.provider.SearchIndexableResource;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import androidx.window.C0444R;
import com.android.settings.accessibility.AccessibilitySettings;
import com.android.settings.accessibility.AccessibilitySlicePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceData;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.search.Indexable$SearchIndexProvider;
import com.android.settingslib.search.SearchIndexableData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

class SliceDataConverter {
    private Context mContext;
    private final MetricsFeatureProvider mMetricsFeatureProvider;

    public SliceDataConverter(Context context) {
        this.mContext = context;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public List<SliceData> getSliceData() {
        ArrayList arrayList = new ArrayList();
        for (SearchIndexableData next : FeatureFactory.getFactory(this.mContext).getSearchFeatureProvider().getSearchIndexableResources().getProviderValues()) {
            String name = next.getTargetClass().getName();
            Indexable$SearchIndexProvider searchIndexProvider = next.getSearchIndexProvider();
            if (searchIndexProvider == null) {
                Log.e("SliceDataConverter", name + " dose not implement Search Index Provider");
            } else {
                arrayList.addAll(getSliceDataFromProvider(searchIndexProvider, name));
            }
        }
        arrayList.addAll(getAccessibilitySliceData());
        return arrayList;
    }

    private List<SliceData> getSliceDataFromProvider(Indexable$SearchIndexProvider indexable$SearchIndexProvider, String str) {
        ArrayList arrayList = new ArrayList();
        List<SearchIndexableResource> xmlResourcesToIndex = indexable$SearchIndexProvider.getXmlResourcesToIndex(this.mContext, true);
        if (xmlResourcesToIndex == null) {
            return arrayList;
        }
        for (SearchIndexableResource searchIndexableResource : xmlResourcesToIndex) {
            int i = searchIndexableResource.xmlResId;
            if (i == 0) {
                Log.e("SliceDataConverter", str + " provides invalid XML (0) in search provider.");
            } else {
                arrayList.addAll(getSliceDataFromXML(i, str));
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0118, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        android.util.Log.w("SliceDataConverter", "Get slice data from XML failed ", r0);
        r16.mMetricsFeatureProvider.action(0, 1727, 0, r4 + "_" + "", 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x013e, code lost:
        if (0 != 0) goto L_0x017f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0141, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0156, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x015d, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x017d, code lost:
        if (0 != 0) goto L_0x017f;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0118 A[ExcHandler: Exception (r0v8 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:1:0x0010] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0156  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0185  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.android.settings.slices.SliceData> getSliceDataFromXML(int r17, java.lang.String r18) {
        /*
            r16 = this;
            r1 = r16
            r0 = r17
            r4 = r18
            java.lang.String r2 = "SliceDataConverter"
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            java.lang.String r3 = ""
            r5 = 0
            android.content.Context r7 = r1.mContext     // Catch:{ InvalidSliceDataException -> 0x015d, NotFoundException | IOException | XmlPullParserException -> 0x0141, Exception -> 0x0118 }
            android.content.res.Resources r7 = r7.getResources()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            android.content.res.XmlResourceParser r5 = r7.getXml(r0)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
        L_0x001a:
            int r7 = r5.next()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            r8 = 1
            if (r7 == r8) goto L_0x0025
            r8 = 2
            if (r7 == r8) goto L_0x0025
            goto L_0x001a
        L_0x0025:
            java.lang.String r7 = r5.getName()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.lang.String r8 = "PreferenceScreen"
            boolean r8 = r8.equals(r7)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            if (r8 == 0) goto L_0x00eb
            android.util.AttributeSet r7 = android.util.Xml.asAttributeSet(r5)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            android.content.Context r8 = r1.mContext     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.lang.String r7 = com.android.settings.core.PreferenceXmlParserUtils.getDataTitle(r8, r7)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            android.content.Context r8 = r1.mContext     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            r9 = 2174(0x87e, float:3.046E-42)
            java.util.List r0 = com.android.settings.core.PreferenceXmlParserUtils.extractMetadata(r8, r0, r9)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
        L_0x0047:
            boolean r8 = r0.hasNext()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            if (r8 == 0) goto L_0x017f
            java.lang.Object r8 = r0.next()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            android.os.Bundle r8 = (android.os.Bundle) r8     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.lang.String r9 = "controller"
            java.lang.String r3 = r8.getString(r9)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            boolean r9 = android.text.TextUtils.isEmpty(r3)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            if (r9 == 0) goto L_0x0063
        L_0x005f:
            r17 = r0
            goto L_0x00e5
        L_0x0063:
            java.lang.String r9 = "key"
            java.lang.String r9 = r8.getString(r9)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            android.content.Context r10 = r1.mContext     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.core.BasePreferenceController r10 = com.android.settings.slices.SliceBuilderUtils.getPreferenceController(r10, r3, r9)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            boolean r11 = r10.isSliceable()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            if (r11 == 0) goto L_0x005f
            boolean r11 = r10.isAvailable()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            if (r11 != 0) goto L_0x007c
            goto L_0x005f
        L_0x007c:
            java.lang.String r11 = "title"
            java.lang.String r11 = r8.getString(r11)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.lang.String r12 = "summary"
            java.lang.String r12 = r8.getString(r12)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.lang.String r13 = "icon"
            int r13 = r8.getInt(r13)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            int r14 = r10.getSliceType()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.lang.String r15 = "unavailable_slice_subtitle"
            java.lang.String r8 = r8.getString(r15)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            boolean r15 = r10.isPublicSlice()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            r17 = r0
            int r0 = r10.getSliceHighlightMenuRes()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = new com.android.settings.slices.SliceData$Builder     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            r1.<init>()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = r1.setKey(r9)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            android.net.Uri r9 = r10.getSliceUri()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = r1.setUri(r9)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = r1.setTitle(r11)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = r1.setSummary(r12)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = r1.setIcon(r13)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = r1.setScreenTitle(r7)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = r1.setPreferenceControllerClassName(r3)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = r1.setFragmentName(r4)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = r1.setSliceType(r14)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = r1.setUnavailableSliceSubtitle(r8)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r1 = r1.setIsPublicSlice(r15)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData$Builder r0 = r1.setHighlightMenuRes(r0)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            com.android.settings.slices.SliceData r0 = r0.build()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            r6.add(r0)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
        L_0x00e5:
            r1 = r16
            r0 = r17
            goto L_0x0047
        L_0x00eb:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            r1.<init>()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.lang.String r8 = "XML document must start with <PreferenceScreen> tag; found"
            r1.append(r8)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            r1.append(r7)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.lang.String r7 = " at "
            r1.append(r7)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.lang.String r7 = r5.getPositionDescription()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            r1.append(r7)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            java.lang.String r1 = r1.toString()     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            r0.<init>(r1)     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
            throw r0     // Catch:{ InvalidSliceDataException -> 0x0112, NotFoundException | IOException | XmlPullParserException -> 0x010e, Exception -> 0x0118 }
        L_0x010e:
            r0 = move-exception
            r1 = r16
            goto L_0x0142
        L_0x0112:
            r0 = move-exception
            r1 = r16
            goto L_0x015e
        L_0x0116:
            r0 = move-exception
            goto L_0x0183
        L_0x0118:
            r0 = move-exception
            java.lang.String r1 = "Get slice data from XML failed "
            android.util.Log.w(r2, r1, r0)     // Catch:{ all -> 0x0116 }
            r1 = r16
            com.android.settingslib.core.instrumentation.MetricsFeatureProvider r7 = r1.mMetricsFeatureProvider     // Catch:{ all -> 0x0116 }
            r8 = 0
            r9 = 1727(0x6bf, float:2.42E-42)
            r10 = 0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0116 }
            r0.<init>()     // Catch:{ all -> 0x0116 }
            r0.append(r4)     // Catch:{ all -> 0x0116 }
            java.lang.String r1 = "_"
            r0.append(r1)     // Catch:{ all -> 0x0116 }
            r0.append(r3)     // Catch:{ all -> 0x0116 }
            java.lang.String r11 = r0.toString()     // Catch:{ all -> 0x0116 }
            r12 = 1
            r7.action(r8, r9, r10, r11, r12)     // Catch:{ all -> 0x0116 }
            if (r5 == 0) goto L_0x0182
            goto L_0x017f
        L_0x0141:
            r0 = move-exception
        L_0x0142:
            r7 = r5
            java.lang.String r3 = "Error parsing PreferenceScreen: "
            android.util.Log.w(r2, r3, r0)     // Catch:{ all -> 0x015a }
            com.android.settingslib.core.instrumentation.MetricsFeatureProvider r0 = r1.mMetricsFeatureProvider     // Catch:{ all -> 0x015a }
            r1 = 0
            r2 = 1726(0x6be, float:2.419E-42)
            r3 = 0
            r5 = 1
            r4 = r18
            r0.action(r1, r2, r3, r4, r5)     // Catch:{ all -> 0x015a }
            if (r7 == 0) goto L_0x0182
            r7.close()
            goto L_0x0182
        L_0x015a:
            r0 = move-exception
            r5 = r7
            goto L_0x0183
        L_0x015d:
            r0 = move-exception
        L_0x015e:
            r12 = r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0116 }
            r3.<init>()     // Catch:{ all -> 0x0116 }
            java.lang.String r7 = "Invalid data when building SliceData for "
            r3.append(r7)     // Catch:{ all -> 0x0116 }
            r3.append(r4)     // Catch:{ all -> 0x0116 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0116 }
            android.util.Log.w(r2, r3, r0)     // Catch:{ all -> 0x0116 }
            com.android.settingslib.core.instrumentation.MetricsFeatureProvider r8 = r1.mMetricsFeatureProvider     // Catch:{ all -> 0x0116 }
            r9 = 0
            r10 = 1725(0x6bd, float:2.417E-42)
            r11 = 0
            r13 = 1
            r8.action(r9, r10, r11, r12, r13)     // Catch:{ all -> 0x0116 }
            if (r5 == 0) goto L_0x0182
        L_0x017f:
            r5.close()
        L_0x0182:
            return r6
        L_0x0183:
            if (r5 == 0) goto L_0x0188
            r5.close()
        L_0x0188:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.slices.SliceDataConverter.getSliceDataFromXML(int, java.lang.String):java.util.List");
    }

    private List<SliceData> getAccessibilitySliceData() {
        ArrayList arrayList = new ArrayList();
        String name = AccessibilitySlicePreferenceController.class.getName();
        String name2 = AccessibilitySettings.class.getName();
        SliceData.Builder preferenceControllerClassName = new SliceData.Builder().setFragmentName(name2).setScreenTitle(this.mContext.getText(C0444R.string.accessibility_settings)).setPreferenceControllerClassName(name);
        HashSet hashSet = new HashSet();
        Collections.addAll(hashSet, this.mContext.getResources().getStringArray(C0444R.array.config_settings_slices_accessibility_components));
        List<AccessibilityServiceInfo> accessibilityServiceInfoList = getAccessibilityServiceInfoList();
        PackageManager packageManager = this.mContext.getPackageManager();
        for (AccessibilityServiceInfo resolveInfo : accessibilityServiceInfoList) {
            ResolveInfo resolveInfo2 = resolveInfo.getResolveInfo();
            ServiceInfo serviceInfo = resolveInfo2.serviceInfo;
            String flattenToString = new ComponentName(serviceInfo.packageName, serviceInfo.name).flattenToString();
            if (hashSet.contains(flattenToString)) {
                String charSequence = resolveInfo2.loadLabel(packageManager).toString();
                int iconResource = resolveInfo2.getIconResource();
                if (iconResource == 0) {
                    iconResource = C0444R.C0447drawable.ic_accessibility_generic;
                }
                preferenceControllerClassName.setKey(flattenToString).setTitle(charSequence).setUri(new Uri.Builder().scheme("content").authority("com.android.settings.slices").appendPath("action").appendPath(flattenToString).build()).setIcon(iconResource).setSliceType(1);
                try {
                    arrayList.add(preferenceControllerClassName.build());
                } catch (SliceData.InvalidSliceDataException e) {
                    Log.w("SliceDataConverter", "Invalid data when building a11y SliceData for " + flattenToString, e);
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public List<AccessibilityServiceInfo> getAccessibilityServiceInfoList() {
        return AccessibilityManager.getInstance(this.mContext).getInstalledAccessibilityServiceList();
    }
}
