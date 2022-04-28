package com.android.settingslib.collapsingtoolbar;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarDelegate;
import com.android.settingslib.utils.BuildCompatUtils;

public class CollapsingToolbarBaseActivity extends FragmentActivity {
    private int mCustomizeLayoutResId = 0;
    private CollapsingToolbarDelegate mToolbardelegate;

    private class DelegateCallback implements CollapsingToolbarDelegate.HostCallback {
        private DelegateCallback() {
        }

        public ActionBar setActionBar(Toolbar toolbar) {
            CollapsingToolbarBaseActivity.super.setActionBar(toolbar);
            return CollapsingToolbarBaseActivity.super.getActionBar();
        }

        public void setOuterTitle(CharSequence charSequence) {
            CollapsingToolbarBaseActivity.super.setTitle(charSequence);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mCustomizeLayoutResId <= 0 || BuildCompatUtils.isAtLeastS()) {
            CollapsingToolbarDelegate collapsingToolbarDelegate = new CollapsingToolbarDelegate(new DelegateCallback());
            this.mToolbardelegate = collapsingToolbarDelegate;
            super.setContentView(collapsingToolbarDelegate.onCreateView(getLayoutInflater(), (ViewGroup) null));
            return;
        }
        super.setContentView(this.mCustomizeLayoutResId);
    }

    public void setContentView(int i) {
        ViewGroup viewGroup;
        CollapsingToolbarDelegate collapsingToolbarDelegate = this.mToolbardelegate;
        if (collapsingToolbarDelegate == null) {
            viewGroup = (ViewGroup) findViewById(R$id.content_frame);
        } else {
            viewGroup = collapsingToolbarDelegate.getContentFrameLayout();
        }
        if (viewGroup != null) {
            viewGroup.removeAllViews();
        }
        LayoutInflater.from(this).inflate(i, viewGroup);
    }

    public void setContentView(View view) {
        ViewGroup viewGroup;
        CollapsingToolbarDelegate collapsingToolbarDelegate = this.mToolbardelegate;
        if (collapsingToolbarDelegate == null) {
            viewGroup = (ViewGroup) findViewById(R$id.content_frame);
        } else {
            viewGroup = collapsingToolbarDelegate.getContentFrameLayout();
        }
        if (viewGroup != null) {
            viewGroup.addView(view);
        }
    }

    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        ViewGroup viewGroup;
        CollapsingToolbarDelegate collapsingToolbarDelegate = this.mToolbardelegate;
        if (collapsingToolbarDelegate == null) {
            viewGroup = (ViewGroup) findViewById(R$id.content_frame);
        } else {
            viewGroup = collapsingToolbarDelegate.getContentFrameLayout();
        }
        if (viewGroup != null) {
            viewGroup.addView(view, layoutParams);
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.mToolbardelegate.setTitle(charSequence);
    }

    public void setTitle(int i) {
        setTitle(getText(i));
    }

    public boolean onNavigateUp() {
        if (super.onNavigateUp()) {
            return true;
        }
        finishAfterTransition();
        return true;
    }
}
