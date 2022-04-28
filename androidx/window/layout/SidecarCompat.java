package androidx.window.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.window.core.SpecificationComputer;
import androidx.window.core.Version;
import androidx.window.layout.ExtensionInterfaceCompat;
import androidx.window.sidecar.SidecarDeviceState;
import androidx.window.sidecar.SidecarInterface;
import androidx.window.sidecar.SidecarProvider;
import androidx.window.sidecar.SidecarWindowLayoutInfo;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SidecarCompat.kt */
public final class SidecarCompat implements ExtensionInterfaceCompat {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    private static final String TAG = "SidecarCompat";
    @NotNull
    private final Map<Activity, ComponentCallbacks> componentCallbackMap;
    /* access modifiers changed from: private */
    @Nullable
    public ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallback;
    @Nullable
    private final SidecarInterface sidecar;
    /* access modifiers changed from: private */
    @NotNull
    public final SidecarAdapter sidecarAdapter;
    /* access modifiers changed from: private */
    @NotNull
    public final Map<IBinder, Activity> windowListenerRegisteredContexts;

    public SidecarCompat(@Nullable SidecarInterface sidecarInterface, @NotNull SidecarAdapter sidecarAdapter2) {
        Intrinsics.checkNotNullParameter(sidecarAdapter2, "sidecarAdapter");
        this.sidecar = sidecarInterface;
        this.sidecarAdapter = sidecarAdapter2;
        this.windowListenerRegisteredContexts = new LinkedHashMap();
        this.componentCallbackMap = new LinkedHashMap();
    }

