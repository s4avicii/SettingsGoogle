package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AccessibilitySettingsContentObserver extends ContentObserver {
    private final Map<Uri, String> mUriToKey = new HashMap(2);
    private final Map<List<String>, ContentObserverCallback> mUrisToCallback = new HashMap();

    public interface ContentObserverCallback {
        void onChange(String str);
    }

    AccessibilitySettingsContentObserver(Handler handler) {
        super(handler);
        addDefaultKeysToMap();
    }

    public void register(ContentResolver contentResolver) {
        for (Uri registerContentObserver : this.mUriToKey.keySet()) {
            contentResolver.registerContentObserver(registerContentObserver, false, this);
        }
    }

    public void unregister(ContentResolver contentResolver) {
        contentResolver.unregisterContentObserver(this);
    }

    private void addDefaultKeysToMap() {
        addKeyToMap("accessibility_enabled");
        addKeyToMap("enabled_accessibility_services");
    }

    private boolean isDefaultKey(String str) {
        return "accessibility_enabled".equals(str) || "enabled_accessibility_services".equals(str);
    }

    private void addKeyToMap(String str) {
        this.mUriToKey.put(Settings.Secure.getUriFor(str), str);
    }

    public void registerKeysToObserverCallback(List<String> list, ContentObserverCallback contentObserverCallback) {
        for (String addKeyToMap : list) {
            addKeyToMap(addKeyToMap);
        }
        this.mUrisToCallback.put(list, contentObserverCallback);
    }

    public void registerObserverCallback(ContentObserverCallback contentObserverCallback) {
        this.mUrisToCallback.put(Collections.emptyList(), contentObserverCallback);
    }

    public final void onChange(boolean z, Uri uri) {
        String str = this.mUriToKey.get(uri);
        if (str == null) {
            Log.w("AccessibilitySettingsContentObserver", "AccessibilitySettingsContentObserver can not find the key for uri: " + uri);
            return;
        }
        for (List next : this.mUrisToCallback.keySet()) {
            if (isDefaultKey(str) || next.contains(str)) {
                this.mUrisToCallback.get(next).onChange(str);
            }
        }
    }
}
