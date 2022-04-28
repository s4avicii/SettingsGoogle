package com.android.settings.dream;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.dream.DreamBackend;
import com.android.settingslib.widget.LayoutPreference;
import java.util.List;
import java.util.stream.Collectors;

public class DreamPickerController extends BasePreferenceController {
    private static final String KEY = "dream_picker";
    /* access modifiers changed from: private */
    public DreamBackend.DreamInfo mActiveDream;
    private DreamAdapter mAdapter;
    /* access modifiers changed from: private */
    public final DreamBackend mBackend;
    private final List<DreamBackend.DreamInfo> mDreamInfos;
    /* access modifiers changed from: private */
    public final MetricsFeatureProvider mMetricsFeatureProvider;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY;
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

    public DreamPickerController(Context context) {
        this(context, DreamBackend.getInstance(context));
    }

    public DreamPickerController(Context context, DreamBackend dreamBackend) {
        super(context, KEY);
        this.mBackend = dreamBackend;
        List<DreamBackend.DreamInfo> dreamInfos = dreamBackend.getDreamInfos();
        this.mDreamInfos = dreamInfos;
        this.mActiveDream = getActiveDreamInfo(dreamInfos);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public int getAvailabilityStatus() {
        return this.mDreamInfos.size() > 0 ? 0 : 2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mAdapter = new DreamAdapter((List) this.mDreamInfos.stream().map(new DreamPickerController$$ExternalSyntheticLambda0(this)).collect(Collectors.toList()));
        LayoutPreference layoutPreference = (LayoutPreference) preferenceScreen.findPreference(getPreferenceKey());
        if (layoutPreference != null) {
            RecyclerView recyclerView = (RecyclerView) layoutPreference.findViewById(C0444R.C0448id.dream_list);
            recyclerView.setLayoutManager(new AutoFitGridLayoutManager(this.mContext));
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(this.mContext, C0444R.dimen.dream_preference_card_padding));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(this.mAdapter);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ DreamItem lambda$displayPreference$0(DreamBackend.DreamInfo dreamInfo) {
        return new DreamItem(dreamInfo);
    }

    private static DreamBackend.DreamInfo getActiveDreamInfo(List<DreamBackend.DreamInfo> list) {
        return (DreamBackend.DreamInfo) list.stream().filter(DreamPickerController$$ExternalSyntheticLambda1.INSTANCE).findFirst().orElse((Object) null);
    }

    private class DreamItem implements IDreamItem {
        DreamBackend.DreamInfo mDreamInfo;

        DreamItem(DreamBackend.DreamInfo dreamInfo) {
            this.mDreamInfo = dreamInfo;
        }

        public CharSequence getTitle() {
            return this.mDreamInfo.caption;
        }

        public Drawable getIcon() {
            return this.mDreamInfo.icon;
        }

        public void onItemClicked() {
            DreamPickerController.this.mActiveDream = this.mDreamInfo;
            DreamPickerController.this.mBackend.setActiveDream(this.mDreamInfo.componentName);
            DreamPickerController.this.mMetricsFeatureProvider.action(DreamPickerController.this.mContext, 1788, this.mDreamInfo.componentName.flattenToString());
        }

        public void onCustomizeClicked() {
            DreamPickerController.this.mBackend.launchSettings(DreamPickerController.this.mContext, this.mDreamInfo);
        }

        public Drawable getPreviewImage() {
            return this.mDreamInfo.previewImage;
        }

        public boolean isActive() {
            if (DreamPickerController.this.mActiveDream == null) {
                return false;
            }
            return this.mDreamInfo.componentName.equals(DreamPickerController.this.mActiveDream.componentName);
        }

        public boolean allowCustomization() {
            return isActive() && this.mDreamInfo.settingsComponentName != null;
        }
    }
}
