package com.android.settings.homepage;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.settings.core.PreferenceXmlParserUtils;
import java.io.IOException;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

public class HighlightableMenu {
    private static final Map<String, Integer> MENU_KEY_COMPAT_MAP = new ArrayMap();
    private static final Map<String, String> MENU_TO_PREFERENCE_KEY_MAP = new ArrayMap();
    private static boolean sXmlParsed;

    public static synchronized void fromXml(Context context, int i) {
        synchronized (HighlightableMenu.class) {
            if (!sXmlParsed) {
                Log.d("HighlightableMenu", "parsing highlightable menu from xml");
                try {
                    for (Bundle next : PreferenceXmlParserUtils.extractMetadata(context, i, 8194)) {
                        String string = next.getString("highlightable_menu_key");
                        if (!TextUtils.isEmpty(string)) {
                            String string2 = next.getString("key");
                            if (TextUtils.isEmpty(string2)) {
                                Log.w("HighlightableMenu", "Highlightable menu requires android:key but it's missing in xml: " + string);
                            } else {
                                MENU_TO_PREFERENCE_KEY_MAP.put(string, string2);
                            }
                        }
                    }
                    if (!MENU_TO_PREFERENCE_KEY_MAP.isEmpty()) {
                        sXmlParsed = true;
                        MENU_KEY_COMPAT_MAP.forEach(new HighlightableMenu$$ExternalSyntheticLambda0(context));
                    }
                } catch (IOException | XmlPullParserException e) {
                    Log.e("HighlightableMenu", "Failed to parse preference xml for getting highlightable menu keys", e);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$fromXml$0(Context context, String str, Integer num) {
        String lookupPreferenceKey = lookupPreferenceKey(context.getString(num.intValue()));
        if (lookupPreferenceKey != null) {
            MENU_TO_PREFERENCE_KEY_MAP.put(str, lookupPreferenceKey);
        }
    }

    public static synchronized void addMenuKey(String str) {
        synchronized (HighlightableMenu.class) {
            Log.d("HighlightableMenu", "add menu key: " + str);
            MENU_TO_PREFERENCE_KEY_MAP.put(str, str);
        }
    }

    public static String lookupPreferenceKey(String str) {
        return MENU_TO_PREFERENCE_KEY_MAP.get(str);
    }
}
