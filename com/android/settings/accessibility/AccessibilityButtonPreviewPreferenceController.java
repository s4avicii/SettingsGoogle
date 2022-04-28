package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.widget.IllustrationPreference;

public class AccessibilityButtonPreviewPreferenceController extends BasePreferenceController implements LifecycleObserver, OnResume, OnPause {
    private static final float DEFAULT_OPACITY = 0.55f;
    private static final int DEFAULT_SIZE = 0;
    private static final int SMALL_SIZE = 0;
    private AccessibilityLayerDrawable mAccessibilityPreviewDrawable;
    final ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean z) {
            AccessibilityButtonPreviewPreferenceController.this.updatePreviewPreference();
        }
    };
    private final ContentResolver mContentResolver;
    IllustrationPreference mIllustrationPreference;
    private AccessibilityManager.TouchExplorationStateChangeListener mTouchExplorationStateChangeListener = new C0568x9073d8ae(this);

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AccessibilityButtonPreviewPreferenceController(Context context, String str) {
        super(context, str);
        this.mContentResolver = context.getContentResolver();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(boolean z) {
        updatePreviewPreference();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mIllustrationPreference = (IllustrationPreference) preferenceScreen.findPreference(getPreferenceKey());
        updatePreviewPreference();
    }

    public void onResume() {
        ((AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class)).addTouchExplorationStateChangeListener(this.mTouchExplorationStateChangeListener);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_button_mode"), false, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_size"), false, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_opacity"), false, this.mContentObserver);
    }

    public void onPause() {
        ((AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class)).removeTouchExplorationStateChangeListener(this.mTouchExplorationStateChangeListener);
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
    }

    /* access modifiers changed from: private */
    public void updatePreviewPreference() {
        if (AccessibilityUtil.isFloatingMenuEnabled(this.mContext)) {
            this.mIllustrationPreference.setImageDrawable(getAccessibilityPreviewDrawable(Settings.Secure.getInt(this.mContentResolver, "accessibility_floating_menu_size", 0) == 0 ? C0444R.C0447drawable.accessibility_button_preview_small_floating_menu : C0444R.C0447drawable.accessibility_button_preview_large_floating_menu, (int) (Settings.Secure.getFloat(this.mContentResolver, "accessibility_floating_menu_opacity", DEFAULT_OPACITY) * 100.0f)));
        } else if (AccessibilityUtil.isGestureNavigateEnabled(this.mContext)) {
            IllustrationPreference illustrationPreference = this.mIllustrationPreference;
            Context context = this.mContext;
            illustrationPreference.setImageDrawable(context.getDrawable(AccessibilityUtil.isTouchExploreEnabled(context) ? C0444R.C0447drawable.accessibility_button_preview_three_finger : C0444R.C0447drawable.accessibility_button_preview_two_finger));
        } else {
            this.mIllustrationPreference.setImageDrawable(this.mContext.getDrawable(C0444R.C0447drawable.accessibility_button_navigation));
        }
    }

    private Drawable getAccessibilityPreviewDrawable(int i, int i2) {
        AccessibilityLayerDrawable accessibilityLayerDrawable = this.mAccessibilityPreviewDrawable;
        if (accessibilityLayerDrawable == null) {
            this.mAccessibilityPreviewDrawable = AccessibilityLayerDrawable.createLayerDrawable(this.mContext, i, i2);
        } else {
            accessibilityLayerDrawable.updateLayerDrawable(this.mContext, i, i2);
            this.mAccessibilityPreviewDrawable.invalidateSelf();
        }
        return this.mAccessibilityPreviewDrawable;
    }
}
