package com.android.settingslib.license;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class LicenseHtmlGeneratorFromXml {
    private final Map<String, String> mContentIdToFileContentMap = new HashMap();
    private final Map<String, Map<String, Set<String>>> mFileNameToLibraryToContentIdMap = new HashMap();
    private final List<File> mXmlFiles;

    static class ContentIdAndFileNames {
        final String mContentId;
        final Map<String, List<String>> mLibraryToFileNameMap = new TreeMap();

        ContentIdAndFileNames(String str) {
            this.mContentId = str;
        }
    }

    private LicenseHtmlGeneratorFromXml(List<File> list) {
        this.mXmlFiles = list;
    }

    public static boolean generateHtml(List<File> list, File file, String str) {
        return new LicenseHtmlGeneratorFromXml(list).generateHtml(file, str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0059  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean generateHtml(java.io.File r4, java.lang.String r5) {
        /*
            r3 = this;
            java.util.List<java.io.File> r0 = r3.mXmlFiles
            java.util.Iterator r0 = r0.iterator()
        L_0x0006:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0016
            java.lang.Object r1 = r0.next()
            java.io.File r1 = (java.io.File) r1
            r3.parse(r1)
            goto L_0x0006
        L_0x0016:
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> r0 = r3.mFileNameToLibraryToContentIdMap
            boolean r0 = r0.isEmpty()
            r1 = 0
            if (r0 != 0) goto L_0x005c
            java.util.Map<java.lang.String, java.lang.String> r0 = r3.mContentIdToFileContentMap
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x0028
            goto L_0x005c
        L_0x0028:
            r0 = 0
            java.io.PrintWriter r2 = new java.io.PrintWriter     // Catch:{ FileNotFoundException | SecurityException -> 0x0040 }
            r2.<init>(r4)     // Catch:{ FileNotFoundException | SecurityException -> 0x0040 }
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> r0 = r3.mFileNameToLibraryToContentIdMap     // Catch:{ FileNotFoundException | SecurityException -> 0x003d }
            java.util.Map<java.lang.String, java.lang.String> r3 = r3.mContentIdToFileContentMap     // Catch:{ FileNotFoundException | SecurityException -> 0x003d }
            generateHtml(r0, r3, r2, r5)     // Catch:{ FileNotFoundException | SecurityException -> 0x003d }
            r2.flush()     // Catch:{ FileNotFoundException | SecurityException -> 0x003d }
            r2.close()     // Catch:{ FileNotFoundException | SecurityException -> 0x003d }
            r3 = 1
            return r3
        L_0x003d:
            r3 = move-exception
            r0 = r2
            goto L_0x0041
        L_0x0040:
            r3 = move-exception
        L_0x0041:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r2 = "Failed to generate "
            r5.append(r2)
            r5.append(r4)
            java.lang.String r4 = r5.toString()
            java.lang.String r5 = "LicenseGeneratorFromXml"
            android.util.Log.e(r5, r4, r3)
            if (r0 == 0) goto L_0x005c
            r0.close()
        L_0x005c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.license.LicenseHtmlGeneratorFromXml.generateHtml(java.io.File, java.lang.String):boolean");
    }

    private void parse(File file) {
        InputStreamReader inputStreamReader;
        if (file != null && file.exists() && file.length() != 0) {
            InputStreamReader inputStreamReader2 = null;
            try {
                if (file.getName().endsWith(".gz")) {
                    inputStreamReader = new InputStreamReader(new GZIPInputStream(new FileInputStream(file)));
                } else {
                    inputStreamReader = new FileReader(file);
                }
                InputStreamReader inputStreamReader3 = inputStreamReader;
                parse(inputStreamReader3, this.mFileNameToLibraryToContentIdMap, this.mContentIdToFileContentMap);
                inputStreamReader3.close();
            } catch (IOException | XmlPullParserException e) {
                Log.e("LicenseGeneratorFromXml", "Failed to parse " + file, e);
                if (inputStreamReader2 != null) {
                    try {
                        inputStreamReader2.close();
                    } catch (IOException unused) {
                        Log.w("LicenseGeneratorFromXml", "Failed to close " + file);
                    }
                }
            }
        }
    }

    static void parse(InputStreamReader inputStreamReader, Map<String, Map<String, Set<String>>> map, Map<String, String> map2) throws XmlPullParserException, IOException {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        XmlPullParser newPullParser = Xml.newPullParser();
        newPullParser.setInput(inputStreamReader);
        newPullParser.nextTag();
        newPullParser.require(2, "", "licenses");
        for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
            if (eventType == 2) {
                if ("file-name".equals(newPullParser.getName())) {
                    String attributeValue = newPullParser.getAttributeValue("", "contentId");
                    String attributeValue2 = newPullParser.getAttributeValue("", "lib");
                    if (!TextUtils.isEmpty(attributeValue)) {
                        String trim = readText(newPullParser).trim();
                        if (!TextUtils.isEmpty(trim)) {
                            ((Set) ((Map) hashMap.computeIfAbsent(trim, LicenseHtmlGeneratorFromXml$$ExternalSyntheticLambda5.INSTANCE)).computeIfAbsent(attributeValue2, LicenseHtmlGeneratorFromXml$$ExternalSyntheticLambda4.INSTANCE)).add(attributeValue);
                        }
                    }
                } else if ("file-content".equals(newPullParser.getName())) {
                    String attributeValue3 = newPullParser.getAttributeValue("", "contentId");
                    if (!TextUtils.isEmpty(attributeValue3) && !map2.containsKey(attributeValue3) && !hashMap2.containsKey(attributeValue3)) {
                        String readText = readText(newPullParser);
                        if (!TextUtils.isEmpty(readText)) {
                            hashMap2.put(attributeValue3, readText);
                        }
                    }
                }
            }
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            map.merge((String) entry.getKey(), (Map) entry.getValue(), LicenseHtmlGeneratorFromXml$$ExternalSyntheticLambda0.INSTANCE);
        }
        map2.putAll(hashMap2);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Map lambda$parse$0(String str) {
        return new HashMap();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Set lambda$parse$1(String str) {
        return new HashSet();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Map lambda$parse$3(Map map, Map map2) {
        for (Map.Entry entry : map2.entrySet()) {
            map.merge((String) entry.getKey(), (Set) entry.getValue(), LicenseHtmlGeneratorFromXml$$ExternalSyntheticLambda2.INSTANCE);
        }
        return map;
    }

    private static String readText(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        StringBuffer stringBuffer = new StringBuffer();
        int next = xmlPullParser.next();
        while (next == 4) {
            stringBuffer.append(xmlPullParser.getText());
            next = xmlPullParser.next();
        }
        return stringBuffer.toString();
    }

    static void generateHtml(Map<String, Map<String, Set<String>>> map, Map<String, String> map2, PrintWriter printWriter, String str) {
        int i;
        PrintWriter printWriter2 = printWriter;
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(map.keySet());
        Collections.sort(arrayList);
        TreeMap treeMap = new TreeMap();
        for (Map<String, Set<String>> entrySet : map.values()) {
            for (Map.Entry entry : entrySet.entrySet()) {
                if (!TextUtils.isEmpty((CharSequence) entry.getKey())) {
                    treeMap.merge((String) entry.getKey(), (Set) entry.getValue(), LicenseHtmlGeneratorFromXml$$ExternalSyntheticLambda1.INSTANCE);
                }
            }
        }
        printWriter2.println("<html><head>\n<style type=\"text/css\">\nbody { padding: 0; font-family: sans-serif; }\n.same-license { background-color: #eeeeee;\n                border-top: 20px solid white;\n                padding: 10px; }\n.label { font-weight: bold; }\n.file-list { margin-left: 1em; color: blue; }\n</style>\n</head><body topmargin=\"0\" leftmargin=\"0\" rightmargin=\"0\" bottommargin=\"0\">\n<div class=\"toc\">\n");
        if (!TextUtils.isEmpty(str)) {
            printWriter.println(str);
        }
        HashMap hashMap = new HashMap();
        ArrayList<ContentIdAndFileNames> arrayList2 = new ArrayList<>();
        if (!treeMap.isEmpty()) {
            printWriter2.println("<strong>Libraries</strong>\n<ul class=\"libraries\">");
            i = 0;
            for (Map.Entry entry2 : treeMap.entrySet()) {
                String str2 = (String) entry2.getKey();
                for (String str3 : (Set) entry2.getValue()) {
                    if (!hashMap.containsKey(str3)) {
                        hashMap.put(str3, Integer.valueOf(i));
                        arrayList2.add(new ContentIdAndFileNames(str3));
                        i++;
                    }
                    printWriter2.format("<li><a href=\"#id%d\">%s</a></li>\n", new Object[]{Integer.valueOf(((Integer) hashMap.get(str3)).intValue()), str2});
                }
            }
            printWriter2.println("</ul>\n<strong>Files</strong>");
        } else {
            i = 0;
        }
        for (String str4 : arrayList) {
            for (Map.Entry entry3 : map.get(str4).entrySet()) {
                String str5 = (String) entry3.getKey();
                if (str5 == null) {
                    str5 = "";
                }
                for (String str6 : (Set) entry3.getValue()) {
                    if (!hashMap.containsKey(str6)) {
                        hashMap.put(str6, Integer.valueOf(i));
                        arrayList2.add(new ContentIdAndFileNames(str6));
                        i++;
                    }
                    int intValue = ((Integer) hashMap.get(str6)).intValue();
                    ((ContentIdAndFileNames) arrayList2.get(intValue)).mLibraryToFileNameMap.computeIfAbsent(str5, LicenseHtmlGeneratorFromXml$$ExternalSyntheticLambda3.INSTANCE).add(str4);
                    if (TextUtils.isEmpty(str5)) {
                        printWriter2.format("<li><a href=\"#id%d\">%s</a></li>\n", new Object[]{Integer.valueOf(intValue), str4});
                    } else {
                        printWriter2.format("<li><a href=\"#id%d\">%s - %s</a></li>\n", new Object[]{Integer.valueOf(intValue), str4, str5});
                    }
                }
            }
        }
        printWriter2.println("</ul>\n</div><!-- table of contents -->\n<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
        int i2 = 0;
        for (ContentIdAndFileNames contentIdAndFileNames : arrayList2) {
            printWriter2.format("<tr id=\"id%d\"><td class=\"same-license\">\n", new Object[]{Integer.valueOf(i2)});
            for (Map.Entry next : contentIdAndFileNames.mLibraryToFileNameMap.entrySet()) {
                String str7 = (String) next.getKey();
                if (TextUtils.isEmpty(str7)) {
                    printWriter2.println("<div class=\"label\">Notices for file(s):</div>");
                } else {
                    printWriter2.format("<div class=\"label\"><strong>%s</strong> used by:</div>\n", new Object[]{str7});
                }
                printWriter2.println("<div class=\"file-list\">");
                for (String str8 : (List) next.getValue()) {
                    printWriter2.format("%s <br/>\n", new Object[]{str8});
                }
                printWriter2.println("</div><!-- file-list -->");
                i2++;
            }
            printWriter2.println("<pre class=\"license-text\">");
            printWriter2.println(map2.get(contentIdAndFileNames.mContentId));
            printWriter2.println("</pre><!-- license-text -->");
            printWriter2.println("</td></tr><!-- same-license -->");
        }
        printWriter2.println("</table></body></html>");
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ List lambda$generateHtml$5(String str) {
        return new ArrayList();
    }
}
