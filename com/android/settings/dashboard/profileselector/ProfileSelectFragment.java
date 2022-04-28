package com.android.settings.dashboard.profileselector;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.window.C0444R;
import com.android.settings.Utils;
import com.android.settings.dashboard.DashboardFragment;
import com.google.android.material.tabs.TabLayout;
import java.util.Locale;

public abstract class ProfileSelectFragment extends DashboardFragment {
    private ViewGroup mContentView;

    public abstract Fragment[] getFragments();

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ProfileSelectFragment";
    }

    public int getMetricsCategory() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.placeholder_preference_screen;
    }

    public int getTitleResId() {
        return 0;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContentView = (ViewGroup) super.onCreateView(layoutInflater, viewGroup, bundle);
        FragmentActivity activity = getActivity();
        int titleResId = getTitleResId();
        if (titleResId > 0) {
            activity.setTitle(titleResId);
        }
        int convertPosition = convertPosition(getTabId(activity, getArguments()));
        View findViewById = this.mContentView.findViewById(C0444R.C0448id.tab_container);
        ViewPager viewPager = (ViewPager) findViewById.findViewById(C0444R.C0448id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(this));
        TabLayout tabLayout = (TabLayout) findViewById.findViewById(C0444R.C0448id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabTextColor(tabLayout);
        findViewById.setVisibility(0);
        tabLayout.getTabAt(convertPosition).select();
        ((FrameLayout) this.mContentView.findViewById(16908351)).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        RecyclerView listView = getListView();
        listView.setOverScrollMode(2);
        Utils.setActionBarShadowAnimation(activity, getSettingsLifecycle(), listView);
        return this.mContentView;
    }

    private void setupTabTextColor(TabLayout tabLayout) {
        tabLayout.setTabTextColors(new ColorStateList(new int[][]{new int[]{16842913}, new int[0]}, new int[]{tabLayout.getTabTextColors().getColorForState(new int[]{16842913}, com.android.settingslib.Utils.getColorAttrDefaultColor(getContext(), 17956901)), com.android.settingslib.Utils.getColorAttrDefaultColor(getContext(), 16842808)}));
    }

    /* access modifiers changed from: package-private */
    public int getTabId(Activity activity, Bundle bundle) {
        if (bundle != null) {
            int i = bundle.getInt(":settings:show_fragment_tab", -1);
            if (i != -1) {
                return i;
            }
            if (UserManager.get(activity).isManagedProfile(bundle.getInt("android.intent.extra.USER_ID", UserHandle.SYSTEM.getIdentifier()))) {
                return 1;
            }
        }
        if (UserManager.get(activity).isManagedProfile(activity.getIntent().getContentUserHint())) {
            return 1;
        }
        return 0;
    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final Fragment[] mChildFragments;
        private final Context mContext;

        ViewPagerAdapter(ProfileSelectFragment profileSelectFragment) {
            super(profileSelectFragment.getChildFragmentManager());
            this.mContext = profileSelectFragment.getContext();
            this.mChildFragments = profileSelectFragment.getFragments();
        }

        public Fragment getItem(int i) {
            return this.mChildFragments[ProfileSelectFragment.convertPosition(i)];
        }

        public int getCount() {
            return this.mChildFragments.length;
        }

        public CharSequence getPageTitle(int i) {
            DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class);
            if (ProfileSelectFragment.convertPosition(i) == 1) {
                return devicePolicyManager.getString("Settings.WORK_CATEGORY_HEADER", new ProfileSelectFragment$ViewPagerAdapter$$ExternalSyntheticLambda1(this));
            }
            return devicePolicyManager.getString("Settings.category_personal", new ProfileSelectFragment$ViewPagerAdapter$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$getPageTitle$0() throws Exception {
            return this.mContext.getString(C0444R.string.category_work);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$getPageTitle$1() throws Exception {
            return this.mContext.getString(C0444R.string.category_personal);
        }
    }

    /* access modifiers changed from: private */
    public static int convertPosition(int i) {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1 ? 1 - i : i;
    }
}
