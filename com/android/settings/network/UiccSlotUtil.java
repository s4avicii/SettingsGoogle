package com.android.settings.network;

import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import android.telephony.UiccPortInfo;
import android.telephony.UiccSlotInfo;
import android.telephony.UiccSlotMapping;
import android.util.Log;
import com.android.settingslib.utils.ThreadUtils;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UiccSlotUtil {
    public static ImmutableList<UiccSlotInfo> getSlotInfos(TelephonyManager telephonyManager) {
        UiccSlotInfo[] uiccSlotsInfo = telephonyManager.getUiccSlotsInfo();
        if (uiccSlotsInfo == null) {
            return ImmutableList.m25of();
        }
        return ImmutableList.copyOf((E[]) uiccSlotsInfo);
    }

    public static synchronized void switchToRemovableSlot(int i, Context context) throws UiccSlotsException {
        synchronized (UiccSlotUtil.class) {
            switchToRemovableSlot(context, i, (SubscriptionInfo) null);
        }
    }

    public static synchronized void switchToRemovableSlot(Context context, int i, SubscriptionInfo subscriptionInfo) throws UiccSlotsException {
        synchronized (UiccSlotUtil.class) {
            if (!ThreadUtils.isMainThread()) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
                performSwitchToSlot(telephonyManager, prepareUiccSlotMappingsForRemovableSlot(telephonyManager.getSimSlotMapping(), getInactiveRemovableSlot(telephonyManager.getUiccSlotsInfo(), i), subscriptionInfo, telephonyManager.isMultiSimEnabled()), context);
            } else {
                throw new IllegalThreadStateException("Do not call switchToRemovableSlot on the main thread.");
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: java.util.Collection} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void switchToEuiccSlot(android.content.Context r9, int r10, int r11, android.telephony.SubscriptionInfo r12) throws com.android.settings.network.UiccSlotsException {
        /*
            java.lang.Class<com.android.settings.network.UiccSlotUtil> r0 = com.android.settings.network.UiccSlotUtil.class
            monitor-enter(r0)
            boolean r1 = com.android.settingslib.utils.ThreadUtils.isMainThread()     // Catch:{ all -> 0x00c3 }
            if (r1 != 0) goto L_0x00bb
            java.lang.Class<android.telephony.TelephonyManager> r1 = android.telephony.TelephonyManager.class
            java.lang.Object r1 = r9.getSystemService(r1)     // Catch:{ all -> 0x00c3 }
            android.telephony.TelephonyManager r1 = (android.telephony.TelephonyManager) r1     // Catch:{ all -> 0x00c3 }
            java.util.Collection r2 = r1.getSimSlotMapping()     // Catch:{ all -> 0x00c3 }
            java.lang.String r3 = "UiccSlotUtil"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c3 }
            r4.<init>()     // Catch:{ all -> 0x00c3 }
            java.lang.String r5 = "The SimSlotMapping: "
            r4.append(r5)     // Catch:{ all -> 0x00c3 }
            r4.append(r2)     // Catch:{ all -> 0x00c3 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x00c3 }
            android.util.Log.i(r3, r4)     // Catch:{ all -> 0x00c3 }
            boolean r3 = isTargetSlotActive(r2, r10, r11)     // Catch:{ all -> 0x00c3 }
            if (r3 == 0) goto L_0x003a
            java.lang.String r9 = "UiccSlotUtil"
            java.lang.String r10 = "The slot is active, then the sim can enable directly."
            android.util.Log.i(r9, r10)     // Catch:{ all -> 0x00c3 }
            monitor-exit(r0)
            return
        L_0x003a:
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ all -> 0x00c3 }
            r3.<init>()     // Catch:{ all -> 0x00c3 }
            boolean r4 = r1.isMultiSimEnabled()     // Catch:{ all -> 0x00c3 }
            r5 = 0
            if (r4 != 0) goto L_0x004f
            android.telephony.UiccSlotMapping r12 = new android.telephony.UiccSlotMapping     // Catch:{ all -> 0x00c3 }
            r12.<init>(r11, r10, r5)     // Catch:{ all -> 0x00c3 }
            r3.add(r12)     // Catch:{ all -> 0x00c3 }
            goto L_0x00a0
        L_0x004f:
            if (r12 == 0) goto L_0x0056
            int r3 = r12.getSimSlotIndex()     // Catch:{ all -> 0x00c3 }
            goto L_0x0057
        L_0x0056:
            r3 = r10
        L_0x0057:
            if (r12 == 0) goto L_0x005e
            int r12 = r12.getPortIndex()     // Catch:{ all -> 0x00c3 }
            goto L_0x005f
        L_0x005e:
            r12 = r5
        L_0x005f:
            java.lang.String r4 = "UiccSlotUtil"
            java.lang.String r6 = "Start to set SimSlotMapping from slot%d-port%d to slot%d-port%d"
            r7 = 4
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x00c3 }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r10)     // Catch:{ all -> 0x00c3 }
            r7[r5] = r8     // Catch:{ all -> 0x00c3 }
            r5 = 1
            java.lang.Integer r8 = java.lang.Integer.valueOf(r11)     // Catch:{ all -> 0x00c3 }
            r7[r5] = r8     // Catch:{ all -> 0x00c3 }
            r5 = 2
            java.lang.Integer r8 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x00c3 }
            r7[r5] = r8     // Catch:{ all -> 0x00c3 }
            r5 = 3
            java.lang.Integer r8 = java.lang.Integer.valueOf(r12)     // Catch:{ all -> 0x00c3 }
            r7[r5] = r8     // Catch:{ all -> 0x00c3 }
            java.lang.String r5 = java.lang.String.format(r6, r7)     // Catch:{ all -> 0x00c3 }
            android.util.Log.i(r4, r5)     // Catch:{ all -> 0x00c3 }
            java.util.stream.Stream r2 = r2.stream()     // Catch:{ all -> 0x00c3 }
            com.android.settings.network.UiccSlotUtil$$ExternalSyntheticLambda0 r4 = new com.android.settings.network.UiccSlotUtil$$ExternalSyntheticLambda0     // Catch:{ all -> 0x00c3 }
            r4.<init>(r3, r12, r11, r10)     // Catch:{ all -> 0x00c3 }
            java.util.stream.Stream r10 = r2.map(r4)     // Catch:{ all -> 0x00c3 }
            java.util.stream.Collector r11 = java.util.stream.Collectors.toList()     // Catch:{ all -> 0x00c3 }
            java.lang.Object r10 = r10.collect(r11)     // Catch:{ all -> 0x00c3 }
            r3 = r10
            java.util.Collection r3 = (java.util.Collection) r3     // Catch:{ all -> 0x00c3 }
        L_0x00a0:
            java.lang.String r10 = "UiccSlotUtil"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c3 }
            r11.<init>()     // Catch:{ all -> 0x00c3 }
            java.lang.String r12 = "The SimSlotMapping: "
            r11.append(r12)     // Catch:{ all -> 0x00c3 }
            r11.append(r3)     // Catch:{ all -> 0x00c3 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x00c3 }
            android.util.Log.i(r10, r11)     // Catch:{ all -> 0x00c3 }
            performSwitchToSlot(r1, r3, r9)     // Catch:{ all -> 0x00c3 }
            monitor-exit(r0)
            return
        L_0x00bb:
            java.lang.IllegalThreadStateException r9 = new java.lang.IllegalThreadStateException     // Catch:{ all -> 0x00c3 }
            java.lang.String r10 = "Do not call switchToRemovableSlot on the main thread."
            r9.<init>(r10)     // Catch:{ all -> 0x00c3 }
            throw r9     // Catch:{ all -> 0x00c3 }
        L_0x00c3:
            r9 = move-exception
            monitor-exit(r0)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.UiccSlotUtil.switchToEuiccSlot(android.content.Context, int, int, android.telephony.SubscriptionInfo):void");
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ UiccSlotMapping lambda$switchToEuiccSlot$0(int i, int i2, int i3, int i4, UiccSlotMapping uiccSlotMapping) {
        return (uiccSlotMapping.getPhysicalSlotIndex() == i && uiccSlotMapping.getPortIndex() == i2) ? new UiccSlotMapping(i3, i4, uiccSlotMapping.getLogicalSlotIndex()) : uiccSlotMapping;
    }

    public static int getEsimSlotId(Context context) {
        ImmutableList<UiccSlotInfo> slotInfos = getSlotInfos((TelephonyManager) context.getSystemService(TelephonyManager.class));
        int orElse = IntStream.range(0, slotInfos.size()).filter(new UiccSlotUtil$$ExternalSyntheticLambda2(slotInfos)).findFirst().orElse(-1);
        Log.i("UiccSlotUtil", "firstEsimSlot: " + orElse);
        return orElse;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getEsimSlotId$1(ImmutableList immutableList, int i) {
        UiccSlotInfo uiccSlotInfo = (UiccSlotInfo) immutableList.get(i);
        if (uiccSlotInfo == null) {
            return false;
        }
        return !uiccSlotInfo.isRemovable();
    }

    private static boolean isTargetSlotActive(Collection<UiccSlotMapping> collection, int i, int i2) {
        return collection.stream().anyMatch(new UiccSlotUtil$$ExternalSyntheticLambda4(i, i2));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$isTargetSlotActive$2(int i, int i2, UiccSlotMapping uiccSlotMapping) {
        return uiccSlotMapping.getPhysicalSlotIndex() == i && uiccSlotMapping.getPortIndex() == i2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void performSwitchToSlot(android.telephony.TelephonyManager r5, java.util.Collection<android.telephony.UiccSlotMapping> r6, android.content.Context r7) throws com.android.settings.network.UiccSlotsException {
        /*
            android.content.ContentResolver r0 = r7.getContentResolver()
            java.lang.String r1 = "euicc_switch_slot_timeout_millis"
            r2 = 25000(0x61a8, double:1.23516E-319)
            long r0 = android.provider.Settings.Global.getLong(r0, r1, r2)
            r2 = 0
            java.util.concurrent.CountDownLatch r3 = new java.util.concurrent.CountDownLatch     // Catch:{ InterruptedException -> 0x002f }
            r4 = 1
            r3.<init>(r4)     // Catch:{ InterruptedException -> 0x002f }
            com.android.settings.network.CarrierConfigChangedReceiver r4 = new com.android.settings.network.CarrierConfigChangedReceiver     // Catch:{ InterruptedException -> 0x002f }
            r4.<init>(r3)     // Catch:{ InterruptedException -> 0x002f }
            r4.registerOn(r7)     // Catch:{ InterruptedException -> 0x002a, all -> 0x0027 }
            r5.setSimSlotMapping(r6)     // Catch:{ InterruptedException -> 0x002a, all -> 0x0027 }
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ InterruptedException -> 0x002a, all -> 0x0027 }
            r3.await(r0, r5)     // Catch:{ InterruptedException -> 0x002a, all -> 0x0027 }
            r7.unregisterReceiver(r4)
            goto L_0x0043
        L_0x0027:
            r5 = move-exception
            r2 = r4
            goto L_0x0044
        L_0x002a:
            r5 = move-exception
            r2 = r4
            goto L_0x0030
        L_0x002d:
            r5 = move-exception
            goto L_0x0044
        L_0x002f:
            r5 = move-exception
        L_0x0030:
            java.lang.Thread r6 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x002d }
            r6.interrupt()     // Catch:{ all -> 0x002d }
            java.lang.String r6 = "UiccSlotUtil"
            java.lang.String r0 = "Failed switching to physical slot."
            android.util.Log.e(r6, r0, r5)     // Catch:{ all -> 0x002d }
            if (r2 == 0) goto L_0x0043
            r7.unregisterReceiver(r2)
        L_0x0043:
            return
        L_0x0044:
            if (r2 == 0) goto L_0x0049
            r7.unregisterReceiver(r2)
        L_0x0049:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.UiccSlotUtil.performSwitchToSlot(android.telephony.TelephonyManager, java.util.Collection, android.content.Context):void");
    }

    private static int getInactiveRemovableSlot(UiccSlotInfo[] uiccSlotInfoArr, int i) throws UiccSlotsException {
        if (uiccSlotInfoArr != null) {
            if (i == -1) {
                for (int i2 = 0; i2 < uiccSlotInfoArr.length; i2++) {
                    if (uiccSlotInfoArr[i2].isRemovable() && !((UiccPortInfo) uiccSlotInfoArr[i2].getPorts().stream().findFirst().get()).isActive() && uiccSlotInfoArr[i2].getCardStateInfo() != 3 && uiccSlotInfoArr[i2].getCardStateInfo() != 4) {
                        return i2;
                    }
                }
            } else if (i >= uiccSlotInfoArr.length || !uiccSlotInfoArr[i].isRemovable()) {
                throw new UiccSlotsException("The given slotId is not a removable slot: " + i);
            } else if (!((UiccPortInfo) uiccSlotInfoArr[i].getPorts().stream().findFirst().get()).isActive()) {
                return i;
            }
            return -1;
        }
        throw new UiccSlotsException("UiccSlotInfo is null");
    }

    private static Collection<UiccSlotMapping> prepareUiccSlotMappingsForRemovableSlot(Collection<UiccSlotMapping> collection, int i, SubscriptionInfo subscriptionInfo, boolean z) {
        if (i != -1 && !collection.stream().anyMatch(new UiccSlotUtil$$ExternalSyntheticLambda3(i))) {
            ArrayList arrayList = new ArrayList();
            if (!z) {
                arrayList.add(new UiccSlotMapping(0, i, 0));
                collection = arrayList;
            } else if (subscriptionInfo != null) {
                Log.i("UiccSlotUtil", String.format("Start to set SimSlotMapping from slot%d-port%d to slot%d-port%d", new Object[]{Integer.valueOf(i), 0, Integer.valueOf(subscriptionInfo.getSimSlotIndex()), Integer.valueOf(subscriptionInfo.getPortIndex())}));
                collection = (Collection) collection.stream().map(new UiccSlotUtil$$ExternalSyntheticLambda1(subscriptionInfo, i)).collect(Collectors.toList());
            } else {
                Log.i("UiccSlotUtil", "The removedSubInfo is null");
            }
            Log.i("UiccSlotUtil", "The SimSlotMapping: " + collection);
        }
        return collection;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$prepareUiccSlotMappingsForRemovableSlot$3(int i, UiccSlotMapping uiccSlotMapping) {
        return uiccSlotMapping.getPhysicalSlotIndex() == i && uiccSlotMapping.getPortIndex() == 0;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ UiccSlotMapping lambda$prepareUiccSlotMappingsForRemovableSlot$4(SubscriptionInfo subscriptionInfo, int i, UiccSlotMapping uiccSlotMapping) {
        return (uiccSlotMapping.getPhysicalSlotIndex() == subscriptionInfo.getSimSlotIndex() && uiccSlotMapping.getPortIndex() == subscriptionInfo.getPortIndex()) ? new UiccSlotMapping(0, i, uiccSlotMapping.getLogicalSlotIndex()) : uiccSlotMapping;
    }
}
