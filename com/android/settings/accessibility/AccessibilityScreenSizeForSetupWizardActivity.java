package com.android.settings.accessibility;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.window.C0444R;
import com.android.settings.core.InstrumentedActivity;
import com.android.settings.display.FontSizePreferenceFragmentForSetupWizard;
import com.android.settings.display.ScreenZoomPreferenceFragmentForSetupWizard;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeHelper;

public class AccessibilityScreenSizeForSetupWizardActivity extends InstrumentedActivity {
    private int mLastScrollViewHeight;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Fragment fragment;
        super.onCreate(bundle);
        setTheme(ThemeHelper.trySetDynamicColor(this) ? 2131952218 : 2131952290);
        setContentView((int) C0444R.C0450layout.accessibility_screen_size_setup_wizard);
        updateHeaderLayout();
        scrollToBottom();
        initFooterButton();
        if (bundle == null) {
            if (getFragmentType(getIntent()) == 1) {
                fragment = new FontSizePreferenceFragmentForSetupWizard();
            } else {
                fragment = new ScreenZoomPreferenceFragmentForSetupWizard();
            }
            getSupportFragmentManager().beginTransaction().replace(C0444R.C0448id.content_frame, fragment).commit();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (getTransitionType(getIntent()) == 2) {
            overridePendingTransition(C0444R.C0445anim.sud_stay, 17432577);
        }
        super.onPause();
    }

    public int getMetricsCategory() {
        return getFragmentType(getIntent()) == 1 ? 369 : 370;
    }

    /* access modifiers changed from: package-private */
    public void updateHeaderLayout() {
        if (ThemeHelper.shouldApplyExtendedPartnerConfig(this) && isSuwSupportedTwoPanes()) {
            GlifLayout glifLayout = (GlifLayout) findViewById(C0444R.C0448id.setup_wizard_layout);
            LinearLayout linearLayout = (LinearLayout) glifLayout.findManagedViewById(C0444R.C0448id.sud_layout_header);
            if (linearLayout != null) {
                linearLayout.setPadding(0, glifLayout.getPaddingTop(), 0, glifLayout.getPaddingBottom());
            }
        }
        ((TextView) findViewById(C0444R.C0448id.suc_layout_title)).setText(getFragmentType(getIntent()) == 1 ? C0444R.string.title_font_size : C0444R.string.screen_zoom_title);
        ((TextView) findViewById(C0444R.C0448id.sud_layout_subtitle)).setText(getFragmentType(getIntent()) == 1 ? C0444R.string.font_size_summary : C0444R.string.screen_zoom_summary);
    }

    private boolean isSuwSupportedTwoPanes() {
        return getResources().getBoolean(C0444R.bool.config_suw_supported_two_panes);
    }

    private void initFooterButton() {
        ((FooterBarMixin) ((GlifLayout) findViewById(C0444R.C0448id.setup_wizard_layout)).getMixin(FooterBarMixin.class)).setPrimaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.done).setListener(new C0575x9e325db8(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initFooterButton$0(View view) {
        onBackPressed();
    }

    private void scrollToBottom() {
        this.mLastScrollViewHeight = 0;
        ScrollView scrollView = ((GlifLayout) findViewById(C0444R.C0448id.setup_wizard_layout)).getScrollView();
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new C0576x9e325db9(this, scrollView));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$scrollToBottom$2(ScrollView scrollView) {
        int height = scrollView.getHeight();
        if (height > 0 && height != this.mLastScrollViewHeight) {
            this.mLastScrollViewHeight = height;
            scrollView.post(new C0577x9e325dba(scrollView));
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$scrollToBottom$1(ScrollView scrollView) {
        scrollView.setSmoothScrollingEnabled(false);
        scrollView.fullScroll(130);
        scrollView.setSmoothScrollingEnabled(true);
    }

    private int getTransitionType(Intent intent) {
        return intent.getIntExtra("page_transition_type", -1);
    }

    private int getFragmentType(Intent intent) {
        return intent.getIntExtra("vision_fragment_no", 1);
    }
}
