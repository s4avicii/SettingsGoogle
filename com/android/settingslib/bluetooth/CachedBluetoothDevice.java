package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.LruCache;
import android.util.Pair;
import com.android.internal.util.ArrayUtils;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.AdaptiveOutlineDrawable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class CachedBluetoothDevice implements Comparable<CachedBluetoothDevice> {
    private final Collection<Callback> mCallbacks = new CopyOnWriteArrayList();
    private long mConnectAttempted;
    private final Context mContext;
    BluetoothDevice mDevice;
    LruCache<String, BitmapDrawable> mDrawableCache;
    private int mGroupId;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                CachedBluetoothDevice.this.mIsHeadsetProfileConnectedFail = true;
            } else if (i == 2) {
                CachedBluetoothDevice.this.mIsA2dpProfileConnectedFail = true;
            } else if (i == 21) {
                CachedBluetoothDevice.this.mIsHearingAidProfileConnectedFail = true;
            } else if (i != 22) {
                Log.w("CachedBluetoothDevice", "handleMessage(): unknown message : " + message.what);
            } else {
                CachedBluetoothDevice.this.mIsLeAudioProfileConnectedFail = true;
            }
            Log.w("CachedBluetoothDevice", "Connect to profile : " + message.what + " timeout, show error message !");
            CachedBluetoothDevice.this.refresh();
        }
    };
    private long mHiSyncId;
    /* access modifiers changed from: private */
    public boolean mIsA2dpProfileConnectedFail = false;
    private boolean mIsActiveDeviceA2dp = false;
    private boolean mIsActiveDeviceHeadset = false;
    private boolean mIsActiveDeviceHearingAid = false;
    private boolean mIsActiveDeviceLeAudio = false;
    boolean mIsCoordinatedSetMember = false;
    /* access modifiers changed from: private */
    public boolean mIsHeadsetProfileConnectedFail = false;
    /* access modifiers changed from: private */
    public boolean mIsHearingAidProfileConnectedFail = false;
    /* access modifiers changed from: private */
    public boolean mIsLeAudioProfileConnectedFail = false;
    boolean mJustDiscovered;
    private final BluetoothAdapter mLocalAdapter;
    private boolean mLocalNapRoleConnected;
    private Set<CachedBluetoothDevice> mMemberDevices = new HashSet();
    private final Object mProfileLock = new Object();
    private final LocalBluetoothProfileManager mProfileManager;
    private final Collection<LocalBluetoothProfile> mProfiles = new CopyOnWriteArrayList();
    private final Collection<LocalBluetoothProfile> mRemovedProfiles = new CopyOnWriteArrayList();
    short mRssi;
    private CachedBluetoothDevice mSubDevice;
    private boolean mUnpairing;

    public interface Callback {
        void onDeviceAttributesChanged();
    }

    private boolean isTwsBatteryAvailable(int i, int i2) {
        return i >= 0 && i2 >= 0;
    }

    CachedBluetoothDevice(Context context, LocalBluetoothProfileManager localBluetoothProfileManager, BluetoothDevice bluetoothDevice) {
        this.mContext = context;
        this.mLocalAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mProfileManager = localBluetoothProfileManager;
        this.mDevice = bluetoothDevice;
        fillData();
        this.mHiSyncId = 0;
        this.mGroupId = -1;
        initDrawableCache();
        this.mUnpairing = false;
    }

    private void initDrawableCache() {
        this.mDrawableCache = new LruCache<String, BitmapDrawable>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8) {
            /* access modifiers changed from: protected */
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap().getByteCount() / 1024;
            }
        };
    }

    private String describe(LocalBluetoothProfile localBluetoothProfile) {
        StringBuilder sb = new StringBuilder();
        sb.append("Address:");
        sb.append(this.mDevice);
        if (localBluetoothProfile != null) {
            sb.append(" Profile:");
            sb.append(localBluetoothProfile);
        }
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public void onProfileStateChanged(LocalBluetoothProfile localBluetoothProfile, int i) {
        Log.d("CachedBluetoothDevice", "onProfileStateChanged: profile " + localBluetoothProfile + ", device " + this.mDevice.getAnonymizedAddress() + ", newProfileState " + i);
        if (this.mLocalAdapter.getState() == 13) {
            Log.d("CachedBluetoothDevice", " BT Turninig Off...Profile conn state change ignored...");
            return;
        }
        synchronized (this.mProfileLock) {
            if ((localBluetoothProfile instanceof A2dpProfile) || (localBluetoothProfile instanceof HeadsetProfile) || (localBluetoothProfile instanceof HearingAidProfile)) {
                setProfileConnectedStatus(localBluetoothProfile.getProfileId(), false);
                if (i != 0) {
                    if (i == 1) {
                        this.mHandler.sendEmptyMessageDelayed(localBluetoothProfile.getProfileId(), 60000);
                    } else if (i == 2) {
                        this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    } else if (i != 3) {
                        Log.w("CachedBluetoothDevice", "onProfileStateChanged(): unknown profile state : " + i);
                    } else if (this.mHandler.hasMessages(localBluetoothProfile.getProfileId())) {
                        this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    }
                } else if (this.mHandler.hasMessages(localBluetoothProfile.getProfileId())) {
                    this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    setProfileConnectedStatus(localBluetoothProfile.getProfileId(), true);
                }
            }
            if (i == 2) {
                if (localBluetoothProfile instanceof MapProfile) {
                    localBluetoothProfile.setEnabled(this.mDevice, true);
                }
                if (!this.mProfiles.contains(localBluetoothProfile)) {
                    this.mRemovedProfiles.remove(localBluetoothProfile);
                    this.mProfiles.add(localBluetoothProfile);
                    if ((localBluetoothProfile instanceof PanProfile) && ((PanProfile) localBluetoothProfile).isLocalRoleNap(this.mDevice)) {
                        this.mLocalNapRoleConnected = true;
                    }
                }
            } else if ((localBluetoothProfile instanceof MapProfile) && i == 0) {
                localBluetoothProfile.setEnabled(this.mDevice, false);
            } else if (this.mLocalNapRoleConnected && (localBluetoothProfile instanceof PanProfile) && ((PanProfile) localBluetoothProfile).isLocalRoleNap(this.mDevice) && i == 0) {
                Log.d("CachedBluetoothDevice", "Removing PanProfile from device after NAP disconnect");
                this.mProfiles.remove(localBluetoothProfile);
                this.mRemovedProfiles.add(localBluetoothProfile);
                this.mLocalNapRoleConnected = false;
            }
        }
        fetchActiveDevices();
    }

    /* access modifiers changed from: package-private */
    public void setProfileConnectedStatus(int i, boolean z) {
        if (i == 1) {
            this.mIsHeadsetProfileConnectedFail = z;
        } else if (i == 2) {
            this.mIsA2dpProfileConnectedFail = z;
        } else if (i == 21) {
            this.mIsHearingAidProfileConnectedFail = z;
        } else if (i != 22) {
            Log.w("CachedBluetoothDevice", "setProfileConnectedStatus(): unknown profile id : " + i);
        } else {
            this.mIsLeAudioProfileConnectedFail = z;
        }
    }

    public void disconnect() {
        synchronized (this.mProfileLock) {
            this.mDevice.disconnect();
        }
        PbapServerProfile pbapProfile = this.mProfileManager.getPbapProfile();
        if (pbapProfile != null && isConnectedProfile(pbapProfile)) {
            pbapProfile.setEnabled(this.mDevice, false);
        }
    }

    public void connect() {
        if (ensurePaired()) {
            this.mConnectAttempted = SystemClock.elapsedRealtime();
            connectDevice();
        }
    }

    public long getHiSyncId() {
        return this.mHiSyncId;
    }

    public void setHiSyncId(long j) {
        this.mHiSyncId = j;
    }

    public boolean isHearingAidDevice() {
        return this.mHiSyncId != 0;
    }

    public void setIsCoordinatedSetMember(boolean z) {
        this.mIsCoordinatedSetMember = z;
    }

    public boolean isCoordinatedSetMemberDevice() {
        return this.mIsCoordinatedSetMember;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public void setGroupId(int i) {
        this.mGroupId = i;
    }

    private void connectDevice() {
        synchronized (this.mProfileLock) {
            if (this.mProfiles.isEmpty()) {
                Log.d("CachedBluetoothDevice", "No profiles. Maybe we will connect later for device " + this.mDevice);
                return;
            }
            this.mDevice.connect();
        }
    }

    private boolean ensurePaired() {
        if (getBondState() != 10) {
            return true;
        }
        startPairing();
        return false;
    }

    public boolean startPairing() {
        if (this.mLocalAdapter.isDiscovering()) {
            this.mLocalAdapter.cancelDiscovery();
        }
        return this.mDevice.createBond();
    }

    public void unpair() {
        BluetoothDevice bluetoothDevice;
        int bondState = getBondState();
        if (bondState == 11) {
            this.mDevice.cancelBondProcess();
        }
        if (bondState != 10 && (bluetoothDevice = this.mDevice) != null) {
            this.mUnpairing = true;
            if (bluetoothDevice.removeBond()) {
                releaseLruCache();
                Log.d("CachedBluetoothDevice", "Command sent successfully:REMOVE_BOND " + describe((LocalBluetoothProfile) null));
            }
        }
    }

    public int getProfileConnectionState(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile != null) {
            return localBluetoothProfile.getConnectionStatus(this.mDevice);
        }
        return 0;
    }

    private void fillData() {
        updateProfiles();
        fetchActiveDevices();
        migratePhonebookPermissionChoice();
        migrateMessagePermissionChoice();
        lambda$refresh$0();
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public String getAddress() {
        return this.mDevice.getAddress();
    }

    public String getIdentityAddress() {
        String identityAddress = this.mDevice.getIdentityAddress();
        return TextUtils.isEmpty(identityAddress) ? getAddress() : identityAddress;
    }

    public String getName() {
        String alias = this.mDevice.getAlias();
        return TextUtils.isEmpty(alias) ? getAddress() : alias;
    }

    public void setName(String str) {
        if (str != null && !TextUtils.equals(str, getName())) {
            this.mDevice.setAlias(str);
            lambda$refresh$0();
        }
    }

    public boolean setActive() {
        boolean z;
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        if (a2dpProfile == null || !isConnectedProfile(a2dpProfile) || !a2dpProfile.setActiveDevice(getDevice())) {
            z = false;
        } else {
            Log.i("CachedBluetoothDevice", "OnPreferenceClickListener: A2DP active device=" + this);
            z = true;
        }
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        if (headsetProfile != null && isConnectedProfile(headsetProfile) && headsetProfile.setActiveDevice(getDevice())) {
            Log.i("CachedBluetoothDevice", "OnPreferenceClickListener: Headset active device=" + this);
            z = true;
        }
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        if (hearingAidProfile != null && isConnectedProfile(hearingAidProfile) && hearingAidProfile.setActiveDevice(getDevice())) {
            Log.i("CachedBluetoothDevice", "OnPreferenceClickListener: Hearing Aid active device=" + this);
            z = true;
        }
        LeAudioProfile leAudioProfile = this.mProfileManager.getLeAudioProfile();
        if (leAudioProfile == null || !isConnectedProfile(leAudioProfile) || !leAudioProfile.setActiveDevice(getDevice())) {
            return z;
        }
        Log.i("CachedBluetoothDevice", "OnPreferenceClickListener: LeAudio active device=" + this);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void refreshName() {
        Log.d("CachedBluetoothDevice", "Device name: " + getName());
        lambda$refresh$0();
    }

    public boolean hasHumanReadableName() {
        return !TextUtils.isEmpty(this.mDevice.getAlias());
    }

    public int getBatteryLevel() {
        return this.mDevice.getBatteryLevel();
    }

    /* access modifiers changed from: package-private */
    public void refresh() {
        ThreadUtils.postOnBackgroundThread((Runnable) new CachedBluetoothDevice$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refresh$1() {
        Uri uriMetaData;
        if (BluetoothUtils.isAdvancedDetailsHeader(this.mDevice) && (uriMetaData = BluetoothUtils.getUriMetaData(getDevice(), 5)) != null && this.mDrawableCache.get(uriMetaData.toString()) == null) {
            this.mDrawableCache.put(uriMetaData.toString(), (BitmapDrawable) BluetoothUtils.getBtDrawableWithDescription(this.mContext, this).first);
        }
        ThreadUtils.postOnMainThread(new CachedBluetoothDevice$$ExternalSyntheticLambda1(this));
    }

    public void setJustDiscovered(boolean z) {
        if (this.mJustDiscovered != z) {
            this.mJustDiscovered = z;
            lambda$refresh$0();
        }
    }

    public int getBondState() {
        return this.mDevice.getBondState();
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActiveDeviceChanged(boolean r4, int r5) {
        /*
            r3 = this;
            r0 = 1
            r1 = 0
            if (r5 == r0) goto L_0x0049
            r2 = 2
            if (r5 == r2) goto L_0x0040
            r2 = 21
            if (r5 == r2) goto L_0x0037
            r2 = 22
            if (r5 == r2) goto L_0x002e
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "onActiveDeviceChanged: unknown profile "
            r0.append(r2)
            r0.append(r5)
            java.lang.String r5 = " isActive "
            r0.append(r5)
            r0.append(r4)
            java.lang.String r4 = r0.toString()
            java.lang.String r5 = "CachedBluetoothDevice"
            android.util.Log.w(r5, r4)
            goto L_0x0052
        L_0x002e:
            boolean r5 = r3.mIsActiveDeviceLeAudio
            if (r5 == r4) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = r1
        L_0x0034:
            r3.mIsActiveDeviceLeAudio = r4
            goto L_0x0051
        L_0x0037:
            boolean r5 = r3.mIsActiveDeviceHearingAid
            if (r5 == r4) goto L_0x003c
            goto L_0x003d
        L_0x003c:
            r0 = r1
        L_0x003d:
            r3.mIsActiveDeviceHearingAid = r4
            goto L_0x0051
        L_0x0040:
            boolean r5 = r3.mIsActiveDeviceA2dp
            if (r5 == r4) goto L_0x0045
            goto L_0x0046
        L_0x0045:
            r0 = r1
        L_0x0046:
            r3.mIsActiveDeviceA2dp = r4
            goto L_0x0051
        L_0x0049:
            boolean r5 = r3.mIsActiveDeviceHeadset
            if (r5 == r4) goto L_0x004e
            goto L_0x004f
        L_0x004e:
            r0 = r1
        L_0x004f:
            r3.mIsActiveDeviceHeadset = r4
        L_0x0051:
            r1 = r0
        L_0x0052:
            if (r1 == 0) goto L_0x0057
            r3.lambda$refresh$0()
        L_0x0057:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.onActiveDeviceChanged(boolean, int):void");
    }

    /* access modifiers changed from: package-private */
    public void onAudioModeChanged() {
        lambda$refresh$0();
    }

    public boolean isActiveDevice(int i) {
        if (i == 1) {
            return this.mIsActiveDeviceHeadset;
        }
        if (i == 2) {
            return this.mIsActiveDeviceA2dp;
        }
        if (i == 21) {
            return this.mIsActiveDeviceHearingAid;
        }
        if (i == 22) {
            return this.mIsActiveDeviceLeAudio;
        }
        Log.w("CachedBluetoothDevice", "getActiveDevice: unknown profile " + i);
        return false;
    }

    /* access modifiers changed from: package-private */
    public void setRssi(short s) {
        if (this.mRssi != s) {
            this.mRssi = s;
            lambda$refresh$0();
        }
    }

    public boolean isConnected() {
        synchronized (this.mProfileLock) {
            for (LocalBluetoothProfile profileConnectionState : this.mProfiles) {
                if (getProfileConnectionState(profileConnectionState) == 2) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isConnectedProfile(LocalBluetoothProfile localBluetoothProfile) {
        return getProfileConnectionState(localBluetoothProfile) == 2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0020, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002c, code lost:
        return r3;
     */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0021 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0010  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isBusy() {
        /*
            r5 = this;
            java.lang.Object r0 = r5.mProfileLock
            monitor-enter(r0)
            java.util.Collection<com.android.settingslib.bluetooth.LocalBluetoothProfile> r1 = r5.mProfiles     // Catch:{ all -> 0x002d }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x002d }
        L_0x0009:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x002d }
            r3 = 1
            if (r2 == 0) goto L_0x0021
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x002d }
            com.android.settingslib.bluetooth.LocalBluetoothProfile r2 = (com.android.settingslib.bluetooth.LocalBluetoothProfile) r2     // Catch:{ all -> 0x002d }
            int r2 = r5.getProfileConnectionState(r2)     // Catch:{ all -> 0x002d }
            if (r2 == r3) goto L_0x001f
            r4 = 3
            if (r2 != r4) goto L_0x0009
        L_0x001f:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r3
        L_0x0021:
            int r5 = r5.getBondState()     // Catch:{ all -> 0x002d }
            r1 = 11
            if (r5 != r1) goto L_0x002a
            goto L_0x002b
        L_0x002a:
            r3 = 0
        L_0x002b:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r3
        L_0x002d:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.isBusy():boolean");
    }

    private boolean updateProfiles() {
        ParcelUuid[] uuids;
        ParcelUuid[] uuids2 = this.mDevice.getUuids();
        if (uuids2 == null || (uuids = this.mLocalAdapter.getUuids()) == null) {
            return false;
        }
        processPhonebookAccess();
        synchronized (this.mProfileLock) {
            this.mProfileManager.updateProfiles(uuids2, uuids, this.mProfiles, this.mRemovedProfiles, this.mLocalNapRoleConnected, this.mDevice);
        }
        Log.d("CachedBluetoothDevice", "updating profiles for " + this.mDevice.getAnonymizedAddress());
        BluetoothClass bluetoothClass = this.mDevice.getBluetoothClass();
        if (bluetoothClass != null) {
            Log.v("CachedBluetoothDevice", "Class: " + bluetoothClass.toString());
        }
        Log.v("CachedBluetoothDevice", "UUID:");
        for (ParcelUuid parcelUuid : uuids2) {
            Log.v("CachedBluetoothDevice", "  " + parcelUuid);
        }
        return true;
    }

    private void fetchActiveDevices() {
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        if (a2dpProfile != null) {
            this.mIsActiveDeviceA2dp = this.mDevice.equals(a2dpProfile.getActiveDevice());
        }
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        if (headsetProfile != null) {
            this.mIsActiveDeviceHeadset = this.mDevice.equals(headsetProfile.getActiveDevice());
        }
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        if (hearingAidProfile != null) {
            this.mIsActiveDeviceHearingAid = hearingAidProfile.getActiveDevices().contains(this.mDevice);
        }
        LeAudioProfile leAudioProfile = this.mProfileManager.getLeAudioProfile();
        if (leAudioProfile != null) {
            this.mIsActiveDeviceLeAudio = leAudioProfile.getActiveDevices().contains(this.mDevice);
        }
    }

    /* access modifiers changed from: package-private */
    public void onUuidChanged() {
        updateProfiles();
        ParcelUuid[] uuids = this.mDevice.getUuids();
        long j = 30000;
        if (!ArrayUtils.contains(uuids, BluetoothUuid.HOGP)) {
            if (ArrayUtils.contains(uuids, BluetoothUuid.HEARING_AID)) {
                j = 15000;
            } else if (!ArrayUtils.contains(uuids, BluetoothUuid.LE_AUDIO)) {
                j = 5000;
            }
        }
        Log.d("CachedBluetoothDevice", "onUuidChanged: Time since last connect=" + (SystemClock.elapsedRealtime() - this.mConnectAttempted));
        if (this.mConnectAttempted + j > SystemClock.elapsedRealtime()) {
            Log.d("CachedBluetoothDevice", "onUuidChanged: triggering connectDevice");
            connectDevice();
        }
        lambda$refresh$0();
    }

    /* access modifiers changed from: package-private */
    public void onBondingStateChanged(int i) {
        if (i == 10) {
            synchronized (this.mProfileLock) {
                this.mProfiles.clear();
            }
            this.mDevice.setPhonebookAccessPermission(0);
            this.mDevice.setMessageAccessPermission(0);
            this.mDevice.setSimAccessPermission(0);
        }
        refresh();
        if (i == 12 && this.mDevice.isBondingInitiatedLocally()) {
            connect();
        }
    }

    public BluetoothClass getBtClass() {
        return this.mDevice.getBluetoothClass();
    }

    public List<LocalBluetoothProfile> getProfiles() {
        return new ArrayList(this.mProfiles);
    }

    public List<LocalBluetoothProfile> getConnectableProfiles() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mProfileLock) {
            for (LocalBluetoothProfile next : this.mProfiles) {
                if (next.accessProfileEnabled()) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    public List<LocalBluetoothProfile> getRemovedProfiles() {
        return new ArrayList(this.mRemovedProfiles);
    }

    public void registerCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void unregisterCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: dispatchAttributesChanged */
    public void lambda$refresh$0() {
        for (Callback onDeviceAttributesChanged : this.mCallbacks) {
            onDeviceAttributesChanged.onDeviceAttributesChanged();
        }
    }

    public String toString() {
        return this.mDevice.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CachedBluetoothDevice)) {
            return false;
        }
        return this.mDevice.equals(((CachedBluetoothDevice) obj).mDevice);
    }

    public int hashCode() {
        return this.mDevice.getAddress().hashCode();
    }

    public int compareTo(CachedBluetoothDevice cachedBluetoothDevice) {
        int i = (cachedBluetoothDevice.isConnected() ? 1 : 0) - (isConnected() ? 1 : 0);
        if (i != 0) {
            return i;
        }
        int i2 = 1;
        int i3 = cachedBluetoothDevice.getBondState() == 12 ? 1 : 0;
        if (getBondState() != 12) {
            i2 = 0;
        }
        int i4 = i3 - i2;
        if (i4 != 0) {
            return i4;
        }
        int i5 = (cachedBluetoothDevice.mJustDiscovered ? 1 : 0) - (this.mJustDiscovered ? 1 : 0);
        if (i5 != 0) {
            return i5;
        }
        int i6 = cachedBluetoothDevice.mRssi - this.mRssi;
        if (i6 != 0) {
            return i6;
        }
        return getName().compareTo(cachedBluetoothDevice.getName());
    }

    private void migratePhonebookPermissionChoice() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("bluetooth_phonebook_permission", 0);
        if (sharedPreferences.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getPhonebookAccessPermission() == 0) {
                int i = sharedPreferences.getInt(this.mDevice.getAddress(), 0);
                if (i == 1) {
                    this.mDevice.setPhonebookAccessPermission(1);
                } else if (i == 2) {
                    this.mDevice.setPhonebookAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(this.mDevice.getAddress());
            edit.commit();
        }
    }

    private void migrateMessagePermissionChoice() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("bluetooth_message_permission", 0);
        if (sharedPreferences.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getMessageAccessPermission() == 0) {
                int i = sharedPreferences.getInt(this.mDevice.getAddress(), 0);
                if (i == 1) {
                    this.mDevice.setMessageAccessPermission(1);
                } else if (i == 2) {
                    this.mDevice.setMessageAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(this.mDevice.getAddress());
            edit.commit();
        }
    }

    private void processPhonebookAccess() {
        if (this.mDevice.getBondState() == 12 && BluetoothUuid.containsAnyUuid(this.mDevice.getUuids(), PbapServerProfile.PBAB_CLIENT_UUIDS)) {
            BluetoothClass bluetoothClass = this.mDevice.getBluetoothClass();
            if (this.mDevice.getPhonebookAccessPermission() == 0) {
                if (bluetoothClass != null && (bluetoothClass.getDeviceClass() == 1032 || bluetoothClass.getDeviceClass() == 1028)) {
                    EventLog.writeEvent(1397638484, new Object[]{"138529441", -1, ""});
                }
                this.mDevice.setPhonebookAccessPermission(2);
            }
        }
    }

    public String getConnectionSummary() {
        return getConnectionSummary(false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0079, code lost:
        r0 = getBatteryLevel();
        r9 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x007f, code lost:
        if (r0 <= -1) goto L_0x0086;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0081, code lost:
        r0 = com.android.settingslib.Utils.formatPercentage(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0086, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0087, code lost:
        r11 = com.android.settingslib.R$string.bluetooth_pairing;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x008b, code lost:
        if (r4 == false) goto L_0x00e8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0094, code lost:
        if (com.android.settingslib.bluetooth.BluetoothUtils.getBooleanMetaData(r14.mDevice, 6) == false) goto L_0x00a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0096, code lost:
        r9 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r14.mDevice, 10);
        r4 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r14.mDevice, 11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00a5, code lost:
        r4 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00aa, code lost:
        if (isTwsBatteryAvailable(r9, r4) == false) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00ac, code lost:
        r13 = com.android.settingslib.R$string.bluetooth_battery_level_untethered;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00af, code lost:
        if (r0 == null) goto L_0x00b4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00b1, code lost:
        r13 = com.android.settingslib.R$string.bluetooth_battery_level;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00b4, code lost:
        r13 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00b5, code lost:
        if (r5 != false) goto L_0x00bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00b7, code lost:
        if (r6 != false) goto L_0x00bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00b9, code lost:
        if (r7 != false) goto L_0x00bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00bb, code lost:
        if (r8 == false) goto L_0x00ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00bd, code lost:
        r5 = com.android.settingslib.Utils.isAudioModeOngoingCall(r14.mContext);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00c5, code lost:
        if (r14.mIsActiveDeviceHearingAid != false) goto L_0x00d3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00c9, code lost:
        if (r14.mIsActiveDeviceHeadset == false) goto L_0x00cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00cb, code lost:
        if (r5 != false) goto L_0x00d3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00cf, code lost:
        if (r14.mIsActiveDeviceA2dp == false) goto L_0x00ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00d1, code lost:
        if (r5 != false) goto L_0x00ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00d7, code lost:
        if (isTwsBatteryAvailable(r9, r4) == false) goto L_0x00de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00d9, code lost:
        if (r15 != false) goto L_0x00de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00db, code lost:
        r13 = com.android.settingslib.R$string.bluetooth_active_battery_level_untethered;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x00de, code lost:
        if (r0 == null) goto L_0x00e5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x00e0, code lost:
        if (r15 != false) goto L_0x00e5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x00e2, code lost:
        r13 = com.android.settingslib.R$string.bluetooth_active_battery_level;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x00e5, code lost:
        r13 = com.android.settingslib.R$string.bluetooth_active_no_battery_level;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x00e8, code lost:
        r4 = -1;
        r13 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x00ea, code lost:
        if (r13 != r11) goto L_0x00f4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x00f0, code lost:
        if (getBondState() != 11) goto L_0x00f3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x00f3, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x00f8, code lost:
        if (isTwsBatteryAvailable(r9, r4) == false) goto L_0x010f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x010e, code lost:
        return r14.mContext.getString(r13, new java.lang.Object[]{com.android.settingslib.Utils.formatPercentage(r9), com.android.settingslib.Utils.formatPercentage(r4)});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0119, code lost:
        return r14.mContext.getString(r13, new java.lang.Object[]{r0});
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getConnectionSummary(boolean r15) {
        /*
            r14 = this;
            boolean r0 = r14.isProfileConnectedFail()
            if (r0 == 0) goto L_0x0015
            boolean r0 = r14.isConnected()
            if (r0 == 0) goto L_0x0015
            android.content.Context r14 = r14.mContext
            int r15 = com.android.settingslib.R$string.profile_connect_timeout_subtext
            java.lang.String r14 = r14.getString(r15)
            return r14
        L_0x0015:
            java.lang.Object r0 = r14.mProfileLock
            monitor-enter(r0)
            java.util.List r1 = r14.getProfiles()     // Catch:{ all -> 0x011a }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x011a }
            r2 = 0
            r3 = 1
            r4 = r2
            r5 = r3
            r6 = r5
            r7 = r6
            r8 = r7
        L_0x0027:
            boolean r9 = r1.hasNext()     // Catch:{ all -> 0x011a }
            r10 = 2
            if (r9 == 0) goto L_0x0078
            java.lang.Object r9 = r1.next()     // Catch:{ all -> 0x011a }
            com.android.settingslib.bluetooth.LocalBluetoothProfile r9 = (com.android.settingslib.bluetooth.LocalBluetoothProfile) r9     // Catch:{ all -> 0x011a }
            int r11 = r14.getProfileConnectionState(r9)     // Catch:{ all -> 0x011a }
            if (r11 == 0) goto L_0x0050
            if (r11 == r3) goto L_0x0044
            if (r11 == r10) goto L_0x0042
            r9 = 3
            if (r11 == r9) goto L_0x0044
            goto L_0x0027
        L_0x0042:
            r4 = r3
            goto L_0x0027
        L_0x0044:
            android.content.Context r14 = r14.mContext     // Catch:{ all -> 0x011a }
            int r15 = com.android.settingslib.bluetooth.BluetoothUtils.getConnectionStateSummary(r11)     // Catch:{ all -> 0x011a }
            java.lang.String r14 = r14.getString(r15)     // Catch:{ all -> 0x011a }
            monitor-exit(r0)     // Catch:{ all -> 0x011a }
            return r14
        L_0x0050:
            boolean r10 = r9.isProfileReady()     // Catch:{ all -> 0x011a }
            if (r10 == 0) goto L_0x0027
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.A2dpProfile     // Catch:{ all -> 0x011a }
            if (r10 != 0) goto L_0x0076
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.A2dpSinkProfile     // Catch:{ all -> 0x011a }
            if (r10 == 0) goto L_0x005f
            goto L_0x0076
        L_0x005f:
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.HeadsetProfile     // Catch:{ all -> 0x011a }
            if (r10 != 0) goto L_0x0074
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.HfpClientProfile     // Catch:{ all -> 0x011a }
            if (r10 == 0) goto L_0x0068
            goto L_0x0074
        L_0x0068:
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.HearingAidProfile     // Catch:{ all -> 0x011a }
            if (r10 == 0) goto L_0x006e
            r7 = r2
            goto L_0x0027
        L_0x006e:
            boolean r9 = r9 instanceof com.android.settingslib.bluetooth.LeAudioProfile     // Catch:{ all -> 0x011a }
            if (r9 == 0) goto L_0x0027
            r8 = r2
            goto L_0x0027
        L_0x0074:
            r6 = r2
            goto L_0x0027
        L_0x0076:
            r5 = r2
            goto L_0x0027
        L_0x0078:
            monitor-exit(r0)     // Catch:{ all -> 0x011a }
            int r0 = r14.getBatteryLevel()
            r1 = 0
            r9 = -1
            if (r0 <= r9) goto L_0x0086
            java.lang.String r0 = com.android.settingslib.Utils.formatPercentage((int) r0)
            goto L_0x0087
        L_0x0086:
            r0 = r1
        L_0x0087:
            int r11 = com.android.settingslib.R$string.bluetooth_pairing
            r12 = 11
            if (r4 == 0) goto L_0x00e8
            android.bluetooth.BluetoothDevice r4 = r14.mDevice
            r13 = 6
            boolean r4 = com.android.settingslib.bluetooth.BluetoothUtils.getBooleanMetaData(r4, r13)
            if (r4 == 0) goto L_0x00a5
            android.bluetooth.BluetoothDevice r4 = r14.mDevice
            r9 = 10
            int r9 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r4, r9)
            android.bluetooth.BluetoothDevice r4 = r14.mDevice
            int r4 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r4, r12)
            goto L_0x00a6
        L_0x00a5:
            r4 = r9
        L_0x00a6:
            boolean r13 = r14.isTwsBatteryAvailable(r9, r4)
            if (r13 == 0) goto L_0x00af
            int r13 = com.android.settingslib.R$string.bluetooth_battery_level_untethered
            goto L_0x00b5
        L_0x00af:
            if (r0 == 0) goto L_0x00b4
            int r13 = com.android.settingslib.R$string.bluetooth_battery_level
            goto L_0x00b5
        L_0x00b4:
            r13 = r11
        L_0x00b5:
            if (r5 != 0) goto L_0x00bd
            if (r6 != 0) goto L_0x00bd
            if (r7 != 0) goto L_0x00bd
            if (r8 == 0) goto L_0x00ea
        L_0x00bd:
            android.content.Context r5 = r14.mContext
            boolean r5 = com.android.settingslib.Utils.isAudioModeOngoingCall(r5)
            boolean r6 = r14.mIsActiveDeviceHearingAid
            if (r6 != 0) goto L_0x00d3
            boolean r6 = r14.mIsActiveDeviceHeadset
            if (r6 == 0) goto L_0x00cd
            if (r5 != 0) goto L_0x00d3
        L_0x00cd:
            boolean r6 = r14.mIsActiveDeviceA2dp
            if (r6 == 0) goto L_0x00ea
            if (r5 != 0) goto L_0x00ea
        L_0x00d3:
            boolean r5 = r14.isTwsBatteryAvailable(r9, r4)
            if (r5 == 0) goto L_0x00de
            if (r15 != 0) goto L_0x00de
            int r13 = com.android.settingslib.R$string.bluetooth_active_battery_level_untethered
            goto L_0x00ea
        L_0x00de:
            if (r0 == 0) goto L_0x00e5
            if (r15 != 0) goto L_0x00e5
            int r13 = com.android.settingslib.R$string.bluetooth_active_battery_level
            goto L_0x00ea
        L_0x00e5:
            int r13 = com.android.settingslib.R$string.bluetooth_active_no_battery_level
            goto L_0x00ea
        L_0x00e8:
            r4 = r9
            r13 = r11
        L_0x00ea:
            if (r13 != r11) goto L_0x00f4
            int r15 = r14.getBondState()
            if (r15 != r12) goto L_0x00f3
            goto L_0x00f4
        L_0x00f3:
            return r1
        L_0x00f4:
            boolean r15 = r14.isTwsBatteryAvailable(r9, r4)
            if (r15 == 0) goto L_0x010f
            android.content.Context r14 = r14.mContext
            java.lang.Object[] r15 = new java.lang.Object[r10]
            java.lang.String r0 = com.android.settingslib.Utils.formatPercentage((int) r9)
            r15[r2] = r0
            java.lang.String r0 = com.android.settingslib.Utils.formatPercentage((int) r4)
            r15[r3] = r0
            java.lang.String r14 = r14.getString(r13, r15)
            return r14
        L_0x010f:
            android.content.Context r14 = r14.mContext
            java.lang.Object[] r15 = new java.lang.Object[r3]
            r15[r2] = r0
            java.lang.String r14 = r14.getString(r13, r15)
            return r14
        L_0x011a:
            r14 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x011a }
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.getConnectionSummary(boolean):java.lang.String");
    }

    private boolean isProfileConnectedFail() {
        return this.mIsA2dpProfileConnectedFail || this.mIsHearingAidProfileConnectedFail || (!isConnectedSapDevice() && this.mIsHeadsetProfileConnectedFail) || this.mIsLeAudioProfileConnectedFail;
    }

    public boolean isConnectedA2dpDevice() {
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        return a2dpProfile != null && a2dpProfile.getConnectionStatus(this.mDevice) == 2;
    }

    public boolean isConnectedHfpDevice() {
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        return headsetProfile != null && headsetProfile.getConnectionStatus(this.mDevice) == 2;
    }

    public boolean isConnectedHearingAidDevice() {
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        return hearingAidProfile != null && hearingAidProfile.getConnectionStatus(this.mDevice) == 2;
    }

    public boolean isConnectedLeAudioDevice() {
        LeAudioProfile leAudioProfile = this.mProfileManager.getLeAudioProfile();
        return leAudioProfile != null && leAudioProfile.getConnectionStatus(this.mDevice) == 2;
    }

    private boolean isConnectedSapDevice() {
        SapProfile sapProfile = this.mProfileManager.getSapProfile();
        return sapProfile != null && sapProfile.getConnectionStatus(this.mDevice) == 2;
    }

    public CachedBluetoothDevice getSubDevice() {
        return this.mSubDevice;
    }

    public void setSubDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mSubDevice = cachedBluetoothDevice;
    }

    public void switchSubDeviceContent() {
        BluetoothDevice bluetoothDevice = this.mDevice;
        short s = this.mRssi;
        boolean z = this.mJustDiscovered;
        CachedBluetoothDevice cachedBluetoothDevice = this.mSubDevice;
        this.mDevice = cachedBluetoothDevice.mDevice;
        this.mRssi = cachedBluetoothDevice.mRssi;
        this.mJustDiscovered = cachedBluetoothDevice.mJustDiscovered;
        cachedBluetoothDevice.mDevice = bluetoothDevice;
        cachedBluetoothDevice.mRssi = s;
        cachedBluetoothDevice.mJustDiscovered = z;
        fetchActiveDevices();
    }

    public Set<CachedBluetoothDevice> getMemberDevice() {
        return this.mMemberDevices;
    }

    public void setMemberDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mMemberDevices.add(cachedBluetoothDevice);
    }

    public void removeMemberDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mMemberDevices.remove(cachedBluetoothDevice);
    }

    public void switchMemberDeviceContent(CachedBluetoothDevice cachedBluetoothDevice, CachedBluetoothDevice cachedBluetoothDevice2) {
        BluetoothDevice bluetoothDevice = this.mDevice;
        short s = this.mRssi;
        boolean z = this.mJustDiscovered;
        this.mDevice = cachedBluetoothDevice2.mDevice;
        this.mRssi = cachedBluetoothDevice2.mRssi;
        this.mJustDiscovered = cachedBluetoothDevice2.mJustDiscovered;
        setMemberDevice(cachedBluetoothDevice);
        this.mMemberDevices.remove(cachedBluetoothDevice2);
        cachedBluetoothDevice2.mDevice = bluetoothDevice;
        cachedBluetoothDevice2.mRssi = s;
        cachedBluetoothDevice2.mJustDiscovered = z;
        fetchActiveDevices();
    }

    public Pair<Drawable, String> getDrawableWithDescription() {
        Uri uriMetaData = BluetoothUtils.getUriMetaData(this.mDevice, 5);
        Pair<Drawable, String> btClassDrawableWithDescription = BluetoothUtils.getBtClassDrawableWithDescription(this.mContext, this);
        if (BluetoothUtils.isAdvancedDetailsHeader(this.mDevice) && uriMetaData != null) {
            BitmapDrawable bitmapDrawable = this.mDrawableCache.get(uriMetaData.toString());
            if (bitmapDrawable != null) {
                return new Pair<>(new AdaptiveOutlineDrawable(this.mContext.getResources(), bitmapDrawable.getBitmap()), (String) btClassDrawableWithDescription.second);
            }
            refresh();
        }
        return new Pair<>(BluetoothUtils.buildBtRainbowDrawable(this.mContext, (Drawable) btClassDrawableWithDescription.first, getAddress().hashCode()), (String) btClassDrawableWithDescription.second);
    }

    /* access modifiers changed from: package-private */
    public void releaseLruCache() {
        this.mDrawableCache.evictAll();
    }

    /* access modifiers changed from: package-private */
    public boolean getUnpairing() {
        return this.mUnpairing;
    }
}
