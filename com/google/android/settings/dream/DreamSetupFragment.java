package com.google.android.settings.dream;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.C0444R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.dream.AutoFitGridLayoutManager;
import com.android.settings.dream.DreamAdapter;
import com.android.settings.dream.IDreamItem;
import com.android.settingslib.dream.DreamBackend;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifLayout;
import java.util.List;
import java.util.stream.Collectors;

public class DreamSetupFragment extends SettingsPreferenceFragment {
    /* access modifiers changed from: private */
    public DreamBackend.DreamInfo mActiveDream;
    /* access modifiers changed from: private */
    public DreamBackend mBackend;
    private FooterButton mFooterButton;

    public int getMetricsCategory() {
        return 47;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0444R.C0450layout.dream_setup_layout, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        DreamBackend instance = DreamBackend.getInstance(getContext());
        this.mBackend = instance;
        List<DreamBackend.DreamInfo> dreamInfos = instance.getDreamInfos();
        this.mActiveDream = (DreamBackend.DreamInfo) dreamInfos.stream().filter(DreamSetupFragment$$ExternalSyntheticLambda2.INSTANCE).findFirst().orElse((Object) null);
        DreamAdapter dreamAdapter = new DreamAdapter((List) dreamInfos.stream().map(new DreamSetupFragment$$ExternalSyntheticLambda1(this)).collect(Collectors.toList()));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(C0444R.C0448id.dream_setup_list);
        recyclerView.setLayoutManager(new AutoFitGridLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(dreamAdapter);
        this.mFooterButton = new FooterButton.Builder(getContext()).setListener(new DreamSetupFragment$$ExternalSyntheticLambda0(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build();
        updateFooterButtonText();
        ((FooterBarMixin) ((GlifLayout) view.findViewById(C0444R.C0448id.setup_wizard_layout)).getMixin(FooterBarMixin.class)).setPrimaryButton(this.mFooterButton);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ DreamItem lambda$onViewCreated$1(DreamBackend.DreamInfo dreamInfo) {
        return new DreamItem(dreamInfo);
    }

    /* access modifiers changed from: private */
    public void updateFooterButtonText() {
        this.mFooterButton.setText(getContext().getString(canCustomizeDream() ? C0444R.string.wizard_next : C0444R.string.wizard_finish));
    }

    private boolean canCustomizeDream() {
        DreamBackend.DreamInfo dreamInfo = this.mActiveDream;
        return (dreamInfo == null || dreamInfo.settingsComponentName == null) ? false : true;
    }

    /* access modifiers changed from: private */
    public void onPrimaryButtonClicked(View view) {
        if (canCustomizeDream()) {
            Intent component = new Intent().setComponent(this.mActiveDream.settingsComponentName);
            WizardManagerHelper.copyWizardManagerExtras(getIntent(), component);
            startActivity(component);
        }
        setResult(0);
        finish();
    }

    private class DreamItem implements IDreamItem {
        private final DreamBackend.DreamInfo mDreamInfo;

        private DreamItem(DreamBackend.DreamInfo dreamInfo) {
            this.mDreamInfo = dreamInfo;
        }

        public CharSequence getTitle() {
            return this.mDreamInfo.caption;
        }

        public Drawable getIcon() {
            return this.mDreamInfo.icon;
        }

        public void onItemClicked() {
            DreamSetupFragment.this.mActiveDream = this.mDreamInfo;
            DreamSetupFragment.this.mBackend.setActiveDream(this.mDreamInfo.componentName);
            DreamSetupFragment.this.updateFooterButtonText();
        }

        public Drawable getPreviewImage() {
            return this.mDreamInfo.previewImage;
        }

        public boolean isActive() {
            if (DreamSetupFragment.this.mActiveDream == null) {
                return false;
            }
            return this.mDreamInfo.componentName.equals(DreamSetupFragment.this.mActiveDream.componentName);
        }
    }
}