    @Nullable
    public final SidecarInterface getSidecar() {
        return this.sidecar;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public SidecarCompat(@NotNull Context context) {
        this(Companion.getSidecarCompat$window_release(context), new SidecarAdapter((SpecificationComputer.VerificationMode) null, 1, (DefaultConstructorMarker) null));
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public void setExtensionCallback(@NotNull ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallbackInterface) {
        Intrinsics.checkNotNullParameter(extensionCallbackInterface, "extensionCallback");
        this.extensionCallback = new DistinctElementCallback(extensionCallbackInterface);
        SidecarInterface sidecarInterface = this.sidecar;
        if (sidecarInterface != null) {
            sidecarInterface.setSidecarCallback(new DistinctSidecarElementCallback(this.sidecarAdapter, new TranslatingCallback(this)));
        }
    }

    @NotNull
    public final WindowLayoutInfo getWindowLayoutInfo(@NotNull Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        IBinder activityWindowToken$window_release = Companion.getActivityWindowToken$window_release(activity);
        if (activityWindowToken$window_release == null) {
            return new WindowLayoutInfo(CollectionsKt__CollectionsKt.emptyList());
        }
        SidecarInterface sidecarInterface = this.sidecar;
        SidecarDeviceState sidecarDeviceState = null;
        SidecarWindowLayoutInfo windowLayoutInfo = sidecarInterface == null ? null : sidecarInterface.getWindowLayoutInfo(activityWindowToken$window_release);
        SidecarAdapter sidecarAdapter2 = this.sidecarAdapter;
        SidecarInterface sidecarInterface2 = this.sidecar;
        if (sidecarInterface2 != null) {
            sidecarDeviceState = sidecarInterface2.getDeviceState();
        }
        if (sidecarDeviceState == null) {
            sidecarDeviceState = new SidecarDeviceState();
        }
        return sidecarAdapter2.translate(windowLayoutInfo, sidecarDeviceState);
    }

    public void onWindowLayoutChangeListenerAdded(@NotNull Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        IBinder activityWindowToken$window_release = Companion.getActivityWindowToken$window_release(activity);
        if (activityWindowToken$window_release != null) {
            register(activityWindowToken$window_release, activity);
            return;
        }
        activity.getWindow().getDecorView().addOnAttachStateChangeListener(new FirstAttachAdapter(this, activity));
    }

    public final void register(@NotNull IBinder iBinder, @NotNull Activity activity) {
        SidecarInterface sidecarInterface;
        Intrinsics.checkNotNullParameter(iBinder, "windowToken");
        Intrinsics.checkNotNullParameter(activity, "activity");
        this.windowListenerRegisteredContexts.put(iBinder, activity);
        SidecarInterface sidecarInterface2 = this.sidecar;
        if (sidecarInterface2 != null) {
            sidecarInterface2.onWindowLayoutChangeListenerAdded(iBinder);
        }
        if (this.windowListenerRegisteredContexts.size() == 1 && (sidecarInterface = this.sidecar) != null) {
            sidecarInterface.onDeviceStateListenersChanged(false);
        }
        ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallbackInterface = this.extensionCallback;
        if (extensionCallbackInterface != null) {
            extensionCallbackInterface.onWindowLayoutChanged(activity, getWindowLayoutInfo(activity));
        }
        registerConfigurationChangeListener(activity);
    }

    private final void registerConfigurationChangeListener(Activity activity) {
        if (this.componentCallbackMap.get(activity) == null) {
            C0453xe5f1d4c7 sidecarCompat$registerConfigurationChangeListener$configChangeObserver$1 = new C0453xe5f1d4c7(this, activity);
            this.componentCallbackMap.put(activity, sidecarCompat$registerConfigurationChangeListener$configChangeObserver$1);
            activity.registerComponentCallbacks(sidecarCompat$registerConfigurationChangeListener$configChangeObserver$1);
        }
    }

    public void onWindowLayoutChangeListenerRemoved(@NotNull Activity activity) {
        SidecarInterface sidecarInterface;
        Intrinsics.checkNotNullParameter(activity, "activity");
        IBinder activityWindowToken$window_release = Companion.getActivityWindowToken$window_release(activity);
        if (activityWindowToken$window_release != null) {
            SidecarInterface sidecarInterface2 = this.sidecar;
            if (sidecarInterface2 != null) {
                sidecarInterface2.onWindowLayoutChangeListenerRemoved(activityWindowToken$window_release);
            }
            unregisterComponentCallback(activity);
            boolean z = this.windowListenerRegisteredContexts.size() == 1;
            this.windowListenerRegisteredContexts.remove(activityWindowToken$window_release);
            if (z && (sidecarInterface = this.sidecar) != null) {
                sidecarInterface.onDeviceStateListenersChanged(true);
            }
        }
    }

    private final void unregisterComponentCallback(Activity activity) {
        activity.unregisterComponentCallbacks(this.componentCallbackMap.get(activity));
        this.componentCallbackMap.remove(activity);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:58|59|60|61|69|70|71|72|73|(2:75|(2:77|97)(2:78|79))(2:80|81)) */
    /* JADX WARNING: Code restructure failed: missing block: B:98:?, code lost:
        return true;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:72:0x0114 */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x001f A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0021 A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002d A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0058 A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x005a A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0066 A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0081 A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0083 A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x008f A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00aa A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ab A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00b7 A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0144 A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0155 A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x016d A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0179 A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0185 A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0191 A[Catch:{ NoSuchFieldError -> 0x00c0, all -> 0x019d }] */
    @android.annotation.SuppressLint({"BanUncheckedReflection"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean validateExtensionInterface() {
        /*
            r7 = this;
            r0 = 1
            r1 = 0
            androidx.window.sidecar.SidecarInterface r2 = r7.sidecar     // Catch:{ all -> 0x019d }
            r3 = 0
            if (r2 != 0) goto L_0x0009
        L_0x0007:
            r2 = r3
            goto L_0x001d
        L_0x0009:
            java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x019d }
            if (r2 != 0) goto L_0x0010
            goto L_0x0007
        L_0x0010:
            java.lang.String r4 = "setSidecarCallback"
            java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch:{ all -> 0x019d }
            java.lang.Class<androidx.window.sidecar.SidecarInterface$SidecarCallback> r6 = androidx.window.sidecar.SidecarInterface.SidecarCallback.class
            r5[r1] = r6     // Catch:{ all -> 0x019d }
            java.lang.reflect.Method r2 = r2.getMethod(r4, r5)     // Catch:{ all -> 0x019d }
        L_0x001d:
            if (r2 != 0) goto L_0x0021
            r2 = r3
            goto L_0x0025
        L_0x0021:
            java.lang.Class r2 = r2.getReturnType()     // Catch:{ all -> 0x019d }
        L_0x0025:
            java.lang.Class r4 = java.lang.Void.TYPE     // Catch:{ all -> 0x019d }
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r2, r4)     // Catch:{ all -> 0x019d }
            if (r4 == 0) goto L_0x0191
            androidx.window.sidecar.SidecarInterface r2 = r7.sidecar     // Catch:{ all -> 0x019d }
            if (r2 != 0) goto L_0x0032
            goto L_0x0035
        L_0x0032:
            r2.getDeviceState()     // Catch:{ all -> 0x019d }
        L_0x0035:
            androidx.window.sidecar.SidecarInterface r2 = r7.sidecar     // Catch:{ all -> 0x019d }
            if (r2 != 0) goto L_0x003a
            goto L_0x003d
        L_0x003a:
            r2.onDeviceStateListenersChanged(r0)     // Catch:{ all -> 0x019d }
        L_0x003d:
            androidx.window.sidecar.SidecarInterface r2 = r7.sidecar     // Catch:{ all -> 0x019d }
            if (r2 != 0) goto L_0x0043
        L_0x0041:
            r2 = r3
            goto L_0x0056
        L_0x0043:
            java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x019d }
            if (r2 != 0) goto L_0x004a
            goto L_0x0041
        L_0x004a:
            java.lang.String r4 = "getWindowLayoutInfo"
            java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch:{ all -> 0x019d }
            java.lang.Class<android.os.IBinder> r6 = android.os.IBinder.class
            r5[r1] = r6     // Catch:{ all -> 0x019d }
            java.lang.reflect.Method r2 = r2.getMethod(r4, r5)     // Catch:{ all -> 0x019d }
        L_0x0056:
            if (r2 != 0) goto L_0x005a
            r2 = r3
            goto L_0x005e
        L_0x005a:
            java.lang.Class r2 = r2.getReturnType()     // Catch:{ all -> 0x019d }
        L_0x005e:
            java.lang.Class<androidx.window.sidecar.SidecarWindowLayoutInfo> r4 = androidx.window.sidecar.SidecarWindowLayoutInfo.class
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r2, r4)     // Catch:{ all -> 0x019d }
            if (r4 == 0) goto L_0x0185
            androidx.window.sidecar.SidecarInterface r2 = r7.sidecar     // Catch:{ all -> 0x019d }
            if (r2 != 0) goto L_0x006c
        L_0x006a:
            r2 = r3
            goto L_0x007f
        L_0x006c:
            java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x019d }
            if (r2 != 0) goto L_0x0073
            goto L_0x006a
        L_0x0073:
            java.lang.String r4 = "onWindowLayoutChangeListenerAdded"
            java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch:{ all -> 0x019d }
            java.lang.Class<android.os.IBinder> r6 = android.os.IBinder.class
            r5[r1] = r6     // Catch:{ all -> 0x019d }
            java.lang.reflect.Method r2 = r2.getMethod(r4, r5)     // Catch:{ all -> 0x019d }
        L_0x007f:
            if (r2 != 0) goto L_0x0083
            r2 = r3
            goto L_0x0087
        L_0x0083:
            java.lang.Class r2 = r2.getReturnType()     // Catch:{ all -> 0x019d }
        L_0x0087:
            java.lang.Class r4 = java.lang.Void.TYPE     // Catch:{ all -> 0x019d }
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual(r2, r4)     // Catch:{ all -> 0x019d }
            if (r4 == 0) goto L_0x0179
            androidx.window.sidecar.SidecarInterface r7 = r7.sidecar     // Catch:{ all -> 0x019d }
            if (r7 != 0) goto L_0x0095
        L_0x0093:
            r7 = r3
            goto L_0x00a8
        L_0x0095:
            java.lang.Class r7 = r7.getClass()     // Catch:{ all -> 0x019d }
            if (r7 != 0) goto L_0x009c
            goto L_0x0093
        L_0x009c:
            java.lang.String r2 = "onWindowLayoutChangeListenerRemoved"
            java.lang.Class[] r4 = new java.lang.Class[r0]     // Catch:{ all -> 0x019d }
            java.lang.Class<android.os.IBinder> r5 = android.os.IBinder.class
            r4[r1] = r5     // Catch:{ all -> 0x019d }
            java.lang.reflect.Method r7 = r7.getMethod(r2, r4)     // Catch:{ all -> 0x019d }
        L_0x00a8:
            if (r7 != 0) goto L_0x00ab
            goto L_0x00af
        L_0x00ab:
            java.lang.Class r3 = r7.getReturnType()     // Catch:{ all -> 0x019d }
        L_0x00af:
            java.lang.Class r7 = java.lang.Void.TYPE     // Catch:{ all -> 0x019d }
            boolean r7 = kotlin.jvm.internal.Intrinsics.areEqual(r3, r7)     // Catch:{ all -> 0x019d }
            if (r7 == 0) goto L_0x016d
            androidx.window.sidecar.SidecarDeviceState r7 = new androidx.window.sidecar.SidecarDeviceState     // Catch:{ all -> 0x019d }
            r7.<init>()     // Catch:{ all -> 0x019d }
            r2 = 3
            r7.posture = r2     // Catch:{ NoSuchFieldError -> 0x00c0 }
            goto L_0x00f4
        L_0x00c0:
            java.lang.Class<androidx.window.sidecar.SidecarDeviceState> r3 = androidx.window.sidecar.SidecarDeviceState.class
            java.lang.String r4 = "setPosture"
            java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch:{ all -> 0x019d }
            java.lang.Class r6 = java.lang.Integer.TYPE     // Catch:{ all -> 0x019d }
            r5[r1] = r6     // Catch:{ all -> 0x019d }
            java.lang.reflect.Method r3 = r3.getMethod(r4, r5)     // Catch:{ all -> 0x019d }
            java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch:{ all -> 0x019d }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x019d }
            r4[r1] = r5     // Catch:{ all -> 0x019d }
            r3.invoke(r7, r4)     // Catch:{ all -> 0x019d }
            java.lang.Class<androidx.window.sidecar.SidecarDeviceState> r3 = androidx.window.sidecar.SidecarDeviceState.class
            java.lang.String r4 = "getPosture"
            java.lang.Class[] r5 = new java.lang.Class[r1]     // Catch:{ all -> 0x019d }
            java.lang.reflect.Method r3 = r3.getMethod(r4, r5)     // Catch:{ all -> 0x019d }
            java.lang.Object[] r4 = new java.lang.Object[r1]     // Catch:{ all -> 0x019d }
            java.lang.Object r7 = r3.invoke(r7, r4)     // Catch:{ all -> 0x019d }
            if (r7 == 0) goto L_0x0165
            java.lang.Integer r7 = (java.lang.Integer) r7     // Catch:{ all -> 0x019d }
            int r7 = r7.intValue()     // Catch:{ all -> 0x019d }
            if (r7 != r2) goto L_0x015d
        L_0x00f4:
            androidx.window.sidecar.SidecarDisplayFeature r7 = new androidx.window.sidecar.SidecarDisplayFeature     // Catch:{ all -> 0x019d }
            r7.<init>()     // Catch:{ all -> 0x019d }
            android.graphics.Rect r2 = r7.getRect()     // Catch:{ all -> 0x019d }
            java.lang.String r3 = "displayFeature.rect"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, r3)     // Catch:{ all -> 0x019d }
            r7.setRect(r2)     // Catch:{ all -> 0x019d }
            r7.getType()     // Catch:{ all -> 0x019d }
            r7.setType(r0)     // Catch:{ all -> 0x019d }
            androidx.window.sidecar.SidecarWindowLayoutInfo r2 = new androidx.window.sidecar.SidecarWindowLayoutInfo     // Catch:{ all -> 0x019d }
            r2.<init>()     // Catch:{ all -> 0x019d }
            java.util.List r7 = r2.displayFeatures     // Catch:{ NoSuchFieldError -> 0x0114 }
            goto L_0x019e
        L_0x0114:
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ all -> 0x019d }
            r3.<init>()     // Catch:{ all -> 0x019d }
            r3.add(r7)     // Catch:{ all -> 0x019d }
            java.lang.Class<androidx.window.sidecar.SidecarWindowLayoutInfo> r7 = androidx.window.sidecar.SidecarWindowLayoutInfo.class
            java.lang.String r4 = "setDisplayFeatures"
            java.lang.Class[] r5 = new java.lang.Class[r0]     // Catch:{ all -> 0x019d }
            java.lang.Class<java.util.List> r6 = java.util.List.class
            r5[r1] = r6     // Catch:{ all -> 0x019d }
            java.lang.reflect.Method r7 = r7.getMethod(r4, r5)     // Catch:{ all -> 0x019d }
            java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch:{ all -> 0x019d }
            r4[r1] = r3     // Catch:{ all -> 0x019d }
            r7.invoke(r2, r4)     // Catch:{ all -> 0x019d }
            java.lang.Class<androidx.window.sidecar.SidecarWindowLayoutInfo> r7 = androidx.window.sidecar.SidecarWindowLayoutInfo.class
            java.lang.String r4 = "getDisplayFeatures"
            java.lang.Class[] r5 = new java.lang.Class[r1]     // Catch:{ all -> 0x019d }
            java.lang.reflect.Method r7 = r7.getMethod(r4, r5)     // Catch:{ all -> 0x019d }
            java.lang.Object[] r4 = new java.lang.Object[r1]     // Catch:{ all -> 0x019d }
            java.lang.Object r7 = r7.invoke(r2, r4)     // Catch:{ all -> 0x019d }
            if (r7 == 0) goto L_0x0155
            java.util.List r7 = (java.util.List) r7     // Catch:{ all -> 0x019d }
            boolean r7 = kotlin.jvm.internal.Intrinsics.areEqual(r3, r7)     // Catch:{ all -> 0x019d }
            if (r7 == 0) goto L_0x014d
            goto L_0x019e
        L_0x014d:
            java.lang.Exception r7 = new java.lang.Exception     // Catch:{ all -> 0x019d }
            java.lang.String r0 = "Invalid display feature getter/setter"
            r7.<init>(r0)     // Catch:{ all -> 0x019d }
            throw r7     // Catch:{ all -> 0x019d }
        L_0x0155:
            java.lang.NullPointerException r7 = new java.lang.NullPointerException     // Catch:{ all -> 0x019d }
            java.lang.String r0 = "null cannot be cast to non-null type kotlin.collections.List<androidx.window.sidecar.SidecarDisplayFeature>"
            r7.<init>(r0)     // Catch:{ all -> 0x019d }
            throw r7     // Catch:{ all -> 0x019d }
        L_0x015d:
            java.lang.Exception r7 = new java.lang.Exception     // Catch:{ all -> 0x019d }
            java.lang.String r0 = "Invalid device posture getter/setter"
            r7.<init>(r0)     // Catch:{ all -> 0x019d }
            throw r7     // Catch:{ all -> 0x019d }
        L_0x0165:
            java.lang.NullPointerException r7 = new java.lang.NullPointerException     // Catch:{ all -> 0x019d }
            java.lang.String r0 = "null cannot be cast to non-null type kotlin.Int"
            r7.<init>(r0)     // Catch:{ all -> 0x019d }
            throw r7     // Catch:{ all -> 0x019d }
        L_0x016d:
            java.lang.NoSuchMethodException r7 = new java.lang.NoSuchMethodException     // Catch:{ all -> 0x019d }
            java.lang.String r0 = "Illegal return type for 'onWindowLayoutChangeListenerRemoved': "
            java.lang.String r0 = kotlin.jvm.internal.Intrinsics.stringPlus(r0, r3)     // Catch:{ all -> 0x019d }
            r7.<init>(r0)     // Catch:{ all -> 0x019d }
            throw r7     // Catch:{ all -> 0x019d }
        L_0x0179:
            java.lang.NoSuchMethodException r7 = new java.lang.NoSuchMethodException     // Catch:{ all -> 0x019d }
            java.lang.String r0 = "Illegal return type for 'onWindowLayoutChangeListenerAdded': "
            java.lang.String r0 = kotlin.jvm.internal.Intrinsics.stringPlus(r0, r2)     // Catch:{ all -> 0x019d }
            r7.<init>(r0)     // Catch:{ all -> 0x019d }
            throw r7     // Catch:{ all -> 0x019d }
        L_0x0185:
            java.lang.NoSuchMethodException r7 = new java.lang.NoSuchMethodException     // Catch:{ all -> 0x019d }
            java.lang.String r0 = "Illegal return type for 'getWindowLayoutInfo': "
            java.lang.String r0 = kotlin.jvm.internal.Intrinsics.stringPlus(r0, r2)     // Catch:{ all -> 0x019d }
            r7.<init>(r0)     // Catch:{ all -> 0x019d }
            throw r7     // Catch:{ all -> 0x019d }
        L_0x0191:
            java.lang.NoSuchMethodException r7 = new java.lang.NoSuchMethodException     // Catch:{ all -> 0x019d }
            java.lang.String r0 = "Illegal return type for 'setSidecarCallback': "
            java.lang.String r0 = kotlin.jvm.internal.Intrinsics.stringPlus(r0, r2)     // Catch:{ all -> 0x019d }
            r7.<init>(r0)     // Catch:{ all -> 0x019d }
            throw r7     // Catch:{ all -> 0x019d }
        L_0x019d:
            r0 = r1
        L_0x019e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.window.layout.SidecarCompat.validateExtensionInterface():boolean");
    }

    /* compiled from: SidecarCompat.kt */
    private static final class FirstAttachAdapter implements View.OnAttachStateChangeListener {
        @NotNull
        private final WeakReference<Activity> activityWeakReference;
        @NotNull
        private final SidecarCompat sidecarCompat;

        public void onViewDetachedFromWindow(@NotNull View view) {
            Intrinsics.checkNotNullParameter(view, "view");
        }

        public FirstAttachAdapter(@NotNull SidecarCompat sidecarCompat2, @NotNull Activity activity) {
            Intrinsics.checkNotNullParameter(sidecarCompat2, "sidecarCompat");
            Intrinsics.checkNotNullParameter(activity, "activity");
            this.sidecarCompat = sidecarCompat2;
            this.activityWeakReference = new WeakReference<>(activity);
        }

        public void onViewAttachedToWindow(@NotNull View view) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.removeOnAttachStateChangeListener(this);
            Activity activity = (Activity) this.activityWeakReference.get();
            IBinder activityWindowToken$window_release = SidecarCompat.Companion.getActivityWindowToken$window_release(activity);
            if (activity != null && activityWindowToken$window_release != null) {
                this.sidecarCompat.register(activityWindowToken$window_release, activity);
            }
        }
    }

    /* compiled from: SidecarCompat.kt */
    public final class TranslatingCallback implements SidecarInterface.SidecarCallback {
        final /* synthetic */ SidecarCompat this$0;

        public TranslatingCallback(SidecarCompat sidecarCompat) {
            Intrinsics.checkNotNullParameter(sidecarCompat, "this$0");
            this.this$0 = sidecarCompat;
        }

        @SuppressLint({"SyntheticAccessor"})
        public void onDeviceStateChanged(@NotNull SidecarDeviceState sidecarDeviceState) {
            SidecarInterface sidecar;
            Intrinsics.checkNotNullParameter(sidecarDeviceState, "newDeviceState");
            Collection<Activity> values = this.this$0.windowListenerRegisteredContexts.values();
            SidecarCompat sidecarCompat = this.this$0;
            for (Activity activity : values) {
                IBinder activityWindowToken$window_release = SidecarCompat.Companion.getActivityWindowToken$window_release(activity);
                SidecarWindowLayoutInfo sidecarWindowLayoutInfo = null;
                if (!(activityWindowToken$window_release == null || (sidecar = sidecarCompat.getSidecar()) == null)) {
                    sidecarWindowLayoutInfo = sidecar.getWindowLayoutInfo(activityWindowToken$window_release);
                }
                ExtensionInterfaceCompat.ExtensionCallbackInterface access$getExtensionCallback$p = sidecarCompat.extensionCallback;
                if (access$getExtensionCallback$p != null) {
                    access$getExtensionCallback$p.onWindowLayoutChanged(activity, sidecarCompat.sidecarAdapter.translate(sidecarWindowLayoutInfo, sidecarDeviceState));
                }
            }
        }

        @SuppressLint({"SyntheticAccessor"})
        public void onWindowLayoutChanged(@NotNull IBinder iBinder, @NotNull SidecarWindowLayoutInfo sidecarWindowLayoutInfo) {
            Intrinsics.checkNotNullParameter(iBinder, "windowToken");
            Intrinsics.checkNotNullParameter(sidecarWindowLayoutInfo, "newLayout");
            Activity activity = (Activity) this.this$0.windowListenerRegisteredContexts.get(iBinder);
            if (activity == null) {
                Log.w(SidecarCompat.TAG, "Unable to resolve activity from window token. Missing a call to #onWindowLayoutChangeListenerAdded()?");
                return;
            }
            SidecarAdapter access$getSidecarAdapter$p = this.this$0.sidecarAdapter;
            SidecarInterface sidecar = this.this$0.getSidecar();
            SidecarDeviceState deviceState = sidecar == null ? null : sidecar.getDeviceState();
            if (deviceState == null) {
                deviceState = new SidecarDeviceState();
            }
            WindowLayoutInfo translate = access$getSidecarAdapter$p.translate(sidecarWindowLayoutInfo, deviceState);
            ExtensionInterfaceCompat.ExtensionCallbackInterface access$getExtensionCallback$p = this.this$0.extensionCallback;
            if (access$getExtensionCallback$p != null) {
                access$getExtensionCallback$p.onWindowLayoutChanged(activity, translate);
            }
        }
    }

    /* compiled from: SidecarCompat.kt */
    private static final class DistinctElementCallback implements ExtensionInterfaceCompat.ExtensionCallbackInterface {
        @NotNull
        private final WeakHashMap<Activity, WindowLayoutInfo> activityWindowLayoutInfo = new WeakHashMap<>();
        @NotNull
        private final ExtensionInterfaceCompat.ExtensionCallbackInterface callbackInterface;
        @NotNull
        private final ReentrantLock lock = new ReentrantLock();

        public DistinctElementCallback(@NotNull ExtensionInterfaceCompat.ExtensionCallbackInterface extensionCallbackInterface) {
            Intrinsics.checkNotNullParameter(extensionCallbackInterface, "callbackInterface");
            this.callbackInterface = extensionCallbackInterface;
        }

        public void onWindowLayoutChanged(@NotNull Activity activity, @NotNull WindowLayoutInfo windowLayoutInfo) {
            Intrinsics.checkNotNullParameter(activity, "activity");
            Intrinsics.checkNotNullParameter(windowLayoutInfo, "newLayout");
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                if (!Intrinsics.areEqual(windowLayoutInfo, this.activityWindowLayoutInfo.get(activity))) {
                    WindowLayoutInfo put = this.activityWindowLayoutInfo.put(activity, windowLayoutInfo);
                    reentrantLock.unlock();
                    this.callbackInterface.onWindowLayoutChanged(activity, windowLayoutInfo);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    /* compiled from: SidecarCompat.kt */
    private static final class DistinctSidecarElementCallback implements SidecarInterface.SidecarCallback {
        @NotNull
        private final SidecarInterface.SidecarCallback callbackInterface;
        @Nullable
        private SidecarDeviceState lastDeviceState;
        @NotNull
        private final ReentrantLock lock = new ReentrantLock();
        @NotNull
        private final WeakHashMap<IBinder, SidecarWindowLayoutInfo> mActivityWindowLayoutInfo = new WeakHashMap<>();
        @NotNull
        private final SidecarAdapter sidecarAdapter;

        public DistinctSidecarElementCallback(@NotNull SidecarAdapter sidecarAdapter2, @NotNull SidecarInterface.SidecarCallback sidecarCallback) {
            Intrinsics.checkNotNullParameter(sidecarAdapter2, "sidecarAdapter");
            Intrinsics.checkNotNullParameter(sidecarCallback, "callbackInterface");
            this.sidecarAdapter = sidecarAdapter2;
            this.callbackInterface = sidecarCallback;
        }

        public void onDeviceStateChanged(@NotNull SidecarDeviceState sidecarDeviceState) {
            Intrinsics.checkNotNullParameter(sidecarDeviceState, "newDeviceState");
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                if (!this.sidecarAdapter.isEqualSidecarDeviceState(this.lastDeviceState, sidecarDeviceState)) {
                    this.lastDeviceState = sidecarDeviceState;
                    this.callbackInterface.onDeviceStateChanged(sidecarDeviceState);
                    Unit unit = Unit.INSTANCE;
                    reentrantLock.unlock();
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        public void onWindowLayoutChanged(@NotNull IBinder iBinder, @NotNull SidecarWindowLayoutInfo sidecarWindowLayoutInfo) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(sidecarWindowLayoutInfo, "newLayout");
            synchronized (this.lock) {
                if (!this.sidecarAdapter.isEqualSidecarWindowLayoutInfo(this.mActivityWindowLayoutInfo.get(iBinder), sidecarWindowLayoutInfo)) {
                    SidecarWindowLayoutInfo put = this.mActivityWindowLayoutInfo.put(iBinder, sidecarWindowLayoutInfo);
                    this.callbackInterface.onWindowLayoutChanged(iBinder, sidecarWindowLayoutInfo);
                }
            }
        }
    }

    /* compiled from: SidecarCompat.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @Nullable
        public final Version getSidecarVersion() {
            try {
                String apiVersion = SidecarProvider.getApiVersion();
                if (!TextUtils.isEmpty(apiVersion)) {
                    return Version.Companion.parse(apiVersion);
                }
                return null;
            } catch (NoClassDefFoundError | UnsupportedOperationException unused) {
                return null;
            }
        }

        @Nullable
        public final SidecarInterface getSidecarCompat$window_release(@NotNull Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            return SidecarProvider.getSidecarImpl(context.getApplicationContext());
        }

        @Nullable
        public final IBinder getActivityWindowToken$window_release(@Nullable Activity activity) {
            Window window;
            WindowManager.LayoutParams attributes;
            if (activity == null || (window = activity.getWindow()) == null || (attributes = window.getAttributes()) == null) {
                return null;
            }
            return attributes.token;
        }
    }
}
