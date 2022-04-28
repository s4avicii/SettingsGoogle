package com.android.settings.development.tare;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.window.C0444R;

public class AlarmManagerFragment extends Fragment {
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C0444R.C0450layout.tare_policy_fragment, (ViewGroup) null);
        ExpandableListView expandableListView = (ExpandableListView) inflate.findViewById(C0444R.C0448id.factor_list);
        final SavedTabsListAdapter savedTabsListAdapter = new SavedTabsListAdapter();
        expandableListView.setGroupIndicator((Drawable) null);
        expandableListView.setAdapter(savedTabsListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
                Toast.makeText(AlarmManagerFragment.this.getActivity(), (String) savedTabsListAdapter.getChild(i, i2), 0).show();
                return true;
            }
        });
        return inflate;
    }

    public class SavedTabsListAdapter extends BaseExpandableListAdapter {
        private String[][] mChildren = {new String[0], new String[0], this.mResources.getStringArray(C0444R.array.tare_min_satiated_balance_subfactors), this.mResources.getStringArray(C0444R.array.tare_modifiers_subfactors), this.mResources.getStringArray(C0444R.array.tare_alarm_manager_actions), this.mResources.getStringArray(C0444R.array.tare_rewards_subfactors)};
        private String[] mGroups;
        private final LayoutInflater mInflater;
        private Resources mResources;

        public long getChildId(int i, int i2) {
            return (long) i2;
        }

        public long getGroupId(int i) {
            return (long) i;
        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int i, int i2) {
            return true;
        }

        public SavedTabsListAdapter() {
            Resources resources = AlarmManagerFragment.this.getActivity().getResources();
            this.mResources = resources;
            this.mGroups = new String[]{resources.getString(C0444R.string.tare_max_circulation), this.mResources.getString(C0444R.string.tare_max_satiated_balance), this.mResources.getString(C0444R.string.tare_min_satiated_balance), this.mResources.getString(C0444R.string.tare_modifiers), this.mResources.getString(C0444R.string.tare_actions), this.mResources.getString(C0444R.string.tare_rewards)};
            this.mInflater = LayoutInflater.from(AlarmManagerFragment.this.getActivity());
        }

        public int getGroupCount() {
            return this.mGroups.length;
        }

        public int getChildrenCount(int i) {
            return this.mChildren[i].length;
        }

        public Object getGroup(int i) {
            return this.mGroups[i];
        }

        public Object getChild(int i, int i2) {
            return this.mChildren[i][i2];
        }

        public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(17367043, viewGroup, false);
            }
            ((TextView) view.findViewById(16908308)).setText(getGroup(i).toString());
            return view;
        }

        public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(C0444R.C0450layout.tare_child_item, (ViewGroup) null);
            }
            ((TextView) view.findViewById(C0444R.C0448id.factor)).setText(getChild(i, i2).toString());
            ((TextView) view.findViewById(C0444R.C0448id.factor_number)).setText("500");
            return view;
        }
    }
}
