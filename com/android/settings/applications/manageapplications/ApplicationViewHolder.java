package com.android.settings.applications.manageapplications;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.C0444R;
import com.android.settingslib.applications.ApplicationsState;

public class ApplicationViewHolder extends RecyclerView.ViewHolder {
    private final ImageView mAppIcon;
    final TextView mAppName;
    final TextView mDisabled;
    final TextView mSummary;
    final Switch mSwitch;
    final ViewGroup mWidgetContainer;

    ApplicationViewHolder(View view) {
        super(view);
        this.mAppName = (TextView) view.findViewById(16908310);
        this.mAppIcon = (ImageView) view.findViewById(16908294);
        this.mSummary = (TextView) view.findViewById(16908304);
        this.mDisabled = (TextView) view.findViewById(C0444R.C0448id.appendix);
        this.mSwitch = (Switch) view.findViewById(C0444R.C0448id.switchWidget);
        this.mWidgetContainer = (ViewGroup) view.findViewById(16908312);
    }

    static View newView(ViewGroup viewGroup, boolean z) {
        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).inflate(C0444R.C0450layout.preference_app, viewGroup, false);
        ViewGroup viewGroup3 = (ViewGroup) viewGroup2.findViewById(16908312);
        if (z) {
            if (viewGroup3 != null) {
                LayoutInflater.from(viewGroup.getContext()).inflate(C0444R.C0450layout.preference_widget_primary_switch, viewGroup3, true);
                viewGroup2.addView(LayoutInflater.from(viewGroup.getContext()).inflate(C0444R.C0450layout.preference_two_target_divider, viewGroup2, false), viewGroup2.getChildCount() - 1);
            }
        } else if (viewGroup3 != null) {
            viewGroup3.setVisibility(8);
        }
        return viewGroup2;
    }

    /* access modifiers changed from: package-private */
    public void setSummary(CharSequence charSequence) {
        this.mSummary.setText(charSequence);
    }

    /* access modifiers changed from: package-private */
    public void setSummary(int i) {
        this.mSummary.setText(i);
    }

    /* access modifiers changed from: package-private */
    public void setEnabled(boolean z) {
        this.itemView.setEnabled(z);
    }

    /* access modifiers changed from: package-private */
    public void setTitle(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence != null) {
            this.mAppName.setText(charSequence);
            if (!TextUtils.isEmpty(charSequence2)) {
                this.mAppName.setContentDescription(charSequence2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            this.mAppIcon.setImageDrawable(drawable);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateDisableView(ApplicationInfo applicationInfo) {
        if ((applicationInfo.flags & 8388608) == 0) {
            this.mDisabled.setVisibility(0);
            this.mDisabled.setText(C0444R.string.not_installed);
        } else if (!applicationInfo.enabled || applicationInfo.enabledSetting == 4) {
            this.mDisabled.setVisibility(0);
            this.mDisabled.setText(C0444R.string.disabled);
        } else {
            this.mDisabled.setVisibility(8);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateSizeText(ApplicationsState.AppEntry appEntry, CharSequence charSequence, int i) {
        if (ManageApplications.DEBUG) {
            Log.d("ManageApplications", "updateSizeText of " + appEntry.label + " " + appEntry + ": " + appEntry.sizeStr);
        }
        String str = appEntry.sizeStr;
        if (str != null) {
            if (i == 1) {
                setSummary((CharSequence) appEntry.internalSizeStr);
            } else if (i != 2) {
                setSummary((CharSequence) str);
            } else {
                setSummary((CharSequence) appEntry.externalSizeStr);
            }
        } else if (appEntry.size == -2) {
            setSummary(charSequence);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateSwitch(CompoundButton.OnCheckedChangeListener onCheckedChangeListener, boolean z, boolean z2) {
        ViewGroup viewGroup;
        if (this.mSwitch != null && (viewGroup = this.mWidgetContainer) != null) {
            viewGroup.setFocusable(false);
            this.mWidgetContainer.setClickable(false);
            this.mSwitch.setFocusable(true);
            this.mSwitch.setClickable(true);
            this.mSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
            this.mSwitch.setChecked(z2);
            this.mSwitch.setEnabled(z);
        }
    }
}
