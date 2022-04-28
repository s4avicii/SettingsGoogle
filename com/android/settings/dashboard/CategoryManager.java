package com.android.settings.dashboard;

import android.content.ComponentName;
import android.content.Context;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.settingslib.drawer.CategoryKey;
import com.android.settingslib.drawer.DashboardCategory;
import com.android.settingslib.drawer.ProviderTile;
import com.android.settingslib.drawer.Tile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CategoryManager {
    private static CategoryManager sInstance;
    private List<DashboardCategory> mCategories;
    private final Map<String, DashboardCategory> mCategoryByKeyMap = new ArrayMap();
    private final InterestingConfigChanges mInterestingConfigChanges;
    private final Map<Pair<String, String>, Tile> mTileByComponentCache = new ArrayMap();

    private void logTiles(Context context) {
    }

    public static CategoryManager get(Context context) {
        if (sInstance == null) {
            sInstance = new CategoryManager(context);
        }
        return sInstance;
    }

    CategoryManager(Context context) {
        InterestingConfigChanges interestingConfigChanges = new InterestingConfigChanges();
        this.mInterestingConfigChanges = interestingConfigChanges;
        interestingConfigChanges.applyNewConfig(context.getResources());
    }

    public synchronized DashboardCategory getTilesByCategory(Context context, String str) {
        tryInitCategories(context);
        return this.mCategoryByKeyMap.get(str);
    }

    public synchronized List<DashboardCategory> getCategories(Context context) {
        tryInitCategories(context);
        return this.mCategories;
    }

    public synchronized void reloadAllCategories(Context context) {
        boolean applyNewConfig = this.mInterestingConfigChanges.applyNewConfig(context.getResources());
        this.mCategories = null;
        tryInitCategories(context, applyNewConfig);
    }

    public synchronized void updateCategoryFromDenylist(Set<ComponentName> set) {
        if (this.mCategories == null) {
            Log.w("CategoryManager", "Category is null, skipping denylist update");
            return;
        }
        for (int i = 0; i < this.mCategories.size(); i++) {
            DashboardCategory dashboardCategory = this.mCategories.get(i);
            int i2 = 0;
            while (i2 < dashboardCategory.getTilesCount()) {
                if (set.contains(dashboardCategory.getTile(i2).getIntent().getComponent())) {
                    int i3 = i2 - 1;
                    dashboardCategory.removeTile(i2);
                    i2 = i3;
                }
                i2++;
            }
        }
    }

    public synchronized Map<ComponentName, Tile> getTileByComponentMap() {
        ArrayMap arrayMap = new ArrayMap();
        List<DashboardCategory> list = this.mCategories;
        if (list == null) {
            Log.w("CategoryManager", "Category is null, no tiles");
            return arrayMap;
        }
        list.forEach(new CategoryManager$$ExternalSyntheticLambda0(arrayMap));
        return arrayMap;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$getTileByComponentMap$0(Map map, DashboardCategory dashboardCategory) {
        for (int i = 0; i < dashboardCategory.getTilesCount(); i++) {
            Tile tile = dashboardCategory.getTile(i);
            map.put(tile.getIntent().getComponent(), tile);
        }
    }

    private synchronized void tryInitCategories(Context context) {
        tryInitCategories(context, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0099, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void tryInitCategories(android.content.Context r5, boolean r6) {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.List<com.android.settingslib.drawer.DashboardCategory> r0 = r4.mCategories     // Catch:{ all -> 0x009a }
            if (r0 != 0) goto L_0x0098
            java.util.Map<java.lang.String, com.android.settingslib.drawer.DashboardCategory> r0 = r4.mCategoryByKeyMap     // Catch:{ all -> 0x009a }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x009a }
            if (r6 == 0) goto L_0x0012
            java.util.Map<android.util.Pair<java.lang.String, java.lang.String>, com.android.settingslib.drawer.Tile> r6 = r4.mTileByComponentCache     // Catch:{ all -> 0x009a }
            r6.clear()     // Catch:{ all -> 0x009a }
        L_0x0012:
            java.util.Map<java.lang.String, com.android.settingslib.drawer.DashboardCategory> r6 = r4.mCategoryByKeyMap     // Catch:{ all -> 0x009a }
            r6.clear()     // Catch:{ all -> 0x009a }
            java.util.Map<android.util.Pair<java.lang.String, java.lang.String>, com.android.settingslib.drawer.Tile> r6 = r4.mTileByComponentCache     // Catch:{ all -> 0x009a }
            java.util.List r6 = com.android.settingslib.drawer.TileUtils.getCategories(r5, r6)     // Catch:{ all -> 0x009a }
            r4.mCategories = r6     // Catch:{ all -> 0x009a }
            java.util.Iterator r6 = r6.iterator()     // Catch:{ all -> 0x009a }
        L_0x0023:
            boolean r1 = r6.hasNext()     // Catch:{ all -> 0x009a }
            if (r1 == 0) goto L_0x0037
            java.lang.Object r1 = r6.next()     // Catch:{ all -> 0x009a }
            com.android.settingslib.drawer.DashboardCategory r1 = (com.android.settingslib.drawer.DashboardCategory) r1     // Catch:{ all -> 0x009a }
            java.util.Map<java.lang.String, com.android.settingslib.drawer.DashboardCategory> r2 = r4.mCategoryByKeyMap     // Catch:{ all -> 0x009a }
            java.lang.String r3 = r1.key     // Catch:{ all -> 0x009a }
            r2.put(r3, r1)     // Catch:{ all -> 0x009a }
            goto L_0x0023
        L_0x0037:
            java.util.Map<android.util.Pair<java.lang.String, java.lang.String>, com.android.settingslib.drawer.Tile> r6 = r4.mTileByComponentCache     // Catch:{ all -> 0x009a }
            java.util.Map<java.lang.String, com.android.settingslib.drawer.DashboardCategory> r1 = r4.mCategoryByKeyMap     // Catch:{ all -> 0x009a }
            r4.backwardCompatCleanupForCategory(r6, r1)     // Catch:{ all -> 0x009a }
            java.util.Map<java.lang.String, com.android.settingslib.drawer.DashboardCategory> r6 = r4.mCategoryByKeyMap     // Catch:{ all -> 0x009a }
            r4.sortCategories(r5, r6)     // Catch:{ all -> 0x009a }
            java.util.Map<java.lang.String, com.android.settingslib.drawer.DashboardCategory> r6 = r4.mCategoryByKeyMap     // Catch:{ all -> 0x009a }
            r4.filterDuplicateTiles(r6)     // Catch:{ all -> 0x009a }
            if (r0 == 0) goto L_0x0098
            r4.logTiles(r5)     // Catch:{ all -> 0x009a }
            java.util.Map<java.lang.String, com.android.settingslib.drawer.DashboardCategory> r6 = r4.mCategoryByKeyMap     // Catch:{ all -> 0x009a }
            java.lang.String r0 = "com.android.settings.category.ia.homepage"
            java.lang.Object r6 = r6.get(r0)     // Catch:{ all -> 0x009a }
            com.android.settingslib.drawer.DashboardCategory r6 = (com.android.settingslib.drawer.DashboardCategory) r6     // Catch:{ all -> 0x009a }
            if (r6 != 0) goto L_0x005b
            monitor-exit(r4)
            return
        L_0x005b:
            java.util.List r6 = r6.getTiles()     // Catch:{ all -> 0x009a }
            java.util.Iterator r6 = r6.iterator()     // Catch:{ all -> 0x009a }
        L_0x0063:
            boolean r0 = r6.hasNext()     // Catch:{ all -> 0x009a }
            if (r0 == 0) goto L_0x0098
            java.lang.Object r0 = r6.next()     // Catch:{ all -> 0x009a }
            com.android.settingslib.drawer.Tile r0 = (com.android.settingslib.drawer.Tile) r0     // Catch:{ all -> 0x009a }
            java.lang.String r1 = r0.getKey(r5)     // Catch:{ all -> 0x009a }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x009a }
            if (r2 == 0) goto L_0x0094
            java.lang.String r1 = "CategoryManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x009a }
            r2.<init>()     // Catch:{ all -> 0x009a }
            java.lang.String r3 = "Key hint missing for homepage tile: "
            r2.append(r3)     // Catch:{ all -> 0x009a }
            java.lang.CharSequence r0 = r0.getTitle(r5)     // Catch:{ all -> 0x009a }
            r2.append(r0)     // Catch:{ all -> 0x009a }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x009a }
            android.util.Log.w(r1, r0)     // Catch:{ all -> 0x009a }
            goto L_0x0063
        L_0x0094:
            com.android.settings.homepage.HighlightableMenu.addMenuKey(r1)     // Catch:{ all -> 0x009a }
            goto L_0x0063
        L_0x0098:
            monitor-exit(r4)
            return
        L_0x009a:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.dashboard.CategoryManager.tryInitCategories(android.content.Context, boolean):void");
    }

    /* access modifiers changed from: package-private */
    public synchronized void backwardCompatCleanupForCategory(Map<Pair<String, String>, Tile> map, Map<String, DashboardCategory> map2) {
        HashMap hashMap = new HashMap();
        for (Map.Entry next : map.entrySet()) {
            String str = (String) ((Pair) next.getKey()).first;
            List list = (List) hashMap.get(str);
            if (list == null) {
                list = new ArrayList();
                hashMap.put(str, list);
            }
            list.add((Tile) next.getValue());
        }
        for (Map.Entry value : hashMap.entrySet()) {
            List<Tile> list2 = (List) value.getValue();
            Iterator it = list2.iterator();
            boolean z = true;
            boolean z2 = false;
            while (true) {
                if (it.hasNext()) {
                    if (!CategoryKey.KEY_COMPAT_MAP.containsKey(((Tile) it.next()).getCategory())) {
                        break;
                    }
                    z2 = true;
                } else {
                    z = false;
                    break;
                }
            }
            if (z2 && !z) {
                for (Tile tile : list2) {
                    String str2 = CategoryKey.KEY_COMPAT_MAP.get(tile.getCategory());
                    tile.setCategory(str2);
                    DashboardCategory dashboardCategory = map2.get(str2);
                    if (dashboardCategory == null) {
                        dashboardCategory = new DashboardCategory(str2);
                        map2.put(str2, dashboardCategory);
                    }
                    dashboardCategory.addTile(tile);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void sortCategories(Context context, Map<String, DashboardCategory> map) {
        for (Map.Entry<String, DashboardCategory> value : map.entrySet()) {
            ((DashboardCategory) value.getValue()).sortTiles(context.getPackageName());
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void filterDuplicateTiles(Map<String, DashboardCategory> map) {
        for (Map.Entry<String, DashboardCategory> value : map.entrySet()) {
            DashboardCategory dashboardCategory = (DashboardCategory) value.getValue();
            int tilesCount = dashboardCategory.getTilesCount();
            ArraySet arraySet = new ArraySet();
            ArraySet arraySet2 = new ArraySet();
            for (int i = tilesCount - 1; i >= 0; i--) {
                Tile tile = dashboardCategory.getTile(i);
                if (tile instanceof ProviderTile) {
                    String description = tile.getDescription();
                    if (arraySet.contains(description)) {
                        dashboardCategory.removeTile(i);
                    } else {
                        arraySet.add(description);
                    }
                } else {
                    ComponentName component = tile.getIntent().getComponent();
                    if (arraySet2.contains(component)) {
                        dashboardCategory.removeTile(i);
                    } else {
                        arraySet2.add(component);
                    }
                }
            }
        }
    }
}
