package com.android.settings.homepage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.widget.HighlightableTopLevelPreferenceAdapter;

public class TopLevelHighlightMixin implements Parcelable, DialogInterface.OnShowListener, DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
    public static final Parcelable.Creator<TopLevelHighlightMixin> CREATOR = new Parcelable.Creator<TopLevelHighlightMixin>() {
        public TopLevelHighlightMixin createFromParcel(Parcel parcel) {
            return new TopLevelHighlightMixin(parcel);
        }

        public TopLevelHighlightMixin[] newArray(int i) {
            return new TopLevelHighlightMixin[i];
        }
    };
    private String mCurrentKey;
    private DialogInterface mDialog;
    private String mHiddenKey;
    private String mPreviousKey;
    private HighlightableTopLevelPreferenceAdapter mTopLevelAdapter;

    public int describeContents() {
        return 0;
    }

    public TopLevelHighlightMixin() {
    }

    public TopLevelHighlightMixin(Parcel parcel) {
        this.mCurrentKey = parcel.readString();
        this.mPreviousKey = parcel.readString();
        this.mHiddenKey = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mCurrentKey);
        parcel.writeString(this.mPreviousKey);
        parcel.writeString(this.mHiddenKey);
    }

    public void onShow(DialogInterface dialogInterface) {
        this.mDialog = dialogInterface;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        this.mDialog = null;
    }

    public void onCancel(DialogInterface dialogInterface) {
        HighlightableTopLevelPreferenceAdapter highlightableTopLevelPreferenceAdapter = this.mTopLevelAdapter;
        if (highlightableTopLevelPreferenceAdapter != null) {
            String str = this.mPreviousKey;
            this.mCurrentKey = str;
            this.mPreviousKey = null;
            highlightableTopLevelPreferenceAdapter.highlightPreference(str, false);
        }
    }

    /* access modifiers changed from: package-private */
    public RecyclerView.Adapter onCreateAdapter(TopLevelSettings topLevelSettings, PreferenceScreen preferenceScreen) {
        if (TextUtils.isEmpty(this.mCurrentKey)) {
            this.mCurrentKey = getHighlightPrefKeyFromArguments(topLevelSettings.getArguments());
        }
        Log.d("TopLevelHighlightMixin", "onCreateAdapter, pref key: " + this.mCurrentKey);
        HighlightableTopLevelPreferenceAdapter highlightableTopLevelPreferenceAdapter = new HighlightableTopLevelPreferenceAdapter((SettingsHomepageActivity) topLevelSettings.getActivity(), preferenceScreen, topLevelSettings.getListView(), this.mCurrentKey);
        this.mTopLevelAdapter = highlightableTopLevelPreferenceAdapter;
        return highlightableTopLevelPreferenceAdapter;
    }

    /* access modifiers changed from: package-private */
    public void reloadHighlightMenuKey(Bundle bundle) {
        if (this.mTopLevelAdapter != null) {
            ensureDialogDismissed();
            this.mCurrentKey = getHighlightPrefKeyFromArguments(bundle);
            Log.d("TopLevelHighlightMixin", "reloadHighlightMenuKey, pref key: " + this.mCurrentKey);
            this.mTopLevelAdapter.highlightPreference(this.mCurrentKey, true);
        }
    }

    /* access modifiers changed from: package-private */
    public void setHighlightPreferenceKey(String str) {
        if (this.mTopLevelAdapter != null) {
            ensureDialogDismissed();
            this.mPreviousKey = this.mCurrentKey;
            this.mCurrentKey = str;
            this.mTopLevelAdapter.highlightPreference(str, false);
        }
    }

    /* access modifiers changed from: package-private */
    public void highlightPreferenceIfNeeded(FragmentActivity fragmentActivity) {
        HighlightableTopLevelPreferenceAdapter highlightableTopLevelPreferenceAdapter = this.mTopLevelAdapter;
        if (highlightableTopLevelPreferenceAdapter != null) {
            highlightableTopLevelPreferenceAdapter.requestHighlight();
        }
    }

    /* access modifiers changed from: package-private */
    public void setMenuHighlightShowed(boolean z) {
        if (this.mTopLevelAdapter != null) {
            ensureDialogDismissed();
            if (z) {
                this.mCurrentKey = this.mHiddenKey;
                this.mHiddenKey = null;
            } else {
                if (this.mHiddenKey == null) {
                    this.mHiddenKey = this.mCurrentKey;
                }
                this.mCurrentKey = null;
            }
            this.mTopLevelAdapter.highlightPreference(this.mCurrentKey, z);
        }
    }

    /* access modifiers changed from: package-private */
    public void setHighlightMenuKey(String str, boolean z) {
        if (this.mTopLevelAdapter != null) {
            ensureDialogDismissed();
            String lookupPreferenceKey = HighlightableMenu.lookupPreferenceKey(str);
            if (TextUtils.isEmpty(lookupPreferenceKey)) {
                Log.e("TopLevelHighlightMixin", "Invalid highlight menu key: " + str);
                return;
            }
            Log.d("TopLevelHighlightMixin", "Menu key: " + str);
            this.mCurrentKey = lookupPreferenceKey;
            this.mTopLevelAdapter.highlightPreference(lookupPreferenceKey, z);
        }
    }

    private static String getHighlightPrefKeyFromArguments(Bundle bundle) {
        String string = bundle.getString(":settings:fragment_args_key");
        String lookupPreferenceKey = HighlightableMenu.lookupPreferenceKey(string);
        if (TextUtils.isEmpty(lookupPreferenceKey)) {
            Log.e("TopLevelHighlightMixin", "Invalid highlight menu key: " + string);
        } else {
            Log.d("TopLevelHighlightMixin", "Menu key: " + string);
        }
        return lookupPreferenceKey;
    }

    private void ensureDialogDismissed() {
        DialogInterface dialogInterface = this.mDialog;
        if (dialogInterface != null) {
            onCancel(dialogInterface);
            this.mDialog.dismiss();
        }
    }
}
