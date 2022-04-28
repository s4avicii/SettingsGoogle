package com.android.settings.dashboard.profileselector;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.database.DataSetObserver;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.window.C0444R;
import com.android.internal.util.UserIcons;
import com.android.settingslib.drawable.UserIconDrawable;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter implements SpinnerAdapter, ListAdapter {
    private ArrayList<UserDetails> data;
    private final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;
    private final LayoutInflater mInflater;

    public boolean areAllItemsEnabled() {
        return true;
    }

    public int getItemViewType(int i) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isEnabled(int i) {
        return true;
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
    }

    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
    }

    public static class UserDetails {
        /* access modifiers changed from: private */
        public final Drawable mIcon;
        private final String mName;
        /* access modifiers changed from: private */
        public final UserHandle mUserHandle;

        public UserDetails(UserHandle userHandle, UserManager userManager, Context context) {
            Drawable drawable;
            this.mUserHandle = userHandle;
            UserInfo userInfo = userManager.getUserInfo(userHandle.getIdentifier());
            if (userInfo.isManagedProfile()) {
                this.mName = ((DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)).getString("Settings.WORK_PROFILE_USER_LABEL", new UserAdapter$UserDetails$$ExternalSyntheticLambda0(context));
                drawable = context.getPackageManager().getUserBadgeForDensityNoBackground(userHandle, 0);
            } else {
                this.mName = userInfo.name;
                int i = userInfo.id;
                if (userManager.getUserIcon(i) != null) {
                    drawable = new BitmapDrawable(context.getResources(), userManager.getUserIcon(i));
                } else {
                    drawable = UserIcons.getDefaultUserIcon(context.getResources(), i, false);
                }
            }
            this.mIcon = encircle(context, drawable);
        }

        private static Drawable encircle(Context context, Drawable drawable) {
            return new UserIconDrawable(UserIconDrawable.getDefaultSize(context)).setIconDrawable(drawable).bake();
        }
    }

    public UserAdapter(Context context, ArrayList<UserDetails> arrayList) {
        if (arrayList != null) {
            this.mContext = context;
            this.data = arrayList;
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
            this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
            return;
        }
        throw new IllegalArgumentException("A list of user details must be provided");
    }

    public UserHandle getUserHandle(int i) {
        if (i < 0 || i >= this.data.size()) {
            return null;
        }
        return this.data.get(i).mUserHandle;
    }

    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = createUser(viewGroup);
        }
        UserDetails userDetails = this.data.get(i);
        ((ImageView) view.findViewById(16908294)).setImageDrawable(userDetails.mIcon);
        ((TextView) view.findViewById(16908310)).setText(getTitle(userDetails));
        return view;
    }

    private String getTitle(UserDetails userDetails) {
        int identifier = userDetails.mUserHandle.getIdentifier();
        if (identifier == -2 || identifier == ActivityManager.getCurrentUser()) {
            return this.mDevicePolicyManager.getString("Settings.category_personal", new UserAdapter$$ExternalSyntheticLambda0(this));
        }
        return this.mDevicePolicyManager.getString("Settings.WORK_CATEGORY_HEADER", new UserAdapter$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getTitle$0() throws Exception {
        return this.mContext.getString(C0444R.string.category_personal);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getTitle$1() throws Exception {
        return this.mContext.getString(C0444R.string.category_work);
    }

    private View createUser(ViewGroup viewGroup) {
        return this.mInflater.inflate(C0444R.C0450layout.user_preference, viewGroup, false);
    }

    public int getCount() {
        return this.data.size();
    }

    public UserDetails getItem(int i) {
        return this.data.get(i);
    }

    public long getItemId(int i) {
        return (long) this.data.get(i).mUserHandle.getIdentifier();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        return getDropDownView(i, view, viewGroup);
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public static UserAdapter createUserSpinnerAdapter(UserManager userManager, Context context) {
        List<UserHandle> userProfiles = userManager.getUserProfiles();
        if (userProfiles.size() < 2) {
            return null;
        }
        UserHandle userHandle = new UserHandle(UserHandle.myUserId());
        userProfiles.remove(userHandle);
        userProfiles.add(0, userHandle);
        return createUserAdapter(userManager, context, userProfiles);
    }

    public static UserAdapter createUserAdapter(UserManager userManager, Context context, List<UserHandle> list) {
        ArrayList arrayList = new ArrayList(list.size());
        int size = list.size();
        for (int i = 0; i < size; i++) {
            arrayList.add(new UserDetails(list.get(i), userManager, context));
        }
        return new UserAdapter(context, arrayList);
    }
}
